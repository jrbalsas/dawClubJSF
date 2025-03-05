package com.daw.club;

import com.daw.club.model.Cliente;
import com.daw.club.model.dao.ClienteDAO;
import com.daw.club.qualifiers.DAOMap;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.event.Startup;
import jakarta.enterprise.inject.Default;
import jakarta.faces.annotation.FacesConfig;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.security.enterprise.authentication.mechanism.http.FormAuthenticationMechanismDefinition;
import jakarta.security.enterprise.authentication.mechanism.http.LoginToContinue;
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

    @Inject @DAOMap
    //@Inject @DAOJpa
    private ClienteDAO clienteDAO;

    public void onStartup(@Observes Startup event) {
        logger.info(">>>Inicializando aplicación");

        configureApp();
        createSampleData();
    }

    public String getProperty(String property) {
        return appProperties.getProperty(property);
    }

    private void configureApp() {
        // Load application properties file from main/resources
        try (InputStream inputStream = this.getClass().getResourceAsStream("/application.properties")) {
            appProperties = new Properties();
            appProperties.load(inputStream);
        } catch (IOException e) {
            logger.severe("No se ha podido abrir el fichero de configuración application.properties");
        }

        if (!appProperties.containsKey("app.data")) {
            //Set default add data folder if it is not set on application.properties
            appProperties.setProperty("app.data", System.getProperty("user.home") + "/webapp-data");
        }
        //Create app data subfolders
        initFolder(getProperty("app.data")); //Create root folder for external files
        logger.info("Application external files path: " + appProperties.getProperty("app.data"));
        initFolder(getProperty("app.data") + getProperty("customer.images"));  //Create subfolder for customer images
        logger.info("Customer images subfolder: " + appProperties.getProperty("customer.images"));
    }

    public void createSampleData() {
        logger.info("Creando clientes de prueba");
        //set default ciphered password to sample customers: secreto
        String nuevaClave = "PBKDF2WithHmacSHA512:3072:kN6Xy8mLfmpS15I2QQ6oww2GV8ahZGZMKi8jq8CXge7mRQtItsqXl7EJ/JSEX4I/VofdPpWqLj20mgkkk4+hZw==:phiHq1GmgmNMFusGuCsarWtbiiKKkuAs+PEla7mlrmU=";
        clienteDAO.crea(new Cliente(0, "Paco López", "11111111-A", false).setClaveCifrada(nuevaClave));
        clienteDAO.crea(new Cliente(0, "María Jiménez", "22222222-B", true).setClaveCifrada(nuevaClave));
        clienteDAO.crea(new Cliente(0, "Carlos García", "33333333-C", true).setClaveCifrada(nuevaClave));
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
