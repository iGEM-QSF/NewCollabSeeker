package wad.config;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.HashSet;
import java.util.Set;
import javax.inject.Inject;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
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
import org.springframework.social.twitter.connect.TwitterConnectionFactory;
import wad.auth.JpaAuthenticationProvider;
import wad.repository.TeamRepository;

@Configuration
@EnableWebMvcSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private TeamRepository teamRepository;
    
    @Inject
    private DataSource dataSource;
    
    private ConnectionFactoryLocator connectionFactoryLocator;
    
    private SecureRandom random = new SecureRandom();
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/login", "/signup", "/static*/**", "/connect/**","/team/**","/search/**","/categories/**","/").permitAll()
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
        ConnectionFactoryRegistry registry = new ConnectionFactoryRegistry();
        registry.addConnectionFactory(new FacebookConnectionFactory("ID","SECRET"));
        registry.addConnectionFactory(new TwitterConnectionFactory("ID", "SECRET"));
        this.connectionFactoryLocator = registry;
        
        /*
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            throw new IllegalStateException("No user signed in.");
        }
        */
        
        //System.out.println(connectionFactoryLocator.getConnectionFactory(Facebook.class));
        //System.out.println(this.connectionFactoryLocator().getConnectionFactory(FacebookConnectionFactory.class).toString());
        //ConnectionRepository connectionRepository = this.connectionRepository();
        
        ConnectionRepository UCRepository = this.usersConnectionRepository().createConnectionRepository(new BigInteger(65, random).toString(32));
        
        ConnectController controller = new ConnectController(this.connectionFactoryLocator, UCRepository);
        controller.addInterceptor(new CustomFacebookConnectInterceptor(this.teamRepository, UCRepository));
        controller.addInterceptor(new CustomTwitterConnectInterceptor(this.teamRepository, UCRepository));
        System.out.println("ConnectController ready.");
        return controller;
    }
    
    /*
    @Bean
    public ConnectionFactoryLocator connectionFactoryLocator() {
        ConnectionFactoryRegistry registry = new ConnectionFactoryRegistry();
        //registry.addConnectionFactory(new FacebookConnectionFactory(environment.getProperty("facebook.clientId"),
        //environment.getProperty("facebook.clientSecret")));
        registry.addConnectionFactory(new FacebookConnectionFactory("clientId", "clientSecret"));
        return registry;
    }
    
    @Bean
    @Scope(value = "request", proxyMode = ScopedProxyMode.INTERFACES)
    public ConnectionRepository connectionRepository() {
        System.out.println("Focus here!");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            throw new IllegalStateException("No user signed in.");
        }
        return usersConnectionRepository().createConnectionRepository(auth.getName());
    }
    */
    
    @Bean
    public UsersConnectionRepository usersConnectionRepository() {
        return new JdbcUsersConnectionRepository(dataSource, this.connectionFactoryLocator, textEncryptor());
    }
    
    @Bean
    public TextEncryptor textEncryptor() {
        //String localEncryptionPassword = "aoijrg3ai5435l6jrwjj465jÖGLIJWrgjkl";
        String localEncryptionPassword = new BigInteger(128, random).toString(32);
        String localEncryptionSalt = new String(Hex.encode(BCrypt.gensalt().getBytes()));
        return Encryptors.text(localEncryptionPassword, localEncryptionSalt);
    }
    
    @Bean
    public Set<String> cats(){
        return new HashSet();
    }
    
}
