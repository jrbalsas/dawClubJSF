package com.daw.club.model.dao;

import com.daw.club.model.Cliente;
import com.daw.club.qualifiers.DAOMap;
import jakarta.enterprise.context.ApplicationScoped;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ApplicationScoped      //Elegible for Dependency Injection
@DAOMap
public class ClienteDAOMap implements ClienteDAO, Serializable{

    private  Map<Integer, Cliente> clientes;
    private  Integer idCliente = 1;

    public ClienteDAOMap() {
        clientes = new HashMap<>();
    }
    
    @Override
    public Cliente buscaId(Integer id) {
        Cliente localizado = clientes.get(id);
        if (localizado != null) localizado= new Cliente(localizado);                
        return localizado;
    }
    @Override
    public List<Cliente> buscaTodos() {
        //return new ArrayList<Cliente>(clientes.values()); JDK<8
        return clientes.values().stream().collect(Collectors.toList());
    }

    @Override
    public boolean crea(Cliente c) {
        Cliente nc=new Cliente(c);
        nc.setId(idCliente);
        clientes.put(idCliente, nc);
        c.setId(idCliente);
        idCliente++;
        return true;
    }
        
    @Override
    public boolean guarda(Cliente c) {
        boolean result=false;
        if (clientes.containsKey(c.getId())) {
            Cliente nc=new Cliente(c);
            clientes.replace(c.getId(),nc);
            result=true;
        }       
        return result;
    }
    
    @Override
    public boolean borra(Integer id) {
        boolean result=false;
        if (clientes.containsKey(id)) {
            clientes.remove(id);
            result = true;
        }
        return result;
    }
    
    public int numClientes() {
        return clientes.size();
    }

    @Override
    public Cliente buscaByNIF(String nif) {
        //return clientes.values().stream().filter( c -> c.getDni().equals(nif)).findAny().orElse(null);
        Cliente localizado = null;
        for (Cliente c: clientes.values()) {
            if (c.getDni().equals(nif)) {
                localizado=c;
                break;
            }            
        }
        if (localizado!=null) localizado=new Cliente(localizado);
        return localizado;
    }

}
