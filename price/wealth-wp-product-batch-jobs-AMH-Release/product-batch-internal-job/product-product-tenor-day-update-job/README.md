# product-product-tenor-day-update-job

This is an internal job to update `Product Tenor In Day (termRemainDayCnt)` based on the values of `Product Launch Date (prodLnchDt)` & `Product Maturity Date (prodMturDt)`.

### Command

/appvol/product-spring-batch/bin/HKHBAP/cron/wpc_prod_tenor_day_update.sh HK HBAP ALL

### Implement

* Case 1) If maturity date == null (i.e. evergreen product), Tenor in Days = "blank"
* Case 2) If Maturity Date < current date, Tenor in Days = 0
* Case 3) If launch date == null, Tenor in Days = Maturity Date - current date
* Case 4) If current date >= Launch Date, Tenor in Days = Maturity Date - current date
* Case 5) If current date < Launch Date, Tenors in Days = Maturity Date - Launch Date

### Reference

https://wpb-confluence.systems.example.com/pages/viewpage.action?pageId=2861553172