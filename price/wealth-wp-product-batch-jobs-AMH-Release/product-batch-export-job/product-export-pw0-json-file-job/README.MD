## BackGround

After online soap message is switched from wpc to smart-plus, 
some system will want to load full data of PW0 one.

But the amount of some product type cde is so large, such as WRTS, which has more than 300,000 items in the production environment.
Both soap-adapter and graphql application doesn't support such as large throughput in one HTTP request.
So we need a batch job to split them into serval smaller requests and finally put them into a complete file.

## Detailed Design
We will choose WRTS product as example from now on.
#### PW0 Request Example
```xml
<?xml version="1.0" encoding="UTF-8"?>
<soap:Envelope xmlns:soap="http://www.w3.org/2003/05/soap-envelope">
    <soap:Header/>
    <soap:Body>
        <ns2:enquireJson xmlns:ns2="http://impl.service.webservice.wpc.wmd.dummy.com/">
            <arg0>
                <countryCode>HK</countryCode>
                <groupMemberCode>HBAP</groupMemberCode>
                <target>
                    <criterion xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:type="ns2:junction">
                        <criterionList xsi:type="ns2:equalExpression">
                            <propertyKey>prodTypCde</propertyKey>
                            <value xmlns:xs="http://www.w3.org/2001/XMLSchema" xsi:type="xs:string">WRTS</value>
                        </criterionList>
                        <criterionList xsi:type="ns2:inExpression">
                            <propertyKey>prodStatCde</propertyKey>
                            <valueList xmlns:xs="http://www.w3.org/2001/XMLSchema" xsi:type="xs:string">S</valueList>
                        </criterionList>
                        <operator>AND</operator>
                    </criterion>
                    <targetName>blackRockBatch</targetName>
                </target>
            </arg0>
        </ns2:enquireJson>
    </soap:Body>
</soap:Envelope>
```
That means it will query product that prodTypeCde equals WRTS and prodStatCde equals S.

#### Data Distribution
Now we need to find a field that can divide WRTS products into serval batches of relatively average quantity and size.
Generally speaking, we will choose the field prodStatCde as grouping field. Let us see the WRTS product distribution in the production environment.

| prodStatCde | count  |
|-------------|--------|
| D      | 338472 |
| A   | 9435   |
| S   | 151    |

As you see, the distribution is very uneven if we choose prodStatCde.

If we choose prcEffDt as grouping field. (As of 2024.05.20)

| prcEffDt   | count  |
|------------|--------|
| 2024-05-20 | 8976   |
| 2021-07-27 | 900    |
| 2021-02-26 | 845    |
| NULL       | 689    |
| ......     | ...... |
| 2016-10-19 | 1      |
| 2016-10-28 | 1      |

We can see the distribution is more evenly distributed.

#### Solution
1. Fine the minimum prcEffDt and maximum prcEffDt of the WRTS product.
2. Start with the minimum value and use half a year as a batch.

For example, if the minimum prcEffDt is 1997-10-03 and the minimum prcEffDt is 2024-05-20.

So we need to converted it these batch request:

batch 1:
```xml
<criterionList xsi:type="ns2:inExpression">
    <propertyKey>prcEffDt</propertyKey>
    <valueList xmlns:xs="http://www.w3.org/2001/XMLSchema" xsi:type="xs:string">1997-10-03</valueList>
    <valueList xmlns:xs="http://www.w3.org/2001/XMLSchema" xsi:type="xs:string">1997-10-04</valueList>
    <valueList xmlns:xs="http://www.w3.org/2001/XMLSchema" xsi:type="xs:string">......</valueList>
    <valueList xmlns:xs="http://www.w3.org/2001/XMLSchema" xsi:type="xs:string">1998-04-03</valueList>
</criterionList>
```

batch 2:
```xml
<criterionList xsi:type="ns2:inExpression">
    <propertyKey>prcEffDt</propertyKey>
    <valueList xmlns:xs="http://www.w3.org/2001/XMLSchema" xsi:type="xs:string">1998-04-04</valueList>
    <valueList xmlns:xs="http://www.w3.org/2001/XMLSchema" xsi:type="xs:string">1998-04-05</valueList>
    <valueList xmlns:xs="http://www.w3.org/2001/XMLSchema" xsi:type="xs:string">......</valueList>
    <valueList xmlns:xs="http://www.w3.org/2001/XMLSchema" xsi:type="xs:string">1998-10-03</valueList>
</criterionList>
```
......

last batch:
```xml
<criterionList xsi:type="ns2:inExpression">
    <propertyKey>prcEffDt</propertyKey>
    <valueList xmlns:xs="http://www.w3.org/2001/XMLSchema" xsi:type="xs:string">......</valueList>
    <valueList xmlns:xs="http://www.w3.org/2001/XMLSchema" xsi:type="xs:string">2024-05-20</valueList>
</criterionList>
```

How to run this job in local:
```xml
add the following program arguments:
ctryRecCde=HK grpMembrRecCde=HBAP outputPath=C:\\dummy\\HKHBAP\\PW0\\ prodTypCde=SEC targetName=apexaProduct
```

How to run this job in EC2:
```xml
/appvol/product-spring-batch/bin/HKHBAP/cron/wpc_export_pw0_json_file_job.sh HK HBAP WRTS apexaProduct
```
Solution Design:
https://wpb-confluence.systems.uk.dummy/display/WWS/Generate+Apexa+batch+file+for+initial+upload+solution+design+document

