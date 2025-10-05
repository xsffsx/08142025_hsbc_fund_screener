package com.dummy.wmd.wpc.graphql.validator;

import java.util.*;

/**
 * required(true | false)
 */
@SuppressWarnings("java:S3740")
public class UniqueValidator implements Validator {
    private final List<String> keys;

    public UniqueValidator(List<String> keys){
        this.keys = keys;
    }

    public UniqueValidator() {
        this.keys = new ArrayList<>();
    }

    @Override
    public boolean support(Object value) {
        if(null == value) return false;
        return value instanceof List;
    }

    @Override
    public String getName() {
        return "unique";
    }

    @Override
    public String getDefaultMessage(Object value) {
        return "Value in the array should be unique, value=" + value;
    }

    @Override
    public void validate(Object value, ValidationContext context) {
        if(null == value) { // null case handled by required rule
            return;
        }

        List list = (List)value;

        Set set = new LinkedHashSet();
        if(keys.isEmpty()){
            set.addAll(list);
            if(list.size() != set.size()){
                context.addError(getName(), getDefaultMessage(value));
            }
        } else {
            list.forEach(item -> {
                Map<String, Object> keyItems = new LinkedHashMap<>();
                keys.forEach(key -> keyItems.put(key, ((Map)item).get(key)));
                set.add(keyItems);
            });
            if(list.size() != set.size()){
                context.addError(getName(), String.format("Key combination of %s in the array should be unique", keys));
            }
        }
    }

    @Override
    public String toString() {
        return "UniqueValidator{" +
                "keys=" + keys +
                '}';
    }
}
