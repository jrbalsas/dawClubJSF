package com.daw.club;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Default;
import jakarta.faces.annotation.FacesConfig;
import jakarta.inject.Named;
import jakarta.security.enterprise.authentication.mechanism.http.BasicAuthenticationMechanismDefinition;
import jakarta.security.enterprise.authentication.mechanism.http.CustomFormAuthenticationMechanismDefinition;
import jakarta.security.enterprise.authentication.mechanism.http.FormAuthenticationMechanismDefinition;
import jakarta.security.enterprise.authentication.mechanism.http.LoginToContinue;
import jakarta.security.enterprise.identitystore.DatabaseIdentityStoreDefinition;
import jakarta.security.enterprise.identitystore.Pbkdf2PasswordHash;
import org.glassfish.soteria.identitystores.annotation.Credentials;
import org.glassfish.soteria.identitystores.annotation.EmbeddedIdentityStoreDefinition;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * Configure App and JEE Security API
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

@FacesConfig //Required for using JSF 2.3 features
@Named("app")
@Default
@ApplicationScoped
public class AppConfig {

    private Properties appProperties;
    private final Logger logger = Logger.getLogger(AppConfig.class.getName());

    public AppConfig() {
    }

    @PostConstruct
    public void init() {
        //Load application properties file
        try (InputStream inputStream = this.getClass().getResourceAsStream("/application.properties")) {
            appProperties = new Properties();
            appProperties.load(inputStream);
        } catch (IOException e) {
            logger.severe("No se ha podido abrir el fichero de configuración application.properties");
        }

        if (!appProperties.containsKey("appFilesFolder")) {
            //Set default folder if it is not set on application.properties
            appProperties.setProperty("appFilesFolder", System.getProperty("user.home") + "/club" );
        }
        //Create external folders
        initFolder(getProperty("appFilesFolder")); //Create root folder for external files
        logger.info("Application external files path: " + appProperties.getProperty("appFilesFolder"));
        initFolder(getProperty("appFilesFolder")+getProperty("customer.images"));  //Create subfolder for customer images
        logger.info("Customer images subfolder: " + appProperties.getProperty("customer.images"));

    }

    public String getProperty(String property) {
        return appProperties.getProperty(property);
    }

    /** Create folder if not exists
     * @return true if folder is created
     */
    public boolean initFolder(String folderPath) {

        Path path = Paths.get(folderPath);
        boolean created = false;
        if (!Files.exists(path)) {
            try {
                Files.createDirectory(path);
                logger.info("Created folder: " + folderPath);
                created = true;
            } catch (IOException ex) {
                logger.severe("Error creating folder: " + ex.getMessage());
            }
        }
        return created;
    }

}
