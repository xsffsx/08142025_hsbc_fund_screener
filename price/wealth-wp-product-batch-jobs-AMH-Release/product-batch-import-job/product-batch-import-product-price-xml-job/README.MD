# Product Price History file for SMART ID Job

[confluence page](https://wpb-confluence.systems.uk.dummy/display/WWS/Process+Product+price+history+xml+files)

## Job Description

This job is used to process the product price xml file whose product type is ut, and save it to mongodb by calling graphql.

### File format:
This files contains two parts:
1. **prodKeySeg**: To position a product.
2. **prodPrcSeg**: It is a list to describe the product price..

example file:
`````xml
<?xml version="1.0" encoding="UTF-8"?>
<prodPrcLst>
    <prodPrc>
        <prodKeySeg>
            <ctryRecCde>HK</ctryRecCde>
            <grpMembrRecCde>HBAP</grpMembrRecCde>
            <prodTypeCde>UT</prodTypeCde>
            <prodCde>U62448</prodCde>
        </prodKeySeg>
        <prodPrcSeg>
            <prcEffDt>2018-10-22</prcEffDt>
            <pdcyPrcCde>D</pdcyPrcCde>
            <prcInpDt>2022-11-10</prcInpDt>
            <ccyProdMktPrcCde>HKD</ccyProdMktPrcCde>
            <prodBidPrcAmt>2.453</prodBidPrcAmt>
            <prodOffrPrcAmt>2.453</prodOffrPrcAmt>
            <prodNavPrcAmt>2.453</prodNavPrcAmt>
            <prodMktPrcAmt>2.453</prodMktPrcAmt>
            <recDtTmSeg>
                <recCreatDtTm>2022-12-26 08:11:22</recCreatDtTm>
                <recUpdtDtTm>2022-12-26 08:11:22</recUpdtDtTm>
                <timeZone>GMT+8</timeZone>
            </recDtTmSeg>
        </prodPrcSeg>
        <prodPrcSeg>
            <prcEffDt>2018-10-23</prcEffDt>
            <pdcyPrcCde>D</pdcyPrcCde>
            <prcInpDt>2022-11-10</prcInpDt>
            <ccyProdMktPrcCde>HKD</ccyProdMktPrcCde>
            <prodBidPrcAmt>3.453</prodBidPrcAmt>
            <prodOffrPrcAmt>3.453</prodOffrPrcAmt>
            <prodNavPrcAmt>3.453</prodNavPrcAmt>
            <prodMktPrcAmt>3.453</prodMktPrcAmt>
            <recDtTmSeg>
                <recCreatDtTm>2022-12-26 08:11:22</recCreatDtTm>
                <recUpdtDtTm>2022-12-26 08:11:22</recUpdtDtTm>
                <timeZone>GMT+8</timeZone>
            </recDtTmSeg>
        </prodPrcSeg>
    </prodPrc>
    <prodPrc>
        <prodKeySeg>
            <ctryRecCde>HK</ctryRecCde>
            <grpMembrRecCde>HBAP</grpMembrRecCde>
            <prodTypeCde>UT</prodTypeCde>
            <prodCde>U62447</prodCde>
        </prodKeySeg>
        <prodPrcSeg>
            <prcEffDt>2019-10-23</prcEffDt>
            <pdcyPrcCde>D</pdcyPrcCde>
            <prcInpDt>2022-12-10</prcInpDt>
            <ccyProdMktPrcCde>HKD</ccyProdMktPrcCde>
            <prodBidPrcAmt>12.453</prodBidPrcAmt>
            <prodOffrPrcAmt>12.453</prodOffrPrcAmt>
            <prodNavPrcAmt>12.453</prodNavPrcAmt>
            <prodMktPrcAmt>12.453</prodMktPrcAmt>
            <recDtTmSeg>
                <recCreatDtTm>2022-12-26 08:11:22</recCreatDtTm>
                <recUpdtDtTm>2022-12-26 08:11:22</recUpdtDtTm>
                <timeZone>GMT+8</timeZone>
            </recDtTmSeg>
        </prodPrcSeg>
    </prodPrc>
</prodPrcLst>
`````

### Job Parameters:
ctryRecCde

grpMembrRecCde

systemCde

incomingPath: The path where the xml file is stored

### Application yaml file:
#### system-update-config
Config product fields and product price history fields that can be updated by each system.

## Step

### UtProdPriceXmlFileReader
1. Scan the path to obtain files whose file names meet the pattern. eg: **HK_HBAP_AMHCUTASPH_Price-UT_20221226081122.xml**
2. Convert the contents of the file to a list of ProductPrice.class.
3. Find the original product by using the product key of the ProductPrice as the condition for querying the product.

eg:
`````xml
<prodKeySeg>
<ctryRecCde>HK</ctryRecCde>
<grpMembrRecCde>HBAP</grpMembrRecCde>
<prodTypeCde>UT</prodTypeCde>
<prodCde>U62448</prodCde>
<prodCdeAltClassCde>P</prodCdeAltClassCde>
<ctryProdTradeCde>HK</ctryProdTradeCde>
<ccyProdCde>HKD</ccyProdCde>
</prodKeySeg>
`````

will be converted into mongo query conditions:
`````js       
db.getCollection('product').find({
    'altId.prodAltNum': 'U62448',
    'altId.prodTypeCde': 'UT',
    'altId.prodCdeAltClassCde': 'P',
    'prodStatCde': {$ne: 'D'}
});
`````
## Processor
### 1.UtProdPriceValidator
There are two solution that will not be handled by next processor:
1. Can not find original product in db by product key.
2. All the prcEffDt of prodPrcSeg is empty.

### 2.PriceHistoryProcessor
Find the prodPrcSeg with the max prcEffDt from the prodPrcSeg list. And then will update two parts:
#### product:
The product also has prcEffDt and amount field. There are two solution:
a. If product prcEffDt > max prodPrcSeg prcEffDt, then won't do anything.
b. If product prcEffDt <= max prodPrcSeg prcEffDt, then update the prcEffDt, prcInpDt, prodBidPrcAmt, prodOffrPrcAmt, prodNavPrcAmt, prodMktPrcAmt of product from max prodPrcSeg.

#### prod_prc_hist:
Take out all the prodPrcSeg in the list and Find the price history record one by one by prodId(from prodKeySeg), prcEffDt and pdcyPrcCde. There are two solution:
a. If found, then update the prcInpDt, prodBidPrcAmt, prodOffrPrcAmt, prodNavPrcAmt, prodMktPrcAmt field.
b. If not found, then inset a new record.

### 3.PrcCmparProcessor
Calculate the range of change of the product prodBidPrcAmt, prodOffrPrcAmt, prodNavPrcAmt and prodMktPrcAmt updated in the PriceHistoryProcessor, and save it prcCmpar of product.

### Writer

Call the api of graphql(productBatchCreate, productBatchUpdate,productPriceHistoryBatchImport) to save the updated product and prod_prc_hist to mongodb

### How to run
#### 1. Please check whether the graphql url configured in the yaml file, the postgresql configuration, and the scanned file directory are correct

#### 2.Start ImportProductPriceXmlJobApplication, three parameters need to be passed in: ctryRecCde, grpMembrRecCde, systemCode, incomingPath(optional)
eg:
systemCde=AMHGSOPS.AS

grpMembrRecCde=HBAP

ctryRecCde=HK

incomingPath=C:\Users\45244712\IdeaProjects\wealth-wp-product-batch-jobs\test