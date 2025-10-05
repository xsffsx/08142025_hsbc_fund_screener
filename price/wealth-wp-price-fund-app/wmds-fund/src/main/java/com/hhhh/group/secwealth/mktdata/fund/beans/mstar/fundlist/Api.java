
package com.hhhh.group.secwealth.mktdata.fund.beans.mstar.fundlist;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;



@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"aabrpAssetAllocBondLong", "aabrpAssetAllocBondNet", "aabrpAssetAllocBondShort",
    "aabrpAssetAllocCashLong", "aabrpAssetAllocCashNet", "aabrpAssetAllocCashShort", "aabrpAssetAllocConvBondLong",
    "aabrpAssetAllocEquityLong", "aabrpAssetAllocEquityNet", "aabrpAssetAllocEquityShort", "aabrpConvertibleShort",
    "aabrpOtherLong", "aabrpOtherNet", "aabrpOtherShort", "aabrpPortfolioDate", "aabrpPreferredStockLong",
    "aabrpPreferredStockNet", "aabrpPreferredStockShort", "rebrpEquityRegionAfrica", "rebrpEquityRegionAsiadevLongRescaled",
    "rebrpEquityRegionAsiaemrgLongRescaled", "rebrpEquityRegionAustralasiaLongRescaled", "rebrpEquityRegionCanada",
    "rebrpEquityRegionDevelopedLongRescaled", "rebrpEquityRegionEmergingLongRescaled", "rebrpEquityRegionEuropeemrgLongRescaled",
    "rebrpEquityRegionEuropeexeuro", "rebrpEquityRegionEurozone", "rebrpEquityRegionJapanLongRescaled",
    "rebrpEquityRegionLatinAmericaLongRescaled", "rebrpEquityRegionMiddleEast", "rebrpEquityRegionNotClassifiedLongRescaled",
    "rebrpEquityRegionUnitedKingdomLongRescaled", "rebrpEquityRegionUnitedStates", "rebrpPortfolioDate",
    "gssbrpEquitySectorBasicMaterialsLongRescaled", "gssbrpEquitySectorCommunicationServicesLongRescaled",
    "gssbrpEquitySectorConsumerCyclicalLongRescaled", "gssbrpEquitySectorConsumerDefensiveLongRescaled",
    "gssbrpEquitySectorEnergyLongRescaled", "gssbrpEquitySectorFinancialServicesLongRescaled",
    "gssbrpEquitySectorHealthcareLongRescaled", "gssbrpEquitySectorIndustrialsLongRescaled",
    "gssbrpEquitySectorRealEstateLongRescaled", "gssbrpEquitySectorTechnologyLongRescaled",
    "gssbrpEquitySectorUtilitiesLongRescaled", "gssbrpGlobalSectorPortfolioDate",
    "gbsrpFixedIncPrimarySectorAgencyMortgageBackedLongRescaled", "gbsrpFixedIncPrimarySectorAssetBackedLongRescaled",
    "gbsrpFixedIncPrimarySectorBankLoanLongRescaled", "gbsrpFixedIncPrimarySectorCashEquivalentsLongRescaled",
    "gbsrpFixedIncPrimarySectorCommercialMortgageBackedLongRescaled", "gbsrpFixedIncPrimarySectorConvertibleLongRescaled",
    "gbsrpFixedIncPrimarySectorCorporateBondLongRescaled", "gbsrpFixedIncPrimarySectorCoveredBondLongRescaled",
    "gbsrpFixedIncPrimarySectorForwardFutureLongRescaled", "gbsrpFixedIncPrimarySectorGovernmentLongRescaled",
    "gbsrpFixedIncPrimarySectorGovernmentRelatedLongRescaled", "gbsrpFixedIncPrimarySectorMunicipalTaxExemptLongRescaled",
    "gbsrpFixedIncPrimarySectorMunicipalTaxableLongRescaled",
    "gbsrpFixedIncPrimarySectorNonAgencyResidentialMortgageBackedLongRescaled",
    "gbsrpFixedIncPrimarySectorOptionWarrantLongRescaled", "gbsrpFixedIncPrimarySectorPreferredStockLongRescaled",
    "gbsrpFixedIncPrimarySectorSwapLongRescaled", "gbsrpFixedIncSuperSectorCashEquivalentsLongRescaled",
    "gbsrpFixedIncSuperSectorCorporateLongRescaled", "gbsrpFixedIncSuperSectorDerivativeLongRescaled",
    "gbsrpFixedIncSuperSectorGovernmentLongRescaled", "gbsrpFixedIncSuperSectorMunicipalLongRescaled",
    "gbsrpFixedIncSuperSectorSecuritizedLongRescaled", "gbsrpPortfolioDate", "psrpNumberOfBondHoldings",
    "psrpNumberOfStockHoldings", "psrpPortfolioDate", "icInvestmentStrategy", "icMultilingualInvestmentStrategies",
    "tsDayEndBidPrice", "tsnav52WkHigh", "tsnav52WkLow", "tsDayEndOfferPrice", "dpDividend", "dpDividendDate", "mrRating3Year",
    "mrRating5Year", "mrRating10Year", "mrRatingDate", "mrRatingOverall", "tarRating3Yr", "tarRating5Yr", "tarRating10Yr",
    "tarRatingOverall", "cqbrpCreditQualA", "cqbrpCreditQualAA", "cqbrpCreditQualAAA", "cqbrpCreditQualB", "cqbrpCreditQualBB",
    "cqbrpCreditQualBBB", "cqbrpCreditQualBelowB", "cqbrpCreditQualNotRated", "cqbrpCreditQualDate", "cyrCategoryYear1",
    "cyrCategoryYear2", "cyrCategoryYear3", "cyrCategoryYear4", "cyrCategoryYear5", "cyrCategoryYear6", "cyrCategoryYear7",
    "cyrCategoryYear8", "cyrCategoryYear9", "cyrCategoryYear10", "cyrBestFitIndexYear1", "cyrBestFitIndexYear2",
    "cyrBestFitIndexYear3", "cyrBestFitIndexYear4", "cyrBestFitIndexYear5", "cyrBestFitIndexYear6", "cyrBestFitIndexYear7",
    "cyrBestFitIndexYear8", "cyrBestFitIndexYear9", "ttrBestFitIndexReturnYTD", "ttrCategoryReturnYTD",
    "ttrBestFitIndexReturn1Mth", "ttrBestFitIndexReturn3Mth", "ttrBestFitIndexReturn6Mth", "ttrBestFitIndexReturn1Yr",
    "ttrBestFitIndexReturn3Yr", "ttrBestFitIndexReturn5Yr", "ttrBestFitIndexReturn10Yr", "ttrCategoryReturn1Mth",
    "ttrCategoryReturn3Mth", "ttrCategoryReturn6Mth", "ttrCategoryReturn1Yr", "ttrCategoryReturn3Yr", "ttrCategoryReturn5Yr",
    "ttrCategoryReturn10Yr", "fmManagers", "fm2Managers", "mptpiIndexName", "dpDayEndDate", "ttrMonthEndDate",
    "dpCategoryReturn1Mth", "dpCategoryReturn3Mth", "dpCategoryReturn6Mth", "dpCategoryReturn1Yr", "dpCategoryReturn3Yr",
    "dpCategoryReturn5Yr", "dpCategoryReturn10Yr", "dpCategoryReturnYTD", "srStubYearEndReturnEndDate", "srStubYearEndReturn",
    "fbPrimaryProspectusBenchmarks", "ttrPrimaryIndexReturn1Mth", "ttrPrimaryIndexReturn3Mth", "ttrPrimaryIndexReturn6Mth",
    "ttrPrimaryIndexReturn1Yr", "ttrPrimaryIndexReturn3Yr", "ttrPrimaryIndexReturn5Yr", "ttrPrimaryIndexReturn10Yr",
    "ttrPrimaryIndexReturnYTD", "tsDayEndBidOfferPricesDate", "tsDayEndNAVDate", "cyrPrimaryIndexYear1", "cyrPrimaryIndexYear10",
    "cyrPrimaryIndexYear2", "cyrPrimaryIndexYear3", "cyrPrimaryIndexYear4", "cyrPrimaryIndexYear5", "cyrPrimaryIndexYear6",
    "cyrPrimaryIndexYear7", "cyrPrimaryIndexYear8", "cyrPrimaryIndexYear9", "mptpiIndexId"})
@XmlRootElement(name = "api")
public class Api {

    @XmlElement(name = "AABRP-AssetAllocBondLong", required = true)
    protected String aabrpAssetAllocBondLong;
    @XmlElement(name = "AABRP-AssetAllocBondNet", required = true)
    protected String aabrpAssetAllocBondNet;
    @XmlElement(name = "AABRP-AssetAllocBondShort", required = true)
    protected String aabrpAssetAllocBondShort;
    @XmlElement(name = "AABRP-AssetAllocCashLong", required = true)
    protected String aabrpAssetAllocCashLong;
    @XmlElement(name = "AABRP-AssetAllocCashNet", required = true)
    protected String aabrpAssetAllocCashNet;
    @XmlElement(name = "AABRP-AssetAllocCashShort", required = true)
    protected String aabrpAssetAllocCashShort;
    @XmlElement(name = "AABRP-AssetAllocConvBondLong", required = true)
    protected String aabrpAssetAllocConvBondLong;
    @XmlElement(name = "AABRP-AssetAllocEquityLong", required = true)
    protected String aabrpAssetAllocEquityLong;
    @XmlElement(name = "AABRP-AssetAllocEquityNet", required = true)
    protected String aabrpAssetAllocEquityNet;
    @XmlElement(name = "AABRP-AssetAllocEquityShort", required = true)
    protected String aabrpAssetAllocEquityShort;
    @XmlElement(name = "AABRP-ConvertibleShort", required = true)
    protected String aabrpConvertibleShort;
    @XmlElement(name = "AABRP-OtherLong", required = true)
    protected String aabrpOtherLong;
    @XmlElement(name = "AABRP-OtherNet", required = true)
    protected String aabrpOtherNet;
    @XmlElement(name = "AABRP-OtherShort", required = true)
    protected String aabrpOtherShort;
    @XmlElement(name = "AABRP-PortfolioDate", required = true)
    protected String aabrpPortfolioDate;
    @XmlElement(name = "AABRP-PreferredStockLong", required = true)
    protected String aabrpPreferredStockLong;
    @XmlElement(name = "AABRP-PreferredStockNet", required = true)
    protected String aabrpPreferredStockNet;
    @XmlElement(name = "AABRP-PreferredStockShort", required = true)
    protected String aabrpPreferredStockShort;

    @XmlElement(name = "REBRP-EquityRegionAfrica", required = true)
    protected String rebrpEquityRegionAfrica;
    @XmlElement(name = "REBRP-EquityRegionAsiadevLongRescaled", required = true)
    protected String rebrpEquityRegionAsiadevLongRescaled;
    @XmlElement(name = "REBRP-EquityRegionAsiaemrgLongRescaled", required = true)
    protected String rebrpEquityRegionAsiaemrgLongRescaled;
    @XmlElement(name = "REBRP-EquityRegionAustralasiaLongRescaled", required = true)
    protected String rebrpEquityRegionAustralasiaLongRescaled;
    @XmlElement(name = "REBRP-EquityRegionCanada", required = true)
    protected String rebrpEquityRegionCanada;
    @XmlElement(name = "REBRP-EquityRegionDevelopedLongRescaled", required = true)
    protected String rebrpEquityRegionDevelopedLongRescaled;
    @XmlElement(name = "REBRP-EquityRegionEmergingLongRescaled", required = true)
    protected String rebrpEquityRegionEmergingLongRescaled;
    @XmlElement(name = "REBRP-EquityRegionEuropeemrgLongRescaled", required = true)
    protected String rebrpEquityRegionEuropeemrgLongRescaled;
    @XmlElement(name = "REBRP-EquityRegionEuropeexeuro", required = true)
    protected String rebrpEquityRegionEuropeexeuro;
    @XmlElement(name = "REBRP-EquityRegionEurozone", required = true)
    protected String rebrpEquityRegionEurozone;
    @XmlElement(name = "REBRP-EquityRegionJapanLongRescaled", required = true)
    protected String rebrpEquityRegionJapanLongRescaled;
    @XmlElement(name = "REBRP-EquityRegionLatinAmericaLongRescaled", required = true)
    protected String rebrpEquityRegionLatinAmericaLongRescaled;
    @XmlElement(name = "REBRP-EquityRegionMiddleEast", required = true)
    protected String rebrpEquityRegionMiddleEast;
    @XmlElement(name = "REBRP-EquityRegionNotClassifiedLongRescaled", required = true)
    protected String rebrpEquityRegionNotClassifiedLongRescaled;
    @XmlElement(name = "REBRP-EquityRegionUnitedKingdomLongRescaled", required = true)
    protected String rebrpEquityRegionUnitedKingdomLongRescaled;
    @XmlElement(name = "REBRP-EquityRegionUnitedStates", required = true)
    protected String rebrpEquityRegionUnitedStates;
    @XmlElement(name = "REBRP-PortfolioDate", required = true)
    protected String rebrpPortfolioDate;

    @XmlElement(name = "GSSBRP-EquitySectorBasicMaterialsLongRescaled", required = true)
    protected String gssbrpEquitySectorBasicMaterialsLongRescaled;
    @XmlElement(name = "GSSBRP-EquitySectorCommunicationServicesLongRescaled", required = true)
    protected String gssbrpEquitySectorCommunicationServicesLongRescaled;
    @XmlElement(name = "GSSBRP-EquitySectorConsumerCyclicalLongRescaled", required = true)
    protected String gssbrpEquitySectorConsumerCyclicalLongRescaled;
    @XmlElement(name = "GSSBRP-EquitySectorConsumerDefensiveLongRescaled", required = true)
    protected String gssbrpEquitySectorConsumerDefensiveLongRescaled;
    @XmlElement(name = "GSSBRP-EquitySectorEnergyLongRescaled", required = true)
    protected String gssbrpEquitySectorEnergyLongRescaled;
    @XmlElement(name = "GSSBRP-EquitySectorFinancialServicesLongRescaled", required = true)
    protected String gssbrpEquitySectorFinancialServicesLongRescaled;
    @XmlElement(name = "GSSBRP-EquitySectorHealthcareLongRescaled", required = true)
    protected String gssbrpEquitySectorHealthcareLongRescaled;
    @XmlElement(name = "GSSBRP-EquitySectorIndustrialsLongRescaled", required = true)
    protected String gssbrpEquitySectorIndustrialsLongRescaled;
    @XmlElement(name = "GSSBRP-EquitySectorRealEstateLongRescaled", required = true)
    protected String gssbrpEquitySectorRealEstateLongRescaled;
    @XmlElement(name = "GSSBRP-EquitySectorTechnologyLongRescaled", required = true)
    protected String gssbrpEquitySectorTechnologyLongRescaled;
    @XmlElement(name = "GSSBRP-EquitySectorUtilitiesLongRescaled", required = true)
    protected String gssbrpEquitySectorUtilitiesLongRescaled;
    @XmlElement(name = "GSSBRP-GlobalSectorPortfolioDate", required = true)
    protected String gssbrpGlobalSectorPortfolioDate;

    @XmlElement(name = "GBSRP-FixedIncPrimarySectorAgencyMortgage-BackedLongRescaled", required = true)
    protected String gbsrpFixedIncPrimarySectorAgencyMortgageBackedLongRescaled;
    @XmlElement(name = "GBSRP-FixedIncPrimarySectorAssetBackedLongRescaled", required = true)
    protected String gbsrpFixedIncPrimarySectorAssetBackedLongRescaled;
    @XmlElement(name = "GBSRP-FixedIncPrimarySectorBankLoanLongRescaled", required = true)
    protected String gbsrpFixedIncPrimarySectorBankLoanLongRescaled;
    @XmlElement(name = "GBSRP-FixedIncPrimarySectorCashEquivalentsLongRescaled", required = true)
    protected String gbsrpFixedIncPrimarySectorCashEquivalentsLongRescaled;
    @XmlElement(name = "GBSRP-FixedIncPrimarySectorCommercialMortgageBackedLongRescaled", required = true)
    protected String gbsrpFixedIncPrimarySectorCommercialMortgageBackedLongRescaled;
    @XmlElement(name = "GBSRP-FixedIncPrimarySectorConvertibleLongRescaled", required = true)
    protected String gbsrpFixedIncPrimarySectorConvertibleLongRescaled;
    @XmlElement(name = "GBSRP-FixedIncPrimarySectorCorporateBondLongRescaled", required = true)
    protected String gbsrpFixedIncPrimarySectorCorporateBondLongRescaled;
    @XmlElement(name = "GBSRP-FixedIncPrimarySectorCoveredBondLongRescaled", required = true)
    protected String gbsrpFixedIncPrimarySectorCoveredBondLongRescaled;
    @XmlElement(name = "GBSRP-FixedIncPrimarySectorForwardFutureLongRescaled", required = true)
    protected String gbsrpFixedIncPrimarySectorForwardFutureLongRescaled;
    @XmlElement(name = "GBSRP-FixedIncPrimarySectorGovernmentLongRescaled", required = true)
    protected String gbsrpFixedIncPrimarySectorGovernmentLongRescaled;
    @XmlElement(name = "GBSRP-FixedIncPrimarySectorGovernmentRelatedLongRescaled", required = true)
    protected String gbsrpFixedIncPrimarySectorGovernmentRelatedLongRescaled;
    @XmlElement(name = "GBSRP-FixedIncPrimarySectorMunicipalTaxExemptLongRescaled", required = true)
    protected String gbsrpFixedIncPrimarySectorMunicipalTaxExemptLongRescaled;
    @XmlElement(name = "GBSRP-FixedIncPrimarySectorMunicipalTaxableLongRescaled", required = true)
    protected String gbsrpFixedIncPrimarySectorMunicipalTaxableLongRescaled;
    @XmlElement(name = "GBSRP-FixedIncPrimarySectorNonAgencyResidentialMortgageBackedLongRescaled", required = true)
    protected String gbsrpFixedIncPrimarySectorNonAgencyResidentialMortgageBackedLongRescaled;
    @XmlElement(name = "GBSRP-FixedIncPrimarySectorOptionWarrantLongRescaled", required = true)
    protected String gbsrpFixedIncPrimarySectorOptionWarrantLongRescaled;
    @XmlElement(name = "GBSRP-FixedIncPrimarySectorPreferredStockLongRescaled", required = true)
    protected String gbsrpFixedIncPrimarySectorPreferredStockLongRescaled;
    @XmlElement(name = "GBSRP-FixedIncPrimarySectorSwapLongRescaled", required = true)
    protected String gbsrpFixedIncPrimarySectorSwapLongRescaled;
    @XmlElement(name = "GBSRP-FixedIncSuperSectorCashEquivalentsLongRescaled", required = true)
    protected String gbsrpFixedIncSuperSectorCashEquivalentsLongRescaled;
    @XmlElement(name = "GBSRP-FixedIncSuperSectorCorporateLongRescaled", required = true)
    protected String gbsrpFixedIncSuperSectorCorporateLongRescaled;
    @XmlElement(name = "GBSRP-FixedIncSuperSectorDerivativeLongRescaled", required = true)
    protected String gbsrpFixedIncSuperSectorDerivativeLongRescaled;
    @XmlElement(name = "GBSRP-FixedIncSuperSectorGovernmentLongRescaled", required = true)
    protected String gbsrpFixedIncSuperSectorGovernmentLongRescaled;
    @XmlElement(name = "GBSRP-FixedIncSuperSectorMunicipalLongRescaled", required = true)
    protected String gbsrpFixedIncSuperSectorMunicipalLongRescaled;
    @XmlElement(name = "GBSRP-FixedIncSuperSectorSecuritizedLongRescaled", required = true)
    protected String gbsrpFixedIncSuperSectorSecuritizedLongRescaled;
    @XmlElement(name = "GBSRP-PortfolioDate", required = true)
    protected String gbsrpPortfolioDate;

    @XmlElement(name = "PSRP-NumberOfBondHoldings", required = true)
    protected String psrpNumberOfBondHoldings;
    @XmlElement(name = "PSRP-NumberOfStockHoldings", required = true)
    protected String psrpNumberOfStockHoldings;
    @XmlElement(name = "PSRP-PortfolioDate", required = true)
    protected String psrpPortfolioDate;

    // quote Summary
    @XmlElement(name = "IC-InvestmentStrategy", required = true)
    protected String icInvestmentStrategy;
    @XmlElement(name = "IC-MultilingualInvestmentStrategies", required = true)
    protected ICMultilingualInvestmentStrategies icMultilingualInvestmentStrategies;
    @XmlElement(name = "TS-DayEndBidPrice", required = true)
    protected String tsDayEndBidPrice;
    @XmlElement(name = "TS-NAV52wkHigh", required = true)
    protected String tsnav52WkHigh;
    @XmlElement(name = "TS-NAV52wkLow", required = true)
    protected String tsnav52WkLow;
    @XmlElement(name = "TS-DayEndOfferPrice", required = true)
    protected String tsDayEndOfferPrice;
    @XmlElement(name = "DP-Dividend", required = true)
    protected String dpDividend;
    @XmlElement(name = "DP-DividendDate", required = true)
    protected String dpDividendDate;

    @XmlElement(name = "MR-Rating3Year", required = true)
    protected String mrRating3Year;
    @XmlElement(name = "MR-Rating5Year", required = true)
    protected String mrRating5Year;
    @XmlElement(name = "MR-Rating10Year", required = true)
    protected String mrRating10Year;
    @XmlElement(name = "MR-RatingDate", required = true)
    protected String mrRatingDate;
    @XmlElement(name = "MR-RatingOverall", required = true)
    protected String mrRatingOverall;

    @XmlElement(name = "TAR-Rating3Yr", required = true)
    protected String tarRating3Yr;
    @XmlElement(name = "TAR-Rating5Yr", required = true)
    protected String tarRating5Yr;
    @XmlElement(name = "TAR-Rating10Yr", required = true)
    protected String tarRating10Yr;
    @XmlElement(name = "TAR-RatingOverall", required = true)
    protected String tarRatingOverall;

    @XmlElement(name = "CQBRP-CreditQualA")
    protected String cqbrpCreditQualA;
    @XmlElement(name = "CQBRP-CreditQualAA")
    protected String cqbrpCreditQualAA;
    @XmlElement(name = "CQBRP-CreditQualAAA")
    protected String cqbrpCreditQualAAA;
    @XmlElement(name = "CQBRP-CreditQualB")
    protected String cqbrpCreditQualB;
    @XmlElement(name = "CQBRP-CreditQualBB")
    protected String cqbrpCreditQualBB;
    @XmlElement(name = "CQBRP-CreditQualBBB")
    protected String cqbrpCreditQualBBB;
    @XmlElement(name = "CQBRP-CreditQualBelowB")
    protected String cqbrpCreditQualBelowB;
    @XmlElement(name = "CQBRP-CreditQualNotRated")
    protected String cqbrpCreditQualNotRated;
    @XmlElement(name = "CQBRP-CreditQualDate")
    protected String cqbrpCreditQualDate;

    @XmlElement(name = "CYR-CategoryYear1", required = true)
    protected String cyrCategoryYear1;
    @XmlElement(name = "CYR-CategoryYear2", required = true)
    protected String cyrCategoryYear2;
    @XmlElement(name = "CYR-CategoryYear3", required = true)
    protected String cyrCategoryYear3;
    @XmlElement(name = "CYR-CategoryYear4", required = true)
    protected String cyrCategoryYear4;
    @XmlElement(name = "CYR-CategoryYear5", required = true)
    protected String cyrCategoryYear5;
    @XmlElement(name = "CYR-CategoryYear6", required = true)
    protected String cyrCategoryYear6;
    @XmlElement(name = "CYR-CategoryYear7", required = true)
    protected String cyrCategoryYear7;
    @XmlElement(name = "CYR-CategoryYear8", required = true)
    protected String cyrCategoryYear8;
    @XmlElement(name = "CYR-CategoryYear9", required = true)
    protected String cyrCategoryYear9;
    @XmlElement(name = "CYR-CategoryYear10", required = true)
    protected String cyrCategoryYear10;

    @XmlElement(name = "CYR-BestFitIndexYear1", required = true)
    protected String cyrBestFitIndexYear1;
    @XmlElement(name = "CYR-BestFitIndexYear2", required = true)
    protected String cyrBestFitIndexYear2;
    @XmlElement(name = "CYR-BestFitIndexYear3", required = true)
    protected String cyrBestFitIndexYear3;
    @XmlElement(name = "CYR-BestFitIndexYear4", required = true)
    protected String cyrBestFitIndexYear4;
    @XmlElement(name = "CYR-BestFitIndexYear5", required = true)
    protected String cyrBestFitIndexYear5;
    @XmlElement(name = "CYR-BestFitIndexYear6", required = true)
    protected String cyrBestFitIndexYear6;
    @XmlElement(name = "CYR-BestFitIndexYear7", required = true)
    protected String cyrBestFitIndexYear7;
    @XmlElement(name = "CYR-BestFitIndexYear8", required = true)
    protected String cyrBestFitIndexYear8;
    @XmlElement(name = "CYR-BestFitIndexYear9", required = true)
    protected String cyrBestFitIndexYear9;

    @XmlElement(name = "TTR-BestFitIndexReturnYTD", required = true)
    protected String ttrBestFitIndexReturnYTD;
    @XmlElement(name = "TTR-CategoryReturnYTD", required = true)
    protected String ttrCategoryReturnYTD;

    @XmlElement(name = "TTR-BestFitIndexReturn1Mth")
    protected String ttrBestFitIndexReturn1Mth;
    @XmlElement(name = "TTR-BestFitIndexReturn3Mth")
    protected String ttrBestFitIndexReturn3Mth;
    @XmlElement(name = "TTR-BestFitIndexReturn6Mth")
    protected String ttrBestFitIndexReturn6Mth;
    @XmlElement(name = "TTR-BestFitIndexReturn1Yr")
    protected String ttrBestFitIndexReturn1Yr;
    @XmlElement(name = "TTR-BestFitIndexReturn3Yr")
    protected String ttrBestFitIndexReturn3Yr;
    @XmlElement(name = "TTR-BestFitIndexReturn5Yr")
    protected String ttrBestFitIndexReturn5Yr;
    @XmlElement(name = "TTR-BestFitIndexReturn10Yr")
    protected String ttrBestFitIndexReturn10Yr;
    @XmlElement(name = "TTR-CategoryReturn1Mth")
    protected String ttrCategoryReturn1Mth;
    @XmlElement(name = "TTR-CategoryReturn3Mth")
    protected String ttrCategoryReturn3Mth;
    @XmlElement(name = "TTR-CategoryReturn6Mth")
    protected String ttrCategoryReturn6Mth;
    @XmlElement(name = "TTR-CategoryReturn1Yr")
    protected String ttrCategoryReturn1Yr;
    @XmlElement(name = "TTR-CategoryReturn3Yr")
    protected String ttrCategoryReturn3Yr;
    @XmlElement(name = "TTR-CategoryReturn5Yr")
    protected String ttrCategoryReturn5Yr;
    @XmlElement(name = "TTR-CategoryReturn10Yr")
    protected String ttrCategoryReturn10Yr;

    @XmlElement(name = "FM-Managers", required = true)
    protected FMManagers fmManagers;
    @XmlElement(name = "FM2-Managers", required = true)
    protected FM2Managers fm2Managers;

    @XmlElement(name = "MPTPI-IndexName")
    protected String mptpiIndexName;
    @XmlElement(name = "DP-DayEndDate")
    protected String dpDayEndDate;
    @XmlElement(name = "TTR-MonthEndDate")
    protected String ttrMonthEndDate;
    @XmlElement(name = "DP-CategoryReturn1Mth")
    protected String dpCategoryReturn1Mth;
    @XmlElement(name = "DP-CategoryReturn3Mth")
    protected String dpCategoryReturn3Mth;
    @XmlElement(name = "DP-CategoryReturn6Mth")
    protected String dpCategoryReturn6Mth;
    @XmlElement(name = "DP-CategoryReturn1Yr")
    protected String dpCategoryReturn1Yr;
    @XmlElement(name = "DP-CategoryReturn3Yr")
    protected String dpCategoryReturn3Yr;
    @XmlElement(name = "DP-CategoryReturn5Yr")
    protected String dpCategoryReturn5Yr;
    @XmlElement(name = "DP-CategoryReturn10Yr")
    protected String dpCategoryReturn10Yr;
    @XmlElement(name = "DP-CategoryReturnYTD")
    protected String dpCategoryReturnYTD;
    @XmlElement(name = "SR-StubYearEndReturnEndDate")
    protected String srStubYearEndReturnEndDate;
    @XmlElement(name = "SR-StubYearEndReturn")
    protected String srStubYearEndReturn;
    @XmlElement(name = "FB-PrimaryProspectusBenchmarks", required = true)
    protected FBPrimaryProspectusBenchmarks fbPrimaryProspectusBenchmarks;
    @XmlElement(name = "TTR-PrimaryIndexReturn1Mth")
    protected String ttrPrimaryIndexReturn1Mth;
    @XmlElement(name = "TTR-PrimaryIndexReturn3Mth")
    protected String ttrPrimaryIndexReturn3Mth;
    @XmlElement(name = "TTR-PrimaryIndexReturn6Mth")
    protected String ttrPrimaryIndexReturn6Mth;
    @XmlElement(name = "TTR-PrimaryIndexReturn1Yr")
    protected String ttrPrimaryIndexReturn1Yr;
    @XmlElement(name = "TTR-PrimaryIndexReturn3Yr")
    protected String ttrPrimaryIndexReturn3Yr;
    @XmlElement(name = "TTR-PrimaryIndexReturn5Yr")
    protected String ttrPrimaryIndexReturn5Yr;
    @XmlElement(name = "TTR-PrimaryIndexReturn10Yr")
    protected String ttrPrimaryIndexReturn10Yr;
    @XmlElement(name = "TTR-PrimaryIndexReturnYTD")
    protected String ttrPrimaryIndexReturnYTD;
    @XmlElement(name = "TS-DayEndBidOfferPricesDate")
    protected String tsDayEndBidOfferPricesDate;
    @XmlElement(name = "TS-DayEndNAVDate")
    protected String tsDayEndNAVDate;
    @XmlElement(name = "CYR-PrimaryIndexYear1")
    protected String cyrPrimaryIndexYear1;
    @XmlElement(name = "CYR-PrimaryIndexYear10")
    protected String cyrPrimaryIndexYear10;
    @XmlElement(name = "CYR-PrimaryIndexYear2")
    protected String cyrPrimaryIndexYear2;
    @XmlElement(name = "CYR-PrimaryIndexYear3")
    protected String cyrPrimaryIndexYear3;
    @XmlElement(name = "CYR-PrimaryIndexYear4")
    protected String cyrPrimaryIndexYear4;
    @XmlElement(name = "CYR-PrimaryIndexYear5")
    protected String cyrPrimaryIndexYear5;
    @XmlElement(name = "CYR-PrimaryIndexYear6")
    protected String cyrPrimaryIndexYear6;
    @XmlElement(name = "CYR-PrimaryIndexYear7")
    protected String cyrPrimaryIndexYear7;
    @XmlElement(name = "CYR-PrimaryIndexYear8")
    protected String cyrPrimaryIndexYear8;
    @XmlElement(name = "CYR-PrimaryIndexYear9")
    protected String cyrPrimaryIndexYear9;
    @XmlElement(name = "MPTPI-IndexID")
    protected String mptpiIndexId;

    @XmlAttribute(name = "_id")
    protected String id;


    public String getAabrpAssetAllocBondLong() {
        return this.aabrpAssetAllocBondLong;
    }


    public void setAabrpAssetAllocBondLong(final String aabrpAssetAllocBondLong) {
        this.aabrpAssetAllocBondLong = aabrpAssetAllocBondLong;
    }


    public String getAabrpAssetAllocBondNet() {
        return this.aabrpAssetAllocBondNet;
    }


    public void setAabrpAssetAllocBondNet(final String aabrpAssetAllocBondNet) {
        this.aabrpAssetAllocBondNet = aabrpAssetAllocBondNet;
    }


    public String getAabrpAssetAllocBondShort() {
        return this.aabrpAssetAllocBondShort;
    }


    public void setAabrpAssetAllocBondShort(final String aabrpAssetAllocBondShort) {
        this.aabrpAssetAllocBondShort = aabrpAssetAllocBondShort;
    }


    public String getAabrpAssetAllocCashLong() {
        return this.aabrpAssetAllocCashLong;
    }


    public void setAabrpAssetAllocCashLong(final String aabrpAssetAllocCashLong) {
        this.aabrpAssetAllocCashLong = aabrpAssetAllocCashLong;
    }


    public String getAabrpAssetAllocCashNet() {
        return this.aabrpAssetAllocCashNet;
    }


    public void setAabrpAssetAllocCashNet(final String aabrpAssetAllocCashNet) {
        this.aabrpAssetAllocCashNet = aabrpAssetAllocCashNet;
    }


    public String getAabrpAssetAllocCashShort() {
        return this.aabrpAssetAllocCashShort;
    }


    public void setAabrpAssetAllocCashShort(final String aabrpAssetAllocCashShort) {
        this.aabrpAssetAllocCashShort = aabrpAssetAllocCashShort;
    }


    public String getAabrpAssetAllocConvBondLong() {
        return this.aabrpAssetAllocConvBondLong;
    }


    public void setAabrpAssetAllocConvBondLong(final String aabrpAssetAllocConvBondLong) {
        this.aabrpAssetAllocConvBondLong = aabrpAssetAllocConvBondLong;
    }


    public String getAabrpAssetAllocEquityLong() {
        return this.aabrpAssetAllocEquityLong;
    }


    public void setAabrpAssetAllocEquityLong(final String aabrpAssetAllocEquityLong) {
        this.aabrpAssetAllocEquityLong = aabrpAssetAllocEquityLong;
    }


    public String getAabrpAssetAllocEquityNet() {
        return this.aabrpAssetAllocEquityNet;
    }


    public void setAabrpAssetAllocEquityNet(final String aabrpAssetAllocEquityNet) {
        this.aabrpAssetAllocEquityNet = aabrpAssetAllocEquityNet;
    }


    public String getAabrpAssetAllocEquityShort() {
        return this.aabrpAssetAllocEquityShort;
    }


    public void setAabrpAssetAllocEquityShort(final String aabrpAssetAllocEquityShort) {
        this.aabrpAssetAllocEquityShort = aabrpAssetAllocEquityShort;
    }


    public String getAabrpConvertibleShort() {
        return this.aabrpConvertibleShort;
    }


    public void setAabrpConvertibleShort(final String aabrpConvertibleShort) {
        this.aabrpConvertibleShort = aabrpConvertibleShort;
    }


    public String getAabrpOtherLong() {
        return this.aabrpOtherLong;
    }


    public void setAabrpOtherLong(final String aabrpOtherLong) {
        this.aabrpOtherLong = aabrpOtherLong;
    }


    public String getAabrpOtherNet() {
        return this.aabrpOtherNet;
    }


    public void setAabrpOtherNet(final String aabrpOtherNet) {
        this.aabrpOtherNet = aabrpOtherNet;
    }


    public String getAabrpOtherShort() {
        return this.aabrpOtherShort;
    }


    public void setAabrpOtherShort(final String aabrpOtherShort) {
        this.aabrpOtherShort = aabrpOtherShort;
    }


    public String getAabrpPortfolioDate() {
        return this.aabrpPortfolioDate;
    }


    public void setAabrpPortfolioDate(final String aabrpPortfolioDate) {
        this.aabrpPortfolioDate = aabrpPortfolioDate;
    }


    public String getAabrpPreferredStockLong() {
        return this.aabrpPreferredStockLong;
    }


    public void setAabrpPreferredStockLong(final String aabrpPreferredStockLong) {
        this.aabrpPreferredStockLong = aabrpPreferredStockLong;
    }


    public String getAabrpPreferredStockNet() {
        return this.aabrpPreferredStockNet;
    }


    public void setAabrpPreferredStockNet(final String aabrpPreferredStockNet) {
        this.aabrpPreferredStockNet = aabrpPreferredStockNet;
    }


    public String getAabrpPreferredStockShort() {
        return this.aabrpPreferredStockShort;
    }


    public void setAabrpPreferredStockShort(final String aabrpPreferredStockShort) {
        this.aabrpPreferredStockShort = aabrpPreferredStockShort;
    }


    public String getRebrpPortfolioDate() {
        return this.rebrpPortfolioDate;
    }


    public void setRebrpPortfolioDate(final String rebrpPortfolioDate) {
        this.rebrpPortfolioDate = rebrpPortfolioDate;
    }



    public String getRebrpEquityRegionAfrica() {
        return this.rebrpEquityRegionAfrica;
    }


    public void setRebrpEquityRegionAfrica(final String rebrpEquityRegionAfrica) {
        this.rebrpEquityRegionAfrica = rebrpEquityRegionAfrica;
    }


    public String getRebrpEquityRegionAsiadevLongRescaled() {
        return this.rebrpEquityRegionAsiadevLongRescaled;
    }


    public void setRebrpEquityRegionAsiadevLongRescaled(final String rebrpEquityRegionAsiadevLongRescaled) {
        this.rebrpEquityRegionAsiadevLongRescaled = rebrpEquityRegionAsiadevLongRescaled;
    }


    public String getRebrpEquityRegionAsiaemrgLongRescaled() {
        return this.rebrpEquityRegionAsiaemrgLongRescaled;
    }


    public void setRebrpEquityRegionAsiaemrgLongRescaled(final String rebrpEquityRegionAsiaemrgLongRescaled) {
        this.rebrpEquityRegionAsiaemrgLongRescaled = rebrpEquityRegionAsiaemrgLongRescaled;
    }


    public String getRebrpEquityRegionAustralasiaLongRescaled() {
        return this.rebrpEquityRegionAustralasiaLongRescaled;
    }


    public void setRebrpEquityRegionAustralasiaLongRescaled(final String rebrpEquityRegionAustralasiaLongRescaled) {
        this.rebrpEquityRegionAustralasiaLongRescaled = rebrpEquityRegionAustralasiaLongRescaled;
    }


    public String getRebrpEquityRegionCanada() {
        return this.rebrpEquityRegionCanada;
    }


    public void setRebrpEquityRegionCanada(final String rebrpEquityRegionCanada) {
        this.rebrpEquityRegionCanada = rebrpEquityRegionCanada;
    }


    public String getRebrpEquityRegionDevelopedLongRescaled() {
        return this.rebrpEquityRegionDevelopedLongRescaled;
    }


    public void setRebrpEquityRegionDevelopedLongRescaled(final String rebrpEquityRegionDevelopedLongRescaled) {
        this.rebrpEquityRegionDevelopedLongRescaled = rebrpEquityRegionDevelopedLongRescaled;
    }


    public String getRebrpEquityRegionEmergingLongRescaled() {
        return this.rebrpEquityRegionEmergingLongRescaled;
    }


    public void setRebrpEquityRegionEmergingLongRescaled(final String rebrpEquityRegionEmergingLongRescaled) {
        this.rebrpEquityRegionEmergingLongRescaled = rebrpEquityRegionEmergingLongRescaled;
    }


    public String getRebrpEquityRegionEuropeemrgLongRescaled() {
        return this.rebrpEquityRegionEuropeemrgLongRescaled;
    }


    public void setRebrpEquityRegionEuropeemrgLongRescaled(final String rebrpEquityRegionEuropeemrgLongRescaled) {
        this.rebrpEquityRegionEuropeemrgLongRescaled = rebrpEquityRegionEuropeemrgLongRescaled;
    }


    public String getRebrpEquityRegionEuropeexeuro() {
        return this.rebrpEquityRegionEuropeexeuro;
    }


    public void setRebrpEquityRegionEuropeexeuro(final String rebrpEquityRegionEuropeexeuro) {
        this.rebrpEquityRegionEuropeexeuro = rebrpEquityRegionEuropeexeuro;
    }


    public String getRebrpEquityRegionEurozone() {
        return this.rebrpEquityRegionEurozone;
    }


    public void setRebrpEquityRegionEurozone(final String rebrpEquityRegionEurozone) {
        this.rebrpEquityRegionEurozone = rebrpEquityRegionEurozone;
    }


    public String getRebrpEquityRegionJapanLongRescaled() {
        return this.rebrpEquityRegionJapanLongRescaled;
    }


    public void setRebrpEquityRegionJapanLongRescaled(final String rebrpEquityRegionJapanLongRescaled) {
        this.rebrpEquityRegionJapanLongRescaled = rebrpEquityRegionJapanLongRescaled;
    }


    public String getRebrpEquityRegionLatinAmericaLongRescaled() {
        return this.rebrpEquityRegionLatinAmericaLongRescaled;
    }


    public void setRebrpEquityRegionLatinAmericaLongRescaled(final String rebrpEquityRegionLatinAmericaLongRescaled) {
        this.rebrpEquityRegionLatinAmericaLongRescaled = rebrpEquityRegionLatinAmericaLongRescaled;
    }


    public String getRebrpEquityRegionMiddleEast() {
        return this.rebrpEquityRegionMiddleEast;
    }


    public void setRebrpEquityRegionMiddleEast(final String rebrpEquityRegionMiddleEast) {
        this.rebrpEquityRegionMiddleEast = rebrpEquityRegionMiddleEast;
    }


    public String getRebrpEquityRegionNotClassifiedLongRescaled() {
        return this.rebrpEquityRegionNotClassifiedLongRescaled;
    }


    public void setRebrpEquityRegionNotClassifiedLongRescaled(final String rebrpEquityRegionNotClassifiedLongRescaled) {
        this.rebrpEquityRegionNotClassifiedLongRescaled = rebrpEquityRegionNotClassifiedLongRescaled;
    }


    public String getRebrpEquityRegionUnitedKingdomLongRescaled() {
        return this.rebrpEquityRegionUnitedKingdomLongRescaled;
    }


    public void setRebrpEquityRegionUnitedKingdomLongRescaled(final String rebrpEquityRegionUnitedKingdomLongRescaled) {
        this.rebrpEquityRegionUnitedKingdomLongRescaled = rebrpEquityRegionUnitedKingdomLongRescaled;
    }


    public String getRebrpEquityRegionUnitedStates() {
        return this.rebrpEquityRegionUnitedStates;
    }


    public void setRebrpEquityRegionUnitedStates(final String rebrpEquityRegionUnitedStates) {
        this.rebrpEquityRegionUnitedStates = rebrpEquityRegionUnitedStates;
    }


    public String getGssbrpEquitySectorBasicMaterialsLongRescaled() {
        return this.gssbrpEquitySectorBasicMaterialsLongRescaled;
    }


    public void setGssbrpEquitySectorBasicMaterialsLongRescaled(final String gssbrpEquitySectorBasicMaterialsLongRescaled) {
        this.gssbrpEquitySectorBasicMaterialsLongRescaled = gssbrpEquitySectorBasicMaterialsLongRescaled;
    }


    public String getGssbrpEquitySectorCommunicationServicesLongRescaled() {
        return this.gssbrpEquitySectorCommunicationServicesLongRescaled;
    }


    public void setGssbrpEquitySectorCommunicationServicesLongRescaled(
        final String gssbrpEquitySectorCommunicationServicesLongRescaled) {
        this.gssbrpEquitySectorCommunicationServicesLongRescaled = gssbrpEquitySectorCommunicationServicesLongRescaled;
    }


    public String getGssbrpEquitySectorConsumerCyclicalLongRescaled() {
        return this.gssbrpEquitySectorConsumerCyclicalLongRescaled;
    }


    public void setGssbrpEquitySectorConsumerCyclicalLongRescaled(final String gssbrpEquitySectorConsumerCyclicalLongRescaled) {
        this.gssbrpEquitySectorConsumerCyclicalLongRescaled = gssbrpEquitySectorConsumerCyclicalLongRescaled;
    }


    public String getGssbrpEquitySectorConsumerDefensiveLongRescaled() {
        return this.gssbrpEquitySectorConsumerDefensiveLongRescaled;
    }


    public void setGssbrpEquitySectorConsumerDefensiveLongRescaled(final String gssbrpEquitySectorConsumerDefensiveLongRescaled) {
        this.gssbrpEquitySectorConsumerDefensiveLongRescaled = gssbrpEquitySectorConsumerDefensiveLongRescaled;
    }


    public String getGssbrpEquitySectorEnergyLongRescaled() {
        return this.gssbrpEquitySectorEnergyLongRescaled;
    }


    public void setGssbrpEquitySectorEnergyLongRescaled(final String gssbrpEquitySectorEnergyLongRescaled) {
        this.gssbrpEquitySectorEnergyLongRescaled = gssbrpEquitySectorEnergyLongRescaled;
    }


    public String getGssbrpEquitySectorFinancialServicesLongRescaled() {
        return this.gssbrpEquitySectorFinancialServicesLongRescaled;
    }


    public void setGssbrpEquitySectorFinancialServicesLongRescaled(final String gssbrpEquitySectorFinancialServicesLongRescaled) {
        this.gssbrpEquitySectorFinancialServicesLongRescaled = gssbrpEquitySectorFinancialServicesLongRescaled;
    }


    public String getGssbrpEquitySectorHealthcareLongRescaled() {
        return this.gssbrpEquitySectorHealthcareLongRescaled;
    }


    public void setGssbrpEquitySectorHealthcareLongRescaled(final String gssbrpEquitySectorHealthcareLongRescaled) {
        this.gssbrpEquitySectorHealthcareLongRescaled = gssbrpEquitySectorHealthcareLongRescaled;
    }


    public String getGssbrpEquitySectorIndustrialsLongRescaled() {
        return this.gssbrpEquitySectorIndustrialsLongRescaled;
    }


    public void setGssbrpEquitySectorIndustrialsLongRescaled(final String gssbrpEquitySectorIndustrialsLongRescaled) {
        this.gssbrpEquitySectorIndustrialsLongRescaled = gssbrpEquitySectorIndustrialsLongRescaled;
    }


    public String getGssbrpEquitySectorRealEstateLongRescaled() {
        return this.gssbrpEquitySectorRealEstateLongRescaled;
    }


    public void setGssbrpEquitySectorRealEstateLongRescaled(final String gssbrpEquitySectorRealEstateLongRescaled) {
        this.gssbrpEquitySectorRealEstateLongRescaled = gssbrpEquitySectorRealEstateLongRescaled;
    }


    public String getGssbrpEquitySectorTechnologyLongRescaled() {
        return this.gssbrpEquitySectorTechnologyLongRescaled;
    }


    public void setGssbrpEquitySectorTechnologyLongRescaled(final String gssbrpEquitySectorTechnologyLongRescaled) {
        this.gssbrpEquitySectorTechnologyLongRescaled = gssbrpEquitySectorTechnologyLongRescaled;
    }


    public String getGssbrpEquitySectorUtilitiesLongRescaled() {
        return this.gssbrpEquitySectorUtilitiesLongRescaled;
    }


    public void setGssbrpEquitySectorUtilitiesLongRescaled(final String gssbrpEquitySectorUtilitiesLongRescaled) {
        this.gssbrpEquitySectorUtilitiesLongRescaled = gssbrpEquitySectorUtilitiesLongRescaled;
    }


    public String getGssbrpGlobalSectorPortfolioDate() {
        return this.gssbrpGlobalSectorPortfolioDate;
    }


    public void setGssbrpGlobalSectorPortfolioDate(final String gssbrpGlobalSectorPortfolioDate) {
        this.gssbrpGlobalSectorPortfolioDate = gssbrpGlobalSectorPortfolioDate;
    }


    public String getIcInvestmentStrategy() {
        return this.icInvestmentStrategy;
    }


    public void setIcInvestmentStrategy(final String icInvestmentStrategy) {
        this.icInvestmentStrategy = icInvestmentStrategy;
    }


    public ICMultilingualInvestmentStrategies getIcMultilingualInvestmentStrategies() {
        return this.icMultilingualInvestmentStrategies;
    }


    public void setIcMultilingualInvestmentStrategies(final ICMultilingualInvestmentStrategies icMultilingualInvestmentStrategies) {
        this.icMultilingualInvestmentStrategies = icMultilingualInvestmentStrategies;
    }


    public String getTsDayEndBidPrice() {
        return this.tsDayEndBidPrice;
    }


    public void setTsDayEndBidPrice(final String tsDayEndBidPrice) {
        this.tsDayEndBidPrice = tsDayEndBidPrice;
    }


    public String getTsnav52WkLow() {
        return this.tsnav52WkLow;
    }


    public void setTsnav52WkLow(final String tsnav52WkLow) {
        this.tsnav52WkLow = tsnav52WkLow;
    }


    public String getTsnav52WkHigh() {
        return this.tsnav52WkHigh;
    }


    public void setTsnav52WkHigh(final String tsnav52WkHigh) {
        this.tsnav52WkHigh = tsnav52WkHigh;
    }


    public String getTsDayEndOfferPrice() {
        return this.tsDayEndOfferPrice;
    }


    public void setTsDayEndOfferPrice(final String tsDayEndOfferPrice) {
        this.tsDayEndOfferPrice = tsDayEndOfferPrice;
    }


    public String getDpDividend() {
        return this.dpDividend;
    }


    public void setDpDividend(final String dpDividend) {
        this.dpDividend = dpDividend;
    }


    public String getDpDividendDate() {
        return this.dpDividendDate;
    }


    public void setDpDividendDate(final String dpDividendDate) {
        this.dpDividendDate = dpDividendDate;
    }


    public String getMrRating3Year() {
        return this.mrRating3Year;
    }


    public void setMrRating3Year(final String mrRating3Year) {
        this.mrRating3Year = mrRating3Year;
    }


    public String getMrRating5Year() {
        return this.mrRating5Year;
    }


    public void setMrRating5Year(final String mrRating5Year) {
        this.mrRating5Year = mrRating5Year;
    }


    public String getMrRating10Year() {
        return this.mrRating10Year;
    }


    public void setMrRating10Year(final String mrRating10Year) {
        this.mrRating10Year = mrRating10Year;
    }


    public String getTarRating3Yr() {
        return this.tarRating3Yr;
    }


    public void setTarRating3Yr(final String tarRating3Yr) {
        this.tarRating3Yr = tarRating3Yr;
    }


    public String getTarRating5Yr() {
        return this.tarRating5Yr;
    }


    public void setTarRating5Yr(final String tarRating5Yr) {
        this.tarRating5Yr = tarRating5Yr;
    }


    public String getTarRating10Yr() {
        return this.tarRating10Yr;
    }


    public void setTarRating10Yr(final String tarRating10Yr) {
        this.tarRating10Yr = tarRating10Yr;
    }


    public String getTarRatingOverall() {
        return this.tarRatingOverall;
    }


    public void setTarRatingOverall(final String tarRatingOverall) {
        this.tarRatingOverall = tarRatingOverall;
    }


    public String getMrRatingDate() {
        return this.mrRatingDate;
    }


    public void setMrRatingDate(final String mrRatingDate) {
        this.mrRatingDate = mrRatingDate;
    }


    public String getMrRatingOverall() {
        return this.mrRatingOverall;
    }


    public void setMrRatingOverall(final String mrRatingOverall) {
        this.mrRatingOverall = mrRatingOverall;
    }


    public String getCqbrpCreditQualA() {
        return this.cqbrpCreditQualA;
    }


    public void setCqbrpCreditQualA(final String cqbrpCreditQualA) {
        this.cqbrpCreditQualA = cqbrpCreditQualA;
    }


    public String getCqbrpCreditQualAA() {
        return this.cqbrpCreditQualAA;
    }


    public void setCqbrpCreditQualAA(final String cqbrpCreditQualAA) {
        this.cqbrpCreditQualAA = cqbrpCreditQualAA;
    }


    public String getCqbrpCreditQualAAA() {
        return this.cqbrpCreditQualAAA;
    }


    public void setCqbrpCreditQualAAA(final String cqbrpCreditQualAAA) {
        this.cqbrpCreditQualAAA = cqbrpCreditQualAAA;
    }


    public String getCqbrpCreditQualB() {
        return this.cqbrpCreditQualB;
    }


    public void setCqbrpCreditQualB(final String cqbrpCreditQualB) {
        this.cqbrpCreditQualB = cqbrpCreditQualB;
    }


    public String getCqbrpCreditQualBB() {
        return this.cqbrpCreditQualBB;
    }


    public void setCqbrpCreditQualBB(final String cqbrpCreditQualBB) {
        this.cqbrpCreditQualBB = cqbrpCreditQualBB;
    }


    public String getCqbrpCreditQualBBB() {
        return this.cqbrpCreditQualBBB;
    }


    public void setCqbrpCreditQualBBB(final String cqbrpCreditQualBBB) {
        this.cqbrpCreditQualBBB = cqbrpCreditQualBBB;
    }


    public String getCqbrpCreditQualBelowB() {
        return this.cqbrpCreditQualBelowB;
    }


    public void setCqbrpCreditQualBelowB(final String cqbrpCreditQualBelowB) {
        this.cqbrpCreditQualBelowB = cqbrpCreditQualBelowB;
    }


    public String getCqbrpCreditQualNotRated() {
        return this.cqbrpCreditQualNotRated;
    }


    public void setCqbrpCreditQualNotRated(final String cqbrpCreditQualNotRated) {
        this.cqbrpCreditQualNotRated = cqbrpCreditQualNotRated;
    }


    public String getCqbrpCreditQualDate() {
        return this.cqbrpCreditQualDate;
    }


    public void setCqbrpCreditQualDate(final String cqbrpCreditQualDate) {
        this.cqbrpCreditQualDate = cqbrpCreditQualDate;
    }


    public String getCyrCategoryYear1() {
        return this.cyrCategoryYear1;
    }


    public void setCyrCategoryYear1(final String cyrCategoryYear1) {
        this.cyrCategoryYear1 = cyrCategoryYear1;
    }


    public String getCyrCategoryYear2() {
        return this.cyrCategoryYear2;
    }


    public void setCyrCategoryYear2(final String cyrCategoryYear2) {
        this.cyrCategoryYear2 = cyrCategoryYear2;
    }


    public String getCyrCategoryYear3() {
        return this.cyrCategoryYear3;
    }


    public void setCyrCategoryYear3(final String cyrCategoryYear3) {
        this.cyrCategoryYear3 = cyrCategoryYear3;
    }


    public String getCyrCategoryYear4() {
        return this.cyrCategoryYear4;
    }


    public void setCyrCategoryYear4(final String cyrCategoryYear4) {
        this.cyrCategoryYear4 = cyrCategoryYear4;
    }


    public String getCyrCategoryYear5() {
        return this.cyrCategoryYear5;
    }


    public void setCyrCategoryYear5(final String cyrCategoryYear5) {
        this.cyrCategoryYear5 = cyrCategoryYear5;
    }


    public String getCyrCategoryYear6() {
        return this.cyrCategoryYear6;
    }


    public void setCyrCategoryYear6(final String cyrCategoryYear6) {
        this.cyrCategoryYear6 = cyrCategoryYear6;
    }


    public String getCyrCategoryYear7() {
        return this.cyrCategoryYear7;
    }


    public void setCyrCategoryYear7(final String cyrCategoryYear7) {
        this.cyrCategoryYear7 = cyrCategoryYear7;
    }


    public String getCyrCategoryYear8() {
        return this.cyrCategoryYear8;
    }


    public void setCyrCategoryYear8(final String cyrCategoryYear8) {
        this.cyrCategoryYear8 = cyrCategoryYear8;
    }


    public String getCyrCategoryYear9() {
        return this.cyrCategoryYear9;
    }


    public void setCyrCategoryYear9(final String cyrCategoryYear9) {
        this.cyrCategoryYear9 = cyrCategoryYear9;
    }


    public String getCyrCategoryYear10() {
        return this.cyrCategoryYear10;
    }


    public void setCyrCategoryYear10(final String cyrCategoryYear10) {
        this.cyrCategoryYear10 = cyrCategoryYear10;
    }


    public String getCyrBestFitIndexYear1() {
        return this.cyrBestFitIndexYear1;
    }


    public void setCyrBestFitIndexYear1(final String cyrBestFitIndexYear1) {
        this.cyrBestFitIndexYear1 = cyrBestFitIndexYear1;
    }


    public String getCyrBestFitIndexYear2() {
        return this.cyrBestFitIndexYear2;
    }


    public void setCyrBestFitIndexYear2(final String cyrBestFitIndexYear2) {
        this.cyrBestFitIndexYear2 = cyrBestFitIndexYear2;
    }


    public String getCyrBestFitIndexYear3() {
        return this.cyrBestFitIndexYear3;
    }


    public void setCyrBestFitIndexYear3(final String cyrBestFitIndexYear3) {
        this.cyrBestFitIndexYear3 = cyrBestFitIndexYear3;
    }


    public String getCyrBestFitIndexYear4() {
        return this.cyrBestFitIndexYear4;
    }


    public void setCyrBestFitIndexYear4(final String cyrBestFitIndexYear4) {
        this.cyrBestFitIndexYear4 = cyrBestFitIndexYear4;
    }


    public String getCyrBestFitIndexYear5() {
        return this.cyrBestFitIndexYear5;
    }


    public void setCyrBestFitIndexYear5(final String cyrBestFitIndexYear5) {
        this.cyrBestFitIndexYear5 = cyrBestFitIndexYear5;
    }


    public String getCyrBestFitIndexYear6() {
        return this.cyrBestFitIndexYear6;
    }


    public void setCyrBestFitIndexYear6(final String cyrBestFitIndexYear6) {
        this.cyrBestFitIndexYear6 = cyrBestFitIndexYear6;
    }


    public String getCyrBestFitIndexYear7() {
        return this.cyrBestFitIndexYear7;
    }


    public void setCyrBestFitIndexYear7(final String cyrBestFitIndexYear7) {
        this.cyrBestFitIndexYear7 = cyrBestFitIndexYear7;
    }


    public String getCyrBestFitIndexYear8() {
        return this.cyrBestFitIndexYear8;
    }


    public void setCyrBestFitIndexYear8(final String cyrBestFitIndexYear8) {
        this.cyrBestFitIndexYear8 = cyrBestFitIndexYear8;
    }


    public FMManagers getFmManagers() {
        return this.fmManagers;
    }


    public void setFmManagers(final FMManagers fmManagers) {
        this.fmManagers = fmManagers;
    }


    public FM2Managers getFM2Managers() {
        return this.fm2Managers;
    }


    public void setFM2Managers(final FM2Managers fm2Managers) {
        this.fm2Managers = fm2Managers;
    }


    public String getCyrBestFitIndexYear9() {
        return this.cyrBestFitIndexYear9;
    }


    public void setCyrBestFitIndexYear9(final String cyrBestFitIndexYear9) {
        this.cyrBestFitIndexYear9 = cyrBestFitIndexYear9;
    }


    public String getTtrBestFitIndexReturnYTD() {
        return this.ttrBestFitIndexReturnYTD;
    }


    public void setTtrBestFitIndexReturnYTD(final String ttrBestFitIndexReturnYTD) {
        this.ttrBestFitIndexReturnYTD = ttrBestFitIndexReturnYTD;
    }


    public String getTtrCategoryReturnYTD() {
        return this.ttrCategoryReturnYTD;
    }


    public void setTtrCategoryReturnYTD(final String ttrCategoryReturnYTD) {
        this.ttrCategoryReturnYTD = ttrCategoryReturnYTD;
    }


    public String getGbsrpFixedIncPrimarySectorAgencyMortgageBackedLongRescaled() {
        return this.gbsrpFixedIncPrimarySectorAgencyMortgageBackedLongRescaled;
    }


    public void setGbsrpFixedIncPrimarySectorAgencyMortgageBackedLongRescaled(
        final String gbsrpFixedIncPrimarySectorAgencyMortgageBackedLongRescaled) {
        this.gbsrpFixedIncPrimarySectorAgencyMortgageBackedLongRescaled = gbsrpFixedIncPrimarySectorAgencyMortgageBackedLongRescaled;
    }


    public String getGbsrpFixedIncPrimarySectorAssetBackedLongRescaled() {
        return this.gbsrpFixedIncPrimarySectorAssetBackedLongRescaled;
    }


    public void setGbsrpFixedIncPrimarySectorAssetBackedLongRescaled(final String gbsrpFixedIncPrimarySectorAssetBackedLongRescaled) {
        this.gbsrpFixedIncPrimarySectorAssetBackedLongRescaled = gbsrpFixedIncPrimarySectorAssetBackedLongRescaled;
    }


    public String getGbsrpFixedIncPrimarySectorBankLoanLongRescaled() {
        return this.gbsrpFixedIncPrimarySectorBankLoanLongRescaled;
    }


    public void setGbsrpFixedIncPrimarySectorBankLoanLongRescaled(final String gbsrpFixedIncPrimarySectorBankLoanLongRescaled) {
        this.gbsrpFixedIncPrimarySectorBankLoanLongRescaled = gbsrpFixedIncPrimarySectorBankLoanLongRescaled;
    }


    public String getGbsrpFixedIncPrimarySectorCashEquivalentsLongRescaled() {
        return this.gbsrpFixedIncPrimarySectorCashEquivalentsLongRescaled;
    }


    public void setGbsrpFixedIncPrimarySectorCashEquivalentsLongRescaled(
        final String gbsrpFixedIncPrimarySectorCashEquivalentsLongRescaled) {
        this.gbsrpFixedIncPrimarySectorCashEquivalentsLongRescaled = gbsrpFixedIncPrimarySectorCashEquivalentsLongRescaled;
    }


    public String getGbsrpFixedIncPrimarySectorCommercialMortgageBackedLongRescaled() {
        return this.gbsrpFixedIncPrimarySectorCommercialMortgageBackedLongRescaled;
    }


    public void setGbsrpFixedIncPrimarySectorCommercialMortgageBackedLongRescaled(
        final String gbsrpFixedIncPrimarySectorCommercialMortgageBackedLongRescaled) {
        this.gbsrpFixedIncPrimarySectorCommercialMortgageBackedLongRescaled = gbsrpFixedIncPrimarySectorCommercialMortgageBackedLongRescaled;
    }


    public String getGbsrpFixedIncPrimarySectorConvertibleLongRescaled() {
        return this.gbsrpFixedIncPrimarySectorConvertibleLongRescaled;
    }


    public void setGbsrpFixedIncPrimarySectorConvertibleLongRescaled(final String gbsrpFixedIncPrimarySectorConvertibleLongRescaled) {
        this.gbsrpFixedIncPrimarySectorConvertibleLongRescaled = gbsrpFixedIncPrimarySectorConvertibleLongRescaled;
    }


    public String getGbsrpFixedIncPrimarySectorCorporateBondLongRescaled() {
        return this.gbsrpFixedIncPrimarySectorCorporateBondLongRescaled;
    }


    public void setGbsrpFixedIncPrimarySectorCorporateBondLongRescaled(
        final String gbsrpFixedIncPrimarySectorCorporateBondLongRescaled) {
        this.gbsrpFixedIncPrimarySectorCorporateBondLongRescaled = gbsrpFixedIncPrimarySectorCorporateBondLongRescaled;
    }


    public String getGbsrpFixedIncPrimarySectorCoveredBondLongRescaled() {
        return this.gbsrpFixedIncPrimarySectorCoveredBondLongRescaled;
    }


    public void setGbsrpFixedIncPrimarySectorCoveredBondLongRescaled(final String gbsrpFixedIncPrimarySectorCoveredBondLongRescaled) {
        this.gbsrpFixedIncPrimarySectorCoveredBondLongRescaled = gbsrpFixedIncPrimarySectorCoveredBondLongRescaled;
    }


    public String getGbsrpFixedIncPrimarySectorForwardFutureLongRescaled() {
        return this.gbsrpFixedIncPrimarySectorForwardFutureLongRescaled;
    }


    public void setGbsrpFixedIncPrimarySectorForwardFutureLongRescaled(
        final String gbsrpFixedIncPrimarySectorForwardFutureLongRescaled) {
        this.gbsrpFixedIncPrimarySectorForwardFutureLongRescaled = gbsrpFixedIncPrimarySectorForwardFutureLongRescaled;
    }


    public String getGbsrpFixedIncPrimarySectorGovernmentLongRescaled() {
        return this.gbsrpFixedIncPrimarySectorGovernmentLongRescaled;
    }


    public void setGbsrpFixedIncPrimarySectorGovernmentLongRescaled(final String gbsrpFixedIncPrimarySectorGovernmentLongRescaled) {
        this.gbsrpFixedIncPrimarySectorGovernmentLongRescaled = gbsrpFixedIncPrimarySectorGovernmentLongRescaled;
    }


    public String getGbsrpFixedIncPrimarySectorGovernmentRelatedLongRescaled() {
        return this.gbsrpFixedIncPrimarySectorGovernmentRelatedLongRescaled;
    }


    public void setGbsrpFixedIncPrimarySectorGovernmentRelatedLongRescaled(
        final String gbsrpFixedIncPrimarySectorGovernmentRelatedLongRescaled) {
        this.gbsrpFixedIncPrimarySectorGovernmentRelatedLongRescaled = gbsrpFixedIncPrimarySectorGovernmentRelatedLongRescaled;
    }


    public String getGbsrpFixedIncPrimarySectorMunicipalTaxExemptLongRescaled() {
        return this.gbsrpFixedIncPrimarySectorMunicipalTaxExemptLongRescaled;
    }


    public void setGbsrpFixedIncPrimarySectorMunicipalTaxExemptLongRescaled(
        final String gbsrpFixedIncPrimarySectorMunicipalTaxExemptLongRescaled) {
        this.gbsrpFixedIncPrimarySectorMunicipalTaxExemptLongRescaled = gbsrpFixedIncPrimarySectorMunicipalTaxExemptLongRescaled;
    }


    public String getGbsrpFixedIncPrimarySectorMunicipalTaxableLongRescaled() {
        return this.gbsrpFixedIncPrimarySectorMunicipalTaxableLongRescaled;
    }


    public void setGbsrpFixedIncPrimarySectorMunicipalTaxableLongRescaled(
        final String gbsrpFixedIncPrimarySectorMunicipalTaxableLongRescaled) {
        this.gbsrpFixedIncPrimarySectorMunicipalTaxableLongRescaled = gbsrpFixedIncPrimarySectorMunicipalTaxableLongRescaled;
    }


    public String getGbsrpFixedIncPrimarySectorNonAgencyResidentialMortgageBackedLongRescaled() {
        return this.gbsrpFixedIncPrimarySectorNonAgencyResidentialMortgageBackedLongRescaled;
    }


    public void setGbsrpFixedIncPrimarySectorNonAgencyResidentialMortgageBackedLongRescaled(
        final String gbsrpFixedIncPrimarySectorNonAgencyResidentialMortgageBackedLongRescaled) {
        this.gbsrpFixedIncPrimarySectorNonAgencyResidentialMortgageBackedLongRescaled = gbsrpFixedIncPrimarySectorNonAgencyResidentialMortgageBackedLongRescaled;
    }


    public String getGbsrpFixedIncPrimarySectorOptionWarrantLongRescaled() {
        return this.gbsrpFixedIncPrimarySectorOptionWarrantLongRescaled;
    }


    public void setGbsrpFixedIncPrimarySectorOptionWarrantLongRescaled(
        final String gbsrpFixedIncPrimarySectorOptionWarrantLongRescaled) {
        this.gbsrpFixedIncPrimarySectorOptionWarrantLongRescaled = gbsrpFixedIncPrimarySectorOptionWarrantLongRescaled;
    }


    public String getGbsrpFixedIncPrimarySectorPreferredStockLongRescaled() {
        return this.gbsrpFixedIncPrimarySectorPreferredStockLongRescaled;
    }


    public void setGbsrpFixedIncPrimarySectorPreferredStockLongRescaled(
        final String gbsrpFixedIncPrimarySectorPreferredStockLongRescaled) {
        this.gbsrpFixedIncPrimarySectorPreferredStockLongRescaled = gbsrpFixedIncPrimarySectorPreferredStockLongRescaled;
    }


    public String getGbsrpFixedIncPrimarySectorSwapLongRescaled() {
        return this.gbsrpFixedIncPrimarySectorSwapLongRescaled;
    }


    public void setGbsrpFixedIncPrimarySectorSwapLongRescaled(final String gbsrpFixedIncPrimarySectorSwapLongRescaled) {
        this.gbsrpFixedIncPrimarySectorSwapLongRescaled = gbsrpFixedIncPrimarySectorSwapLongRescaled;
    }


    public String getGbsrpFixedIncSuperSectorCashEquivalentsLongRescaled() {
        return this.gbsrpFixedIncSuperSectorCashEquivalentsLongRescaled;
    }


    public void setGbsrpFixedIncSuperSectorCashEquivalentsLongRescaled(
        final String gbsrpFixedIncSuperSectorCashEquivalentsLongRescaled) {
        this.gbsrpFixedIncSuperSectorCashEquivalentsLongRescaled = gbsrpFixedIncSuperSectorCashEquivalentsLongRescaled;
    }


    public String getGbsrpFixedIncSuperSectorCorporateLongRescaled() {
        return this.gbsrpFixedIncSuperSectorCorporateLongRescaled;
    }


    public void setGbsrpFixedIncSuperSectorCorporateLongRescaled(final String gbsrpFixedIncSuperSectorCorporateLongRescaled) {
        this.gbsrpFixedIncSuperSectorCorporateLongRescaled = gbsrpFixedIncSuperSectorCorporateLongRescaled;
    }


    public String getGbsrpFixedIncSuperSectorDerivativeLongRescaled() {
        return this.gbsrpFixedIncSuperSectorDerivativeLongRescaled;
    }


    public void setGbsrpFixedIncSuperSectorDerivativeLongRescaled(final String gbsrpFixedIncSuperSectorDerivativeLongRescaled) {
        this.gbsrpFixedIncSuperSectorDerivativeLongRescaled = gbsrpFixedIncSuperSectorDerivativeLongRescaled;
    }


    public String getGbsrpFixedIncSuperSectorGovernmentLongRescaled() {
        return this.gbsrpFixedIncSuperSectorGovernmentLongRescaled;
    }


    public void setGbsrpFixedIncSuperSectorGovernmentLongRescaled(final String gbsrpFixedIncSuperSectorGovernmentLongRescaled) {
        this.gbsrpFixedIncSuperSectorGovernmentLongRescaled = gbsrpFixedIncSuperSectorGovernmentLongRescaled;
    }


    public String getGbsrpFixedIncSuperSectorMunicipalLongRescaled() {
        return this.gbsrpFixedIncSuperSectorMunicipalLongRescaled;
    }


    public void setGbsrpFixedIncSuperSectorMunicipalLongRescaled(final String gbsrpFixedIncSuperSectorMunicipalLongRescaled) {
        this.gbsrpFixedIncSuperSectorMunicipalLongRescaled = gbsrpFixedIncSuperSectorMunicipalLongRescaled;
    }


    public String getGbsrpFixedIncSuperSectorSecuritizedLongRescaled() {
        return this.gbsrpFixedIncSuperSectorSecuritizedLongRescaled;
    }


    public void setGbsrpFixedIncSuperSectorSecuritizedLongRescaled(final String gbsrpFixedIncSuperSectorSecuritizedLongRescaled) {
        this.gbsrpFixedIncSuperSectorSecuritizedLongRescaled = gbsrpFixedIncSuperSectorSecuritizedLongRescaled;
    }


    public String getGbsrpPortfolioDate() {
        return this.gbsrpPortfolioDate;
    }


    public void setGbsrpPortfolioDate(final String gbsrpPortfolioDate) {
        this.gbsrpPortfolioDate = gbsrpPortfolioDate;
    }


    public String getPsrpNumberOfBondHoldings() {
        return this.psrpNumberOfBondHoldings;
    }


    public void setPsrpNumberOfBondHoldings(final String psrpNumberOfBondHoldings) {
        this.psrpNumberOfBondHoldings = psrpNumberOfBondHoldings;
    }


    public String getPsrpNumberOfStockHoldings() {
        return this.psrpNumberOfStockHoldings;
    }


    public void setPsrpNumberOfStockHoldings(final String psrpNumberOfStockHoldings) {
        this.psrpNumberOfStockHoldings = psrpNumberOfStockHoldings;
    }


    public String getPsrpPortfolioDate() {
        return this.psrpPortfolioDate;
    }


    public void setPsrpPortfolioDate(final String psrpPortfolioDate) {
        this.psrpPortfolioDate = psrpPortfolioDate;
    }


    public String getTtrBestFitIndexReturn1Mth() {
        return this.ttrBestFitIndexReturn1Mth;
    }


    public void setTtrBestFitIndexReturn1Mth(final String ttrBestFitIndexReturn1Mth) {
        this.ttrBestFitIndexReturn1Mth = ttrBestFitIndexReturn1Mth;
    }


    public String getTtrBestFitIndexReturn3Mth() {
        return this.ttrBestFitIndexReturn3Mth;
    }


    public void setTtrBestFitIndexReturn3Mth(final String ttrBestFitIndexReturn3Mth) {
        this.ttrBestFitIndexReturn3Mth = ttrBestFitIndexReturn3Mth;
    }


    public String getTtrBestFitIndexReturn6Mth() {
        return this.ttrBestFitIndexReturn6Mth;
    }


    public void setTtrBestFitIndexReturn6Mth(final String ttrBestFitIndexReturn6Mth) {
        this.ttrBestFitIndexReturn6Mth = ttrBestFitIndexReturn6Mth;
    }


    public String getTtrBestFitIndexReturn1Yr() {
        return this.ttrBestFitIndexReturn1Yr;
    }


    public void setTtrBestFitIndexReturn1Yr(final String ttrBestFitIndexReturn1Yr) {
        this.ttrBestFitIndexReturn1Yr = ttrBestFitIndexReturn1Yr;
    }


    public String getTtrBestFitIndexReturn3Yr() {
        return this.ttrBestFitIndexReturn3Yr;
    }


    public void setTtrBestFitIndexReturn3Yr(final String ttrBestFitIndexReturn3Yr) {
        this.ttrBestFitIndexReturn3Yr = ttrBestFitIndexReturn3Yr;
    }


    public String getTtrBestFitIndexReturn5Yr() {
        return this.ttrBestFitIndexReturn5Yr;
    }


    public void setTtrBestFitIndexReturn5Yr(final String ttrBestFitIndexReturn5Yr) {
        this.ttrBestFitIndexReturn5Yr = ttrBestFitIndexReturn5Yr;
    }


    public String getTtrBestFitIndexReturn10Yr() {
        return this.ttrBestFitIndexReturn10Yr;
    }


    public void setTtrBestFitIndexReturn10Yr(final String ttrBestFitIndexReturn10Yr) {
        this.ttrBestFitIndexReturn10Yr = ttrBestFitIndexReturn10Yr;
    }


    public String getTtrCategoryReturn1Mth() {
        return this.ttrCategoryReturn1Mth;
    }


    public void setTtrCategoryReturn1Mth(final String ttrCategoryReturn1Mth) {
        this.ttrCategoryReturn1Mth = ttrCategoryReturn1Mth;
    }


    public String getTtrCategoryReturn3Mth() {
        return this.ttrCategoryReturn3Mth;
    }


    public void setTtrCategoryReturn3Mth(final String ttrCategoryReturn3Mth) {
        this.ttrCategoryReturn3Mth = ttrCategoryReturn3Mth;
    }


    public String getTtrCategoryReturn6Mth() {
        return this.ttrCategoryReturn6Mth;
    }


    public void setTtrCategoryReturn6Mth(final String ttrCategoryReturn6Mth) {
        this.ttrCategoryReturn6Mth = ttrCategoryReturn6Mth;
    }


    public String getTtrCategoryReturn1Yr() {
        return this.ttrCategoryReturn1Yr;
    }


    public void setTtrCategoryReturn1Yr(final String ttrCategoryReturn1Yr) {
        this.ttrCategoryReturn1Yr = ttrCategoryReturn1Yr;
    }


    public String getTtrCategoryReturn3Yr() {
        return this.ttrCategoryReturn3Yr;
    }


    public void setTtrCategoryReturn3Yr(final String ttrCategoryReturn3Yr) {
        this.ttrCategoryReturn3Yr = ttrCategoryReturn3Yr;
    }


    public String getTtrCategoryReturn5Yr() {
        return this.ttrCategoryReturn5Yr;
    }


    public void setTtrCategoryReturn5Yr(final String ttrCategoryReturn5Yr) {
        this.ttrCategoryReturn5Yr = ttrCategoryReturn5Yr;
    }


    public String getTtrCategoryReturn10Yr() {
        return this.ttrCategoryReturn10Yr;
    }


    public void setTtrCategoryReturn10Yr(final String ttrCategoryReturn10Yr) {
        this.ttrCategoryReturn10Yr = ttrCategoryReturn10Yr;
    }


    public String getMptpiIndexName() {
        return this.mptpiIndexName;
    }


    public void setMptpiIndexName(final String mptpiIndexName) {
        this.mptpiIndexName = mptpiIndexName;
    }


    public String getDpDayEndDate() {
        return this.dpDayEndDate;
    }


    public void setDpDayEndDate(final String dpDayEndDate) {
        this.dpDayEndDate = dpDayEndDate;
    }


    public String getTtrMonthEndDate() {
        return this.ttrMonthEndDate;
    }


    public void setTtrMonthEndDate(final String ttrMonthEndDate) {
        this.ttrMonthEndDate = ttrMonthEndDate;
    }


    public String getDpCategoryReturn1Mth() {
        return this.dpCategoryReturn1Mth;
    }


    public void setDpCategoryReturn1Mth(final String dpCategoryReturn1Mth) {
        this.dpCategoryReturn1Mth = dpCategoryReturn1Mth;
    }


    public String getDpCategoryReturn3Mth() {
        return this.dpCategoryReturn3Mth;
    }


    public void setDpCategoryReturn3Mth(final String dpCategoryReturn3Mth) {
        this.dpCategoryReturn3Mth = dpCategoryReturn3Mth;
    }


    public String getDpCategoryReturn6Mth() {
        return this.dpCategoryReturn6Mth;
    }


    public void setDpCategoryReturn6Mth(final String dpCategoryReturn6Mth) {
        this.dpCategoryReturn6Mth = dpCategoryReturn6Mth;
    }


    public String getDpCategoryReturn1Yr() {
        return this.dpCategoryReturn1Yr;
    }


    public void setDpCategoryReturn1Yr(final String dpCategoryReturn1Yr) {
        this.dpCategoryReturn1Yr = dpCategoryReturn1Yr;
    }


    public String getDpCategoryReturn3Yr() {
        return this.dpCategoryReturn3Yr;
    }


    public void setDpCategoryReturn3Yr(final String dpCategoryReturn3Yr) {
        this.dpCategoryReturn3Yr = dpCategoryReturn3Yr;
    }


    public String getDpCategoryReturn5Yr() {
        return this.dpCategoryReturn5Yr;
    }


    public void setDpCategoryReturn5Yr(final String dpCategoryReturn5Yr) {
        this.dpCategoryReturn5Yr = dpCategoryReturn5Yr;
    }


    public String getDpCategoryReturn10Yr() {
        return this.dpCategoryReturn10Yr;
    }


    public void setDpCategoryReturn10Yr(final String dpCategoryReturn10Yr) {
        this.dpCategoryReturn10Yr = dpCategoryReturn10Yr;
    }


    public String getDpCategoryReturnYTD() {
        return this.dpCategoryReturnYTD;
    }


    public void setDpCategoryReturnYTD(final String dpCategoryReturnYTD) {
        this.dpCategoryReturnYTD = dpCategoryReturnYTD;
    }


    public String getSrStubYearEndReturnEndDate() {
        return this.srStubYearEndReturnEndDate;
    }


    public void setSrStubYearEndReturnEndDate(final String srStubYearEndReturnEndDate) {
        this.srStubYearEndReturnEndDate = srStubYearEndReturnEndDate;
    }


    public String getSrStubYearEndReturn() {
        return this.srStubYearEndReturn;
    }


    public void setSrStubYearEndReturn(final String srStubYearEndReturn) {
        this.srStubYearEndReturn = srStubYearEndReturn;
    }


    public FBPrimaryProspectusBenchmarks getFbPrimaryProspectusBenchmarks() {
        return this.fbPrimaryProspectusBenchmarks;
    }


    public void setFbPrimaryProspectusBenchmarks(final FBPrimaryProspectusBenchmarks fbPrimaryProspectusBenchmarks) {
        this.fbPrimaryProspectusBenchmarks = fbPrimaryProspectusBenchmarks;
    }


    public String getTtrPrimaryIndexReturn1Mth() {
        return this.ttrPrimaryIndexReturn1Mth;
    }


    public void setTtrPrimaryIndexReturn1Mth(final String ttrPrimaryIndexReturn1Mth) {
        this.ttrPrimaryIndexReturn1Mth = ttrPrimaryIndexReturn1Mth;
    }


    public String getTtrPrimaryIndexReturn3Mth() {
        return this.ttrPrimaryIndexReturn3Mth;
    }


    public void setTtrPrimaryIndexReturn3Mth(final String ttrPrimaryIndexReturn3Mth) {
        this.ttrPrimaryIndexReturn3Mth = ttrPrimaryIndexReturn3Mth;
    }


    public String getTtrPrimaryIndexReturn6Mth() {
        return this.ttrPrimaryIndexReturn6Mth;
    }


    public void setTtrPrimaryIndexReturn6Mth(final String ttrPrimaryIndexReturn6Mth) {
        this.ttrPrimaryIndexReturn6Mth = ttrPrimaryIndexReturn6Mth;
    }


    public String getTtrPrimaryIndexReturn1Yr() {
        return this.ttrPrimaryIndexReturn1Yr;
    }


    public void setTtrPrimaryIndexReturn1Yr(final String ttrPrimaryIndexReturn1Yr) {
        this.ttrPrimaryIndexReturn1Yr = ttrPrimaryIndexReturn1Yr;
    }


    public String getTtrPrimaryIndexReturn3Yr() {
        return this.ttrPrimaryIndexReturn3Yr;
    }


    public void setTtrPrimaryIndexReturn3Yr(final String ttrPrimaryIndexReturn3Yr) {
        this.ttrPrimaryIndexReturn3Yr = ttrPrimaryIndexReturn3Yr;
    }


    public String getTtrPrimaryIndexReturn5Yr() {
        return this.ttrPrimaryIndexReturn5Yr;
    }


    public void setTtrPrimaryIndexReturn5Yr(final String ttrPrimaryIndexReturn5Yr) {
        this.ttrPrimaryIndexReturn5Yr = ttrPrimaryIndexReturn5Yr;
    }


    public String getTtrPrimaryIndexReturn10Yr() {
        return this.ttrPrimaryIndexReturn10Yr;
    }


    public void setTtrPrimaryIndexReturn10Yr(final String ttrPrimaryIndexReturn10Yr) {
        this.ttrPrimaryIndexReturn10Yr = ttrPrimaryIndexReturn10Yr;
    }


    public String getTtrPrimaryIndexReturnYTD() {
        return this.ttrPrimaryIndexReturnYTD;
    }


    public void setTtrPrimaryIndexReturnYTD(final String ttrPrimaryIndexReturnYTD) {
        this.ttrPrimaryIndexReturnYTD = ttrPrimaryIndexReturnYTD;
    }

    public String getTsDayEndBidOfferPricesDate() {
        return this.tsDayEndBidOfferPricesDate;
    }

    public void setTsDayEndBidOfferPricesDate(final String tsDayEndBidOfferPricesDate) {
        this.tsDayEndBidOfferPricesDate = tsDayEndBidOfferPricesDate;
    }

    public String getTsDayEndNAVDate() {
        return this.tsDayEndNAVDate;
    }

    public void setTsDayEndNAVDate(final String tsDayEndNAVDate) {
        this.tsDayEndNAVDate = tsDayEndNAVDate;
    }


    public String getCyrPrimaryIndexYear1() {
        return this.cyrPrimaryIndexYear1;
    }


    public void setCyrPrimaryIndexYear1(final String cyrPrimaryIndexYear1) {
        this.cyrPrimaryIndexYear1 = cyrPrimaryIndexYear1;
    }


    public String getCyrPrimaryIndexYear10() {
        return this.cyrPrimaryIndexYear10;
    }


    public void setCyrPrimaryIndexYear10(final String cyrPrimaryIndexYear10) {
        this.cyrPrimaryIndexYear10 = cyrPrimaryIndexYear10;
    }


    public String getCyrPrimaryIndexYear2() {
        return this.cyrPrimaryIndexYear2;
    }


    public void setCyrPrimaryIndexYear2(final String cyrPrimaryIndexYear2) {
        this.cyrPrimaryIndexYear2 = cyrPrimaryIndexYear2;
    }


    public String getCyrPrimaryIndexYear3() {
        return this.cyrPrimaryIndexYear3;
    }


    public void setCyrPrimaryIndexYear3(final String cyrPrimaryIndexYear3) {
        this.cyrPrimaryIndexYear3 = cyrPrimaryIndexYear3;
    }


    public String getCyrPrimaryIndexYear4() {
        return this.cyrPrimaryIndexYear4;
    }


    public void setCyrPrimaryIndexYear4(final String cyrPrimaryIndexYear4) {
        this.cyrPrimaryIndexYear4 = cyrPrimaryIndexYear4;
    }


    public String getCyrPrimaryIndexYear5() {
        return this.cyrPrimaryIndexYear5;
    }


    public void setCyrPrimaryIndexYear5(final String cyrPrimaryIndexYear5) {
        this.cyrPrimaryIndexYear5 = cyrPrimaryIndexYear5;
    }


    public String getCyrPrimaryIndexYear6() {
        return this.cyrPrimaryIndexYear6;
    }


    public void setCyrPrimaryIndexYear6(final String cyrPrimaryIndexYear6) {
        this.cyrPrimaryIndexYear6 = cyrPrimaryIndexYear6;
    }


    public String getCyrPrimaryIndexYear7() {
        return this.cyrPrimaryIndexYear7;
    }


    public void setCyrPrimaryIndexYear7(final String cyrPrimaryIndexYear7) {
        this.cyrPrimaryIndexYear7 = cyrPrimaryIndexYear7;
    }


    public String getCyrPrimaryIndexYear8() {
        return this.cyrPrimaryIndexYear8;
    }


    public void setCyrPrimaryIndexYear8(final String cyrPrimaryIndexYear8) {
        this.cyrPrimaryIndexYear8 = cyrPrimaryIndexYear8;
    }


    public String getCyrPrimaryIndexYear9() {
        return this.cyrPrimaryIndexYear9;
    }


    public void setCyrPrimaryIndexYear9(final String cyrPrimaryIndexYear9) {
        this.cyrPrimaryIndexYear9 = cyrPrimaryIndexYear9;
    }


    public String getMptpiIndexId() {
        return this.mptpiIndexId;
    }


    public void setMptpiIndexId(final String mptpiIndexId) {
        this.mptpiIndexId = mptpiIndexId;
    }


    public String getId() {
        return this.id;
    }


    public void setId(final String id) {
        this.id = id;
    }
}
