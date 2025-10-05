
package com.hhhh.group.secwealth.mktdata.common.dao;

import java.util.List;

import javax.persistence.EntityManager;

public interface BaseDao {

    public EntityManager getEntityManager() throws Exception;;

    
    public void save(Object entity) throws Exception;

    
    public Object update(Object entity) throws Exception;

    
    public <T> void delete(Class<T> clazz, Object id) throws Exception;

    
    public <T> void delete(Class<T> clazz, Object[] ids) throws Exception;

    
    public <T> T getById(Class<T> clazz, Object id) throws Exception;

    
    public <T> List<T> getAll(Class<T> clazz) throws Exception;

    
    public <T> List<T> find(String jpql, Object... objects) throws Exception;

    
    public int executeJpql(String jpql, Object... objects) throws Exception;

    public int createNativeQueryUpdate(String sql, Object... objects) throws Exception;


    @SuppressWarnings("rawtypes")
    public List executeNativeSqlQuery(String sql, Object... objects) throws Exception;

    public boolean existsById(Class<?> clazz, String idAttributeNames, Object id) throws Exception;

}
