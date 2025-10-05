/*
 */
package com.dummy.wpc.datadaptor.reader;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemStream;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;

import com.dummy.wpc.datadaptor.mapper.IMapper;

/**
 * <p><b>
 * Insert description of the class's responsibility/role.
 * </b></p>
 */
public interface IReader extends ItemReader, ItemStream, InitializingBean {
    public void setThrowErrWhenReadFailed(boolean throwErrWhenReadFailed);
    public void setResource(Resource resource);
    public void setMapper(IMapper mapper);

}
