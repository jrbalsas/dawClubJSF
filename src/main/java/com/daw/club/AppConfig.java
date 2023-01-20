package com.daw.club;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.faces.annotation.FacesConfig;
import jakarta.security.enterprise.authentication.mechanism.http.BasicAuthenticationMechanismDefinition;
import jakarta.security.enterprise.authentication.mechanism.http.CustomFormAuthenticationMechanismDefinition;
import jakarta.security.enterprise.authentication.mechanism.http.FormAuthenticationMechanismDefinition;
import jakarta.security.enterprise.authentication.mechanism.http.LoginToContinue;
import jakarta.security.enterprise.identitystore.DatabaseIdentityStoreDefinition;
import jakarta.security.enterprise.identitystore.Pbkdf2PasswordHash;
import org.glassfish.soteria.identitystores.annotation.Credentials;
import org.glassfish.soteria.identitystores.annotation.EmbeddedIdentityStoreDefinition;

/** Configure App and JEE Security API
 *
 * @author jrbalsas
 */
/*
    SELECT some IdentityStore implementations
*/
/* Soteria RI in memory IdentityStore (org.glassfish.soteria dependency needed in pom.xml */
@EmbeddedIdentityStoreDefinition({
    @Credentials(callerName = "admin", password = "secret1", groups = {"ADMINISTRADORES"}),
    @Credentials(callerName = "user", password = "secret2", groups = {"USUARIOS"})
})
/* JEE Security Database IdentityStore implementation*/
//@DatabaseIdentityStoreDefinition(
//    dataSourceLookup = "java:global/jdbc/gestClub", 
//    callerQuery = "select password from authinfo where dni = ?",
//    groupsQuery = "select rolname from authinfo where dni = ?",
//    hashAlgorithm = Pbkdf2PasswordHash.class,
//    //priorityExpression = "#{100}",
//    hashAlgorithmParameters = {
//        "Pbkdf2PasswordHash.Algorithm=PBKDF2WithHmacSHA512",
//        "Pbkdf2PasswordHash.Iterations=3072",                
//        "Pbkdf2PasswordHash.SaltSizeBytes=64"
//    }
//)
/* 
 * SELECT ONE HttpAuthenticationMchanismDefinition
 */
/* Use browser authentication dialog */
//@BasicAuthenticationMechanismDefinition(
//        realmName = "Club de Tenis"
//)
/* Estandard form validation with j_security_check action*/
@FormAuthenticationMechanismDefinition(
        loginToContinue = @LoginToContinue(
                loginPage = "/login.jsf",
                errorPage = "/login.jsf?error",
                useForwardToLogin = false
        )
)
/* Custom form validation for programatic autentication (see ClubLoginController.authenticate())*/
//@CustomFormAuthenticationMechanismDefinition(
//        loginToContinue = @LoginToContinue(
//                loginPage = "/customlogin.jsf",
//                errorPage = "",
//                useForwardToLogin = false
//        )
//)
@ApplicationScoped
@FacesConfig //Required for using JSF 2.3 features
public class AppConfig {
    
}
