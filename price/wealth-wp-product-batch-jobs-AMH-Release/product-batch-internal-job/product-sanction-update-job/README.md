# product-sanction-update-job

This job will update `Sanction List for Restricted Buy (sanctionBuyList)` & `Sanction List for Restricted Sell (sanctionSellList)`

### Application start up

> com.dummy.wpb.product.SanctionUpdateJobApplication HK HBAP ELI

### Job command

> /appvol/product-spring-batch/bin/HKHBAP/cron/wpc_sanction_list_update.sh HK HBAP ELI

### Implement

#### 1. Get the underlying stock list of specific product type (e.g. ELI)
#### 2. Get sanction buy list and sanction sell list from each underlying stocks
#### 3. Calculate the sanction buy list and sanction sell list and update to DB.

```
e.g. An ELI product ELI-1 has 2 underlying stocks: SEC-1 & SEC-2

SEC-1 sanction buy list  = [A, B]
SEC-2 sanction buy list = [A, C]

SEC-1 sanction sell list  = [D]
SEC-2 sanction sell list = [E, F]

The job will update sanction list of ELI-1 as below:
ELI-1 sanction buy list  = [A, B, C]
ELI-1 sanction sell list = [D, E, F]
```

```
// aggregate sanction list
db.product.aggregate([
    {
        "$match": {
            "ctryRecCde": "%ctryRecCde%",
            "grpMembrRecCde": "%grpMembrRecCde%",
            "prodStatCde": {"$in": ["A", "C", "P", "S", "T"]},
            "prodTypeCde": "ELI",
            "eqtyLinkInvst.undlStock": {"$exists": true}
        }
    },
    {
        "$unwind": "$eqtyLinkInvst.undlStock"
    },
    {
        "$project": {
            "sanctionBuyList": 1,
            "sanctionSellList": 1,
            "undlProdId": "$eqtyLinkInvst.undlStock.prodIdUndlInstm"
        }
    },
    {
        "$lookup": {
            "from": "product",
            "localField": "undlProdId",
            "foreignField": "prodId",
            "as": "undlProd"
        }
    },
    {
        "$unwind": "$undlProd"
    },
    {
        "$match": {
            "undlProd.prodStatCde": {"$in": ["A", "C", "P", "S", "T"]}
        }
    },
    {
        "$unwind": {
            "path": "$undlProd.sanctionBuyList",
            "preserveNullAndEmptyArrays": true
        }
    },
    {
        "$unwind": {
            "path": "$undlProd.sanctionSellList",
            "preserveNullAndEmptyArrays": true
        }
    },
    {
        "$group": {
            "_id": "$_id",
            "sanctionBuyList": {"$first": {"$ifNull": ["$sanctionBuyList", []]}},
            "sanctionSellList": {"$first": {"$ifNull": ["$sanctionSellList", []]}},
            "undl_sanctionBuyList": {
                "$addToSet": {
                    "$cond": {
                        "if": {"$ne": ["$undlProd.sanctionBuyList", null]},
                        "then": "$undlProd.sanctionBuyList",
                        "else": "$unset"
                    }
                }
            },
            "undl_sanctionSellList": {
                "$addToSet": {
                    "$cond": {
                        "if": {"$ne": ["$undlProd.sanctionSellList", null]},
                        "then": "$undlProd.sanctionSellList",
                        "else": "$unset"
                    }
                }
            }
        }
    },
    {
        "$project": {
            "_id": 1,
            "undl_sanctionBuyList": 1,
            "undl_sanctionSellList": 1,
            "sameBuyList": {"$setEquals": ["$sanctionBuyList", "$undl_sanctionBuyList"]},
            "sameSellList": {"$setEquals": ["$sanctionSellList", "$undl_sanctionSellList"]}
        }
    },
    {
        "$match": {
            "$or": [{"sameBuyList": false}, {"sameSellList": false}]
        }
    }
], {
    allowDiskUse: true
});
```

### Reference

https://wpb-confluence.systems.example.com/pages/viewpage.action?pageId=3511116152
