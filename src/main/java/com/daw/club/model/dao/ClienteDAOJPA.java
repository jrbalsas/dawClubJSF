/*Sample JPA DAO implementation*/
package com.daw.club.model.dao;

import com.daw.club.model.Cliente;
import com.daw.club.qualifiers.DAOJpa;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaQuery;
import javax.transaction.Transactional;
import static javax.transaction.Transactional.TxType.REQUIRED;

/**
 *
 * @author jrbalsas
 */
@RequestScoped  //Elegible for Dependency Injection
@DAOJpa
public class ClienteDAOJPA implements ClienteDAO, Serializable {

    private final Logger logger = Logger.getLogger(ClienteDAOJPA.class.getName());

    //@Inject   //For servlet containers, e.g. Tomcat, inject using CDI @Produces methods    
    @PersistenceContext(unitName = "ClubPU") //Only for JEE full application servers
                                             //Requires to enable Persistence-unit in persistence.xml
    private EntityManager em;

    public ClienteDAOJPA() {
    }

    @Override
    public Cliente buscaId(Integer id) {
        return em.find(Cliente.class, id);
    }

    @Override
    public List<Cliente> buscaTodos() {
        List<Cliente> lc = null;
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Cliente.class));
            Query q = em.createQuery(cq);
            lc = q.getResultList();
        } catch (Exception ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return lc;
    }

    @Override
    @Transactional(REQUIRED)
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
    @Transactional(REQUIRED)
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
    @Transactional(REQUIRED)
    public boolean borra(Integer id) {
        boolean borrado = false;
        try {
            Cliente c = null;
            try {
                c = em.getReference(Cliente.class, id);
                c.getId();
            } catch (EntityNotFoundException ex) {
                logger.log(Level.SEVERE, ex.getMessage(), ex);

            }
            em.remove(c);
            borrado = true;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return borrado;
    }

}
