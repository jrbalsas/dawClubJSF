package com.daw.club;

import com.daw.club.services.ClubAuthService;
import com.daw.club.qualifiers.DAOMap;
import java.util.Set;
import java.util.logging.Logger;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.security.enterprise.credential.UsernamePasswordCredential;
import javax.security.enterprise.identitystore.CredentialValidationResult;
import static javax.security.enterprise.identitystore.CredentialValidationResult.INVALID_RESULT;
import javax.security.enterprise.identitystore.IdentityStore;

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

    public ClubIdentityStore() {
    }

    public CredentialValidationResult validate(UsernamePasswordCredential usernamePasswordCredential) {

        String username = usernamePasswordCredential.getCaller();
        String password = usernamePasswordCredential.getPasswordAsString();

        //Check for valid credentials
        if (authService.authUser(username, password)) {
            //Get roles for valid user
            Set<String> roles = authService.getRoles(username);
            
            //Authenticate user in Application Server
            return new CredentialValidationResult(username, roles);
        }
        //Credentials invalid
        return INVALID_RESULT;
    }
}
