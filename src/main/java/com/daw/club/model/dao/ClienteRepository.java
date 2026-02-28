package com.daw.club.model.dao;

import com.daw.club.model.Cliente;
import jakarta.data.repository.CrudRepository;
import jakarta.data.repository.Repository;

/** Jakarta Data repository for using in ClienteDAOJData DAO Implementation*/
@Repository
public interface ClienteRepository extends CrudRepository<Cliente,Integer> {
    
    // Inherit basic CRUD methods definitions

    // Jakarta Data operation custom definition using method name
    Cliente findByDni(String dni);
    void deleteByDni(String dni);

    // Sample Jakarta Data method definitions

    // More information in https://jakarta.ee/specifications/data/1.0/apidocs/jakarta.data/module-summary.html

    // Jakarta Data operation custom definition using JPQL query
    //@Query("Select c from Cliente c where c.dni=?1")
    //public Cliente findByNIF(String nif);

    // Jakarta Data named parameters require @param annotation
    //@Query("Select c from Cliente c where c.dni=:dni")
    //public Cliente findByNIF(@Param("dni") String nif);

    // Jakarta Data operation custom definition using Native query
    //@Query("Select count(c) from Cliente c")
    //public long cuentaClientes();

    //@Query("Select dni,nombre from Cliente")
    //public List<Objects[]> dnisClientes();

    // Jakarta Data custom method definition using lifecycle annotations
/*
    @Find
    public Cliente buscaId(Integer id);
    @Query("Select c from Cliente c")
    public List<Cliente> buscaTodos();
    @Insert
    public void crea(Cliente c);
    @Update
    public void guarda(Cliente c);
    @Delete
    public int borra(@By("id") Integer id);
    @Find
    public Cliente buscaByNIF(@By("dni") String nif);
*/

}
