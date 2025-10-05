package com.dummy.wmd.wpc.graphql.fetcher.amendment;

import com.dummy.wmd.wpc.graphql.constant.DocType;
import com.dummy.wmd.wpc.graphql.constant.Field;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.bson.Document;
import org.springframework.stereotype.Component;

/**
 * Retrieve descriptive information of a given document
 */
@Slf4j
@Component
public class SummaryFetcher implements DataFetcher<String> {

    @Override
    public String get(DataFetchingEnvironment environment) {
        Document source = environment.getSource();
        DocType docType = DocType.valueOf(source.getString(Field.docType));
        Document doc = (Document) source.get(Field.doc);

        String summary;
        switch(docType){
            case product:
            case product_customer_eligibility:
            case product_staff_eligibility:
            case product_prod_relation:
            case chanl_related_fileds:
                String prodTypeCde = doc.getString(Field.prodTypeCde);
                String prodName = doc.getString(Field.prodName);
                String prodAltPrimNum = doc.getString(Field.prodAltPrimNum);
                summary = String.format("%s - %s", prodTypeCde, StringUtils.isNotBlank(prodName) ? prodName : prodAltPrimNum);
                break;
            case reference_data:
                String cdvTypeCde = doc.getString(Field.cdvTypeCde);
                String cdvCde = doc.getString(Field.cdvCde);
                String cdvDesc = doc.getString(Field.cdvDesc);
                summary = String.format("%s/%s - %s", cdvTypeCde, cdvCde, cdvDesc);
                break;
            case aset_voltl_class_char:
                summary = "Asset Volatility Class Characteristic";
                break;
            case aset_voltl_class_corl:
                summary = "Asset Volatility Class Correlation";
                break;
            case staff_license_check:
                prodTypeCde = doc.getString(Field.prodTypeCde);
                String prodSubtpCde = doc.getString(Field.prodSubtpCde);
                summary = StringUtils.isNotBlank(prodSubtpCde) ? prodSubtpCde : prodTypeCde;
                break;
            case fin_doc_hist:
                summary = String.format("%s - %s", doc.getString(Field.docFinCde), doc.getString(Field.docRecvName));
                break;
            default:
                summary = "";
        }

        return summary;
    }
}