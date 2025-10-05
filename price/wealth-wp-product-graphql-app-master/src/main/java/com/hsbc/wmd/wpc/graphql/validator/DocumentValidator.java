package com.dummy.wmd.wpc.graphql.validator;

import org.bson.Document;

import java.util.List;

public interface DocumentValidator {
    List<Error> validateCreate(Document document);
    List<Error> validateUpdate(Document document);
}
