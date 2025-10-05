package com.dummy.wpb.wpc.utils.validation;

import com.google.common.collect.ImmutableMap;
import com.dummy.wpb.wpc.utils.constant.Field;
import com.dummy.wpb.wpc.utils.constant.Table;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Check for cases:
 * - One attribute with multiple types.
 * - Attributes are same ignore case.
 * - TO_DO: Type is mismatch with data column.
 * - TO_DO: Attribute name is not a valid name.
 * - Attribute name is not defined in product metadata.
 */
public class ExtFieldValidator implements Validator {
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private static Map<String, String> extTables;
    private List<Map<String, Object>> metadataList;

    static {
        extTables = new ImmutableMap.Builder<String, String>()
                .put(Table.TB_PROD_USER_DEFIN_EXT_FIELD, "ext")
                .put(Table.TB_PROD_USER_DEFIN_EG_EXT_FIEL, "extEg")
                .put(Table.TB_PROD_USER_DEFIN_OP_EXT_FIEL, "extOp")
                .build();
    }

    public ExtFieldValidator(List<Map<String, Object>> metadataList, NamedParameterJdbcTemplate namedParameterJdbcTemplate){
        this.metadataList = metadataList;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    /**
     *
     * @return
     */
    public List<Error> validate(){
        List<Error> errors = new ArrayList<>();
        duplicateFieldDefinition(errors);
        metadataAbsence(errors);
        return errors;
    }

    private void duplicateFieldDefinition(List<Error> errors) {
        extTables.forEach((table, attrName) ->
            duplicateFieldDefinition(errors, table)
        );
    }

    private void duplicateFieldDefinition(List<Error> errors, String table) {
        String sql = String.format("select distinct FIELD_CDE, FIELD_DATA_TYPE_TEXT from %s", table);
        Map<String, String> field2type = new HashMap<>();
        namedParameterJdbcTemplate.query(sql, Collections.emptyMap(), rs -> {
            String fieldCode = rs.getString(Field.FIELD_CDE);
            String fieldType = rs.getString(Field.FIELD_DATA_TYPE_TEXT);
            List<String> keys = field2type.keySet().stream().filter(key -> key.equalsIgnoreCase(fieldCode)).collect(Collectors.toList());
            if(!keys.isEmpty()) {
                String key = keys.get(0);
                List<Map<String, String>> extraData = new ArrayList<>();
                extraData.add(new ImmutableMap.Builder<String, String>().put(Field.FIELD_CDE, key).put(Field.FIELD_DATA_TYPE_TEXT, field2type.get(key)).build());
                extraData.add(new ImmutableMap.Builder<String, String>().put(Field.FIELD_CDE, fieldCode).put(Field.FIELD_DATA_TYPE_TEXT, fieldType).build());
                errors.add(new Error(table, "duplicate field definition", extraData));
            } else {
                field2type.put(fieldCode, fieldType);
            }
        });
    }

    private void metadataAbsence(List<Error> errors){
        extTables.forEach((table, attrName) ->
            metadataAbsence(errors, table, attrName)
        );
    }

    private void metadataAbsence(List<Error> errors, String table, String parent){
        List<String> attrList = metadataList.stream()
                .filter(item -> parent.equals(item.get("parent")))
                .map(item -> (String)item.get("attrName"))
                // remove the array suffix if there is
                .map(item -> item.endsWith("[*]") ? item.substring(0, item.length() - 3) : item)
                .collect(Collectors.toList());

        String sql = String.format("select distinct FIELD_CDE, FIELD_DATA_TYPE_TEXT from %s", table);
        List<Map<String, String>> missingList = new ArrayList<>();
        namedParameterJdbcTemplate.query(sql, Collections.emptyMap(), rs -> {
            String fieldCode = rs.getString(Field.FIELD_CDE);
            String fieldType = rs.getString(Field.FIELD_DATA_TYPE_TEXT);

            if(!attrList.contains(fieldCode)) {
                missingList.add(new ImmutableMap.Builder<String, String>().put(Field.FIELD_CDE, fieldCode).put(Field.FIELD_DATA_TYPE_TEXT, fieldType).build());
            }
        });
        if(!missingList.isEmpty()) {
            errors.add(new Error(table, "field definition not found", missingList));
        }
    }
}
