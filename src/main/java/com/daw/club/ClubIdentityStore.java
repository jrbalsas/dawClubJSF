package com.daw.club;

import com.daw.club.model.Cliente;
import com.daw.club.model.ClubPrincipal;
import com.daw.club.model.dao.ClienteDAO;
import com.daw.club.services.ClubAuthService;
import com.daw.club.qualifiers.DAOMap;
import java.util.Set;
import java.util.logging.Logger;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.security.enterprise.credential.UsernamePasswordCredential;
import jakarta.security.enterprise.identitystore.CredentialValidationResult;
import static jakarta.security.enterprise.identitystore.CredentialValidationResult.INVALID_RESULT;
import jakarta.security.enterprise.identitystore.IdentityStore;

/**
 * Sample Customized JEE Security IdentityStore
 *
 * @author jrbalsas
 */
@ApplicationScoped
public class ClubIdentityStore implements IdentityStore {

    private static final Logger logger = Logger.getLogger(ClubIdentityStore.class.getName());

    @Inject @DAOMap
    private ClubAuthService authService;

    @Inject @DAOMap
    private ClienteDAO clientesDAO;

    public ClubIdentityStore() {
    }

    public CredentialValidationResult validate(UsernamePasswordCredential usernamePasswordCredential) {

        String username = usernamePasswordCredential.getCaller();
        String password = usernamePasswordCredential.getPasswordAsString();

        //Look for user
        Cliente cliente= clientesDAO.buscaByNIF(username);

        //Check for valid credentials
        if (cliente!=null && authService.authUser(username, password)) {
            //Get roles for valid user
            Set<String> roles = authService.getRoles(username);

            //Authenticate user in Application Server and pass custom User information for Server Principal Object
            return new CredentialValidationResult( new ClubPrincipal(cliente), roles);
            //return new CredentialValidationResult( username, roles); //Server only needs username and roles to create default Principal object
        }
        //Credentials invalid
        return INVALID_RESULT;
    }
}
