# product-product-status-update-job

This is an internal job to update product status & trading indicator.

## Command

> /appvol/product-spring-batch/bin/HKHBAP/cron/wpc_prod_status_update.sh HK HBAP "BOND,DPS,ELI,SN,UT"

## Product match criteria

### 1. BOND, SEC, WRTS, UT

Maturity Date (prodMturDt) < Current Date & Product Status in A, S, C

### 2. DPS

Launch Date (prodLnchDt) < Current Date & Product Status in A, S, C

### 3. SID

Market End Date (strucInvstDepst.mktEndDt) < Current Date & Product Status in A, S, C

### 4. ELI

#### 4.1 Product Sub Type (prodSubtpCde) like 'ELI_DCDC%' & Product launch status (eqtyLinkInvst.lnchProdInd) = 'Y'

Maturity Date (prodMturDt) < Current Date & Product Status in A, S, C

#### 4.2 Product Sub Type (prodSubtpCde) like 'ELI_DCDC%' & Product launch status (eqtyLinkInvst.lnchProdInd) = 'N'

Trade Date (eqtyLinkInvst.trdDt) < Current Date & Product Status in A, S, C

#### 4.3 Product Sub Type (prodSubtpCde) not like 'ELI_DCDC%'

Settlement Date (eqtyLinkInvst.setlDt) < Current Date & Product Status in A, S, C

### 5. Others

Not included in this job

## Effected fields:

* prodStatCde -> E
* allowBuyProdInd -> N
* allowSellProdInd -> N
* allowSwInProdInd -> N
* allowSwOutProdInd -> N
* allowSellMipProdInd -> N

## Reference

https://wpb-confluence.systems.example.com/display/WWS/3.+Product+Status+Code+Update
