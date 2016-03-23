package com.daw.club.model.dao;

import java.util.List;

public interface GenericDAO<T,K> {
    
    public T buscaId(K id);
    public List<T> buscaTodos();
    public boolean crea(T c);
    public boolean guarda(T c);    
    public boolean borra(K id);
}
