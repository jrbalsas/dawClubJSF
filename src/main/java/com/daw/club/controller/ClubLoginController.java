package com.daw.club.controller;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.security.enterprise.AuthenticationStatus;
import jakarta.security.enterprise.SecurityContext;
import jakarta.security.enterprise.authentication.mechanism.http.AuthenticationParameters;
import jakarta.security.enterprise.credential.Credential;
import jakarta.security.enterprise.credential.UsernamePasswordCredential;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

/** Controller for Logout action and Custom Form Login autentication
 *
 * @author jrbalsas
 */
@ViewScoped
@Named
public class ClubLoginController implements Serializable{

    private static final Logger logger = Logger.getLogger(ClubLoginController.class.getName());
    
    @Inject
    FacesContext fc;
    
    //SecurityContext and ExternalContext needed for programatic authentication
    @Inject
    SecurityContext sc;
    
    @Inject
    ExternalContext ec;
    
    @Inject
    HttpServletRequest request; //needed for logout
        
    //model-view
    @Size(min=4, max=16, message = "Identificador entre {min} y {max} caracteres")
    private String username;
    
    @NotEmpty (message = "Introduzca una clave válida")
    private String password;  
    
    public String getUsername() {
        return username;
    }

    public void setUsername(String login) {
        this.username = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    //Action for customlogin.xhtml form
    public String login() {
        String view="";
        
        //Prepare data for programatic authentication
        AuthenticationParameters ap = new AuthenticationParameters();
        Credential credentials= new UsernamePasswordCredential(username,password);
        
        ap.credential(credentials).newAuthentication(true);       

        HttpServletResponse response=(HttpServletResponse)ec.getResponse();
        
        //Programatic authentication
        if (sc.authenticate(request, response, ap) == AuthenticationStatus.SUCCESS) {
            view="/cliente/index?faces-redirect=true";
            logger.log(Level.INFO,"Usuario autenticado");
        } else {
            fc.addMessage("", new FacesMessage(FacesMessage.SEVERITY_WARN, "Error de autenticación", ""));
            logger.log(Level.WARNING,"Error de autenticación");
        }                
        
        return view;
    }
    
    //Sample logout action for Form/CustomForm Authentication
    public String logout() throws ServletException {
        request.logout();
        request.getSession().invalidate();
        //ec.invalidateSession();        
        return "/index?faces-redirect=true";

    }
    
}
