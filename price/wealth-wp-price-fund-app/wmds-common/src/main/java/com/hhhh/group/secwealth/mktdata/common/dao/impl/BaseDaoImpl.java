
package com.hhhh.group.secwealth.mktdata.common.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.hhhh.group.secwealth.mktdata.common.dao.BaseDao;
import com.hhhh.group.secwealth.mktdata.common.exception.SystemException;
import com.hhhh.group.secwealth.mktdata.common.system.constants.ErrTypeConstants;
import com.hhhh.group.secwealth.mktdata.common.util.LogUtil;

@Repository
@Qualifier("baseDao")
public class BaseDaoImpl implements BaseDao {

    @PersistenceContext
    protected EntityManager entityManager;

    
    @Override
    public EntityManager getEntityManager() throws Exception {
        if (this.entityManager == null) {
            throw new SystemException(ErrTypeConstants.DB_DATA_UNAVAILABLE);
        }
        return this.entityManager;
    }

    
    @Override
    public void save(final Object entity) throws Exception {
        try {
            LogUtil.debugBeanToJson(BaseDaoImpl.class, "save Object", entity);
            this.entityManager.persist(entity);
        } catch (Exception e) {
            throw new SystemException(ErrTypeConstants.DB_DATA_UNAVAILABLE, e);
        }
    }

    
    @Override
    public Object update(final Object entity) throws Exception {
        try {
            LogUtil.debugBeanToJson(BaseDaoImpl.class, "update Object", entity);
            return this.entityManager.merge(entity);
        } catch (Exception e) {
            throw new SystemException(ErrTypeConstants.DB_DATA_UNAVAILABLE, e);
        }
    }

    
    @Override
    public <T> void delete(final Class<T> clazz, final Object id) throws Exception {
        try {
            LogUtil.debug(BaseDaoImpl.class, "clazz :{}, delete id: {}", clazz.getName(), id);
            T entity = this.entityManager.find(clazz, id);
            this.entityManager.remove(entity);
        } catch (Exception e) {
            throw new SystemException(ErrTypeConstants.DB_DATA_UNAVAILABLE, e);
        }
    }

    
    @Override
    public <T> void delete(final Class<T> clazz, final Object[] ids) throws Exception {
        try {
            T entity = null;
            for (Object id : ids) {
                LogUtil.debug(BaseDaoImpl.class, "clazz :{}, delete id: {}", clazz.getName(), id);
                entity = this.entityManager.find(clazz, id);
                this.entityManager.remove(entity);
            }
        } catch (Exception e) {
            throw new SystemException(ErrTypeConstants.DB_DATA_UNAVAILABLE, e);
        }
    }

    
    @Override
    public <T> T getById(final Class<T> clazz, final Object id) throws Exception {
        try {
            T entity = this.entityManager.find(clazz, id);
            LogUtil.debugBeanToJson(BaseDaoImpl.class, "getById Object", entity);
            return entity;
        } catch (Exception e) {
            throw new SystemException(ErrTypeConstants.DB_DATA_UNAVAILABLE, e);
        }
    }

    
    @SuppressWarnings("unchecked")
    @Override
    public <T> List<T> getAll(final Class<T> clazz) throws Exception {
        try {
            String className = clazz.getSimpleName();
            StringBuffer jpql = new StringBuffer("select t from ");
            jpql.append(className).append(" t ");
            List<T> list = this.entityManager.createQuery(jpql.toString()).getResultList();
            LogUtil.debugBeanToJson(BaseDaoImpl.class, "getAll Object", list);
            return list;
        } catch (Exception e) {
            throw new SystemException(ErrTypeConstants.DB_DATA_UNAVAILABLE, e);
        }
    }

    
    @SuppressWarnings("unchecked")
    @Override
    public <T> List<T> find(final String jpql, final Object... objects) throws Exception {
        try {
            Query query = this.entityManager.createQuery(jpql);
            if (objects != null) {
                if (objects != null) {
                    for (int i = 0; i < objects.length; i++) {
                        query.setParameter(i, objects[i]);
                    }
                }
            }
            List<T> list = query.getResultList();
            LogUtil.debugBeanToJson(BaseDaoImpl.class, "find Object", list);
            return list;
        } catch (Exception e) {
            throw new SystemException(ErrTypeConstants.DB_DATA_UNAVAILABLE, e);
        }
    }

    
    @Override
    public int executeJpql(final String jpql, final Object... objects) throws Exception {
        try {
            Query query = this.entityManager.createQuery(jpql);
            if (objects != null) {
                for (int i = 0; i < objects.length; i++) {
                    query.setParameter(i, objects[i]);
                }
            }
            int i = query.executeUpdate();
            LogUtil.debug(BaseDaoImpl.class, "executeJpql Object: {}", i);
            return i;
        } catch (Exception e) {
            throw new SystemException(ErrTypeConstants.DB_DATA_UNAVAILABLE, e);
        }
    }

    
    @Override
    public int createNativeQueryUpdate(final String sql, final Object... objects) throws Exception {
        try {
            Query query = this.entityManager.createNativeQuery(sql);
            if (objects != null) {
                for (int i = 0; i < objects.length; i++) {
                    query.setParameter(i, objects[i]);
                }
            }
            int i = query.executeUpdate();
            LogUtil.debug(BaseDaoImpl.class, "createNativeQueryUpdate Object: {}", i);
            return i;
        } catch (Exception e) {
            throw new SystemException(ErrTypeConstants.DB_DATA_UNAVAILABLE, e);
        }
    }

    
    @SuppressWarnings("rawtypes")
    @Override
    public List executeNativeSqlQuery(final String sql, final Object... objects) throws Exception {
        try {
            Query query = this.entityManager.createNativeQuery(sql);
            if (objects != null) {
                for (int i = 0; i < objects.length; i++) {
                    query.setParameter(i, objects[i]);
                }
            }
            List list = query.getResultList();
            LogUtil.debugBeanToJson(BaseDaoImpl.class, "executeNativeSqlQuery Object", list);
            return list;
        } catch (Exception e) {
            throw new SystemException(ErrTypeConstants.DB_DATA_UNAVAILABLE, e);
        }
    }

    
    @SuppressWarnings("rawtypes")
    @Override
    public boolean existsById(final Class<?> clazz, final String idAttributeNames, final Object id) throws Exception {
        try {
            String className = clazz.getSimpleName();
            StringBuilder sb = new StringBuilder("select count(*) from ");
            sb.append(className).append(" t").append(" where t.").append(idAttributeNames).append(" = :id");
            Query query = this.entityManager.createQuery(sb.toString());
            query.setParameter("id", id);
            List list = query.getResultList();
            if (list != null && !list.isEmpty()) {
                Long counts = (Long) list.get(0);
                if (counts > 0) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            throw new SystemException(ErrTypeConstants.DB_DATA_UNAVAILABLE, e);
        }
    }
}
