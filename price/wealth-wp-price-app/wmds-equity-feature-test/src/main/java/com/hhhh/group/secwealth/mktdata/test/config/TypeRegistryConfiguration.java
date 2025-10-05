package com.hhhh.group.secwealth.mktdata.test.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.core.api.TypeRegistry;
import io.cucumber.core.api.TypeRegistryConfigurer;
import io.cucumber.cucumberexpressions.ParameterByTypeTransformer;
import io.cucumber.datatable.TableCellByTypeTransformer;
import io.cucumber.datatable.TableEntryByTypeTransformer;
import java.lang.reflect.Type;
import java.util.Locale;
import java.util.Map;

public class TypeRegistryConfiguration implements TypeRegistryConfigurer {
    public TypeRegistryConfiguration() {
    }

    public Locale locale() {
        return Locale.ENGLISH;
    }

    public void configureTypeRegistry(TypeRegistry typeRegistry) {
        TypeRegistryConfiguration.JacksonTableTransformer jacksonTableTransformer = new TypeRegistryConfiguration.JacksonTableTransformer();
        typeRegistry.setDefaultDataTableCellTransformer(jacksonTableTransformer);
        typeRegistry.setDefaultDataTableEntryTransformer(jacksonTableTransformer);
        typeRegistry.setDefaultParameterTransformer(jacksonTableTransformer);
    }

    private class JacksonTableTransformer implements ParameterByTypeTransformer, TableEntryByTypeTransformer, TableCellByTypeTransformer {
        private final ObjectMapper objectMapper;

        private JacksonTableTransformer() {
            this.objectMapper = new ObjectMapper();
        }

        public Object transform(String s, Type type) {
            return this.objectMapper.convertValue(s, this.objectMapper.constructType(type));
        }

        public <T> T transform(Map<String, String> entry, Class<T> type, TableCellByTypeTransformer cellTransformer) {
            return this.objectMapper.convertValue(entry, type);
        }

        public <T> T transform(String value, Class<T> cellType) {
            return this.objectMapper.convertValue(value, cellType);
        }

        @Override
        public Object transform(Map<String, String> map, Type type, TableCellByTypeTransformer tableCellByTypeTransformer) throws Throwable {
            return this.objectMapper.convertValue(map, this.objectMapper.constructType(type));
        }
    }
}
