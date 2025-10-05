package com.dummy.wmd.wpc.graphql.utils;

import graphql.schema.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class QueryEditor {
    private final List<String> retrieveAllFieldsFor;
    private GraphQLSchemaUtils schemaUtils;

    public QueryEditor(List<String> retrieveAllFieldsFor, GraphQLSchema schema){
        this.retrieveAllFieldsFor = retrieveAllFieldsFor;
        this.schemaUtils = new GraphQLSchemaUtils(schema);
    }

    /**
     * Handle Asterisk wildcard in the field selection part, return all fields and embedded fields
     *
     * @param query
     * @return
     */
    public String expandFieldSelection(String query) {
        StringBuilder sb = new StringBuilder(query);
        int start = sb.indexOf("{");
        int end = sb.lastIndexOf("}");

        List<LateInsert> lateInsertList = new ArrayList<>();
        StringBuilder fieldName = new StringBuilder();
        int braces = 0;
        int parentheses = 0;
        int pOpen = start;
        for (int i = start + 1; i < end; i++) {
            char c = sb.charAt(i);
            if (parentheses == 0 && Character.valueOf('{').equals(c)) {
                braces++;
                if (1 == braces) {
                    pOpen = i;
                }
            } else if (parentheses == 0 && Character.valueOf('}').equals(c)) {
                braces--;
                fieldName = getStringBuilder(braces, fieldName, lateInsertList, pOpen);
            } else if (Character.valueOf('(').equals(c)) {
                parentheses++;
            } else if (Character.valueOf(')').equals(c)) {
                parentheses--;
            } else if (braces == 0 && parentheses == 0) {
                fieldName.append(c);
            }
        }

        // update in reverse order, since the pos will be changed after the replace
        Collections.reverse(lateInsertList);
        lateInsertList.forEach(item ->
            // replace the ALL_FIELDS with the full field selection
            sb.insert(item.getPos(), String.format("\n%s\n", item.getFieldSelection()))
        );

        return sb.toString();
    }

    private StringBuilder getStringBuilder(int braces, StringBuilder fieldName, List<LateInsert> lateInsertList, int pOpen) {
        if (0 == braces) {
            String queryName = trimQueryName(fieldName.toString());
            fieldName = new StringBuilder();    // clean the fieldName string builder

            if(retrieveAllFieldsFor.contains(queryName)) {
                // insert all field selection right after the { mark
                String fieldSelection = schemaUtils.getFieldSelectionOfQuery(queryName);
                if (null != fieldSelection) {
                    lateInsertList.add(new LateInsert(pOpen + 1, fieldSelection));
                }
            }
        }
        return fieldName;
    }

    private Pattern queryNamePattern = Pattern.compile("\\s*([^\\s]+)\\s*$");

    /**
     * Sample case below, we need to return "productById" as the query name
     *
     * query allFields {
     *   productCountByFilter(filter: {prodTypeCde:"BOND"})
     *
     *   productById(prodId:40000299){
     *     ALL_FIELDS
     *   }
     * }
     * @param text
     * @return
     */
    private String trimQueryName(String text) {
        Matcher matcher = queryNamePattern.matcher(text);
        if(matcher.find()){
            return matcher.group(1);
        }
        throw new IllegalArgumentException("Query name pattern has no match: " + text);
    }
}

@Data
@AllArgsConstructor
class LateInsert {
    int pos;
    String fieldSelection;
}