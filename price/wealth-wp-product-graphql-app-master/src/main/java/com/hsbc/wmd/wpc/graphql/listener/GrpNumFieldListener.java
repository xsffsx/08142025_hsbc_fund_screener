package com.dummy.wmd.wpc.graphql.listener;

import com.google.common.collect.ImmutableMap;
import com.dummy.wmd.wpc.graphql.constant.Field;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.bson.Document;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.dummy.wmd.wpc.graphql.constant.Field.grpNumAudioRecording;
import static com.dummy.wmd.wpc.graphql.constant.LegacyConstants.*;
import static com.dummy.wmd.wpc.graphql.constant.ProdTypeCde.*;

@Component
public class GrpNumFieldListener extends BaseChangeListener {
    @Override
    public Collection<String> interestJsonPaths() {
        return allInterestJsonPaths;
    }

    @Override
    public void beforeValidation(Map<String, Object> oldProd, Map<String, Object> newProd) {
        calcGrpNumAudioRecording(newProd);
        calcGrpNumRiskDisclosure(newProd);
    }

    private void calcGrpNumAudioRecording(Map<String, Object> newProd) {
        String prodTypeCde = (String) newProd.get(Field.prodTypeCde);
        String grpNumAudioRecording = null;

        switch (prodTypeCde) {
            case EQUITY_LINKED_INVESTMENT:
                grpNumAudioRecording = genEliAudioRecording(newProd);
                break;
            case UNIT_TRUST:
                grpNumAudioRecording = genUtAudioRecording(newProd);
                break;
            case BOND_CD:
                grpNumAudioRecording = genBondAudioRecording(newProd);
                break;
            case STRUCTURE_INVESTMENT_DEPOSIT:
            case DEPOSIT_PLUS:
                grpNumAudioRecording = genNAAudioRecording();
                break;
            default:
        }

        if (!StringUtils.equals(grpNumAudioRecording, (String) newProd.get(Field.grpNumAudioRecording))) {
            newProd.put(Field.grpNumAudioRecording, grpNumAudioRecording);
        }
    }

    public String genNAAudioRecording() {
        return ProdGroupCdeAR.AR00.name();
    }

    public String genEliAudioRecording(Map<String, Object> prod) {
        String prodSubtpCde = (String) prod.get(Field.prodSubtpCde);

        Document eqtyLinkInvst = new Document((Map<String, Object>) prod.getOrDefault(Field.eqtyLinkInvst, Collections.emptyMap()));
        String prodExtnlCatCde = eqtyLinkInvst.getString(Field.prodExtnlCatCde);
        Double cptlProtcPct = ((Number) eqtyLinkInvst.get(Field.cptlProtcPct, 0D)).doubleValue();

        List<? extends Map> undlStock = eqtyLinkInvst.getList(Field.undlStock, Map.class);

        if (CollectionUtils.isEmpty(undlStock)) return null;

        if (undlStock.size() == 1) {
            return computeEliRecordingIfOneUndl(prodSubtpCde, prodExtnlCatCde, cptlProtcPct);
        } else {
            return computeEliRecordingIfMoreUndl(prodSubtpCde, prodExtnlCatCde, cptlProtcPct);
        }
    }

    private String computeEliRecordingIfOneUndl(String prodSubtpCde, String prodExtnlCatCde, Double cptlProtcPct) {
        if (StringUtils.equals(DCDC_ELI, prodExtnlCatCde)) {
            if(StringUtils.equals(BEN_ELI, prodSubtpCde)){
                  return ProdGroupCdeAR.AR49.name();
            }else {
                 return ProdGroupCdeAR.AR01.name();
            }
        }
        if (StringUtils.equals(PPN_50, prodSubtpCde))
            return ObjectUtils.compare(cptlProtcPct, 0D) > 0 ? ProdGroupCdeAR.AR11.name() : ProdGroupCdeAR.AR24.name();

        if (StringUtils.equals(FX, prodExtnlCatCde)) {
            Map<String, String> prodSubtpCdeMap = new ImmutableMap.Builder<String, String>()
                    .put(FXN_01, ProdGroupCdeAR.AR41.name())
                    .put(FXN_02, ProdGroupCdeAR.AR43.name())
                    .put(FXN_03, ProdGroupCdeAR.AR45.name())
                    .put(FXN_04, ProdGroupCdeAR.AR47.name())
                    .build();
            return prodSubtpCdeMap.get(prodSubtpCde);
        }

        if (StringUtils.equals(PPN, prodExtnlCatCde)) {
            // reduce the Complexity
            Map<String, String> prodSubtpCdeMap = new ImmutableMap.Builder<String, String>()
                    .put(PPN_FCN, ProdGroupCdeAR.AR01.name())
                    .put(PPN_PPR, ProdGroupCdeAR.AR03.name())
                    .put(PPN_STPDWN, ProdGroupCdeAR.AR05.name())
                    .put(PPN_UPOUT, ProdGroupCdeAR.AR07.name())
                    .put(UO_ELI, ProdGroupCdeAR.AR07.name())
                    .put(PPN_TWSF, ProdGroupCdeAR.AR09.name())
                    .put(PPN_60, ProdGroupCdeAR.AR26.name())
                    .put(PPN_51, ProdGroupCdeAR.AR27.name())
                    .put(PPN_52, ProdGroupCdeAR.AR29.name())
                    .put(PPN_61, ProdGroupCdeAR.AR31.name())
                    .put(PPN_62, ProdGroupCdeAR.AR33.name())
                    .put(PPN_63, ProdGroupCdeAR.AR35.name())
                    .put(PPN_64, ProdGroupCdeAR.AR37.name())
                    .put(PPN_65, ProdGroupCdeAR.AR39.name())
                    .put(PPN_53, ProdGroupCdeAR.AR49.name())
                    .put(HY_01, ProdGroupCdeAR.AR31.name())
                    .build();
            return prodSubtpCdeMap.get(prodSubtpCde);
        }
        return null;
    }

    private String computeEliRecordingIfMoreUndl(String prodSubtpCde, String prodExtnlCatCde, Double cptlProtcPct) {
        if (StringUtils.equals(DCDC_ELI, prodExtnlCatCde)) {
            if(StringUtils.equals(BEN_ELI, prodSubtpCde)){
                  return ProdGroupCdeAR.AR50.name();
            }else {
                 return ProdGroupCdeAR.AR02.name();
            }
        }
        if (StringUtils.equals(PPN_50, prodSubtpCde))
            return ObjectUtils.compare(cptlProtcPct, 0D) > 0 ? ProdGroupCdeAR.AR12.name() : ProdGroupCdeAR.AR25.name();

        if (StringUtils.equals(FX, prodExtnlCatCde)) {
            Map<String, String> prodSubtpCdeMap = new ImmutableMap.Builder<String, String>()
                    .put(FXN_01, ProdGroupCdeAR.AR42.name())
                    .put(FXN_02, ProdGroupCdeAR.AR44.name())
                    .put(FXN_03, ProdGroupCdeAR.AR46.name())
                    .put(FXN_04, ProdGroupCdeAR.AR48.name())
                    .build();
            return prodSubtpCdeMap.get(prodSubtpCde);
        }

        if (StringUtils.equals(PPN, prodExtnlCatCde)) {
            Map<String, String> prodSubtpCdeMap = new ImmutableMap.Builder<String, String>()
                    .put(PPN_FCN, ProdGroupCdeAR.AR02.name())
                    .put(PPN_PPR, ProdGroupCdeAR.AR04.name())
                    .put(PPN_STPDWN, ProdGroupCdeAR.AR06.name())
                    .put(PPN_UPOUT, ProdGroupCdeAR.AR08.name())
                    .put(UO_ELI, ProdGroupCdeAR.AR08.name())
                    .put(PPN_TWSF, ProdGroupCdeAR.AR10.name())
                    .put(PPN_51, ProdGroupCdeAR.AR28.name())
                    .put(PPN_52, ProdGroupCdeAR.AR30.name())
                    .put(PPN_61, ProdGroupCdeAR.AR32.name())
                    .put(PPN_62, ProdGroupCdeAR.AR34.name())
                    .put(PPN_63, ProdGroupCdeAR.AR36.name())
                    .put(PPN_64, ProdGroupCdeAR.AR38.name())
                    .put(PPN_65, ProdGroupCdeAR.AR40.name())
                    .put(PPN_53, ProdGroupCdeAR.AR50.name())
                    .put(HY_01, ProdGroupCdeAR.AR32.name())
                    .build();
            return prodSubtpCdeMap.get(prodSubtpCde);
        }
        return null;
    }

    public String genUtAudioRecording(Map<String, Object> prod) {
        Map<String, Object> utTrstInstm = (Map<String, Object>) prod.getOrDefault("utTrstInstm", Collections.emptyMap());
        String piFundInd = (String) utTrstInstm.get(Field.piFundInd);
        String originGroup = (String) prod.get(grpNumAudioRecording);
        if (StringUtils.equals(INDICATOR_YES, piFundInd)) return originGroup;

        String closeEndFundInd = (String) utTrstInstm.get(Field.closeEndFundInd);
        String cmplxProdInd = (String) prod.get(Field.cmplxProdInd);
        if (!StringUtils.equals(COMPLEX_PROD_IND, cmplxProdInd)) return ProdGroupCdeAR.AR00.name();
        return (StringUtils.equals(INDICATOR_YES, closeEndFundInd) ? ProdGroupCdeAR.AR16 : ProdGroupCdeAR.AR14).name();
    }

    public String genBondAudioRecording(Map<String, Object> prod) {
        String prodSubtpCde = (String) prod.get(Field.prodSubtpCde);
        String cmplxProdInd = (String) prod.get(Field.cmplxProdInd);
        String highYieldBondInd = (String) prod.get(Field.highYieldBondInd);

        if (StringUtils.equals(prodSubtpCde, CD) || !StringUtils.equals(COMPLEX_PROD_IND, cmplxProdInd))
            return ProdGroupCdeAR.AR00.name();
        return (StringUtils.equals(INDICATOR_YES, highYieldBondInd) ? ProdGroupCdeAR.AR18 : ProdGroupCdeAR.AR17).name();
    }

    private enum ProdGroupCdeAR {
        AR00, AR01, AR02, AR03, AR04, AR05, AR06, AR07, AR08, AR09, AR10, AR11, AR12, AR14, AR16, AR17, AR18, AR24, AR25, AR26, AR27, AR28, AR29, AR30, AR31, AR32, AR33, AR34, AR35, AR36, AR37, AR38, AR39, AR40, AR41, AR42, AR43, AR44, AR45, AR46, AR47, AR48, AR49, AR50
    }

    private void calcGrpNumRiskDisclosure(Map<String, Object> newProd) {
        String prodTypeCde = (String) newProd.get("prodTypeCde");
        String grpNumRiskDisclosure = null;

        switch (prodTypeCde) {
            case EQUITY_LINKED_INVESTMENT:
                grpNumRiskDisclosure = genEliGroupCdeRiskDisclosure(newProd);
                break;
            case UNIT_TRUST:
                grpNumRiskDisclosure = genUtGroupCdeRiskDisclosure(newProd);
                break;
            case BOND_CD:
                grpNumRiskDisclosure = genBondGroupCdeRiskDisclosure(newProd);
                break;
            case STRUCTURE_INVESTMENT_DEPOSIT:
                grpNumRiskDisclosure = genSidGroupCdeRiskDisclosure(newProd);
                break;
            case DEPOSIT_PLUS:
                grpNumRiskDisclosure = genDpsGroupCdeRiskDisclosure();
                break;
            default:
        }

        if (!StringUtils.equals(grpNumRiskDisclosure, (String) newProd.get(Field.grpNumRiskDisclosure))) {
            newProd.put(Field.grpNumRiskDisclosure, grpNumRiskDisclosure);
        }
    }

    public String genUtGroupCdeRiskDisclosure(Map<String, Object> prod) {
        String originGroup = (String) prod.get(Field.grpNumRiskDisclosure);

        Map<String, Object> utTrstInstm = (Map<String, Object>) prod.getOrDefault("utTrstInstm", Collections.emptyMap());
        String piFundInd = (String) utTrstInstm.get(Field.piFundInd);

        if (StringUtils.equals(INDICATOR_YES, piFundInd)) return originGroup;

        String cmplxProdInd = (String) prod.get(Field.cmplxProdInd);
        String closeEndFundInd = (String) utTrstInstm.get(Field.closeEndFundInd);
        if (StringUtils.equals(INDICATOR_YES, closeEndFundInd))
            return (StringUtils.equals(COMPLEX_PROD_IND, cmplxProdInd) ? ProdGroupCdeRD.RD04 : ProdGroupCdeRD.RD03).name();
        return (StringUtils.equals(COMPLEX_PROD_IND, cmplxProdInd) ? ProdGroupCdeRD.RD02 : ProdGroupCdeRD.RD01).name();
    }

    public String genDpsGroupCdeRiskDisclosure() {
        return ProdGroupCdeRD.RD05.name();
    }

    public String genSidGroupCdeRiskDisclosure(Map<String, Object> prod) {
        String prodSubtpCde = (String) prod.get(Field.prodSubtpCde);
        switch (prodSubtpCde) {
            case CPI3:
            case CPI5:
                return ProdGroupCdeRD.RD06.name();
            case CFFD:
                return ProdGroupCdeRD.RD07.name();
            case CRA:
                return ProdGroupCdeRD.RD26.name();
            case IRRA:
                return ProdGroupCdeRD.RD27.name();
            default:
        }
        return StringUtils.EMPTY;
    }

    public String genEliGroupCdeRiskDisclosure(Map<String, Object> prod) {
        String prodSubtpCde = (String) prod.get(Field.prodSubtpCde);
        Document eqtyLinkInvst = new Document((Map<String, Object>) prod.getOrDefault(Field.eqtyLinkInvst, Collections.emptyMap()));
        String prodExtnlCatCde = eqtyLinkInvst.getString("prodExtnlCatCde");
        Double cptlProtcPct = ((Number) eqtyLinkInvst.get(Field.cptlProtcPct, 0D)).doubleValue();
        List<? extends Map> undlStock = eqtyLinkInvst.getList(Field.undlStock, Map.class, Collections.emptyList());

        if (CollectionUtils.isEmpty(undlStock)) return null;

        if (undlStock.size() == 1) {
            return computeEliDisclosureIfOneUndl(prodSubtpCde, prodExtnlCatCde, cptlProtcPct);
        } else {
            return computeEliDisclosureIfMoreUndl(prodSubtpCde, prodExtnlCatCde, cptlProtcPct);
        }
    }

    private String computeEliDisclosureIfOneUndl(String prodSubtpCde, String prodExtnlCatCde, Double cptlProtcPct) {
        if (StringUtils.equals(DCDC_ELI, prodExtnlCatCde)) {
            if(StringUtils.equals(BEN_ELI, prodSubtpCde)){
                  return ProdGroupCdeRD.RD59.name();
            }else{
                return ProdGroupCdeRD.RD08.name();
            }
        }
        if (PPN_50.equals(prodSubtpCde)) {
            return ObjectUtils.compare(cptlProtcPct, 0D) > 0 ? ProdGroupCdeRD.RD18.name() : ProdGroupCdeRD.RD34.name();
        }

        if (StringUtils.equals(FX, prodExtnlCatCde)) {
            Map<String, String> prodSubtpCdeMap = new ImmutableMap.Builder<String, String>()
                    .put(FXN_01, ProdGroupCdeRD.RD51.name())
                    .put(FXN_02, ProdGroupCdeRD.RD53.name())
                    .put(FXN_03, ProdGroupCdeRD.RD55.name())
                    .put(FXN_04, ProdGroupCdeRD.RD57.name())
                    .build();
            return prodSubtpCdeMap.get(prodSubtpCde);
        }
        if (StringUtils.equals(PPN, prodExtnlCatCde)) {
            // reduce the Complexity
            Map<String, String> prodSubtpCdeMap = new ImmutableMap.Builder<String, String>()
                    .put(PPN_FCN, ProdGroupCdeRD.RD08.name())
                    .put(PPN_PPR, ProdGroupCdeRD.RD10.name())
                    .put(PPN_STPDWN, ProdGroupCdeRD.RD12.name())
                    .put(PPN_UPOUT, ProdGroupCdeRD.RD14.name())
                    .put(PPN_TWSF, ProdGroupCdeRD.RD16.name())
                    .put(UO_ELI, ProdGroupCdeRD.RD14.name())
                    .put(PPN_60, ProdGroupCdeRD.RD36.name())
                    .put(PPN_51, ProdGroupCdeRD.RD37.name())
                    .put(PPN_52, ProdGroupCdeRD.RD39.name())
                    .put(PPN_61, ProdGroupCdeRD.RD41.name())
                    .put(PPN_62, ProdGroupCdeRD.RD43.name())
                    .put(PPN_63, ProdGroupCdeRD.RD45.name())
                    .put(PPN_64, ProdGroupCdeRD.RD47.name())
                    .put(PPN_65, ProdGroupCdeRD.RD49.name())
                    .put(PPN_53, ProdGroupCdeRD.RD59.name())
                    .put(HY_01, ProdGroupCdeRD.RD41.name())
                    .build();
            return prodSubtpCdeMap.get(prodSubtpCde);
        }

        return null;
    }

    private String computeEliDisclosureIfMoreUndl(String prodSubtpCde, String prodExtnlCatCde, Double cptlProtcPct) {
        if (StringUtils.equals(DCDC_ELI, prodExtnlCatCde)) {
             if(StringUtils.equals(BEN_ELI, prodSubtpCde)){
                  return ProdGroupCdeRD.RD60.name();
            }else {
                 return ProdGroupCdeRD.RD09.name();
             }
        }
        if (PPN_50.equals(prodSubtpCde)) {
            return ObjectUtils.compare(cptlProtcPct, 0D) > 0 ? ProdGroupCdeRD.RD19.name() : ProdGroupCdeRD.RD35.name();
        }
        if (StringUtils.equals(FX, prodExtnlCatCde)) {
            Map<String, String> prodSubtpCdeMap = new ImmutableMap.Builder<String, String>()
                    .put(FXN_01, ProdGroupCdeRD.RD52.name())
                    .put(FXN_02, ProdGroupCdeRD.RD54.name())
                    .put(FXN_03, ProdGroupCdeRD.RD56.name())
                    .put(FXN_04, ProdGroupCdeRD.RD58.name())
                    .build();
            return prodSubtpCdeMap.get(prodSubtpCde);
        }
        if (StringUtils.equals(PPN, prodExtnlCatCde)) {
            Map<String, String> prodSubtpCdeMap = new ImmutableMap.Builder<String, String>()
                    .put(PPN_FCN, ProdGroupCdeRD.RD09.name())
                    .put(PPN_PPR, ProdGroupCdeRD.RD11.name())
                    .put(PPN_STPDWN, ProdGroupCdeRD.RD13.name())
                    .put(PPN_UPOUT, ProdGroupCdeRD.RD15.name())
                    .put(PPN_TWSF, ProdGroupCdeRD.RD17.name())
                    .put(UO_ELI, ProdGroupCdeRD.RD15.name())
                    .put(PPN_51, ProdGroupCdeRD.RD38.name())
                    .put(PPN_52, ProdGroupCdeRD.RD40.name())
                    .put(PPN_61, ProdGroupCdeRD.RD42.name())
                    .put(PPN_62, ProdGroupCdeRD.RD44.name())
                    .put(PPN_63, ProdGroupCdeRD.RD46.name())
                    .put(PPN_64, ProdGroupCdeRD.RD48.name())
                    .put(PPN_65, ProdGroupCdeRD.RD50.name())
                    .put(PPN_53, ProdGroupCdeRD.RD60.name())
                    .put(HY_01, ProdGroupCdeRD.RD42.name())
                    .build();
            return prodSubtpCdeMap.get(prodSubtpCde);
        }
        return null;
    }

    public String genBondGroupCdeRiskDisclosure(Map<String, Object> prod) {
        String prodSubtpCde = (String) prod.get(Field.prodSubtpCde);
        String cmplxProdInd = (String) prod.get(Field.cmplxProdInd);
        String highYieldBondInd = (String) prod.get(Field.highYieldBondInd);

        if (StringUtils.equals(prodSubtpCde, CD)) return ProdGroupCdeRD.RD24.name();
        if (StringUtils.equals(INDICATOR_YES, highYieldBondInd))
            return (StringUtils.equals(COMPLEX_PROD_IND, cmplxProdInd) ? ProdGroupCdeRD.RD22 : ProdGroupCdeRD.RD23).name();
        return (StringUtils.equals(COMPLEX_PROD_IND, cmplxProdInd) ? ProdGroupCdeRD.RD20 : ProdGroupCdeRD.RD21).name();
    }

    private enum ProdGroupCdeRD {
        RD01, RD02, RD03, RD04, RD05, RD06, RD07, RD08, RD09, RD10, RD11, RD12, RD13, RD14, RD15, RD16, RD17, RD18, RD19, RD20, RD21, RD22, RD23, RD24, RD26, RD27, RD34, RD35, RD36, RD37, RD38, RD39, RD40, RD41, RD42, RD43, RD44, RD45, RD46, RD47, RD48, RD49, RD50, RD51, RD52, RD53, RD54, RD55, RD56, RD57, RD58, RD59, RD60
    }
}
