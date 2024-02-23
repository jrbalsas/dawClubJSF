package com.daw.club.services;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.security.enterprise.identitystore.Pbkdf2PasswordHash;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Sample Authentication service based on Pbkdf2 Algorithm
 *
 * @author jrbalsas
 */
@ApplicationScoped
public class ClubAuthService  {

    private static final Logger logger = Logger.getLogger(ClubAuthService.class.getName());

    //Encryption algorithm
    @Inject
    private Pbkdf2PasswordHash passwordHash;

    @PostConstruct
    public void init() {
        //Configure encryption algorithm
        Map<String, String> parameters = new HashMap<>();
        parameters.put("Pbkdf2PasswordHash.Iterations", "3072");
        parameters.put("Pbkdf2PasswordHash.Algorithm", "PBKDF2WithHmacSHA512");
        parameters.put("Pbkdf2PasswordHash.SaltSizeBytes", "64");
        passwordHash.initialize(parameters);
    }

    public boolean verifyPassword(String password, String hashedPassword) {

        return passwordHash.verify(password.toCharArray(), hashedPassword);
    }

    public String encryptPassword(String password) {

        String encryptedPass = passwordHash.generate(password.toCharArray());
        logger.log(Level.INFO, "Clave cifrada: {0}", encryptedPass);
        return encryptedPass;
    }
}
