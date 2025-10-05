/*
 */
package com.hhhh.group.secwealth.mktdata.starter.validation.service;

import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.groups.Default;

import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;

import com.hhhh.group.secwealth.mktdata.starter.base.args.ArgsHolder;
import com.hhhh.group.secwealth.mktdata.starter.validation.bean.InvalidResult;
import com.hhhh.group.secwealth.mktdata.starter.validation.constant.Constant;
import com.hhhh.group.secwealth.mktdata.starter.validation.entity.ValidationEntity;
import com.hhhh.group.secwealth.mktdata.starter.validation.entity.ValidationEntitys;
import com.hhhh.group.secwealth.mktdata.starter.validation.util.converter.CastorConverter;

import lombok.Setter;

public class ValidationService {

    private Map<String, ValidationEntity> validationEntityMap;

    @Setter
    private boolean enabled;

    @Setter
    private String path;

    @Setter
    private String mapping;

    @Setter
    private String config;

    @Setter
    private String scanning;

    @Setter
    private String entityKey;

    @Setter
    private Validator validator;

    @PostConstruct
    public void init() throws Exception {
        if (this.enabled) {
            final ResourceLoader loader = new DefaultResourceLoader();
            final URL mappingURL = loader.getResource(this.path + this.mapping).getURL();
            final URL configURL = loader.getResource(this.path + this.config).getURL();
            final ValidationEntitys entitys =
                (ValidationEntitys) CastorConverter.convertXMLToBean(mappingURL, configURL, ValidationEntitys.class, true);
            entitys.initValidationEntityMap();
            this.validationEntityMap = entitys.getValidationEntityMap();
        } else {
            this.validationEntityMap = new HashMap<>();
        }
    }

    public List<InvalidResult> doValidate(final Object obj) throws Exception {
        return validate(obj, getGroups(String.valueOf(ArgsHolder.getArgs(this.entityKey))));
    }

    private List<Class<?>> getGroups(final String entityKey) throws Exception {
        final List<Class<?>> result = new ArrayList<>();
        result.add(Default.class);

        final ValidationEntity entity = this.validationEntityMap.get(entityKey);
        if (entity != null) {
            final List<String> groups = entity.getGroups();
            for (final String group : groups) {
                final String validationGroup = this.scanning + Constant.SYMBOL_DOT + group;
                result.add(Class.forName(validationGroup));
            }
        }
        return result;
    }

    private List<InvalidResult> validate(final Object obj, final List<Class<?>> groups) {
        final Set<ConstraintViolation<Object>> set = this.validator.validate(obj, groups.toArray(new Class[groups.size()]));
        final List<InvalidResult> results = new ArrayList<>();
        for (final ConstraintViolation<Object> constraintViolation : set) {
            final Class<? extends Annotation> annotation =
                constraintViolation.getConstraintDescriptor().getAnnotation().annotationType();
            final String annotationName = annotation.getSimpleName();
            final String message = constraintViolation.getMessage();
            results.add(new InvalidResult(annotationName, message));
        }
        return results;
    }

}
