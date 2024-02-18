package com.daw.club.controller;

import com.daw.club.AppConfig;
import com.daw.club.model.Cliente;
import com.daw.club.model.ClubPrincipal;
import com.daw.club.model.dao.ClienteDAO;
import com.daw.club.qualifiers.DAOJpa;
import com.daw.club.qualifiers.DAOMap;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serial;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.security.enterprise.SecurityContext;
import jakarta.servlet.http.Part;

@Named(value = "clienteCtrl")
@ViewScoped
public class ClienteController implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private final Logger logger = Logger.getLogger(ClienteController.class.getName());

    //Business logic
    //@Inject @DAOJpa   //JPA DAO implementation
    @Inject @DAOMap     //Inject DAO Map testing implementation
    private ClienteDAO clienteDAO;

    @Inject
    FacesContext fc; //Interaction with Faces front-controller

    @Inject
    SecurityContext sc; //Information about authenticated user

    @Inject
    AppConfig appConfig;

    private Principal principal; //Current authenticated user

    private String rutaImagenes;

    //View-Model
    private Cliente cliente;
    private Part imageFile;

    public ClienteController() {
    }

    @PostConstruct
    public void init() {
        //init  model-view
        cliente = new Cliente();

        //Customer images path in filesystem
        rutaImagenes = appConfig.getProperty("app.data")+appConfig.getProperty("customer.images");

        //log authenticated user info
        String currentUserName="Anónimo";
        principal=sc.getCallerPrincipal(); //Get authenticated user info if available

        if (principal!=null) {
            currentUserName=principal.getName(); //Get username from authenticated user
        }

        if (principal instanceof ClubPrincipal) {
            //Custom authentication user info (created in ClubIdentityStore)
            currentUserName = ((ClubPrincipal) principal).getUsuario().getNombre();
        }
        logger.info(String.format("Petición de usuario %s", currentUserName ) );
    }

    //MODEL-VIEW access methods (controller bean properties)
    
    public List<Cliente> getClientes() {
        return clienteDAO.buscaTodos();
    }
//Modificacion of property from view not needed
//    public void setClientes (List<Cliente> lc) {
//        this.lc=lc;
//    }

    public Cliente getCliente() {
        return cliente;
    }

    public Part getImageFile() {
        return imageFile;
    }

    public void setImageFile(Part imageFile) {
        this.imageFile = imageFile;
    }

    //ACTIONS for visualiza, crea, edit and borra views
    /**
     * Get client from id param
     */
    public void recupera() {
        cliente = clienteDAO.buscaId(cliente.getId());
        if (cliente == null) {            
            fc.addMessage(null, new FacesMessage("El cliente indicado no existe"));
        }
    }

    /**
     * Create a new Client from model data
     */
    public String crea() {
        cliente.setId(0);
        clienteDAO.crea(cliente);
        //Post-Redirect-Get
        return "visualiza?faces-redirect=true&id=" + cliente.getId();
    }

    /**
     * Update current model client to DAO
     */
    public String guarda() {
//      Programatic validation
//        if (valida(c.getDni())==false) {
//            fc.addMessage("formCliente:idDNI", new FacesMessage("La letra no coincide con el DNI"));
//            return ""; //Stay on view to correct error
//        } 
        clienteDAO.guarda(cliente);
        return "visualiza?faces-redirect=true&id=" + cliente.getId();
    }

    /**
     * Delete current model data client
     */
    public String borra() {
        clienteDAO.borra(cliente.getId());
        //TODO Borrar imagen del cliente
        fc.addMessage(null, new FacesMessage("Cliente borrado correctamente"));
        return "listado";
    }

    //ACTIONS for listado.xhtml view
    public String borra(Cliente cliente) {
        clienteDAO.borra(cliente.getId());
        //TODO Borrar imagen del cliente
        fc.addMessage(null, new FacesMessage("Cliente borrado correctamente"));
        return "listado";
    }

    //ACTIONS for listado_din.xhtml view

    /**
     * Set current client from datatable for in-line edition
     */
    public void editRow(Cliente cliente) {

        this.cliente=cliente;
    }

    /**
     * Cancel row edit
     */
    public void cancelEditRow() {
        this.cliente = new Cliente();
    }

    /**
     * Update current client 
     */
    public void actualizaCliente() {

        clienteDAO.guarda(cliente);

        cancelEditRow();
    }

    /** Change Customer Image file
     */
    public String cambiaImagen () {

        //Image not received
        if (imageFile==null) {
            fc.addMessage("", new FacesMessage(FacesMessage.SEVERITY_WARN,"Debe seleccionarse una imagen",""));
            return "";
        }

        //Move temp file to customer images folder
        try (InputStream input = imageFile.getInputStream()) {

            String contentType = imageFile.getContentType();
            // Check if file is actually an image (avoid upload of other files by hackers!).
            // For all content types, see: https://developer.mozilla.org/en-US/docs/Web/HTTP/Basics_of_HTTP/MIME_types#image_types
            if (contentType == null || !contentType.startsWith("image")) {
                throw new RuntimeException("Not a valid Image file");
            }

            //Rename file with primary key, e.g. 1.png
            String newFileName=cliente.getId().toString()+"."+contentType.substring(6,9);

            //Remove old image file
            if (cliente.getImageFileName()!=null) {
                Path path = Path.of(rutaImagenes, cliente.getImageFileName());
                Files.deleteIfExists(path);
            }

            //Copy new file
            Path destFile=Path.of(rutaImagenes,newFileName );
            Files.copy(input, destFile, StandardCopyOption.REPLACE_EXISTING);

            //update View-Model & Model
            cliente.setImageFileName(newFileName);
            clienteDAO.cambiaImagen( cliente.getId(), newFileName );

            fc.addMessage("", new FacesMessage(FacesMessage.SEVERITY_INFO,"Imagen enviada correctamente",""));
            logger.log(Level.INFO, "Uploaded file: {0}", newFileName);
        } catch (IOException | RuntimeException e) {
            fc.addMessage("", new FacesMessage(FacesMessage.SEVERITY_WARN,"No se ha podido modificar la imagen",""));
            logger.warning("Error updating customer image file: "+e.getMessage() );
        }

        return "visualiza?faces-redirect=true&id=" + cliente.getId();
    }

    //VALIDADORES Faces. Using Bean Validation instead
//    public void validaNombre(FacesContext context, UIComponent inputNombre,
//                                Object value) {
//        String nombre=(String)value;
//        if (nombre.length()<4 || nombre.length()>25) {
//            FacesMessage message = new FacesMessage("La longitud del nombre debe estar entre 4 y 25");
//            throw new ValidatorException(message);        
//        
//        }
//    }
//
//    public void validaDni(FacesContext context, UIComponent inputDni,
//                                Object value) {
//        String dni=(String)value;
//        if (!dni.matches("\\d{7,8}-?[a-zA-Z]")) {
//            FacesMessage message = new FacesMessage("El dni no tiene el formato 12345678-A");
//            throw new ValidatorException(message);        
//        
//        }        
//    }
}
