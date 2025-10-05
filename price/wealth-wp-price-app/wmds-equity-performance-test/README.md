# wmds-equity-performance-test
> this module is for quick run performance testing
>

## How to run:
#### 1.Run with jenkins pipeline
```
https://jenkins-custom-glwm01.digital-tools.euw1.prod.aws.cloud.hhhh/job/WEALTH-PLATFORM/job/Portfolio%20Management/job/build-wealth-wp-price-performance-test/
```

![get-report-parameter](images/build-performance-test.png)

#### 2.Get test report with jenkins pipeline

Build [pipeline](https://jenkins-custom-glwm01.digital-tools.euw1.prod.aws.cloud.hhhh/job/WEALTH-PLATFORM/job/Portfolio%20Management/job/build-wealth-wp-price-performance-test/) with parameter `ONLY_DOWNLOAD_REPORT=true`


![get-report-parameter](images/get-report-parameter.png)

After the pipeline done, you will be able to find report at `Build Artifacts`

![build-artifacts](images/build-artifacts.png)

|  Artifact                                     | Description                   |
|  -------------------------------------------  | ----------------------------  |
| aws_metric_pod_cpu_utilization.png            | CPU usage during testing      |
| aws_metric_pod_memory_utilization.png         | Memory usage during testing   |
| lastPerformanceTestDetail.json                | Test information detail       |
| report-us-sit-soak-test-20220919-102126.zip   | JMeter test result report     |
| test_plan_us.jmx                              | JMeter test JMX script        |


#### 3.Get test report with AWS command line

You can find the command line similar to the following via the end of pipeline console output
```
aws s3 cp s3://wealth-dev-wphasecodestaging/hkhasemds/performance-test/reports/report-us-sit-soak-test-xxxx-xxxx.zip . 
```

## How does it work:
- 1.Pipeline combine the parameters and test template into JMX script, and upload it to S3 bucket
- 2.Pipeline invoke lambda `lambda-ssm-exec` to send shell script `run-price-performance-test.sh` to EC2
- 3.On EC2, the shell script will download the JMX and run it in JMeter
- 4.After JMeter run ends, report will be package and upload to S3 bucket

![build-artifacts](images/performance-test-flow.png)


## How to do development:
#### 1.Change the request parameters
Request parameters can be adjusted by modifying the `test_param_{market}.json` files under the `wmds-equity-performance-test/jmeter/data` folder 

#### 2.Change the test TPS
TPS can be adjusted by modifying the `test_tps_{market}.json` files under the `wmds-equity-performance-test/jmeter/data` folder

#### 3.Change the JMeter base test plan
Test plan can be adjusted by modifying the `test_plan_{market}.json` files under the `wmds-equity-performance-test/jmeter/template` folder

#### 4.Add specified JMX script to test
Except the template mode, we can use specified JMX under the `wmds-equity-performance-test/jmeter/testplan` for testing

