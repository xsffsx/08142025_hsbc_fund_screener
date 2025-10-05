package com.dummy.wmd.wpc.graphql;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.dummy.wmd.wpc.graphql.constant.CollectionName;
import com.dummy.wmd.wpc.graphql.model.ProductMetadata;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

import static com.mongodb.client.model.Projections.include;

@SuppressWarnings("java:S5361")
@Component
@Slf4j
public class GraphQLSchemaGenerator {
    private static final String ROOT = "[ROOT]";
    private static final String INPUT = "Input";
    private static ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
    private List<ProductMetadata> rootFields = new LinkedList<>();

    public GraphQLSchemaGenerator(MongoDatabase mongodb) {
        MongoCollection<Document> collection = mongodb.getCollection(CollectionName.metadata.toString());
        List<Document> docs = collection.find()
                .projection(include("attrName", "jsonPath", "parent", "businessName", "businessDefinition", "graphQLType", "calculatedBy"))
                .into(new ArrayList<>());
        List<ProductMetadata> metadataList = objectMapper.convertValue(docs, new TypeReference<List<ProductMetadata>>(){});

        // build product metadata tree
        Map<String, ProductMetadata> jsonpathMap = new LinkedHashMap<>();
        metadataList.forEach(md -> {
            jsonpathMap.put(md.getJsonPath(), md);
            if(ROOT.equals(md.getParent())) {
                rootFields.add(md);
            }
        });

        metadataList.forEach(md -> {
            String parent = md.getParent();
            if(!ROOT.equals(parent)) {
                ProductMetadata parentMetadata = jsonpathMap.get(parent);
                if(null == parentMetadata) {
                    log.warn("Parent not found for {}", md);
                } else {
                    List<ProductMetadata> fields = parentMetadata.getFields();
                    fields.add(md);
                }
            }
        });

    }

    private String toOutputSchema(ProductMetadata metadata) {
        // Build schema DSL
        StringBuilder sb = new StringBuilder();
        List<ProductMetadata> subFields = new ArrayList<>();

        if(metadata.getFields().isEmpty()) {
            return "";  // all non-scalar field should contains subfields, avoi
        }

        sb.append("type ").append(unwrappedType(metadata.getGraphQLType())).append(" {\n");
        metadata.getFields().forEach(field -> {
            String type = field.getGraphQLType();
            if(!isScalarType(type)) {
                subFields.add(field);
            }
            String name = field.getAttrName();
            if(name.endsWith("[*]")) {  // unwrap name of array type
                name = name.substring(0, name.length() - 3);
            }
            String desc = field.getBusinessName();
            if(null != desc){
                desc = desc.replaceAll("\\n", " ");
            } else {
                desc = "TODO";
            }
            sb.append("    # ").append(desc).append("\n");
            sb.append("    ").append(name).append(": ").append(type).append("\n");
        });
        if("ProductType".equals(metadata.getGraphQLType())) {
            // add priceHistory field
            sb.append("    # Product price history").append("\n");
            sb.append("    priceHistory(filter: JSON, sort: JSON, skip: Int = 0, limit: Int = 0): [ProductPriceHistoryType]").append("\n");
            // add amendments field
            sb.append("    # Retrieve related amendments, by default return the last change only, sorting is supported").append("\n");
            sb.append("    amendments(filter:JSON, lastOnly: Boolean = true, docType:DocType, sort: JSON): [AmendmentType]").append("\n");
        }
        sb.append("}\n\n");

        // handle subfield types
        subFields.forEach(field -> {
            String schema = toOutputSchema(field);
            sb.append(schema);
        });

        return sb.toString();
    }

    private String toInputSchema(ProductMetadata metadata) {
        // Build schema DSL
        StringBuilder sb = new StringBuilder();
        List<ProductMetadata> subFields = new ArrayList<>();

        String typeName = unwrappedType(metadata.getGraphQLType()).replaceAll("Type", INPUT);
        sb.append("input ").append(typeName).append(" {\n");
        List<ProductMetadata> fields = metadata.getFields().stream()
                .filter(field -> null == field.getCalculatedBy()).collect(Collectors.toList());

        fields.forEach(field -> {
            String type = field.getGraphQLType().replaceAll("Type", INPUT);
            if(!isScalarType(type)) {
                subFields.add(field);
            }
            String name = field.getAttrName();
            if(name.endsWith("[*]")) {  // unwrap name of array type
                name = name.substring(0, name.length() - 3);
            }
            String desc = field.getBusinessName();
            if(null != desc){
                desc = desc.replaceAll("\\n", " ");
            } else {
                desc = "TODO";
            }
            sb.append("    # ").append(desc).append("\n");
            sb.append("    ").append(name).append(": ").append(type).append("\n");
        });
        sb.append("}\n\n");

        // handle subfield types
        subFields.forEach(field -> {
            String schema = toInputSchema(field);
            sb.append(schema);
        });

        return sb.toString();
    }

    private boolean isScalarType(String type) {
        type = unwrappedType(type);
        return !(type.endsWith("Type") || type.endsWith(INPUT));
    }

    /**
     * Unwrap type like [String] into String
     * @param type
     * @return
     */
    private String unwrappedType(String type) {
        if(type.startsWith("[") && type.endsWith("]")) {
            return type.substring(1, type.length() - 1);
        }
        return type;
    }

    /**
     * Generate schema for product query
     * @return
     */
    public String generateProductTypeSchema(){
        ProductMetadata root = new ProductMetadata();
        root.setGraphQLType("ProductType");
        root.setFields(rootFields);
        root.setBusinessName("Product Output Type");
        root.setBusinessDefinition("Product Output Type");

        return toOutputSchema(root);
    }

    /**
     * Generate schema for product input
     * @return
     */
    public String generateProductInputSchema(){
        ProductMetadata root = new ProductMetadata();
        root.setGraphQLType("ProductInput");
        root.setFields(rootFields);
        root.setBusinessName("Product Input Type");
        root.setBusinessDefinition("Product Input Type");

        return toInputSchema(root);
    }
}
