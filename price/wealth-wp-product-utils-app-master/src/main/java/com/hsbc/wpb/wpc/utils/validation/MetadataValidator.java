package com.dummy.wpb.wpc.utils.validation;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.dummy.wpb.wpc.utils.model.Metadata;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Check for cases:
 * - check uniqueness of the _id field
 * - check uniqueness of the jsonPath field
 * - Check metadata.yaml against json schema (to be defined)
 * - Check parent exists.
 * - Check if graphqlType is valid
 */
public class MetadataValidator implements Validator{
    private List<Map<String, Object>> metadataList;
    private static final  ObjectMapper yamlMapper = new ObjectMapper(new YAMLFactory());

    private static String metaData = "ProductMetadata";

    public MetadataValidator(List<Map<String, Object>> metadataList) {
        this.metadataList = metadataList;
    }

    /**
     *
     * @return
     */
    public List<Error> validate(){
        List<Error> errors = new ArrayList<>();

        List<Metadata> list = yamlMapper.convertValue(metadataList, new TypeReference<List<Metadata>>(){});
        validateIdUniqueness(errors, list);
        validateArraySuffix(errors, list);
        validateJsonPath(errors, list);
        validateParentExistence(errors, list);

        return errors;
    }

    /**
     * graphQLType, jsonPath and attributeName has to be aligned, if the type is an array, jsonPath and attributeName need to be end with [*]
     * @param errors
     * @param list
     */
    private void validateArraySuffix(List<Error> errors, List<Metadata> list) {
        list.forEach(item -> {
            if(item.getGraphQLType().matches("\\[[^\\[]+]")){
                if(!item.getAttrName().endsWith("[*]")){
                    errors.add(new Error(metaData, "attrName should be end with [*] for an array field", item));
                }
                if(!item.getJsonPath().endsWith("[*]")){
                    errors.add(new Error(metaData, "jsonPath should be end with [*] for an array field", item));
                }
            }
        });
    }

    private void validateParentExistence(List<Error> errors, List<Metadata> metadataList) {
        Map<String, Metadata> jsonPathMap = new HashMap<>();
        metadataList.forEach(item -> jsonPathMap.put(item.getJsonPath(), item));
        metadataList.forEach(item -> {
            if(!"[ROOT]".equals(item.getParent()) && !jsonPathMap.containsKey(item.getParent())) {
                errors.add(new Error(metaData, "parent node not found", item));
            }
        });
    }

    private void validateIdUniqueness(List<Error> errors, List<Metadata> metadataList) {
        // validate uniqueness of the _id field
        List<String> duplicated = new ArrayList<>();
        metadataList.stream()
                .map(Metadata::get_id)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .forEach((k, v) -> {
                    if(v > 1) duplicated.add(k);
                });
        if(!duplicated.isEmpty()){
            errors.add(new Error(metaData, "duplicated _id field", duplicated));
        }
    }

    /**
     * jsonPath uniqueness
     * jsonPath should be the same with parent.attrName
     * @param errors
     * @param metadataList
     */
    private void validateJsonPath(List<Error> errors, List<Metadata> metadataList) {
        // validate uniqueness of the _id field
        List<String> duplicated = new ArrayList<>();
        metadataList.stream()
                .map(Metadata::getJsonPath)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .forEach((k, v) -> {
                    if(v > 1) duplicated.add(k);
                });
        if(!duplicated.isEmpty()){
            errors.add(new Error(metaData, "duplicated jsonPath field", duplicated));
        }

        metadataList.forEach(item -> {
            String jsonPath = item.getAttrName();
            if(!"[ROOT]".equals(item.getParent())) {
                jsonPath = String.format("%s.%s", item.getParent(), item.getAttrName());
            }
            if(!jsonPath.equals(item.getJsonPath())){
                errors.add(new Error(metaData, "wrong jsonPath, should be " + jsonPath, item));
            }
        });
    }
}
