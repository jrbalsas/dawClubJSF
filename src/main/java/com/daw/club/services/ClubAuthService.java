package com.daw.club.services;

import java.util.Set;

/** Service interface for managing user credentials and authentication
 *
 * @author jrbalsas
 */
public interface ClubAuthService {
    
    boolean authUser(String username, String password);    

    Set<String> getRoles(String username);
    
    String encryptPassword(String password);

    boolean verifyPassword(String password, String hashedPassword);
    
}
