# WPC Concerns and Solutions

## Business

## Technical

### DB

- Using RDBMS Oracle DB for data storage. Which makes it hard to add extention fields for different markets.

- Due to the nature of RDBMS, it's pretty hard to make the service program run in good efficiency. Take product data for example, it related to over 40 tables, join all those tables to provide flexible product attributes fetching on demand is a nightmare. This lead to the current PW2 message implementation oftern suffered performance issue.

### Service

- We have MQ service, SOAP web service, REST web service, and the new GraphQL service at the moment, which makes it much more complicated for development and deployment, as well as downstream consumption.

- The service implementation are from the aged fix length messages, even SOAP web service is based on the old code base. This makes it hard for maintain and development. Another issue is lower runtime efficiency.

### Batch

- Batch jobs implemented in java main approach, heavily rely on OS specific shell script to trigger, hard to monitor and manage.

### WPS

- The frontend WPS is using very old technology, like JHX, ES framework, which is out of maintain and hard to do troubleshooting and new deverlopment.

- The  Performance on WPS is not good , it takes a bit long time for loading pages;

- There are a lot of fields in WPS product page, can we group the fields more clearly, to make it easy to identify the field?

- WPS upload excel function, need to wait a bit long time to check the process result via activity log;

- There are some simple update on product, whether WPC can provide full batch update? Like DPS, the minimum deposit amount may change in a period for all products, it cost time to change one by one;

- Can provide a function to download WPC product so that we don’t need to request production team every time?


### Other

- All components are using the old SpringFramework 2.5, which is out of support already. This makes it impossible to move to modern development with SpringBoot and leverage new technology like Spring Cloud.

- The concurrent data update on WPC batch and WPS, so operation may get rejection when approve the data, sometime it may need to retry several times;

- Overly reliant on user inputs for data (performance, yields, AUM etc).
  > If input data can be provided from any other source, just need to call the GraphQL mutation service to feed in the data.

- Seems too complex to make small changes and any minor error can lead to a cascade of issues.
  > With metadata definition, a GraphQL Schema will be generated automatically, queries will apply to all the fields defined in the schema. This will reduce much effort to introduce new product fields.

- Too complex to add in automated feeds – needing specialists teams additional costs etc.
  > All product add / update to te DB will be through the GraphQL mutation service. This will apply the same validation check. So any automated feeds just need to call the mutation service to make it, don't even need to involve the core development team.

- Each country/area needs to maintain their own 
  > Current deployment model is by entity. Need to explore the possibility of operating a WPC service for multi entity.

- No clear SME/Manual/process manuals etc. – risk for the business for any development as we know.
  > With GraphQL, simple description of each field is provided in interaction way. Need to build up confluence page to well document the manuals

- No method to test/check any upload without going into a ‘real’ case
  > Provide `validate only` option in upload?

- No automated validation of uploads – eg if a field(s) is missing it should flag the error 
  > Provide `validate only` option in upload?

- It’s just too complex, not user friendly and, frankly, I don’t think fit for purpose given its importance within the overall Wealth environment.

- Missing logs for some uploads(for example Alt ID upload).
  > Log upload / operations into the DB, and provide query interface.

- Validation inconsistency – User can load products with blanks in specific columns, as they are not validated during the upload, but when trying to modify same product via WPS afterwards, there are validation/missing data errors when trying to save.
  > Provide single validation point for any mutation.

### Business Area



- A lot of fields that are not in use/irrelevant but can’t be hidden/disabled but are required during uploads, which causes confusion as users are trying to understand their meaning when they should not be applicable. As a result the size of upload files is overwhelming for users.
  > Product metadata defines the product data structure, it can be customized. Introduce customizable upload template, which contains user selected fields only.

- Logs are giving rejection reasons one-by one. If the upload file has multiple errors, then after 1st load, we see only one of them, after 2nd the next one etc. Especially during early stages, before users are really familiar with the files it takes huge amount of time to prepare correct upload, as the delay between the uploads and logs being available/next upload attempt is significant.
  > Product data validation is per product, each product will be have a corresponding validation status and message

- Add/Change/Partial Change Product Uploads sharing same functionality and templates. Only action code in upload file determines upload type. This increases a risk of human error.
  > Customizable upload template can address the issue

- WPS users not able to check what caused the record update. User can just guess if this was an automated job, upload, M* update or another user doing the changes.
  > Any operation on the product data will be logged in the DB. Introduce product data versionning, product collection holds the latest version, history version will be moved to product-history collection. 