/*Sample JPA DAO implementation*/
package com.daw.club.model.dao;

import com.daw.club.model.Cliente;
import com.daw.club.qualifiers.DAOJpa;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

/**
 *
 * @author jrbalsas
 */
@RequestScoped  //Elegible for Dependency Injection
@DAOJpa
@Transactional  //Application Server automatically manages EntityManager transaction in every method
public class ClienteDAOJPA implements ClienteDAO, Serializable {

    private final Logger logger = Logger.getLogger(ClienteDAOJPA.class.getName());

    @PersistenceContext(unitName = "ClubPU") //Only for JEE full application servers
                                             //Requires to enable Persistence-unit in persistence.xml
    private EntityManager em;

    public ClienteDAOJPA() {
    }

    @Override
    public Cliente buscaId(Integer id) {
        Cliente c=null;
        try {
            c=em.find(Cliente.class, id);
        } catch (Exception ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);

        };
        return c;
    }
    @Override
    public List<Cliente> buscaTodos() {
        List<Cliente> lc = null;
        try {
            Query q = em.createQuery("Select c from Cliente c", Cliente.class);
            lc = q.getResultList();
        } catch (Exception ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return lc;
    }

    public Cliente buscaByNIF(String dni) {
        Cliente c = null;
        try {
            TypedQuery<Cliente> q = em.createQuery("Select c from Cliente c where c.dni=:dni",Cliente.class);
            q.setParameter("dni", dni);
            c = q.getSingleResult();
        } catch (Exception ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return c;
    }

    /** Sample nativeQuery method*/
    public List<String> buscaNIFs() {
        List<String> l = new ArrayList<>();
        try {
            Query q = em.createNativeQuery("Select dni,nombre from Cliente");
            //No maping entity
            List<Objects[]> lt = q.getResultList();
            for (Object[] o : lt) {
                //Access fields using ordinal position
                l.add( o[0].toString() );
            }
        } catch (Exception ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return l;
    } 

    @Override
    public boolean crea(Cliente c) {
        boolean creado = false;
        try {
            em.persist(c);
            creado = true;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return creado;
    }

    @Override
    public boolean guarda(Cliente c) {
        boolean guardado = false;
        try {
            c = em.merge(c);
            guardado = true;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return guardado;
    }

    @Override
    public boolean borra(Integer id) {
        boolean borrado = false;
        try {
            Cliente c = null;
            c = em.find(Cliente.class, id);
            em.remove(c);
            borrado = true;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return borrado;
    }

}
