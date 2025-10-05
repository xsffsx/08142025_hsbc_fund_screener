package com.dummy.wpc.datadaptor.mapper;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.batch.item.file.mapping.FieldSet;

import com.dummy.wpc.batch.xml.EliDctSellPctSeg;
import com.dummy.wpc.batch.xml.ProdKeySeg;
import com.dummy.wpc.batch.xml.ProdPrc;
import com.dummy.wpc.batch.xml.ProdPrcSeg;
import com.dummy.wpc.batch.xml.RecDtTmSeg;
import com.dummy.wpc.datadaptor.constant.ConfigConstant;
import com.dummy.wpc.datadaptor.constant.Const;
import com.dummy.wpc.datadaptor.util.ConstantsPropertiesHelper;
import com.dummy.wpc.datadaptor.util.DecimalHelper;

/**
 * 
 * 
 *
 * @version $Revision: 1.1.2.3 $ $Date: 2013/07/08 11:43:42CST $
 */
public class PriceMapper4MultiWriter extends AbstractFieldSetMapper {
    
    private static final Logger logger = Logger.getLogger(PriceMapper4MultiWriter.class);
    
    @Override
    public Object mapLine(FieldSet fieldSet) {
        MultiWriterObj arr = new MultiWriterObj();
        
        String replica_ctry_list = ConstantsPropertiesHelper.getValue(getJobCode(), ConfigConstant.REPLICATION_COUNTRY_LIST);
        
        if (StringUtils.isBlank(replica_ctry_list)) {
            String key = getJobCode() + "." + ConfigConstant.REPLICATION_COUNTRY_LIST;
            
            logger.error("Can't found configure[" + key + "] for replication price files.");
            throw new IllegalArgumentException("Can't found configure[" + key + "]!");
        }
        
        String[] ctryList = StringUtils.split(replica_ctry_list, ",");
        for (String ctry : ctryList) {
            arr.addObj(convertPrice(fieldSet, ctry));
        }
        
        return arr;
    }
    
    private ProdPrc convertPrice(FieldSet fieldSet, String ctry_cde) {
        String grp_membr_cde = ConstantsPropertiesHelper.getValue(getJobCode(), ConfigConstant.REPLICATION_GROUP_MEMBER_CDE + "."
                + ctry_cde);
        
        if (StringUtils.isBlank(grp_membr_cde)) {
            String key = getJobCode() + "." + ConfigConstant.REPLICATION_GROUP_MEMBER_CDE + "." + ctry_cde;
            
            logger.error("Can't found configure[" + key + "] for replication price files.");
            throw new IllegalArgumentException("Can't found configure[" + key + "]!");
        }
        
        ProdPrc prodPrc = new ProdPrc();
        
        ProdKeySeg prodKey = new ProdKeySeg();
        prodKey.setCtryRecCde(ctry_cde);
        prodKey.setGrpMembrRecCde(grp_membr_cde);
        prodKey.setProdCde(StringUtils.trimToEmpty(fieldSet.readString("PRODCDE")));
        prodKey.setProdTypeCde(StringUtils.trimToEmpty(fieldSet.readString("PRODTYPCDE")));
        prodPrc.setProdKeySeg(prodKey);
        
        // for ELI Discount Sell Percent
        if (Const.EQUITY_LINKED_INVESTMENT.equalsIgnoreCase(prodKey.getProdTypeCde())) {
            String DctSellPercent = fieldSet.readString("DCTSELLPCT");
            if (StringUtils.isNotEmpty(DctSellPercent)) {
                EliDctSellPctSeg esp = new EliDctSellPctSeg();
                esp.setEliDctSellPercentSeg(StringUtils.trimToEmpty(DctSellPercent));
                prodPrc.setEliDctSellPctSeg(esp);
            }
        } else {
            ProdPrcSeg prodPrcSeg = new ProdPrcSeg();
            prodPrcSeg.setPrcEffDt(StringUtils.trimToEmpty(fieldSet.readString("PRCEFFDT")));
            prodPrcSeg.setPdcyPrcCde(fieldSet.readString("PDCYPRCCDE"));
            prodPrcSeg.setPrcInpDt(StringUtils.trimToEmpty(fieldSet.readString("PRCINPDT")));
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
        }
        
        return prodPrc;
    }
    
}
