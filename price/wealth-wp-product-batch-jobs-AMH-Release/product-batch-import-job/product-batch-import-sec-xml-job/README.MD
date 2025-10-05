# Product SEC XML file for SMART ID Job
Read the xml file and convert it into product, and update or add it to the product collection

### Job Parameters:
ctryRecCde

grpMembrRecCde

systemCde

incomingPath: The path where the xml file is stored

### Application yaml file:
#### system-update-config
Config product fields and product price history fields that can be updated by each system.
#### system-default-values
The default value of the field when creating a new product.

### SecXmlFileReader
1. Scan the path to obtain files whose file names meet the pattern. eg: **HK_HBAP_AMHGSOPS.AS_SEC~AA_20230411094503.xml**
2. Convert the contents of the file to a list of StockInstrument.class.
3. Find the original product by using the product key of the StockInstrument as the condition for querying the product.

## SecXmlFileProcessor
If action code is ADD, then read system-default-values and assign the initial value to the field(if the field is empty).

If action code is UPDATE, read system-update-config update only fields that are allowed to be updated.

### Writer

Call the api of graphql(productBatchCreate, productBatchUpdate) to save the updated product to mongodb.

### How to run
#### 1. Please check whether the graphql url configured in the yaml file, the postgresql configuration, and the scanned file directory are correct

#### 2.Start ImportSecXmlJobApplication, three parameters need to be passed in: ctryRecCde, grpMembrRecCde, systemCode, incomingPath(optional)
eg:
systemCde=AMHGSOPS.AS

grpMembrRecCde=HBAP

ctryRecCde=HK

incomingPath=C:\Users\45244712\IdeaProjects\wealth-wp-product-batch-jobs\test