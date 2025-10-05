/*
 */
package com.hhhh.group.secwealth.mktdata.starter.datasource.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.hhhh.group.secwealth.mktdata.starter.datasource.component.DynamicDatasourceHolder;

import lombok.Setter;

@Aspect
public class DatasourceAspect {

    private static final Logger logger = LoggerFactory.getLogger(DatasourceAspect.class);

    @Setter
    private String defaultExCode;

    @Pointcut("@annotation(com.hhhh.group.secwealth.mktdata.starter.datasource.aspect.annotation.SelectDatasource)")
    public void selectDatasourceAnnotationPointCut() {}

    @Before("selectDatasourceAnnotationPointCut()")
    public void beforeSelectDatasource(final JoinPoint joinPoint) throws Exception {
        final Object[] args = joinPoint.getArgs();
        if (args != null && args.length > 0) {
            final String datasource = String.valueOf(args[0]);
            if (!StringUtils.isEmpty(datasource)) {
                DynamicDatasourceHolder.putDatasource(datasource);
            } else {
                DatasourceAspect.logger.error("datasource name should not be empty");
                throw new Exception(this.defaultExCode);
            }
        } else {
            DatasourceAspect.logger.error("If you want to use @SelectDatasource, must pass datasource name as first parameter");
            throw new Exception(this.defaultExCode);
        }
    }

    @After("selectDatasourceAnnotationPointCut()")
    public void afterSelectDatasource(final JoinPoint joinPoint) {
        DynamicDatasourceHolder.removeDatasource();
    }

}
