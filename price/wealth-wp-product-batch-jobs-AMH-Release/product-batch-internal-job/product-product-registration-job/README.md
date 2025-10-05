# Product Registration for SMART ID Job

## Job Description

The job check all products for SMART ID, if not exist, will call SMART+ Global API to fetch the SMART ID and then update to the product as a Q code. Only products with valid ISIN code or SEDOL code will be updated.

The job has only 1 step.

## Step 1

### Reader

Fetch product list which

- Has no Q code (SMART ID) yet
- Has valid ISIN code or SEDOL code

### Processor

- Validate ISIN code. (Use commons-validation, ISINValidator)

### Writer

Call SMART+ Global API to fetch SMART ID.

Update Q code of products with SMART ID.

## Configuration
- globalIdUrl: get registration globalId Smart API.
- amTokenUrl: get Smart staff amtoken API.
- host: get Smart staff amtoken API host.
- username: smart account.
 

## Parameters
- isDaltaSync: true --> delta update; false --> fullSet update.
- prodTypeCde: "ALL" --> all products; "UT" --> one prodType.
- groupSize: chunk size.
- ctryRecCde: 
- grpMembrRecCde: 
- prodId: only use for testing. 
- supportAltIdCdes: filter altId codes, e.g "I", "S".


## Reference
https://alm-confluence.systems.example.com/confluence/display/SMART/Product+Registration+Service
https://wpb-confluence.systems.example.com/display/WWS/2022+UK+Product+registry+%28Retrieve+Smart+Global+ID%29+product+mapping

