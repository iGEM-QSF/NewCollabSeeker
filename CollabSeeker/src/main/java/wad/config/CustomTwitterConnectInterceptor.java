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
import org.springframework.social.twitter.api.Twitter;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.request.WebRequest;
import wad.domain.Team;
import wad.repository.TeamRepository;

class CustomTwitterConnectInterceptor implements ConnectInterceptor<Twitter> {
    
    private final TeamRepository teamRepository;
    private final ConnectionRepository connectionRepository;
    
    public CustomTwitterConnectInterceptor(TeamRepository teamRepository, ConnectionRepository connectionRepository) {
        this.teamRepository = teamRepository;
        this.connectionRepository = connectionRepository;
    }

    @Override
    public void preConnect(ConnectionFactory cf, MultiValueMap mvm, WebRequest wr) {}

    @Override
    public void postConnect(Connection<Twitter> connection, WebRequest wr) {
        String userId = String.valueOf(connection.getApi().userOperations().getProfileId());
        Team team = teamRepository.findByTwitterId(userId);
        if (team == null) {
            return;
        }
        Authentication auth = new UsernamePasswordAuthenticationToken(team.getName(), team.getPassword(), Arrays.asList(new SimpleGrantedAuthority("USER")));
        
        Connection<Twitter> connectionTwitter = (Connection<Twitter>) connectionRepository.findConnections("twitter").get(0);
        connectionRepository.removeConnection(connectionTwitter.getKey());
        
        SecurityContextHolder.clearContext();
        SecurityContextHolder.getContext().setAuthentication(auth);
    }
    
}
