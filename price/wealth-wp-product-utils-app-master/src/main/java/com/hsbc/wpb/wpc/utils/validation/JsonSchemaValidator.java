package com.dummy.wpb.wpc.utils.validation;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.github.fge.jackson.JsonLoader;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.report.ListProcessingReport;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JsonSchemaValidator implements Validator {
    private final JsonSchemaFactory factory = JsonSchemaFactory.byDefault();
    private final JsonSchema schema;
    private final JsonNode jsonNode;
    private static final ObjectMapper yamlMapper = new ObjectMapper(new YAMLFactory());
    private final String name;

    public JsonSchemaValidator(String name, String schemaContent, String configContent) throws IOException, ProcessingException {
        JsonNode schemaNode = JsonLoader.fromString(schemaContent);
        this.name = name;
        jsonNode = yamlMapper.readTree(configContent);
        schema = factory.getJsonSchema(schemaNode);
    }

    @Override
    public List<Error> validate() {
        List<Error> errors = new ArrayList<>();
        try {
            ProcessingReport report = schema.validate(jsonNode);
            if (!report.isSuccess() && report instanceof ListProcessingReport) {
                Error error = new Error("JsonSchema", "validation failed: " + name, ((ListProcessingReport) report).asJson());
                errors.add(error);
            }
        } catch (Exception e) {
            errors.add(new Error("Exception", e.getMessage()));
        }
        return errors;
    }
}
