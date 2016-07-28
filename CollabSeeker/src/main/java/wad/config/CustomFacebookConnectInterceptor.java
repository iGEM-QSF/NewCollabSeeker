package wad.config;

import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionFactory;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.UserProfile;
import org.springframework.social.connect.web.ConnectInterceptor;
import org.springframework.social.facebook.api.Account;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.request.WebRequest;
import wad.domain.Team;
import wad.repository.TeamRepository;

class CustomFacebookConnectInterceptor implements ConnectInterceptor<Facebook> {
    
    private TeamRepository teamRepository;
    private ConnectionRepository connectionRepository;
    
    public CustomFacebookConnectInterceptor(TeamRepository teamRepository, ConnectionRepository connectionRepository) {
        this.teamRepository = teamRepository;
        this.connectionRepository = connectionRepository;
    }

    @Override
    public void preConnect(ConnectionFactory cf, MultiValueMap mvm, WebRequest wr) {}

    @Override
    public void postConnect(Connection<Facebook> connection, WebRequest wr) {
        //UserProfile userProfile = connection.fetchUserProfile();
        //String userId = userProfile.getId();
        //Team team = teamRepository.findByFacebook(userId);
        List<Account> pages = connection.getApi().pageOperations().getAccounts();
        Team team = null;
        for (Account page : pages) {
            String pageId = page.getId();
            team = teamRepository.findByFacebook(pageId);
            if (team != null) {
                break;
            }
        }
        if (team == null) {
            return;
        }
        Authentication auth = new UsernamePasswordAuthenticationToken(team.getName(), team.getPassword(), Arrays.asList(new SimpleGrantedAuthority("USER")));
        
        Connection<Facebook> connectionFacebook = (Connection<Facebook>) connectionRepository.findConnections("facebook").get(0);
        connectionRepository.removeConnection(connectionFacebook.getKey());
        
        SecurityContextHolder.clearContext();
        SecurityContextHolder.getContext().setAuthentication(auth);
    }
    
}
