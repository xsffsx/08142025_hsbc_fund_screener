package com.dummy.wmd.wpc.graphql.validator;

import com.google.common.collect.ImmutableMap;
import com.dummy.wmd.wpc.graphql.constant.CollectionName;
import com.dummy.wmd.wpc.graphql.constant.Field;
import com.dummy.wmd.wpc.graphql.service.MongoDBService;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.util.*;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static org.apache.commons.lang.StringUtils.EMPTY;

/**
 * Validate reference data document
 */
public class ReferenceDataDocumentValidator implements DocumentValidator {

    enum VALIDATION_TYPE {
        CREATE,
        UPDATE,
        IMPORT
    }

    private static final Map<String, Integer> byteLengths = new ImmutableMap.Builder<String, Integer>()
            .put(Field.cdvTypeCde,15)
            .put(Field.cdvCde, 30)
            .put(Field.cdvDesc, 100)
            .put(Field.cdvPllDesc, 300)
            .put(Field.cdvSllDesc, 300)
            .put(Field.cdvParntTypeCde, 15)
            .put(Field.cdvParntCde, 30)
            .put(Field.recCmntText, 100)
            .build();

    /**
     * 1. cdvParntTypeCde + cdvParntCde exists
     * 2. key combination ctryRecCde + grpMembrRecCde + cdvTypeCde + cdvCde does not exist
     *
     * @param doc
     * @return
     */
    @Override
    public List<Error> validateCreate(Document doc) {
        return validate(doc, VALIDATION_TYPE.CREATE);
    }

    /**
     * 1. cdvParntTypeCde + cdvParntCde exists
     * 2. key combination ctryRecCde + grpMembrRecCde + cdvTypeCde + cdvCde should not be changed
     *
     * @param doc
     * @return
     */
    public List<Error> validateUpdate(Document doc) {
        return validate(doc, VALIDATION_TYPE.UPDATE);
    }

    public List<Error> validateImport(Document doc) {
        return validate(doc, VALIDATION_TYPE.IMPORT);
    }

    private List<Error> validate(Document doc, VALIDATION_TYPE validationType) {
        List<Error> errors = new LinkedList<>();
        if (null == doc) {
            errors.add(new Error("$", "@null", "Reference data content is null"));
            return errors;
        }

        byteLengths.forEach((fieldName, byteLength) -> {
            String value = doc.get(fieldName, EMPTY);
            if (value.getBytes(StandardCharsets.UTF_8).length > byteLength) {
                errors.add(new Error("$." + fieldName, fieldName + "@bytes", String.format("Byte length exceeds %d, value = %s", byteLength, value)));
            }
        });

        switch (validationType) {
            case CREATE:
                if (referenceDataExist(doc)) {
                    errors.add(new Error("$", "@duplicate", "Reference data cdvTypeCde / cdvCde already exists"));
                }
                break;
            case UPDATE:
                if (!referenceDataExist(doc)) {
                    errors.add(new Error("$", "@notFound", "Reference data cdvTypeCde / cdvCde doesn't exist"));
                }
                break;
            case IMPORT:
                break;
        }

        // validate mandatory fields
        if(!StringUtils.hasText(doc.getString(Field.cdvDesc))) {
            errors.add(new Error("$.cdvDesc", "cdvDesc@required", "Description in English is mandatory"));
        }

        if(null == doc.get(Field.cdvDispSeqNum)) {
            errors.add(new Error("$.cdvDispSeqNum", "cdvDispSeqNum@required", "Display Sequence is mandatory"));
        }

        // validate cdvTypeCde
        if(!cdvTypeCdeExist(doc)){
            errors.add(new Error("$.cdvTypeCde", "cdvTypeCde@invalid", "Invalid cdvTypeCde"));
        }

        // validate cdvParntTypeCde + cdvParntCde
        errors.addAll(validateParentType(new Document(doc)));
        return errors;
    }

    /**
     * Rules:
     *    if parent type code exists, it must be existed in the db
     * @param doc
     */
    private List<Error> validateParentType(Document doc) {
        List<Error> errors = new ArrayList<>();
        String cdvParntTypeCde = doc.getString(Field.cdvParntTypeCde);
        String cdvParntCde = doc.getString(Field.cdvParntCde);
        boolean cdvParntTypeCdeExist = StringUtils.hasText(doc.getString(Field.cdvParntTypeCde));
        boolean cdvParntCdeExist = StringUtils.hasText(doc.getString(Field.cdvParntCde));

        // cdvParntTypeCde + cdvParntCde fields has to be exists together or not
        if (!cdvParntTypeCdeExist && cdvParntCdeExist) {
            errors.add(new Error(Field.cdvParntTypeCde, "cdvParntTypeCde@required", "required"));
        } else if (cdvParntTypeCdeExist && !cdvParntCdeExist) {
            errors.add(new Error(Field.cdvParntCde, "cdvParntCde@required", "required"));
        } else if (cdvParntTypeCdeExist){    // both exist (implied condition:  && cdvParntCdeExist), check against the db
            Bson filter = and(
                    eq(Field.ctryRecCde, doc.get(Field.ctryRecCde)),
                    eq(Field.grpMembrRecCde, doc.get(Field.grpMembrRecCde)),
                    eq(Field.cdvTypeCde, cdvParntTypeCde),
                    eq(Field.cdvCde, cdvParntCde)
            );
            long count = MongoDBService.countDocuments(CollectionName.reference_data, filter);
            if(count == 0) {
                String message = String.format("Parent type not found: %s -> %s", cdvParntTypeCde, cdvParntCde);
                errors.add(new Error(Field.cdvParntCde, "cdvParntTypeCde@existance", message));
            }
        }
        return errors;
    }

    /**
     * Check weather a reference data code is exist
     *
     * @param doc
     * @return
     */
    private boolean cdvTypeCdeExist(Document doc) {
        String cdvTypeCde = doc.getString(Field.cdvTypeCde);
        // "REFTYP" is the root reference data type, it doesn't follow the rule, skip checking
        if("REFTYP".equals(cdvTypeCde)) {
            return true;
        }

        Bson filter = and(
                eq(Field.ctryRecCde, doc.get(Field.ctryRecCde)),
                eq(Field.grpMembrRecCde, doc.get(Field.grpMembrRecCde)),
                eq(Field.cdvTypeCde, "REFTYP"),
                eq(Field.cdvCde, cdvTypeCde)
        );
        long count = MongoDBService.countDocuments(CollectionName.reference_data, filter);
        return count > 0;
    }

    /**
     * Check weather the reference data document exists
     *
     * @param doc
     * @return true = exist, false = not exist
     */
    private boolean referenceDataExist(Document doc) {
        Bson filter = and(
                eq(Field.ctryRecCde, doc.get(Field.ctryRecCde)),
                eq(Field.grpMembrRecCde, doc.get(Field.grpMembrRecCde)),
                eq(Field.cdvTypeCde, doc.get(Field.cdvTypeCde)),
                eq(Field.cdvCde, doc.get(Field.cdvCde)));
        long count = MongoDBService.countDocuments(CollectionName.reference_data, filter);
        return count > 0;
    }
}
