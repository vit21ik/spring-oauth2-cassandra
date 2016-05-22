package paradigma.security.oauth2.provider.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.stereotype.Service;
import paradigma.entity.security.Client;
import paradigma.repository.security.ClientRepository;
import paradigma.security.GrantedAuthorityCreater;

/**
 * Created on 5/22/16.
 */
@Primary
@Service
public class CassandraClientDetailsService implements ClientDetailsService {

    private ClientRepository clientRepository;

    private GrantedAuthorityCreater grantedAuthorityCreater;

    @Autowired
    public void setClientRepository(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Autowired
    public void setGrantedAuthorityCreater(GrantedAuthorityCreater grantedAuthorityCreater) {
        this.grantedAuthorityCreater = grantedAuthorityCreater;
    }

    @Override
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
        final Client client = clientRepository.findOne(clientId);
        if (client != null) {
            BaseClientDetails clientDetails = new BaseClientDetails();
            clientDetails.setClientId(client.getClient_id());
            clientDetails.setAuthorizedGrantTypes(client.getGrant_types());
            clientDetails.setClientSecret(client.getClient_secret());
            clientDetails.setScope(client.getScope());
            clientDetails.setAuthorities(grantedAuthorityCreater.fromCollection(client.getAuthorities()));
            return clientDetails;
        } else {
            return null;
        }
    }

}
