package wad.config;

import javax.inject.Inject;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.codec.Hex;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.jdbc.JdbcUsersConnectionRepository;
import org.springframework.social.connect.support.ConnectionFactoryRegistry;
import org.springframework.social.connect.web.ConnectController;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import wad.auth.JpaAuthenticationProvider;

@Configuration
@EnableWebMvcSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Inject
    private Environment environment;
    
    @Inject
    private DataSource dataSource;
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/login", "/signup", "/static*/**", "/connect/**").permitAll()
                .antMatchers(HttpMethod.POST, "/persons").permitAll()
                .anyRequest().authenticated();

        http.formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/authenticate")
                .defaultSuccessUrl("/index")
                .usernameParameter("username")
                .passwordParameter("password")
                .permitAll();

        http.logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login")
                .permitAll()
                .invalidateHttpSession(true);
    }

    @Configuration
    protected static class AuthenticationConfiguration extends GlobalAuthenticationConfigurerAdapter {

        @Autowired
        private JpaAuthenticationProvider jpaAuthenticationProvider;

        @Override
        public void init(AuthenticationManagerBuilder auth) throws Exception {
            auth.authenticationProvider(jpaAuthenticationProvider);
        }
    }
    
    @Bean
    public ConnectController connectController() {
        System.out.println("Generating ConnectController...");
        ConnectController controller = new ConnectController(connectionFactoryLocator(), connectionRepository());
        System.out.println(controller.toString());
        controller.addInterceptor(new CustomConnectInterceptor());
        return controller; 
    }
    
    @Bean
    public ConnectionFactoryLocator connectionFactoryLocator() {
        ConnectionFactoryRegistry registry = new ConnectionFactoryRegistry();
        System.out.println("Look here!");
        System.out.println(environment.getProperty("facebook.clientSecret"));
        registry.addConnectionFactory(new FacebookConnectionFactory(environment.getProperty("facebook.clientId"),
        environment.getProperty("facebook.clientSecret")));
        return registry;
    }
    
    @Bean
    @Scope(value = "request", proxyMode = ScopedProxyMode.INTERFACES)
    public ConnectionRepository connectionRepository() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            throw new IllegalStateException("No user signed in.");
        }
        return usersConnectionRepository().createConnectionRepository(auth.getName());
    }
    
    @Bean
    public UsersConnectionRepository usersConnectionRepository() {
        return new JdbcUsersConnectionRepository(dataSource, connectionFactoryLocator(), textEncryptor());
    }
    
    @Bean
    public TextEncryptor textEncryptor() {
        String localEncryptionPassword = "aoijrg3ai5435l6jrwjj465jÖGLIJWrgjkl";
        String localEncryptionSalt = new String(Hex.encode(BCrypt.gensalt().getBytes()));
        return Encryptors.text(localEncryptionPassword, localEncryptionSalt);
    }
}
