package com.dummy.wpc.datadaptor.mapper;

 

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

public class PriceMapper extends AbstractFieldSetMapper {
    
    
    @Override
    public Object mapLine(FieldSet fieldSet) {
        ProdPrc prodPrc = new ProdPrc();
        
        ProdKeySeg prodKey = new ProdKeySeg();
        prodKey.setCtryRecCde(StringUtils.trimToEmpty(fieldSet.readString("CTRYCDE")));
        prodKey.setGrpMembrRecCde(StringUtils.trimToEmpty(fieldSet.readString("GRPMBRCDE")));
        prodKey.setProdCde(StringUtils.trimToEmpty(fieldSet.readString("PRODCDE")));
        prodKey.setProdTypeCde(StringUtils.trimToEmpty(fieldSet.readString("PRODTYPCDE")));
        prodPrc.setProdKeySeg(prodKey);
        
        // for ELI Discount Sell Percent
//        if (Const.EQUITY_LINKED_INVESTMENT.equalsIgnoreCase(prodKey.getProdTypeCde())) {            
            String DctSellPercent = fieldSet.readString("DCTSELLPCT");
            if (StringUtils.isNotEmpty(DctSellPercent)) {
                EliDctSellPctSeg esp = new EliDctSellPctSeg();
                esp.setEliDctSellPercentSeg(StringUtils.trimToEmpty(DctSellPercent));
                prodPrc.setEliDctSellPctSeg(esp);
            }            
//        } else {
            
            //validate date 
            SimpleDateFormat s1=new SimpleDateFormat("MM/dd/yyyy");
            SimpleDateFormat s2=new SimpleDateFormat("yyyy-MM-dd");
            String prcEffDt="";
            String prcinpDt="";
           
             try {
            	 s1.setLenient(false);
            	 s2.setLenient(false);
            	 
			     Date prcEffDate =s1.parse(StringUtils.trimToEmpty(fieldSet.readString("PRCEFFDT")));
			     Date prcinpDate= s1.parse(StringUtils.trimToEmpty(fieldSet.readString("PRCINPDT")));
 
			     prcEffDt= s2.format(prcEffDate);
			     prcinpDt=s2.format(prcinpDate);
			    
			     
			} catch (ParseException e) {
				 String errMsg="ProductCode "+prodKey.getProdCde()+" The format of the date is incorrect,please check further.";
	             TNGMessage.logTNGMsgExInfo("0001", "E", "Product Interface Upload",errMsg );
				 throw new RuntimeException(errMsg);
			}

            ProdPrcSeg prodPrcSeg = new ProdPrcSeg();
            prodPrcSeg.setPrcEffDt(prcEffDt);
            prodPrcSeg.setPdcyPrcCde(fieldSet.readString("PDCYPRCCDE"));
            prodPrcSeg.setPrcInpDt(prcinpDt);
            prodPrcSeg.setCcyProdMktPrcCde(StringUtils.trimToEmpty(fieldSet.readString("CCYMKTPRCCDE")));
            prodPrcSeg.setProdBidPrcAmt(DecimalHelper.trimZero(fieldSet.readString("BIDPRCAMT")));
            prodPrcSeg.setProdOffrPrcAmt(DecimalHelper.trimZero(fieldSet.readString("OFFRPRCAMT")));
            prodPrcSeg.setProdNavPrcAmt(DecimalHelper.trimZero(fieldSet.readString("NAVPRCAMT")));
            prodPrcSeg.setProdMktPrcAmt(DecimalHelper.trimZero(fieldSet.readString("MKTPRCAMT")));
            
            RecDtTmSeg recDtTm = new RecDtTmSeg();
            recDtTm.setRecCreatDtTm(StringUtils.trimToEmpty(fieldSet.readString("RECCRTDTTM")));
            recDtTm.setRecUpdtDtTm(StringUtils.trimToEmpty(fieldSet.readString("RECUPDTDTTM")));
            recDtTm.setTimeZone(StringUtils.trimToEmpty(fieldSet.readString("TIMEZONE")));
            prodPrcSeg.setRecDtTmSeg(recDtTm);
            
            prodPrc.addProdPrcSeg(prodPrcSeg);
            
//        }
        
        
        return prodPrc;
    }
    
}
