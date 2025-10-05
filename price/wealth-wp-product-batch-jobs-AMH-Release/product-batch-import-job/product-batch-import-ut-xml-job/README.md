# UT Product Master file for SMART ID Job

[confluence page](https://wpb-confluence.systems.uk.dummy/display/WWS/Process+UT+Product+Master+file)

## Job Description

This job is used to process the xml file whose product type is ut, parse the xml file into a product object and save it to mongodb by calling graphql.

## Step 

### Reader
### UtPerfmXmlFileReader
1. Scan the path to obtain files whose file names meet the pattern. eg: **HK_HBAP_AMHCUTAS_UT_20221226081122.xml**
2. Convert the contents of the file to a list of UnitTrustInstrument.class.
3. Find the original product by using the product key of the UnitTrustInstrument as the condition for querying the product.

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

### Processor

#### 1. UtTrstInstmFormatProcessor

If the action code is UPDATE:

Read yaml configuration file: *system-update-config.yaml*, Optionally update fields in the xml file

If the action code is ADD:

Read the configuration file: *system-default-values.yaml*. Set a default value for a field with no value

#### 2. ProdFormatProcessor

Enter some common product format processing logic

#### 3. SystemFormatProcessor

Format these fields: *allowBuyProdInd*, *allowSellProdInd*, *allowSellMipProdInd*, *allowSwInProdInd*, *allowSwOutProdInd*

### Writer

Call the api of graphql(productBatchCreate, productBatchUpdate) to save the updated product to mongodb

### How to run
#### 1. Please check whether the graphql url configured in the yaml file, the postgresql configuration, and the scanned file directory are correct

#### 2.Start ImportUtXmlJobApplication, three parameters need to be passed in: ctryRecCde, grpMembrRecCde, systemCode, incomingPath(optional)
eg: ctryRecCde=HK grpMembrRecCde=HBAP systemCode=AMHCUTAS incomingPath=C:\Users\45244712\IdeaProjects\wealth-wp-product-batch-jobs\test