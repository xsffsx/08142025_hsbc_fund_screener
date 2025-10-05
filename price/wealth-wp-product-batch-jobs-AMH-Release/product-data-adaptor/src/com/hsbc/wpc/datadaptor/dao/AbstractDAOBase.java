/*
 */
package com.dummy.wpc.datadaptor.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import com.dummy.wpc.common.exception.WPCBaseException;
import com.dummy.wpc.common.tng.TNGMessage;
import com.dummy.wpc.datadaptor.util.DaoUtils;

/**
 * <p>
 * <b> Insert description of the class's responsibility/role. </b>
 * </p>
 */
public abstract class AbstractDAOBase {

    
	protected DataSource dataSource;
    protected SimpleJdbcTemplate jdbcTemplate;
    protected SimpleJdbcInsert simpleJdbcInsert; 

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        this.jdbcTemplate = new SimpleJdbcTemplate(dataSource);
        if (StringUtils.isNotBlank(getTableName())) {
            this.simpleJdbcInsert = DaoUtils.getSimpleJdbcInsert(dataSource, getTableName());
        }
        initResource(dataSource);
    }

    /**
     * 
     * This method used for subclass initialize other resource, such as some
     * class use SimpleJdbcInsert, then will be need dataSource to initialize.
     * Subclass just need override this method if has other resource to
     * initialize.
     * 
     * @param dataSource
     *            dataSouce
     */
    protected void initResource(DataSource dataSource) {
        // nothing to do
    }

    /**
     * This method just used for subclass retrieve DB connection. If no this
     * requirement, remove it.
     * 
     * @return DB Connection
     * @throws WPCBaseException
     *             retrieve connection error
     */
    protected Connection getConnection() throws WPCBaseException {
        final String m = "getConnection";
        try {
            return this.dataSource.getConnection();
        } catch (SQLException e) {
        	 TNGMessage.logTNGMsg("1020",
                     "E",
                     "Fail get DB Connection.");
            throw new WPCBaseException(this.getClass(), m, e.getMessage(), e);
        }
    }
    
    public int update(String querySql, Object dto) throws WPCBaseException{
        try {
            SqlParameterSource args = new BeanPropertySqlParameterSource(dto);
            return jdbcTemplate.update(querySql, args);
        } catch (DataAccessException e) {
            throw new WPCBaseException(this.getClass(), "update", e.getMessage(), e);
        }
    }
    
    @SuppressWarnings("unchecked")
    public <T> List<T> retrieve(String querySql, T dto) throws WPCBaseException{
        try {
            SqlParameterSource args = new BeanPropertySqlParameterSource(dto);
            return (List<T>) jdbcTemplate.query(querySql, ParameterizedBeanPropertyRowMapper.newInstance(dto.getClass()), args);
        } catch (DataAccessException e) {
            throw new WPCBaseException(this.getClass(), "update", e.getMessage(), e);
        }
    }

    
    public int insert(Object dto) throws WPCBaseException{
        
        if (simpleJdbcInsert == null) {
            throw new WPCBaseException(this.getClass(), "insert", "simpleJdbcInsert init fail with table name ["
                + getTableName() + "], implements method getTableName()");
        }

        try {
            SqlParameterSource source = new BeanPropertySqlParameterSource(dto);
            return simpleJdbcInsert.execute(source);
        } catch (DataAccessException e) {
            throw new WPCBaseException(this.getClass(), "insert", e.getMessage(), e);
        }
    }
    

    public int[] batchInsert(Object[] dtos) throws WPCBaseException{
        
        if (simpleJdbcInsert == null) {
            throw new WPCBaseException(this.getClass(), "insert", "simpleJdbcInsert init fail with table name ["
                + getTableName() + "], implements method getTableName()");
        }
        
        try {
            SqlParameterSource[] sources = new BeanPropertySqlParameterSource[dtos.length];
            for (int index = 0; index < dtos.length; index++) {
                sources[index] = new BeanPropertySqlParameterSource(dtos[index]);
            }
            return simpleJdbcInsert.executeBatch(sources);
        } catch (DataAccessException e) {
            throw new WPCBaseException(this.getClass(), "batchInsert", e.getMessage(), e);
        }
    }
    
    public String getTableName() {
        return "";
    }

}
