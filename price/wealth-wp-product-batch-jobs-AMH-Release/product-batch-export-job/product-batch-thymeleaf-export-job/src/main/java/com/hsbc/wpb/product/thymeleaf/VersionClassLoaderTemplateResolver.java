package com.dummy.wpb.product.thymeleaf;

import org.apache.commons.lang3.StringUtils;
import org.thymeleaf.IEngineConfiguration;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

public class VersionClassLoaderTemplateResolver extends ClassLoaderTemplateResolver {

    @Override
    protected String computeResourceName(IEngineConfiguration configuration, String ownerTemplate, String template, String prefix, String suffix, boolean forceSuffix, Map<String, String> templateAliases, Map<String, Object> templateResolutionAttributes) {
        if (StringUtils.isNotBlank(ownerTemplate)) {
            Path ownerPath = Paths.get(ownerTemplate).getParent();
            if (null != ownerPath) {
                prefix = Paths.get(prefix, ownerPath.toString()) + File.separator;
            }
        }

        return super.computeResourceName(configuration, ownerTemplate, template, prefix, suffix, forceSuffix, templateAliases, templateResolutionAttributes);
    }
}
