# Import Investor CharacterInd job
## Job Description
This job contains five different steps.

### 1. INIT STEP

#### Run in shell script
wpc_upload_investor_character_ind.sh HK HBAP I
#### Run in java class
ImportInvestorCharacterIndApplication 

ctryRecCde=${ctryRecCde} grpMembrRecCde=${grpMembrRecCde} incomingPath=${incomingPath} batchCode=I

#### Step process
##### Reader
Read the file whose file name is in "^RS@SMIP.IC[0-9]{6}$" format and convert to InvestorCharacter.class.

##### Processor
Filter out records that do not correspond to product in mongodb. 

##### Writer
Update the prodDervtCde, prodDervtRvsCde and prdDervRvsEffDt read from the file to mongodb.

### 2. COMM STEP

#### Run in shell script
wpc_upload_investor_character_ind.sh HK HBAP C

#### Run in java class
ImportInvestorCharacterIndApplication

ctryRecCde=${ctryRecCde} grpMembrRecCde=${grpMembrRecCde} incomingPath=${incomingPath} batchCode=C

#### Step process
##### Reader 
Read the file whose file name is in "^RS@SDIP.IC[0-9]{6}$" format and convert to InvestorCharacter.class.

##### Processor
1. Filter out records with action code equals to R or M.
2. Filter out records that do not correspond to product in mongodb. Then inset a new record to prod_dervt_handl_reqmt.

##### Writer
Update prodDervtCde, prodDervtRvsCde and prdDervRvsEffDt according to different action code.

### 3. EX_RETRY STEP

#### Run in shell script
wpc_upload_investor_character_ind.sh HK HBAP E

#### Run in java class
ImportInvestorCharacterIndApplication

ctryRecCde=${ctryRecCde} grpMembrRecCde=${grpMembrRecCde} incomingPath=${incomingPath} batchCode=E

#### Step process
##### Reader 
Read the records from prod_dervt_handl_reqmt.

##### Processor
Filter out records that do not correspond to product in mongodb.

##### Writer
Update the prodDervtCde, prodDervtRvsCde and prdDervRvsEffDt read from the file to mongodb.Then remove the prod_dervt_handl_reqmt record.

### 4. MONTHLY_RECONCILIATION STEP

#### Run in shell script
wpc_upload_investor_character_ind.sh HK HBAP R

#### Run in java class
ImportInvestorCharacterIndApplication

ctryRecCde=${ctryRecCde} grpMembrRecCde=${grpMembrRecCde} incomingPath=${incomingPath} batchCode=R

#### Step process
##### Reader
Read the file whose file name is in "^RS@SMIP.IC[0-9]{6}$" format and convert to InvestorCharacter.class.

##### Processor
Filter out records whose ic flag(combine with prodDervtCde, prodDervtRvsCde, prdDervRvsEffDt) are the same.

##### Writer
Update the prodDervtCde, prodDervtRvsCde and prdDervRvsEffDt read from the file to mongodb.

### 5. DAILY_UPDATE  STEP

#### Run in shell script
wpc_upload_investor_character_ind.sh HK HBAP D

#### Run in java class
ImportInvestorCharacterIndApplication

ctryRecCde=${ctryRecCde} grpMembrRecCde=${grpMembrRecCde} incomingPath=${incomingPath} batchCode=D

##### Reader
Filter product from mongodb with filter:
````json
{
  "ctryRecCde": "${ctryRecCde}",
  "grpMembrRecCde": "${grpMembrRecCde}",
  "prodStatCde": {
    "$ne": "D"
  },
  "prdDervRvsEffDt": "${currentDate}"
}
````
##### Processor
Filter out products where prodDervtCde and prodDervtRvsCde are the same.

##### Writer
Update the product prodDervtCde to the value of prodDervtRvsCde.
