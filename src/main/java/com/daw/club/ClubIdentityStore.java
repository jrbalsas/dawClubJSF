package com.daw.club;

import com.daw.club.model.Cliente;
import com.daw.club.model.ClubPrincipal;
import com.daw.club.model.dao.ClienteDAO;
import com.daw.club.qualifiers.DAOMap;
import com.daw.club.services.ClubAuthService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.security.enterprise.credential.UsernamePasswordCredential;
import jakarta.security.enterprise.identitystore.CredentialValidationResult;
import jakarta.security.enterprise.identitystore.IdentityStore;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import static jakarta.security.enterprise.identitystore.CredentialValidationResult.INVALID_RESULT;
import static java.util.Arrays.asList;

/**
 * Sample Customized JEE Security IdentityStore
 *
 * @author jrbalsas
 */
@ApplicationScoped
public class ClubIdentityStore implements IdentityStore {

    private static final Logger logger = Logger.getLogger(ClubIdentityStore.class.getName());

    @Inject
    private ClubAuthService authService;

    @Inject @DAOMap
    //@Inject @DAOJpa
    private ClienteDAO clientesDAO;

    public ClubIdentityStore() {
    }

    public CredentialValidationResult validate(UsernamePasswordCredential usernamePasswordCredential) {

        String username = usernamePasswordCredential.getCaller();
        String password = usernamePasswordCredential.getPasswordAsString();

        boolean authenticated=false;

        //Look for user
        Cliente cliente= clientesDAO.buscaByNIF(username);

        //Check password with encrypted version
        if (cliente!=null && authService.verifyPassword(password,cliente.getClaveCifrada())) {
            authenticated=true;
            logger.info(String.format("Authenticated user %s", username));
        } else {
            logger.warning(String.format("Authentication error for %s", username));
        }
        // If valid credentials get user permissions and inform to the Application Server
        if ( authenticated  ) {
            //Get roles for valid user...
            Set<String> roles =  new HashSet<>(asList("USUARIOS"));

            //Authenticate user in Application Server and pass custom User information for Server Principal Object
            return new CredentialValidationResult( new ClubPrincipal(cliente), roles);
            //return new CredentialValidationResult( username, roles); //Server only needs username and roles to create default Principal object
        }

        //Credentials invalid
        return INVALID_RESULT;
    }
}
