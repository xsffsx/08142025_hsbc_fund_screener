## Generate Token
### Etnet Token
 > Run the class of GenerateEtnetToken

    request example:
        News Headlines
        Method:GET
        URL: https://hsp2gp2uat.etnet.com.hk/HSAPI/GetData/NewsHeadline?category=relatednews&symbol=00001,00005&recordsPerPage=10&pageId=1&locale=en&token={token} 


### Labci Token
> Run the class of GenerateLabciToken

    request example:
        Chart Data
        Method:POST
        URL:https://mkdlcu.qualityassurance.ebanking.hhhh.com.hk/lciapi/chart/data
        request boby:
        {
        "market": "CN",
        "service": "dIDN_RDF",
        "split": false,
        "displayName": true,
        "currency": true,
        "timeZone": true,
        "tradingWeek": true,
        "item": ["601111.SS"],
        "intCnt": 1,
        "intType": "DAILY",
        "filter": ["DATE","OPEN","HIGH","LOW","CLOSE","VOLUME","SMA=25,50,75"],
        "startTm": "2018-01-16T07:02:00",
        "endTm": "2018-01-17T17:17:29",
        "unitFormat": true,
        "token": "{token}"
        }

    
### Tris Token
> Run the class of GenerateTrisToken

    request example:
        Tris News Headlines
        Method:POST
        URL: http://hktrisu11.hk.hhhh:30000/tris/news/headline 
        request boby:
        {
        "closure": "app_tris_rbwm_stockconnect_sat",
        "token": "{token}",
        "query": "(topics:CN) AND (topics:LZH)",
        "filter": [
            "GUID",
            "NEWSHDLN",
            "HDLN_TES",
            "NEWS_LT",
            "_LANG_LZT"
        ],
        "numResult": 2,
        "sortby": "revisiontime",
        "highlight": false,
        "totalHitCount": true,
        "teaser": true,
        "resultOffset": 0
        }  

> Note: Replace the {token} with program generated token