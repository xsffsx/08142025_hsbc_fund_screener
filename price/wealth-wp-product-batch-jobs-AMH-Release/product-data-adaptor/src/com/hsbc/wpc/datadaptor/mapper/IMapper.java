/*
 */
package com.dummy.wpc.datadaptor.mapper;

import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFRow;

/**
 * <p><b>
 * Insert description of the class's responsibility/role.
 * </b></p>
 */
public interface IMapper {

    /**
     * <p><b>
     * Insert description of the method's responsibility/role.
     * </b></p>
     *
     * @param jobCode
     */
    public void setJobCode(String jobCode);
    public Object maprow(HSSFRow row, Map titleMap) throws Exception;

}
