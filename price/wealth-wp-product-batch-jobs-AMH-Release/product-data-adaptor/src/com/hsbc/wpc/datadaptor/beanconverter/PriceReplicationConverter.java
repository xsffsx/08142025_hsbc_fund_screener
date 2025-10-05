package com.dummy.wpc.datadaptor.beanconverter;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.dummy.wpc.batch.xml.ProdKeySeg;
import com.dummy.wpc.batch.xml.ProdPrc;
import com.dummy.wpc.batch.xml.ProdPrcSeg;
import com.dummy.wpc.batch.xml.RecDtTmSeg;
import com.dummy.wpc.datadaptor.constant.ConfigConstant;
import com.dummy.wpc.datadaptor.mapper.MultiWriterObj;
import com.dummy.wpc.datadaptor.util.ConstantsPropertiesHelper;


public class PriceReplicationConverter extends AbstractBeanConverter {
    
    private static final Logger logger = Logger.getLogger(PriceReplicationConverter.class);
    
    @Override
    public Object convert(Object source) {
        ProdPrc price = (ProdPrc) source;
        
        MultiWriterObj multiWriterObj = new MultiWriterObj();
        
        String replica_ctry_list = ConstantsPropertiesHelper.getValue(getJobCode(), ConfigConstant.REPLICATION_COUNTRY_LIST);
        
        if (StringUtils.isBlank(replica_ctry_list)) {
            String key = getJobCode() + "." + ConfigConstant.REPLICATION_COUNTRY_LIST;
            
            logger.error("Can't found configure[" + key + "] for replication price files.");
            throw new IllegalArgumentException("Can't found configure[" + key + "]!");
        }
        
        String[] ctryList = StringUtils.split(replica_ctry_list, ",");
        for (String ctry : ctryList) {
            multiWriterObj.addObj(convertPrice(price, ctry));
        }
        
        return multiWriterObj;
    }
    
    private ProdPrc convertPrice(ProdPrc price, String ctry_cde) {
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
        prodKey.setProdCde(price.getProdKeySeg().getProdCde());
        prodKey.setProdTypeCde(price.getProdKeySeg().getProdTypeCde());
        prodPrc.setProdKeySeg(prodKey);
        
        for (ProdPrcSeg srcPrcSeg : price.getProdPrcSeg()) {
            ProdPrcSeg prodPrcSeg = new ProdPrcSeg();
            prodPrcSeg.setPrcEffDt(srcPrcSeg.getPrcEffDt());
            prodPrcSeg.setPdcyPrcCde(srcPrcSeg.getPdcyPrcCde());
            prodPrcSeg.setPrcInpDt(srcPrcSeg.getPrcInpDt());
            prodPrcSeg.setCcyProdMktPrcCde(srcPrcSeg.getCcyProdMktPrcCde());
            prodPrcSeg.setProdBidPrcAmt(srcPrcSeg.getProdBidPrcAmt());
            prodPrcSeg.setProdOffrPrcAmt(srcPrcSeg.getProdOffrPrcAmt());
            prodPrcSeg.setProdNavPrcAmt(srcPrcSeg.getProdNavPrcAmt());
            prodPrcSeg.setProdMktPrcAmt(srcPrcSeg.getProdMktPrcAmt());
            
            RecDtTmSeg recDtTm = new RecDtTmSeg();
            recDtTm.setRecCreatDtTm(srcPrcSeg.getRecDtTmSeg().getRecCreatDtTm());
            recDtTm.setRecUpdtDtTm(srcPrcSeg.getRecDtTmSeg().getRecUpdtDtTm());
            recDtTm.setTimeZone(srcPrcSeg.getRecDtTmSeg().getTimeZone());
            prodPrcSeg.setRecDtTmSeg(recDtTm);
            
            prodPrc.addProdPrcSeg(prodPrcSeg);
        }
        
        return prodPrc;
    }
    
}
