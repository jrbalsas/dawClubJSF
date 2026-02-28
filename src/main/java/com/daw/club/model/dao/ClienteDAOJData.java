/*Sample JPA DAO implementation*/
package com.daw.club.model.dao;

import com.daw.club.model.Cliente;
import com.daw.club.qualifiers.DAOJData;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**  DAO Implementation using Jakarta Data Repository
 *
 * @author jrbalsas
 */
@Dependent  //Elegible for Dependency Injection
@DAOJData
public class ClienteDAOJData implements ClienteDAO {

    private final Logger logger = Logger.getLogger(ClienteDAOJData.class.getName());

    @Inject
    private ClienteRepository clientesRepo;

    public ClienteDAOJData() {
    }

    @Override
    public Cliente buscaId(Integer id) {
        Cliente c=null;

        try {
            c=clientesRepo.findById(id).orElse(null);
        } catch (Exception ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return c;
    }
    @Override
    public List<Cliente> buscaTodos() {
        List<Cliente> lc = null;
        try {
            lc = clientesRepo.findAll().toList();

        } catch (Exception ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
        }

        return lc;
    }

    public Cliente buscaByNIF(String dni) {
        Cliente c = null;
        try {
            c=clientesRepo.findByDni(dni);
        } catch (Exception ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return c;
    }

    @Override
    public boolean crea(Cliente c) {
        boolean creado = false;
        try {
            clientesRepo.insert(c);
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
            //clientesRepo.guarda(c);
            clientesRepo.save(c);
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
            //borrado=clientesRepo.borra(id)==1?true:false;
            clientesRepo.deleteById(id);
            borrado=true;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return borrado;
    }

    //Sample method for calling Jakarta Data Repository method using Native query
    /*
    public List<String> buscaNIFs() {
        List<String> l = new ArrayList<>();
        try {

            List<Objects[]> lt = clientesRepo.dnisClientes();
            for (Object[] o : lt) {
                //Access fields using ordinal position
                l.add( o[0].toString() );
            }

        } catch (Exception ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return l;
    }*/

}
