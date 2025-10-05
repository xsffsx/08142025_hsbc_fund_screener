package com.dummy.wpb.product.constant;

import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("java:S2386")
public class EsgConstants {

    private EsgConstants () {}

    public static final String JOB_NAME = "Import ESG CSV Job";
    public static final String ESG_NODE  = "esg";

    private static final String SDG_THM_RK_BSC_NEEDS  = "sdgThmRkBscNeeds";
    private static final String SDG_THM_RK_EMP  = "sdgThmRkEmp";
    private static final String SDG_THM_RK_CLIM_CHG  = "sdgThmRkClimChg";
    private static final String SDG_THM_RK_NATU_CAPT  = "sdgThmRkNatuCapt";
    private static final String SDG_THM_RK_GOV  = "sdgThmRkGov";

    public static final String[] fieldList = {
            "siClass",
            "esgScore",
            "esgRating",
            "carbonIntens",
            SDG_THM_RK_BSC_NEEDS,
            SDG_THM_RK_EMP,
            SDG_THM_RK_CLIM_CHG,
            SDG_THM_RK_NATU_CAPT,
            SDG_THM_RK_GOV
    };

    public static final String[] nameList = {
            "identifierType",
            "identifierValue",
            "validIdentifier",
            "isin",
            "smartId",
            "msciIssuerName",
            "overrideSecurityName",
            "siClass",
            "etiReported",
            "esgScore",
            "esgRating",
            "carbonIntens",
            SDG_THM_RK_BSC_NEEDS,
            SDG_THM_RK_EMP,
            SDG_THM_RK_CLIM_CHG,
            SDG_THM_RK_NATU_CAPT,
            SDG_THM_RK_GOV,
            "errorMessage",
            "esgInd",
            "updateDate"
    };

    public static final Set<String> sdgThemeFieldList = new HashSet<>();
    static {
        sdgThemeFieldList.add(SDG_THM_RK_BSC_NEEDS);
        sdgThemeFieldList.add(SDG_THM_RK_EMP);
        sdgThemeFieldList.add(SDG_THM_RK_CLIM_CHG);
        sdgThemeFieldList.add(SDG_THM_RK_NATU_CAPT);
        sdgThemeFieldList.add(SDG_THM_RK_GOV);
    }
}
