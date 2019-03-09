package com.daw.club.controller;

import com.daw.club.model.Cliente;
import com.daw.club.model.dao.ClienteDAO;
import com.daw.club.qualifiers.DAOJdbc;
import com.daw.club.qualifiers.DAOJpa;
import com.daw.club.qualifiers.DAOList;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

@Named(value="clienteCtrl")
@ViewScoped
public class ClienteController implements Serializable {
     private static final long serialVersionUID = 1L;

    private final Logger logger = Logger.getLogger(ClienteController.class.getName());
     
    //Business logic
    //@Inject @DAOJdbc    //Inject DAO JDBC Implementation
    //@Inject @DAOJpa       //JPA DAO implementation
    @Inject @DAOList  //Inject DAO ArrayList testing implementation         
     private ClienteDAO clienteDAO;
   
    //View-Model
    private Cliente c;
    private List<Cliente> lc;
    private int editRow=0;      //current client editable
    
    public ClienteController() {
    }
    
    @PostConstruct
    public void init() {
        c=new Cliente();
        lc=null;
    }
    
    //Model access
    public List<Cliente> getClientes() {
        //Get clientes from DAO only when creating Controller
        //on entering ViewScope. Next calls reuse data if continue
        //in ViewScope p.e. generating list view
        if (lc==null)
                lc=clienteDAO.buscaTodos();
        return lc;
    }
//    public void setClientes (List<Cliente> lc) {
//        this.lc=lc;
//    }

    public Cliente getCliente () {
        return c;
    }
    public void setCliente (Cliente c) {
        this.c=c;
    }
    
    //ACTIONS for visualiza, crea, edit and borra views
    
    /** Get client from id param*/
    public void recupera() {
        Cliente clienteActual;
        clienteActual=clienteDAO.buscaId(c.getId());
        if (clienteActual!=null) {
             c=clienteActual;
        } else {
            c=new Cliente();
        }
    }
    /** Create a new Client from model data*/
    public String crea() {
        c.setId(0);
        clienteDAO.crea(c); 
        //Post-Redirect-Get
        return "visualiza?faces-redirect=true&id="+c.getId();
    }
    /** Update current model client to DAO*/    
    public String guarda() {
//      Programatic validation
//        if (valida(c.getDni())==false) {
//            FacesContext fc=FacesContext.getCurrentInstance();
//            fc.addMessage("formCliente:idDNI", new FacesMessage("La letra no coincide con el DNI"));
//            return ""; //Stay on view to correct error
//        } 
        clienteDAO.guarda(c);
        return "visualiza?faces-redirect=true&id="+c.getId();
    }        
    /** Delete current model data client*/
    public String borra() {        
        clienteDAO.borra(c.getId());
        return "listado";
    }

    //ACTIONS for listado.xhtml view
    
    public String borra(Cliente cliente) {
        clienteDAO.borra(cliente.getId());
        return "listado";
    }
    
    //ACTIONS for listado_din.xhtml view

    /**Check if current row has edit mode enabled */
    public boolean isEditable(int row) {
        return editRow==row;
    }

    /**Enable row edit model*/
    public void setEditRow(int row) {
        this.editRow = row;
    }

    /** Update current row from table to DAO*/    
    public void guarda(Cliente cliente) {

        clienteDAO.guarda(cliente);
        editRow=0;
        //force get data from DAO (see getClientes() )
        lc=null;
    }        
    
    //Sample logout action
    public String logout() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        facesContext.getExternalContext().invalidateSession();
        return "/index?faces-redirect=true";
        
        
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
