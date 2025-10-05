package com.dummy.wmd.wpc.graphql.fetcher.amendment;

import com.dummy.wmd.wpc.graphql.calc.CalculationManager;
import com.dummy.wmd.wpc.graphql.constant.DocType;
import com.dummy.wmd.wpc.graphql.constant.Field;
import com.dummy.wmd.wpc.graphql.model.DiffType;
import com.dummy.wmd.wpc.graphql.utils.DocumentDiff;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import java.util.Collections;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class DiffFromAmendmentFetcher implements DataFetcher<List<DiffType>> {

    @Autowired
    private CalculationManager calculationManager;

    @Override
    public List<DiffType> get(DataFetchingEnvironment environment) {
        Map<String, Object> source = environment.getSource();

        if(null == source.get(Field.docBase)) {   // `add` case has no docBase
            return Collections.emptyList();
        }

        Document docBase = new Document((Map<String, Object>)source.get(Field.docBase));
        Document docAmended = new Document((Map<String, Object>)source.get(Field.doc));
        DocType docType = DocType.valueOf((String) source.get(Field.docType));
        //let ui show the differences of calculated field
        if (DocType.product == docType){
            calculationManager.updateCalculatedFields(docBase);
            calculationManager.updateCalculatedFields(docAmended);
        }

        return DocumentDiff.diff(docBase, docAmended);
    }
}