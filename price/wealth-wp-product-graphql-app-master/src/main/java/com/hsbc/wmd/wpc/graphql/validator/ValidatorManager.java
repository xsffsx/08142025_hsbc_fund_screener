package com.dummy.wmd.wpc.graphql.validator;

import com.dummy.wmd.wpc.graphql.constant.CollectionName;
import com.dummy.wmd.wpc.graphql.service.ReferenceDataService;
import com.dummy.wmd.wpc.graphql.utils.CodeUtils;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ValidatorManager {
    // jsonPath -> ConditionalRules List
    private Map<String, List<ConditionalRules>> path2RulesMap;
    private ReferenceDataService refDataService;

    public ValidatorManager(MongoDatabase mongoDatabase, ReferenceDataService referenceDataService){
        MongoCollection<Document> collection = mongoDatabase.getCollection(CollectionName.metadata.toString());
        refDataService = referenceDataService;
        path2RulesMap = loadConditionalRules(collection);
    }

    private Map<String, List<ConditionalRules>> loadConditionalRules(MongoCollection<Document> collection) {
        List<Map<String, Object>> list = new LinkedList<>();
        collection.find().into(list);

        Map<String, List<ConditionalRules>> rulesMap = new LinkedHashMap<>();
        for (Map<String, Object> item : list) {
            String jsonPath = (String) item.get("jsonPath");
            List<Document> validations = (List<Document>) item.get("validations");
            if (null == validations) continue;

            validations.forEach(doc -> {
                String given = doc.getString("given");
                Document rules = doc.get("rules", Document.class);
                ConditionalRules conditionalRules = getConditionalRulesList(given, rules);
                List<ConditionalRules> conditionalRulesList = rulesMap.getOrDefault(jsonPath, new ArrayList<>());
                conditionalRulesList.add(conditionalRules);
                rulesMap.put(jsonPath, conditionalRulesList);
            });
        }
        return rulesMap;
    }

    private ConditionalRules getConditionalRulesList(String given, Document rules) {
        ConditionalRules conditionalRules = new ConditionalRules();
        conditionalRules.setCondition(given);

        rules.forEach((name, param) -> conditionalRules.getValidators().add(getValidatorInstance(name, param)));
        return conditionalRules;
    }

    private Map<String, Validator> validatorMap = new ConcurrentHashMap<>();

    private Validator getValidatorInstance(String ruleName, Object param) {
        String key = String.format("%s:%s", ruleName, param);
        Validator validator = validatorMap.get(key);
        if(null == validator) {
            switch (ruleName){
                case "required":
                     validator = new RequiredValidator((boolean)param);
                     break;
                case "unique":
                    if(param instanceof Boolean && Boolean.TRUE.equals(param)) {
                        validator = new UniqueValidator();
                    } else {
                        validator = new UniqueValidator((List<String>) param);
                    }
                     break;
                case "enum":
                    validator = new EnumValidator((List<String>) param);
                    break;
                case "referenceData":
                    validator = new ReferenceDataValidator((Map<String, Object>) param, refDataService);
                    break;
                case "size":
                    validator = new SizeValidator((Map<String, Object>) param);
                    break;
                case "length":
                    validator = new LengthValidator((Map<String, Object>)param);
                    break;
                case "range":
                    validator = new RangeValidator((Map<String, Object>) param);
                    break;
                case "spel":
                    validator = new SpELValidator((Map<String, Object>) param);
                    break;
                case "javascript":
                    validator = new JavascriptValidator((Map<String, Object>) param);
                    break;
                case "bytes":
                    validator = new BytesValidator((Map<String, Object>) param);
                    break;
                default:
                    throw new IllegalArgumentException("Not support rule: " + ruleName);
            }
            validatorMap.put(key, validator);
        }
        return validator;
    }

    /**
     * Get direct sub fields for the given parent
     *
     * @param parent
     * @return
     */
    public List<String> getDirectSubFields(String parent) {
        final String parentPath = parent.replaceAll("\\[\\d+]", "\\[\\*]");
        List<String> list = new ArrayList<>();
        path2RulesMap.forEach((k, v) -> {
            if (k.startsWith(parentPath)) {
                String name = k;
                if (name.endsWith("[*]")) {      // remove the list indicator if there is
                    name = name.substring(0, name.length() - 3);
                }
                if (name.contains(".")) {       // get the last children path
                    int lastIndex = name.lastIndexOf('.');
                    name = name.substring(lastIndex + 1);
                }
                list.add(name);
            }
        });
        return list;
    }

    public List<Validator> getValidators(ValidationContext context, boolean validateRequired){
        String jsonPath = context.getNestedPath();
        jsonPath = CodeUtils.normalizedJsonPath(jsonPath);
        List<ConditionalRules> list = path2RulesMap.get(jsonPath);
        List<Validator> validators = new ArrayList<>();
        if(null == list) {
            list = path2RulesMap.get(jsonPath + "[*]"); // try to match a list attribute
            if(null == list) return validators;
        }

        for(ConditionalRules item: list) {
            String spel = item.getCondition();
            if(context.evaluateCondition(spel)) {
                item.getValidators().forEach(validator -> {
                    boolean skip = validateRequired ^ validator instanceof RequiredValidator;
                    if(!skip) {
                        validators.add(validator);
                    }
                });
            }
        }

        return validators;
    }
}
