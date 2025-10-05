# BOND update from Thomson Reuters

## Job Description

This job will get BOND info from Thomson Reuters API and update to DB


## Step 

### Reader

Get Bond data from Thomson Reuters API

- Get request token
- Get schedule ID
- Get report extraction ID
- Download TR report - Write raw data to CSV (keep raw data for support)


### Processor

- Convert data to ProductStreamItem
- Validate data
- Format data

### Writer

- Update product data
- Update reference data

## Configuration
- TR column info: batch-import-bond-csv-column-info.yaml
 

## Parameters
ctryRecCde=HK grpMembrRecCde=HBAP prodTypeCde=BOND systemCde=REUTERSBOND


## Reference
