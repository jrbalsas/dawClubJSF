package com.daw.club.services;

import com.daw.club.qualifiers.DAOMap;
import static java.util.Arrays.asList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.security.enterprise.identitystore.Pbkdf2PasswordHash;

/**
 * Sample Authentication service with in memory encrypted passwords
 *
 * @author jrbalsas
 */
@ApplicationScoped
@DAOMap
public class ClubAuthServiceMap implements ClubAuthService {

    private static final Logger logger = Logger.getLogger(ClubAuthServiceMap.class.getName());

    //Encryption algorithm
    @Inject
    private Pbkdf2PasswordHash passwordHash;

    //Sample user credentials in memory repository, use App users DAO on real app
    private Map<String, UserAuthInfo> users;

    @PostConstruct
    public void init() {
        //Configure encryption algorithm
        Map<String, String> parameters = new HashMap<>();
        parameters.put("Pbkdf2PasswordHash.Iterations", "3072");
        parameters.put("Pbkdf2PasswordHash.Algorithm", "PBKDF2WithHmacSHA512");
        parameters.put("Pbkdf2PasswordHash.SaltSizeBytes", "64");
        passwordHash.initialize(parameters);

        //Create sample user credentials
        users = new HashMap<>();
        users.put("22222222-B", new UserAuthInfo(encryptPassword("secreto"), new String[]{"ADMINISTRADORES"}));
        users.put("33333333-C", new UserAuthInfo(encryptPassword("secreto"), new String[]{"USUARIOS"}));
        users.put("44444444-D", new UserAuthInfo(encryptPassword("secreto"), new String[]{"ADMINISTRADORES"}));
        users.put("11111111-A", new UserAuthInfo(encryptPassword("secreto"), new String[]{"ADMINISTRADORES"}));
    }

    @Override
    /*Retrieve users credentials from persistence repository and compare with provided in parameters*/
    public boolean authUser(String username, String password) {
        boolean result = false;
        UserAuthInfo user = users.get(username);
                
        //Check password with encrypted version
        if (user != null && verifyPassword(password, user.getPassHash())) {
            result=true;
            logger.log(Level.INFO, String.format("Authenticated %s", username));
        } else {
            logger.log(Level.WARNING, String.format("Authenticated error for %s", username));
        }
        return result;
    }

    @Override
    /*Retrived set with username roles from persistence repository*/
    public Set<String> getRoles(String username) {
                
        Set<String> roles = new HashSet<>();
        if (users.containsKey(username)) {
            roles=new HashSet<>(asList(users.get(username).getRoles()));            
        }
        return roles;
    }

    @Override
    public boolean verifyPassword(String password, String hashedPassword) {

        return passwordHash.verify(password.toCharArray(), hashedPassword);
    }

    @Override
    public String encryptPassword(String password) {

        String encryptedPass = passwordHash.generate(password.toCharArray());
        logger.log(Level.INFO, "Clave cifrada: {0}", encryptedPass);
        return encryptedPass;
    }
}

//Simple temporary class for saving information in users Map
class UserAuthInfo {

    private String passHash;
    private String[] roles;

    public UserAuthInfo(String passHash, String[] roles) {
        this.passHash = passHash;
        this.roles = roles;
    }

    public String[] getRoles() {
        return roles;
    }

    public String getPassHash() {
        return passHash;
    }

}
