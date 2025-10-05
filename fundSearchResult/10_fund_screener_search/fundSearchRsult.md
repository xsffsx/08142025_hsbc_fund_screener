

https://investments3.personal-banking.hsbc.com.hk/shp/wealth-mobile-mds-shp-api-hk-hbap-prod-proxy/v0/wmds/fundSearchResult

# Asset class

```json
{
    "productType": "UT",
    "returnOnlyNumberOfMatches": false,
    "detailedCriterias": [
        {
            "criteriaKey": "CAT",
            "criteriaValue": "AB:AC:EB:ES:GB:GS:JB:JS:UB:US:GR:DC:DH:MB:SC:SA:SK:SO:ST:AS:EF:GO:SD:UF:GI:HA:HE:HL:HU:MA:MM:MO:MG:MU:CF:GP:SS:HG:ME",
            "operator": "in"
        },
        {
            "criteriaKey": "INVSTRG",
            "criteriaValue": "GL:AE:AP:CN:HK:JP:EM:EU:UK:NA:SA:SE:BC:IN:KR:GE:RE",
            "operator": "in"
        },
        {
            "criteriaKey": "FAM",
            "criteriaValue": "HSBCA:ABRDN:NEWAA:DRESD:CREDI:BARIN:MERRL:BNY:BOS:BNP:BPFHK:CIMC:CHINA:CSOP:FIDEL:FRANK:GSAM:HT:HSVM:JINT:INVES:INVET:JFFUL:JHI:MAN:MANUL:NEUBE:PIM:PA:PICTE:SCHRO:TRP:UBS:UBSHK:VALPR:WELLT",
            "operator": "in"
        },
        {
            "criteriaKey": "RISK",
            "criteriaValue": "0:1:2:3:4:5",
            "operator": "in"
        },
        {
            "criteriaKey": "CCY",
            "criteriaValue": "AUD:CAD:CHF:CNY:EUR:GBP:HKD:JPY:NZD:SEK:SGD:USD",
            "operator": "in"
        },
        {
            "criteriaKey": "GBAA",
            "criteriaValue": "Y",
            "operator": "eq"
        },
        {
            "criteriaKey": "ESG",
            "criteriaValue": "Y",
            "operator": "in"
        },
        {
            "criteriaKey": "MSR",
            "criteriaValue": "5:4:3:2:1:null",
            "operator": "in"
        },
        {
            "criteriaKey": "ACQN",
            "criteriaValue": "AAA:AA:A:BBB:BB:B:Below B:",
            "operator": "in"
        },
        {
            "criteriaKey": "Y1QTL",
            "criteriaValue": "1:2:3:4",
            "operator": "in"
        }
    ],
    "rangeCriterias": [
        {
            "criteriaKey": "Y1RTRN",
            "criteriaValues": [
                {
                    "minOperator": "ge",
                    "minCriteriaLimit": -51.05,
                    "maxOperator": "le",
                    "maxCriteriaLimit": 133.35
                }
            ]
        },
        {
            "criteriaKey": "DIVYLD",
            "criteriaValues": [
                {
                    "minOperator": "ge",
                    "minCriteriaLimit": 0,
                    "maxOperator": "le",
                    "maxCriteriaLimit": 10.11
                }
            ]
        }
    ],
    "sortBy": "return1Yr",
    "sortOrder": "desc",
    "numberOfRecords": 10,
    "startDetail": 1,
    "endDetail": 10,
    "sortCriterias": [
        {
            "sortKey": "prodStatCde",
            "sortOrder": "asc"
        },
        {
            "sortKey": "return1Yr",
            "sortOrder": "desc"
        }
    ],
    "holdings": [
        {
            "criteriaKey": "topHoldingsList",
            "criteriaValue": true,
            "top": "5",
            "others": true
        },
        {
            "criteriaKey": "stockSectors",
            "criteriaValue": true,
            "top": "5",
            "others": true
        },
        {
            "criteriaKey": "equityRegional",
            "criteriaValue": true,
            "top": "5",
            "others": true
        },
        {
            "criteriaKey": "bondSectors",
            "criteriaValue": true,
            "top": "5",
            "others": true
        },
        {
            "criteriaKey": "bondRegional",
            "criteriaValue": true,
            "top": "5",
            "others": true
        },
        {
            "criteriaKey": "assetAlloc",
            "criteriaValue": true
        }
    ],
    "restrOnlScribInd": "Y"
}

```


