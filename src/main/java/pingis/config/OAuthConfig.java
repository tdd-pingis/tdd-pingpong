package pingis.config;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.BeanUtils;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationProperties;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OAuthConfig {
    @Autowired
    OAuthProperties oauthProperties;
    
    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        ClientRegistrationProperties clientRegistrationProperties = new ClientRegistrationProperties();
        //copies all fields from the first object to the second
        BeanUtils.copyProperties(oauthProperties, clientRegistrationProperties);

        List<ClientRegistration> clientRegistrations = new ArrayList<>();
        clientRegistrations.add(new ClientRegistration.Builder(clientRegistrationProperties).build());
        return new InMemoryClientRegistrationRepository(clientRegistrations);
    }
}