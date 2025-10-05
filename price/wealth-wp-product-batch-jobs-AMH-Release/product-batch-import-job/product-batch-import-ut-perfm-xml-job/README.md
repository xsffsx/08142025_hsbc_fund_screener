# UT Product Performance file for SMART ID Job

## Job Description

This job is used to process the xml file whose product type is ut, parse the performance xml file into a product object and save it to mongodb by calling graphql.

### Parameters:
ctryRecCde

grpMembrRecCde

systemCde

incomingPath: The path where the xml file is stored

## Step

### UtPerfmXmlFileReader
1. Scan the path to obtain files whose file names meet the pattern. eg: **HK_HBAP_AMHCUTAS_Performance-UT_20221226081122**
2. Convert the contents of the file to a list of ProductPerformance.class.
3. Find the original product by using the product key of the ProductPerformance as the condition for querying the product.

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
### UtPerfmXmlProcessor
Execution will be skipped if any of the conditions are met:
1. The original product cannot be found according to the product key.
2. The content in the prodPerfmSeg tag of the xml file is empty.
3. The content in the prodPerfmSeg tag of the xml file lacks key information(all of the following conditions are met): 

    a. perfmYrToDtPct is empty.

    b. perfm1moPct, perfm3moPct, perfm6moPct, perfm1yrPct, perfm3yrPct, perfm5yrPct, perfmExt1YrPct, perfmSinceLnchPct, rtrnVoltl1YrPct, 

    rtrnVoltlExt1YrPct, rtrnVoltl3YrPct, rtrnVoltlExt3YrPct, perfm6moAmt, perfmYrToDtAmt, perfm1yrAmt, perfm3yrAmt are all empty.

Execute if skip condition is not met:
1. Read the system-config content in the application.yaml file to obtain the fields that the system code allows to update.
2. Find the prodPerfmSeg whose perfmTypeCde is equal to p, convert it into a document and set it into the **performance** field of the product.
3. Find the prodPerfmSeg whose perfmTypeCde is equal to b, convert it into a document and set it into the **benchmark** field of the product.
4. Filter out the fields that are not allowed to be updated for the **performance** and **benchmark** fields.

### Writer

Call the api of graphql(productBatchCreate, productBatchUpdate) to save the updated product to mongodb
### How to run
#### 1. Please check whether the graphql url configured in the yaml file, the postgresql configuration, and the scanned file directory are correct

#### 2.Start ImportUtPerfmXmlJobApplication, three parameters need to be passed in: ctryRecCde, grpMembrRecCde, systemCode, incomingPath(optional)
eg: 

systemCde=AMHCUTAS

grpMembrRecCde=HBAP

ctryRecCde=HK

incomingPath=C:\Users\45244712\IdeaProjects\wealth-wp-product-batch-jobs\product-batch-import-job\product-batch-import-ut-perfm-xml-job\src\test\resources\test\
