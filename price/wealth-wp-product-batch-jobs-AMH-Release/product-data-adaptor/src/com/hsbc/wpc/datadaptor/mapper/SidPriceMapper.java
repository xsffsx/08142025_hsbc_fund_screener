package com.dummy.wpc.datadaptor.mapper;

import org.apache.commons.lang.StringUtils;
import org.springframework.batch.item.file.mapping.FieldSet;

import com.dummy.wpc.batch.xml.ProdKeySeg;
import com.dummy.wpc.batch.xml.ProdPrc;
import com.dummy.wpc.batch.xml.RecDtTmSeg;
import com.dummy.wpc.batch.xml.SidProdPrcSeg;
import com.dummy.wpc.datadaptor.constant.Const;
import com.dummy.wpc.datadaptor.util.DateHelper;
import com.dummy.wpc.datadaptor.util.DecimalHelper;
/**
 * 
 * @author 43701020
 *
 */
public class SidPriceMapper extends AbstractFieldSetMapper {
    
    @Override
    public Object mapLine(FieldSet fs) {
        
        //SID Price
        //CTRYCDE,GRPMBRCDE,PRODTYPCDE,PRODCDE,
        //PRCEARLYRDMEFFDT,PRCEARLYRDMPCT,RECCRTDTTM,RECUPDTDTTM,TIMEZONE
        
        ProdPrc prodPrc = new ProdPrc();
        
        ProdKeySeg prodKey = new ProdKeySeg();
        prodKey.setCtryRecCde(StringUtils.trimToEmpty(fs.readString("CTRYCDE")));
        prodKey.setGrpMembrRecCde(StringUtils.trimToEmpty(fs.readString("GRPMBRCDE")));
        prodKey.setProdCde(StringUtils.trimToEmpty(fs.readString("PRODCDE")));
        prodKey.setProdTypeCde(Const.STRUCTURE_INVESTMENT_DEPOSIT);
        prodPrc.setProdKeySeg(prodKey);

        SidProdPrcSeg sidProdPrcSeg = new SidProdPrcSeg();
        
        sidProdPrcSeg.setPrcEarlyRdmEffDt(DateHelper.formatDate2String(DateHelper.parseToDate(fs.readString("PRCEARLYRDMEFFDT"),
            DateHelper.DEFAULT_DATE_FORMAT), DateHelper.DEFAULT_DATE_FORMAT));
        sidProdPrcSeg.setPrcEarlyRdmPct(DecimalHelper.trimZero(fs.readString("PRCEARLYRDMPCT")));
        
        RecDtTmSeg recDtTm = new RecDtTmSeg();
        recDtTm.setRecCreatDtTm(StringUtils.trimToEmpty(fs.readString("RECCRTDTTM")));
        recDtTm.setRecUpdtDtTm(StringUtils.trimToEmpty(fs.readString("RECUPDTDTTM")));
        recDtTm.setTimeZone(StringUtils.trimToEmpty(fs.readString("TIMEZONE")));
        sidProdPrcSeg.setRecDtTmSeg(recDtTm);
        
        prodPrc.setSidProdPrcSeg(sidProdPrcSeg);
        return prodPrc;
    }
    
}
