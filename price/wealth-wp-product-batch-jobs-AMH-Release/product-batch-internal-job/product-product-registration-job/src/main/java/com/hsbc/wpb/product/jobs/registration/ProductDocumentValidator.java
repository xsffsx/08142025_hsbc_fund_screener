package com.dummy.wpb.product.jobs.registration;

import com.dummy.wpb.product.constant.Field;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.validator.routines.ISINValidator;
import org.bson.Document;
import org.springframework.batch.item.validator.ValidationException;
import org.springframework.batch.item.validator.Validator;

import java.util.List;
import java.util.Optional;

@Slf4j
public class ProductDocumentValidator implements Validator<Document> {
    private ISINValidator validator = ISINValidator.getInstance(true);

    @Override
    public void validate(Document prod) throws ValidationException {
        List<Document> altIds = prod.getList(Field.altId, Document.class);
        Optional<Document> optDoc = altIds.stream().filter(item -> "I".equals(item.getString(Field.prodCdeAltClassCde))).findFirst();
        if(optDoc.isPresent()) {
            Document doc = optDoc.get();
            String code = doc.getString(Field.prodAltNum);
            if(!validator.isValid(code)) {
                String msg = String.format("Invalid ISIN code %s in product %s", code, prod.get(Field.prodId));
                log.warn(msg);
                throw new ValidationException(msg);
            }
        }
    }
}