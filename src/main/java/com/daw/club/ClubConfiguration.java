package com.daw.club;

import com.daw.club.model.dao.ClienteDAOJDBC;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Configuration class
 *
 * @author jrbalsas
 */
@ApplicationScoped
public class ClubConfiguration {

    private EntityManagerFactory emf;

    public ClubConfiguration() {
        //Initialice EntityManagerFactory
        emf = Persistence
                .createEntityManagerFactory("ClubPU");
    }

    /**
     * EntityManager producer
     *
     * @return EntityManager for injection in DAOs
     * @note Needed in Servlet Containers, e.g. Tomcat
     */
    @Produces
    public EntityManager createEntityManager() {
        return emf.createEntityManager();
    }

    public void closeEntityManager(@Disposes EntityManager em) {
        em.close();
    }
}
