/*Sample JPA DAO implementation*/

package com.daw.club.model.dao;

import com.daw.club.model.Cliente;
import com.daw.club.qualifiers.DAOJpa;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaQuery;

/**
 *
 * @author jrbalsas
 */
@Dependent  //Elegible for Dependency Injection
@DAOJpa
public class ClienteDAOJPA implements ClienteDAO, Serializable {

    //@PersistenceContext(unitName = "ClubPU") //Only for JEE full application servers
    @Inject   //For servlet containers, e.g. Tomcat, inject using CDI @Produces methods
    private EntityManager em;

    public ClienteDAOJPA() {
        //EntityManager creation not needed, i.e. injected with CDI
//                em=Persistence
//                .createEntityManagerFactory("ClubPU")
//                .createEntityManager();
    }

    
    @Override
    public Cliente buscaId(Integer id) {
            return em.find(Cliente.class, id);
    }

    @Override
    public List<Cliente> buscaTodos() {
        List<Cliente> lc=null;
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Cliente.class));
            Query q = em.createQuery(cq);
            lc=q.getResultList();
        } catch (Exception ex) {
            Logger.getLogger(ClienteDAOJPA.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        return lc;
    }

    @Override
    public boolean crea(Cliente c) {
        boolean creado = false;
        try {
            em.getTransaction().begin();
            em.persist(c);
            em.getTransaction().commit();
            creado = true;
        }  catch (Exception ex) {
            Logger.getLogger(ClienteDAOJPA.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        return creado;
    }

    @Override
    public boolean guarda(Cliente c) {
        boolean guardado = false;
        try {
            em.getTransaction().begin();
            c = em.merge(c);
            em.getTransaction().commit();
            guardado = true;
        } catch (Exception ex) {
            Logger.getLogger(ClienteDAOJPA.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        } 
        return guardado;
    }

    @Override
    public boolean borra(Integer id) {
        boolean borrado = false;
        try {
            em.getTransaction().begin();
            Cliente c = null;
            try {
                c = em.getReference(Cliente.class, id);
                c.getId();
            } catch (EntityNotFoundException ex) {
                Logger.getLogger(ClienteDAOJPA.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);

            }
            em.remove(c);
            em.getTransaction().commit();
            borrado = true;
        }  catch (Exception ex) {
            Logger.getLogger(ClienteDAOJPA.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        return borrado;
    }

}
