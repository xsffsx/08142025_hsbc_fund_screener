
package com.hhhh.group.secwealth.mktdata.fund.util;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.hhhh.group.secwealth.mktdata.common.dao.pojo.UtCatAsetAlloc;
import com.hhhh.group.secwealth.mktdata.common.system.constants.CommonConstants;
import com.hhhh.group.secwealth.mktdata.fund.beans.request.FundListRequest;
import com.hhhh.group.secwealth.mktdata.fund.beans.request.FundQuoteSummaryRequest;
import com.hhhh.group.secwealth.mktdata.fund.beans.request.FundSearchResultRequest;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.fundsearchresult.FundSearchGeographicRegion;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.fundsearchresult.FundSearchSector;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.fundsearchresult.FundSearchSectorIdList;




public final class FundSearchResultUtil {

    private FundSearchResultUtil() {
    }

    // FundSearchSector full list ID
    // sorted order:51,58,53,54,50,97,55,56,52,98,99,57,59
    public static FundSearchSector getFundSearchSector(final FundSearchSector sector, final List<UtCatAsetAlloc> holdingsBySecors) {
        for (UtCatAsetAlloc utHoldingsHelper : holdingsBySecors) {
            String holdingId = utHoldingsHelper.getUtCatAsetAllocId().getAssetClsCde();
            BigDecimal fundAllocation = utHoldingsHelper.getFundAllocation();
            Boolean isIsFundShortPosition = false;
            if ("1".equals(utHoldingsHelper.getIsFundShortPosition())) {
                isIsFundShortPosition = true;
            }

            switch(holdingId){
                case FundSearchSectorIdList.SECTOR_ID_51:
                    sector.setBasicMaterials(fundAllocation);
                    sector.setBasicMaterialsIndicator(isIsFundShortPosition);
                    break;
                case FundSearchSectorIdList.SECTOR_ID_58:
                    sector.setCommunicationServices(fundAllocation);
                    sector.setCommunicationServicesIndicator(isIsFundShortPosition);
                    break;
                case FundSearchSectorIdList.SECTOR_ID_53:
                    sector.setConsumerCyclical(fundAllocation);
                    sector.setConsumerCyclicalIndicator(isIsFundShortPosition);
                    break;
                case FundSearchSectorIdList.SECTOR_ID_54:
                    sector.setConsumerDefensive(fundAllocation);
                    sector.setConsumerDefensiveIndicator(isIsFundShortPosition);
                    break;
                case FundSearchSectorIdList.SECTOR_ID_50:
                    sector.setEnergy(fundAllocation);
                    sector.setEnergyIndicator(isIsFundShortPosition);
                    break;
                case FundSearchSectorIdList.SECTOR_ID_97:
                    sector.setExcluded(fundAllocation);
                    sector.setExcludedIndicator(isIsFundShortPosition);
                    break;
                case FundSearchSectorIdList.SECTOR_ID_55:
                    sector.setFinancialServices(fundAllocation);
                    sector.setFinancialServicesIndicator(isIsFundShortPosition);
                    break;
                case FundSearchSectorIdList.SECTOR_ID_56:
                    sector.setHealthcare(fundAllocation);
                    sector.setHealthcareIndicator(isIsFundShortPosition);
                    break;
                case FundSearchSectorIdList.SECTOR_ID_52:
                    sector.setIndustrials(fundAllocation);
                    sector.setIndustrialsIndicator(isIsFundShortPosition);
                    break;
                case FundSearchSectorIdList.SECTOR_ID_98:
                    sector.setNotClassified(fundAllocation);
                    sector.setNotClassifiedIndicator(isIsFundShortPosition);
                    break;
                case FundSearchSectorIdList.SECTOR_ID_99:
                    sector.setOthers(fundAllocation);
                    sector.setOthersIndicator(isIsFundShortPosition);
                    break;
                case FundSearchSectorIdList.SECTOR_ID_57:
                    sector.setTechnology(fundAllocation);
                    sector.setTechnologyIndicator(isIsFundShortPosition);
                    break;
                case FundSearchSectorIdList.SECTOR_ID_59:
                    sector.setUtilities(fundAllocation);
                    sector.setUtilitiesIndicator(isIsFundShortPosition);
                    break;
                default:
            }
        }
        return sector;
    }

    // FundSearchGeographicRegion full list ID
    // sorted
    // order:AU,AT,BE,BR,CA,CL,CN,CO,DK,EM,EU,FI,FR,DE,GR,HK,IN,ID,IE,IL,IT,JP,
    // KR,MY,MX,NL,NZ,NO,XX,OT,PE,PH,PT,SG,ES,SE,CH,TW,TH,AE,GB,US
    @SuppressWarnings("java:S1479")
    public static FundSearchGeographicRegion getFundSearchGeographicRegion(final FundSearchGeographicRegion geographicRegion,
        final List<UtCatAsetAlloc> holdingsByGeos) {
        for (UtCatAsetAlloc utHoldingsHelper : holdingsByGeos) {
            String holdingId = utHoldingsHelper.getUtCatAsetAllocId().getAssetClsCde();
            BigDecimal fundAllocation = utHoldingsHelper.getCategoryAllocation();
            Boolean isIsFundShortPosition = false;
            if ("1".equals(utHoldingsHelper.getIsCategoryShortPosition())) {
                isIsFundShortPosition = true;
            }

            switch(holdingId) {
                case "AU":
                    geographicRegion.setAustralia(fundAllocation);
                    geographicRegion.setAustraliaIndicator(isIsFundShortPosition);
                    break;
                case "AT":
                    geographicRegion.setAustria(fundAllocation);
                    geographicRegion.setAustriaIndicator(isIsFundShortPosition);
                    break;
                case "BE":
                    geographicRegion.setBelgium(fundAllocation);
                    geographicRegion.setBelgiumIndicator(isIsFundShortPosition);
                    break;
                case "BR":
                    geographicRegion.setBrazil(fundAllocation);
                    geographicRegion.setBrazilIndicator(isIsFundShortPosition);
                    break;
                case "CA":
                    geographicRegion.setCanada(fundAllocation);
                    geographicRegion.setCanadaIndicator(isIsFundShortPosition);
                    break;
                case "CL":
                    geographicRegion.setChile(fundAllocation);
                    geographicRegion.setChileIndicator(isIsFundShortPosition);
                    break;
                case "CN":
                    geographicRegion.setChina(fundAllocation);
                    geographicRegion.setChinaIndicator(isIsFundShortPosition);
                    break;
                case "CO":
                    geographicRegion.setColombia(fundAllocation);
                    geographicRegion.setColombiaIndicator(isIsFundShortPosition);
                    break;
                case "DK":
                    geographicRegion.setDenmark(fundAllocation);
                    geographicRegion.setDenmarkIndicator(isIsFundShortPosition);
                    break;
                case "EM":
                    geographicRegion.seteMEMEA(fundAllocation);
                    geographicRegion.seteMEMEAIndicator(isIsFundShortPosition);
                    break;
                case "EU":
                    geographicRegion.setEurozone(fundAllocation);
                    geographicRegion.setEurozoneIndicator(isIsFundShortPosition);
                    break;
                case "FI":
                    geographicRegion.setFinland(fundAllocation);
                    geographicRegion.setFinlandIndicator(isIsFundShortPosition);
                    break;
                case "FR":
                    geographicRegion.setFrance(fundAllocation);
                    geographicRegion.setFranceIndicator(isIsFundShortPosition);
                    break;
                case "DE":
                    geographicRegion.setGermany(fundAllocation);
                    geographicRegion.setGermanyIndicator(isIsFundShortPosition);
                    break;
                case "GR":
                    geographicRegion.setGreece(fundAllocation);
                    geographicRegion.setGreeceIndicator(isIsFundShortPosition);
                    break;
                case "HK":
                    geographicRegion.setHongKong(fundAllocation);
                    geographicRegion.setHongKongIndicator(isIsFundShortPosition);
                    break;
                case "IN":
                    geographicRegion.setIndia(fundAllocation);
                    geographicRegion.setIndiaIndicator(isIsFundShortPosition);
                    break;
                case "ID":
                    geographicRegion.setIndonesia(fundAllocation);
                    geographicRegion.setIndonesiaIndicator(isIsFundShortPosition);
                    break;
                case "IE":
                    geographicRegion.setIreland(fundAllocation);
                    geographicRegion.setIrelandIndicator(isIsFundShortPosition);
                    break;
                case "IL":
                    geographicRegion.setIsrael(fundAllocation);
                    geographicRegion.setIsraelIndicator(isIsFundShortPosition);
                    break;
                case "IT":
                    geographicRegion.setItaly(fundAllocation);
                    geographicRegion.setItalyIndicator(isIsFundShortPosition);
                    break;
                case "JP":
                    geographicRegion.setJapan(fundAllocation);
                    geographicRegion.setJapanIndicator(isIsFundShortPosition);
                    break;
                case "KR":
                    geographicRegion.setKorea(fundAllocation);
                    geographicRegion.setKoreaIndicator(isIsFundShortPosition);
                    break;
                case "MY":
                    geographicRegion.setMalaysia(fundAllocation);
                    geographicRegion.setMalaysiaIndicator(isIsFundShortPosition);
                    break;
                case "NZ":
                    geographicRegion.setNewZealand(fundAllocation);
                    geographicRegion.setNewZealandIndicator(isIsFundShortPosition);
                    break;
                case "NO":
                    geographicRegion.setNorway(fundAllocation);
                    geographicRegion.setNorwayIndicator(isIsFundShortPosition);
                    break;
                case "XX":
                    geographicRegion.setNotclassified(fundAllocation);
                    geographicRegion.setNotclassifiedIndicator(isIsFundShortPosition);
                    break;
                case "OT":
                    geographicRegion.setOthercountries(fundAllocation);
                    geographicRegion.setOthercountriesIndicator(isIsFundShortPosition);
                    break;
                case "PE":
                    geographicRegion.setPeru(fundAllocation);
                    geographicRegion.setPeruIndicator(isIsFundShortPosition);
                    break;
                case "PH":
                    geographicRegion.setPhilippines(fundAllocation);
                    geographicRegion.setPhilippinesIndicator(isIsFundShortPosition);
                    break;
                case "PT":
                    geographicRegion.setPortugal(fundAllocation);
                    geographicRegion.setPortugalIndicator(isIsFundShortPosition);
                    break;
                case "SG":
                    geographicRegion.setSingapore(fundAllocation);
                    geographicRegion.setSingaporeIndicator(isIsFundShortPosition);
                    break;
                case "ES":
                    geographicRegion.setSpain(fundAllocation);
                    geographicRegion.setSpainIndicator(isIsFundShortPosition);
                    break;
                case "SE":
                    geographicRegion.setSweden(fundAllocation);
                    geographicRegion.setSwedenIndicator(isIsFundShortPosition);
                    break;
                case "MX":
                    geographicRegion.setMexico(fundAllocation);
                    geographicRegion.setMexicoIndicator(isIsFundShortPosition);
                    break;
                case "NL":
                    geographicRegion.setNetherlands(fundAllocation);
                    geographicRegion.setNetherlandsIndicator(isIsFundShortPosition);
                    break;
                case "CH":
                    geographicRegion.setSwitzerland(fundAllocation);
                    geographicRegion.setSwitzerlandIndicator(isIsFundShortPosition);
                    break;
                case "TW":
                    geographicRegion.setTaiwan(fundAllocation);
                    geographicRegion.setTaiwanIndicator(isIsFundShortPosition);
                    break;
                case "TH":
                    geographicRegion.setThailand(fundAllocation);
                    geographicRegion.setThailandIndicator(isIsFundShortPosition);
                    break;
                case "AE":
                    geographicRegion.setUnitedArabEmirates(fundAllocation);
                    geographicRegion.setUnitedArabEmiratesIndicator(isIsFundShortPosition);
                    break;
                case "GB":
                    geographicRegion.setUnitedKingdom(fundAllocation);
                    geographicRegion.setUnitedKingdomIndicator(isIsFundShortPosition);
                    break;
                case "US":
                    geographicRegion.setUnitedStates(fundAllocation);
                    geographicRegion.setUnitedStatesIndicator(isIsFundShortPosition);
                    break;
                default:
            }
        }
        return geographicRegion;
    }

    public static String buildSearchResultChannelRestrictCode(final FundSearchResultRequest request, final String cmbInd){
        Map<String, String> headers = request.getHeaders();
        String channelRestrictCode = request.getChannelRestrictCode();
        if("CMB".equals(cmbInd)){
            channelRestrictCode = getCmbInd(headers, channelRestrictCode);
        }
        return channelRestrictCode;
    }

    public static String buildCmbQuoteSummaryChannelRestrictCode(final FundQuoteSummaryRequest request, final String cmbInd){
        Map<String, String> headers = request.getHeaders();
        String channelRestrictCode = request.getChannelRestrictCode();
        if("CMB".equals(cmbInd)){
            channelRestrictCode = getCmbInd(headers, channelRestrictCode);
        }
        return channelRestrictCode;
    }

    public static String buildCmbFundListRequestChannelRestrictCode(final FundListRequest request, final String cmbInd){
        Map<String, String> headers = request.getHeaders();
        String channelRestrictCode = request.getChannelRestrictCode();
        if("CMB".equals(cmbInd)){
            channelRestrictCode = getCmbInd(headers, channelRestrictCode);
        }
        return channelRestrictCode;
    }

    private static String getCmbInd(Map<String, String> headers, String channelRestrictCode) {
        String channel = headers.get(CommonConstants.REQUEST_HEADER_CHANNELID);
        if(CommonConstants.CHANNEL_I.equals(channel)){
            channelRestrictCode = "CMB_I";
        }else if(CommonConstants.CHANNEL_B.equals(channel)){
            channelRestrictCode = "CMB_B";
        }
        return channelRestrictCode;
    }

}
