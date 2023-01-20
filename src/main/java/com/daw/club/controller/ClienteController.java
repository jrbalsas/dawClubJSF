package com.daw.club.controller;

import com.daw.club.model.Cliente;
import com.daw.club.model.dao.ClienteDAO;
import com.daw.club.qualifiers.DAOJpa;
import com.daw.club.qualifiers.DAOMap;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named(value = "clienteCtrl")
@ViewScoped
public class ClienteController implements Serializable {

    private static final long serialVersionUID = 1L;

    private final Logger logger = Logger.getLogger(ClienteController.class.getName());

    //Business logic
    //@Inject @DAOJpa   //JPA DAO implementation
    @Inject @DAOMap     //Inject DAO Map testing implementation
    private ClienteDAO clienteDAO;

    @Inject
    FacesContext fc;
    
    //View-Model
    private Cliente cliente;

    public ClienteController() {
    }

    @PostConstruct
    public void init() {
        //init  model-view
        cliente = new Cliente();
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
        return "listado";
    }

    //ACTIONS for listado.xhtml view
    public String borra(Cliente cliente) {
        clienteDAO.borra(cliente.getId());
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
