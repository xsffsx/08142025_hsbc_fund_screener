package com.dummy.wpc.datadaptor.mapper;

 

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.commons.lang.StringUtils;
import org.springframework.batch.item.file.mapping.FieldSet;

import com.dummy.wpc.batch.xml.EliDctSellPctSeg;
import com.dummy.wpc.batch.xml.ProdKeySeg;
import com.dummy.wpc.batch.xml.ProdPrc;
import com.dummy.wpc.batch.xml.ProdPrcSeg;
import com.dummy.wpc.batch.xml.RecDtTmSeg;
import com.dummy.wpc.common.tng.TNGMessage;
import com.dummy.wpc.datadaptor.util.DecimalHelper;

public class FlexPriceMapper extends AbstractFieldSetMapper {
    
    
    @Override
    public Object mapLine(FieldSet fieldSet) {
        ProdPrc prodPrc = new ProdPrc();


        ProdKeySeg prodKey = new ProdKeySeg();
        prodKey.setCtryRecCde(StringUtils.trimToEmpty(fieldSet.readString("REGION")));
        prodKey.setGrpMembrRecCde("HBAP");
        prodKey.setProdCde(StringUtils.trimToEmpty(fieldSet.readString("ISIN")));
        prodKey.setProdTypeCde("ELI");
        prodKey.setProdCdeAltClassCde("I");
        prodPrc.setProdKeySeg(prodKey);

        String DctSellPercent = fieldSet.readString("MTM");
        if (StringUtils.isNotEmpty(DctSellPercent)) {
            EliDctSellPctSeg esp = new EliDctSellPctSeg();
            String dctSellPert = StringUtils.trimToEmpty(DctSellPercent);
            if(dctSellPert.contains(".")){
                String decamalPrc = dctSellPert.substring(dctSellPert.indexOf("."),dctSellPert.length());
                if(decamalPrc.length()>4){
                    BigDecimal bigDecimalPrc = new BigDecimal(dctSellPert);
                    esp.setEliDctSellPercentSeg(bigDecimalPrc.setScale(4,BigDecimal.ROUND_DOWN).toString());
                }else{
                    esp.setEliDctSellPercentSeg(dctSellPert);
                }
            }else {
                esp.setEliDctSellPercentSeg(dctSellPert);
            }
            prodPrc.setEliDctSellPctSeg(esp);
        }

        //validate date
        SimpleDateFormat s1=new SimpleDateFormat("MM/dd/yyyy");
        SimpleDateFormat s2=new SimpleDateFormat("yyyy-MM-dd");
        String prcEffDt="";
        String prcinpDt="";

        s1.setLenient(false);
        s2.setLenient(false);

        Date prcEffDate = null;
        Date prcinpDate = null;
        try {
            prcEffDate = s1.parse(StringUtils.trimToEmpty(fieldSet.readString("VALUATION_DATE")));
            prcinpDate = s1.parse(StringUtils.trimToEmpty(fieldSet.readString("VALUATION_DATE")));
        } catch (ParseException ex1) {
            try {
                prcEffDate = s2.parse(StringUtils.trimToEmpty(fieldSet.readString("VALUATION_DATE")));
                prcinpDate = s2.parse(StringUtils.trimToEmpty(fieldSet.readString("VALUATION_DATE")));
            } catch (ParseException ex2) {
                String errMsg = "ProductCode " + prodKey.getProdCde() + " The format of the date is incorrect,please check further.";
                TNGMessage.logTNGMsgExInfo("0001", "E", "Product Interface Upload", errMsg);
                throw new IllegalArgumentException(errMsg);
            }
        }

        prcEffDt = s2.format(prcEffDate);
        prcinpDt = s2.format(prcinpDate);

        ProdPrcSeg prodPrcSeg = new ProdPrcSeg();
        prodPrcSeg.setPrcEffDt(prcEffDt);
        prodPrcSeg.setPrcInpDt(prcinpDt);
        prodPrcSeg.setPdcyPrcCde(StringUtils.trimToEmpty(null));
        prodPrcSeg.setCcyProdMktPrcCde(StringUtils.trimToEmpty(null));
        prodPrcSeg.setProdBidPrcAmt(StringUtils.trimToEmpty(null));
        prodPrcSeg.setProdOffrPrcAmt(StringUtils.trimToEmpty(null));
        prodPrcSeg.setProdNavPrcAmt(StringUtils.trimToEmpty(null));
        prodPrcSeg.setProdMktPrcAmt(StringUtils.trimToEmpty(null));

        RecDtTmSeg recDtTm = new RecDtTmSeg();
        recDtTm.setRecCreatDtTm(StringUtils.trimToEmpty(null));
        recDtTm.setRecUpdtDtTm(StringUtils.trimToEmpty(null));
        recDtTm.setTimeZone(StringUtils.trimToEmpty(null));
        prodPrcSeg.setRecDtTmSeg(recDtTm);

        prodPrc.addProdPrcSeg(prodPrcSeg);

        return prodPrc;
    }
    
}
