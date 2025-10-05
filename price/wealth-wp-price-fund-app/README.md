# wealth-wp-price-app

|Application Name|Application EIM #|EIM Short Name|
|:----|:---|:---|
|Market Data Service|1178047|MDS|

PROJECT NOT YET MIGRATE TO THIS GITHUB REPO

Please refer to RTC stream `WIP.MKD-SRBP3.Core`, for access right, please contact Soda C X FANG.

Target to migrate to GitHub at Q4 2019

## Build and Deploy
MDS has mainly 2 parts.
- Batch Jobs
- Online Services

### Batch Job
Run maven build
```bash
mvn -f wmds-buildaggregate-batch-ide\pom.xml clean install -Dmaven.test.skip=true
```
Get `wmds-batch-war\target\wmds-batch-war.war` for batch jobs deployment.

```bash
mvn -f wmds-batch-config\pom.xml clean install -P SIT_AMH_UT
```
Reference `wmds-batch-config\target\*-batch_config.zip` for external config.

Reference `wmds-batch-config\target\*-batch_scripts.zip` for shell scripts.

### Online Services
Run maven build
```bash
mvn -f wmds-buildaggregate-online-ide\pom.xml clean install -Dmaven.test.skip=true
```
Get `wmds-online-ear\target\wmds-online-ear.ear` for online services deployment.

```bash
mvn -f wmds-online-config\pom.xml clean install -P SIT_AMH_UT
```
Reference `wmds-online-config\target\wmds-online-config*.zip` for external config.
