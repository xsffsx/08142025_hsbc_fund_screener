## Data processing flow

1. Smart Local will register ESG product by Smart Global API or Batch
2. Smart Global team will send the output file to Smart Local
3. Smart Local team will process the output csv file by ESG Batch and store ESG data to "product" collection in Document DB
4. Other Team like SFP will consume ESG data via Smart+ Graphql api

## Detailed Design
Smart Local ESG Batch will process the Smart Global ESG csv file, sample: GLB_dummy_GENERIC_ESG_HBAP-WEALTH_20240809.csv
After process the file, the useful column field will be store in DB

| Item | Field name                               | Data type | Possible value                                                    | Business name           | DB field name     |
|---|------------------------------------------|-----------|-------------------------------------------------------------------|-------------------------|-------------------|
| Column H	  | si_classification                        | String    | "ESG-ENHANCED", "NOT-ETI", "THEMATIC", "IMPACT", NULL(""/"NULL")  | SI Classification	      | siClass           |
| Column P	  | sdg_theme_alignment_rank_natural_capital | Double    | 1.0                                                               | SDG                     | sdgThmRkNatuCapt  |
| Column Q	  | sdg_theme_alignment_rank_governance	     | Double    | 1.0                                                               | SDG                     | sdgThmRkGov       |
| Column N	  | sdg_theme_alignment_rank_empowerment     | Double    | 1.0                                                               | SDG                     | sdgThmRkEmp       |
| Column O	  | sdg_theme_alignment_rank_climate_change	 | Double    | 1.0                                                               | SDG                     | sdgThmRkClimChg   |
| Column M	  | sdg_theme_alignment_rank_basic_needs     | Double    | 1.0                                                               | SDG                     | sdgThmRkBscNeeds  |
| Column J	  | esg_score                                | Double    | 7.56                                                              | ESG Score               | esgScore          |
| Column K	  | esg_rating                               | String    | "A","AA","AAA","B","BB","BBB"                                     | ESG Rating	             | esgRating         |
| Column L	  | carbon_intensity                         | Double    | 23.04                                                             | Carbon Intensity(WACI)	 | carbonIntens      |


How to run this job in local:
```xml
add the following program arguments:
ctryRecCde=HK grpMembrRecCde=HBAP incomingPath=C:\\dummy\\HKHBAP\\ESG\\ filePattern=GLB_dummy_GENERIC_ESG_HBAP-WEALTH_[0-9]*.csv
```

How to run this job in EC2:
```xml
/appvol/product-spring-batch/bin/HKHBAP/cron/wpc_upload_esg.sh HK HBAP
```


