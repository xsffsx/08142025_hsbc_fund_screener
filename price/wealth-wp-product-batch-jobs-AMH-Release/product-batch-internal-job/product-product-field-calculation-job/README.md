# Product field calculation job

## Job Description
This project contains multiple field calculation jobs, which job will be run depends on the job parameters: calculatedField

### BondMktOrderIndCalculationJob
```text
params: ctryRecCde=HK grpMembrRecCde=HBAP calculatedField=bondMktOrderInd

filter: {"ctryRecCde":"HK","grpMembrRecCde":"HBAP","prodTypeCde":"BOND","dmyProdSubtpRecInd":{"$nin":[null,"Y"]},"debtInstm.mktOrderInd":{"$ne":null}}}

update: {"$set":{"debtInstm.mktOrderInd":"N","recUpdtDtTm": ISODate()}}
```
### BondNewlyIntroducedIndJob
params: ctryRecCde=HK grpMembrRecCde=HBAP calculatedField=bondNewlyIntroducedInd

1. CleanOldSortTasklet
```text
   filter: {"ctryRecCde":"HK","grpMembrRecCde":"HBAP","prodTypeCde":"BOND","introProdCurrPrdInd":"Y"}

   update: { "$set" : { "introProdCurrPrdInd" : null, "recUpdtDtTm" : ISODate() } }
```
2. UpdateTasklet
```text
   filter: {"ctryRecCde":"HK","grpMembrRecCde":"HBAP","prodTypeCde":"BOND","prodLnchDt":{"$gte":"2023-05-04"},"prodStatCde":{"$in":["A","S"]},"dmyProdSubtpRecInd":{"$nin":[null,"Y"]},"prodSubtpCde":{"$ne":"BOND"}}
   
   sort: {"prodLnchDt":-1}

   limit: maxNum 
   
   update: { "$set" : { "introProdCurrPrdInd" : "Y", "recUpdtDtTm" : ISODate() } }
```
### BondNewlyIssueIndJob
params: ctryRecCde=HK grpMembrRecCde=HBAP calculatedField=bondNewlyIssueInd

1. CleanOldSortTasklet
```json
   filter: {"ctryRecCde":"HK","grpMembrRecCde":"HBAP","prodTypeCde":"BOND","debtInstm.newlyIssBondInd":{"$ne":null}}
   
   update:  { "$set" : { "debtInstm.newlyIssBondInd" : "N", "recUpdtDtTm" : ISODate() } }
```
2. UpdateTasklet
```json
   filter: {"ctryRecCde":"HK","grpMembrRecCde":"HBAP","prodTypeCde":"BOND","debtInstm.prodIssDt":{"$gte":"2023-02-04"},"prodStatCde":{"$in":["A","S"]},"dmyProdSubtpRecInd":{"$nin":[null,"Y"]}}
   
   sort: {"debtInstm.prodIssDt":-1, "_id":1}
   
   limit: maxNum
        
   update: { "$set" : { "debtInstm.newlyIssBondInd" : "Y", "recUpdtDtTm" :  ISODate() } }
```
### BondHighestYieldIndJob
params: ctryRecCde=HK grpMembrRecCde=HBAP calculatedField=bondHighestYieldInd

1. CleanOldSortTasklet
```json
   filter: { "ctryRecCde" : "HK", "grpMembrRecCde" : "HBAP", "prodTypeCde" : "BOND", "prodTopYieldRankNum" : { "$ne" : null}}
   
   update: { "$set" : { "prodTopYieldRankNum" : null, "recUpdtDtTm" :  ISODate() } }
```
2. UpdateTasklet
   First need to query reference data: {"ctryRecCde":"HK","grpMembrRecCde":"HBAP","cdvTypeCde":"PRODSUBTP","cdvParntTypeCde":"PRODTYP","cdvParntCde":"BOND"}

```js
db.product.find({
   "ctryRecCde": ${ctryRecCde},
   "grpMembrRecCde": ${grpMembrRecCde},
   "prodTypeCde": "BOND",
   "prodSubtpCde": ${prodSubtpCde},
   "debtInstm.yieldOfferText": {"$ne": null},
   "prodStatCde": {"$in": ["A", "S"]},
   "allowBuyProdInd": "Y",
   "dmyProdSubtpRecInd": {"$nin": [null, "Y"]}
})
.sort({"debtInstm.yieldOfferText" : -1})
.limit(3);
   
update: { "$set" : { "prodTopYieldRankNum" : seq, "recUpdtDtTm" : ISODate() } }
```

refer sql:
```sql
SELECT S.*
FROM (SELECT A.PROD_ID, A.PROD_SUBTP_CDE, MAX(B.YIELD_OFFER_TEXT) AS MAX_YIELD
      FROM PROD A
               JOIN DEBT_INSTM B ON A.PROD_ID = B.PROD_ID_DEBT_INSTM
      WHERE A.CTRY_REC_CDE = :CTRY_REC_CDE
        AND A.GRP_MEMBR_REC_CDE = :GRP_MEMBR_REC_CDE
        AND A.PROD_TYPE_CDE = 'BOND'
        AND A.PROD_SUBTP_CDE = :PROD_SUBTP_CDE
        AND B.YIELD_OFFER_TEXT IS NOT NULL
        AND (A.PROD_STAT_CDE = 'A' OR A.PROD_STAT_CDE = 'S')
        AND A.ALLOW_BUY_PROD_IND = 'Y'
        AND A.DMY_PROD_SUBTP_REC_IND <> 'Y'
      GROUP BY A.PROD_SUBTP_CDE, A.PROD_ID
      ORDER BY MAX_YIELD DESC) S
WHERE ROWNUM <= 3;
```
### BondTopPerformanceIndJob
params: ctryRecCde=HK grpMembrRecCde=HBAP calculatedField=bondTopPerformanceInd

bondTopPerformanceInd update step:

1. Filter products and product price history, group by id to get the max prcEffDt.

Convert to mongo aggregate language: 
````js
db.product.aggregate([
    {
        $match: {
            "ctryRecCde": "HK",
            "grpMembrRecCde": "HBAP",
            "prodTypeCde": "BOND",
            "prodSubtpCde": "CORPBOND",
            "prodStatCde": {"$in": ["A", "S"]},
            "allowBuyProdInd": "Y",
            "dmyProdSubtpRecInd": {"$nin": ["Y", null]}
        }
    },
    {
        $project: {
            "_id": 1,
        }
    },
    {
        $lookup: {
            from: "prod_prc_hist",
            localField: "_id",
            foreignField: "prodId",
            as: "history"
        }
    },
    {
        $unwind: "$history"
    },
    {
        $match: {
            "history.pdcyPrcCde": "D",
            "history.prodOffrPrcAmt": {$nin: [0, null]},
            "history.prcEffDt": {
                $lte: {closingDate},
                $gte: {endDate}
            },
        }
    },
    {
        $group: {
            _id: "$_id",
            maxEffDt: {$max: '$history.prcEffDt'}
        }
    }
], {allowDiskUse: true});
````

2. Query prod_prc_hist according to the combination of prodID and maxPrcEffDt, and then sort from large to small according to diffPrice.

Convert to mongo aggregate language: 
````js
db.prod_prc_hist.aggregate([
    {
        $match: {
            $or: [
                {
                    prodId: 50002510,
                    prcEffDt: ISODate("2021-12-01T00:00:00.000Z")
                },
                {
                    prodId: 50001340,
                    prcEffDt: ISODate("2021-12-01T00:00:00.000Z")
                }
            ]
        },
    },
    {
        $project: {
            "prodId": 1,
            "prcEffDt": 1,
            "prodOffrPrcAmt": 1,
        }
    },
    {
        $lookup: {
            from: "product",
            localField: "prodId",
            foreignField: "_id",
            as: "product"
        }
    },
    {
        $unwind: "$product"
    },
    {
        $project: {
            "prodId": 1,
            "prcEffDt": 1,
            "diffPrice": {
                $divide: [
                    {$subtract: ["$product.prodOffrPrcAmt", "$prodOffrPrcAmt"]},
                    "$prodOffrPrcAmt"
                ]
            }
        }
    },
    {
        $sort: {
            diffPrice: -1
        }
    },
    {
        $limit: 3
    }
], {allowDiskUse: true});
````

refer sql:
```sql
SELECT E.PROD_ID,
       E.PROD_SUBTP_CDE,
       MAX((F.PROD_OFFR_PRC_AMT - E.PROD_OFFR_PRC_AMT) / E.PROD_OFFR_PRC_AMT) AS DIFF_PRICE
FROM PROD F
         JOIN
     (SELECT D.PROD_SUBTP_CDE, D.PROD_ID, C.PROD_OFFR_PRC_AMT, C.PRC_EFF_DT
      FROM PROD_PRC_HIST C
               JOIN
           (SELECT A.CTRY_REC_CDE, A.GRP_MEMBR_REC_CDE, A.PROD_SUBTP_CDE, B.PROD_ID, MAX(B.PRC_EFF_DT) MAX_EFF_DT
            FROM PROD A
                     JOIN PROD_PRC_HIST B ON (A.PROD_ID = B.PROD_ID)
            WHERE A.CTRY_REC_CDE = :CTRY_REC_CDE
              AND A.GRP_MEMBR_REC_CDE = :GRP_MEMBR_REC_CDE
              AND A.PROD_TYPE_CDE = 'BOND'
              AND (A.PROD_STAT_CDE = 'A' OR A.PROD_STAT_CDE = 'S')
              AND A.ALLOW_BUY_PROD_IND = 'Y'
              AND A.DMY_PROD_SUBTP_REC_IND <> 'Y'
              AND A.PROD_SUBTP_CDE = :PROD_SUBTP_CDE
              AND B.PDCY_PRC_CDE = 'D'
              AND B.PROD_OFFR_PRC_AMT IS NOT NULL
              AND B.PROD_OFFR_PRC_AMT <> 0
              AND B.PRC_EFF_DT <= :CLOSING_DATE
              AND B.PRC_EFF_DT >= :END_DATE
            GROUP BY A.CTRY_REC_CDE, A.GRP_MEMBR_REC_CDE, A.PROD_SUBTP_CDE, B.PROD_ID) D
           ON C.PROD_ID = D.PROD_ID AND C.PRC_EFF_DT = D.MAX_EFF_DT) E
     ON E.PROD_ID = F.PROD_ID
WHERE F.CTRY_REC_CDE = :CTRY_REC_CDE
  AND F.GRP_MEMBR_REC_CDE = :GRP_MEMBR_REC_CDE
  AND F.PROD_OFFR_PRC_AMT IS NOT NULL
  AND (F.PROD_OFFR_PRC_AMT - E.PROD_OFFR_PRC_AMT) >= 0
  AND ROWNUM <= :MAXNUM
GROUP BY E.PROD_ID, E.PROD_SUBTP_CDE
ORDER BY DIFF_PRICE DESC;
```

3. Update prodTopPerfmRankNum according to rank of diffPrice.

### EliRiskLvlCalculationJob
params: ctryRecCde=HK grpMembrRecCde=HBAP calculatedField=eliRiskLvlCde

EliRiskLvl update step:
1. Filter products by :
````js
db.product.aggregate([{
   "$match": {
      "ctryRecCde": "HK",
      "grpMembrRecCde": "HBAP",
      "prodStatCde": {"$in": ["A", "C"]},
      "prodTypeCde": "ELI",
      "prodMturDt": {"$gt": ISODate()},
      "eqtyLinkInvst.lnchProdInd": "Y",
      "eqtyLinkInvst.rtrvProdExtnlInd": "N",
      "eqtyLinkInvst.undlQtyInd": {"$ne": null},
      "eqtyLinkInvst.undlStock": {"$ne": null}
   }
}, {"$unwind": "$eqtyLinkInvst.undlStock"}, {
   "$project": {
      "prodId": 1,
      "riskLvlCde": 1,
      "prdProdNum": 1,
      "undlQtyInd": "$eqtyLinkInvst.undlQtyInd",
      "cptlProtcPct": "$eqtyLinkInvst.cptlProtcPct",
      "undlProdId": "$eqtyLinkInvst.undlStock.prodIdUndlInstm"
   }
}, {
   "$lookup": {
      "from": "product",
      "localField": "undlProdId",
      "foreignField": "prodId",
      "as": "undlProd"
   }
}, {"$unwind": "$undlProd"}, {
   "$group": {
      "_id": {
         "prodId": "$prodId",
         "riskLvlCde": "$riskLvlCde",
         "prdProdNum": "$prdProdNum",
         "cptlProtcPct": "$cptlProtcPct",
         "undlQtyInd": "$undlQtyInd"
      }, "undlRiskLvlCde": {"$max": "$undlProd.eliRiskLvlCde"}
   }
}, {
   "$project": {
      "prodId": "$_id.prodId",
      "riskLvlCde": "$_id.riskLvlCde",
      "cptlProtcPct": "$_id.cptlProtcPct",
      "tenor": "$_id.prdProdNum",
      "undlQtyInd": "$_id.undlQtyInd",
      "undlRiskLvlCde": 1
   }
}]);
````
refer sql:
````sql
SELECT p.PROD_ID,
       p.RISK_LVL_CDE,
       p.PRD_PROD_NUM                AS TENOR,
       p1.field_dcml_value_num       AS CPTLPROTCPCT,
       p2.field_strng_value_text     AS UNDLQTYIND,
       MAX(x.field_strng_value_text) AS UNDL_RISK_LVL_CDE
FROM tb_prod p
        JOIN tb_eqty_link_invst e ON p.prod_id = e.prod_id_eqty_link_invst
        JOIN tb_eqty_link_invst_undl_stock s
             ON p.prod_id = s.prod_id_eqty_link_invst
        LEFT OUTER JOIN tb_prod_user_defin_ext_field p1 ON p.prod_id = p1.prod_id AND p1.field_cde = 'cptlProtcPct'
        JOIN tb_prod_user_defin_ext_field p2
             ON p.prod_id = p2.prod_id AND p2.field_cde = 'undlQtyInd'
        LEFT OUTER JOIN tb_prod_user_defin_ext_field x
                        ON x.prod_id = s.prod_id_undl_instm AND x.field_cde = 'eliRiskLvlCde'
WHERE p.prod_stat_cde IN ('A', 'C')
  AND p.ctry_rec_cde = 'HK'
  AND p.grp_membr_rec_cde = 'HBAP'
  AND p.prod_type_cde = 'ELI'
  AND p.prod_mtur_dt > sysdate
  AND e.lnch_prod_ind = 'Y'
  AND e.rtrv_prod_extnl_ind = 'N'
GROUP BY p.prod_id, p.RISK_LVL_CDE, p.PRD_PROD_NUM, p1.field_dcml_value_num, p2.field_strng_value_text
````

2. Calculate new riskLvlCde base on undlQtyInd, cptlProtcPct and prdProdNum, if new riskLvlCde is different with old riskLvlCde, then update riskLvlCde with new riskLvlCde.
