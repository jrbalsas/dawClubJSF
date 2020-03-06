package com.daw.club.model.dao;

import com.daw.club.model.Cliente;
import java.util.List;

public interface ClienteDAO extends GenericDAO<Cliente,Integer>{
    //Declare here specific methods for EntityDAO
    public Cliente buscaByNIF(String nif);       
}
