package com.dummy.wpc.datadaptor.validation;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springmodules.validation.valang.CustomPropertyEditor;
import org.springmodules.validation.valang.parser.SimpleValangBased;
import org.springmodules.validation.valang.parser.ValangParser;
import org.springmodules.validation.valang.predicates.ValidationRule;

public class DefaultValidator extends SimpleValangBased implements Validator, InitializingBean {
	private static Logger log = Logger.getLogger(DefaultValidator.class);
	private Resource resource;

	private String valang = null;

	private Collection customPropertyEditors = null;

	private Collection rules = null;
	private boolean initialized = false;

	public DefaultValidator() {
		super();
	}

	public void afterPropertiesSet() throws Exception {
	}

	public boolean supports(Class clazz) {
		return true;
	}

	private void initRules() {

		if (initialized == true) {
			return;
		}
		if (initialized == false) {
			initialized = true;
		}

		File ruleFile = null;
		BufferedReader br = null;
		try {
			if (!resource.exists()) {
				log.error("Rule configuration does not exist, the validator will be ignored.");
				return;
			}

			ruleFile = resource.getFile();
			this.valang = org.apache.commons.lang.StringUtils.trimToEmpty(FileUtils.readFileToString(ruleFile));
			if(org.apache.commons.lang.StringUtils.isEmpty(this.valang)){
			    log.error("Rule is empty, the validator will be ignored.");
                return;
			}else{
			    ValangParser parser = createValangParser(this.valang);
			    rules = parser.parseValidation();
			}
		} catch (Exception e) {
			log.error("Rule parse validation exception.Cannot initial the rules, the validator will be ignored.",e);
		} finally {
			try {
				if (br != null) {
					br.close();
				}
			} catch (IOException e) {
				log.error("Rule reading exception.Cannot initial the rules, the validator will be ignored.",e);
			}
		}

	}

	public void validate(Object target, Errors errors) {
		initRules();
		if (rules == null) {
			return;
		}
		BeanWrapper beanWrapper = (target instanceof BeanWrapper) ? (BeanWrapper) target : new BeanWrapperImpl(target);

		if (getCustomPropertyEditors() != null) {
			for (Iterator iter = getCustomPropertyEditors().iterator(); iter.hasNext();) {
				CustomPropertyEditor customPropertyEditor = (CustomPropertyEditor) iter.next();

				if (customPropertyEditor.getRequiredType() == null) {
					throw new IllegalArgumentException("[requiredType] is required on CustomPropertyEditor instances!");
				} else if (customPropertyEditor.getPropertyEditor() == null) {
					throw new IllegalArgumentException("[propertyEditor] is required on CustomPropertyEditor instances!");
				}

				if (StringUtils.hasLength(customPropertyEditor.getPropertyPath())) {
					beanWrapper.registerCustomEditor(customPropertyEditor.getRequiredType(), customPropertyEditor.getPropertyPath(), customPropertyEditor.getPropertyEditor());
				} else {
					beanWrapper.registerCustomEditor(customPropertyEditor.getRequiredType(), customPropertyEditor.getPropertyEditor());
				}
			}
		}

		for (Iterator iter = rules.iterator(); iter.hasNext();) {
			ValidationRule rule = (ValidationRule) iter.next();
			rule.validate(beanWrapper, errors);
		}
	}

	public Resource getResource() {
		return resource;
	}

	public void setResource(Resource resource) {
		this.resource = resource;
	}

	public void setValang(String valang) {
		this.valang = valang;
	}

	public void setCustomPropertyEditors(Collection customPropertyEditors) {
		this.customPropertyEditors = customPropertyEditors;
	}

	public void setDateParserRegistrations(Map dateParserRegistrations) {
		setDateParsers(dateParserRegistrations);
	}

	private Collection getCustomPropertyEditors() {
		return this.customPropertyEditors;
	}

	private String getValang() {
		return this.valang;
	}

	public Collection getRules() {
		return rules;
	}
}