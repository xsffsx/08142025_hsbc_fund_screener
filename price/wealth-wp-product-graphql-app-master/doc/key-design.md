# Key design considerations

Some key design considerations for WPC 2020.

## MongoDB

Use MongoDB for storage, product related information will be grouped in one product document and put into the product collection. In this approach, data query and fetching can be in one go, which has great efficiency.

Price history has too much data and much higher update frequency, consider to put into another collection, drawback of the separation would be complicated the query process.

## Product Metadata

It defines product fields and structure, which is a collection has records like `attributeName`, `attributeType`, `businessName`, `businessDescription`, `validationRules`, etc.

It can be tailored to fit for a given entity's requirements, eliminate those fields they don't need. Add a field is as easy as add one record into the collection.

GraphQL Schema will be generated from the metadata for services.

JSON Schema will be generated for input product validation.

User can select fields from this collection to genreate an excel data upload template for data uploading.

## GraphQL Service

The core service provided in a much more generic approach, thus we can keep it stable.

### Query Services

- productsByFilter: A sophisticate query service, which can support the most complicated query criterias, sorting and pagination. Basically it describe the query criterias in JSON format. Samples below

  Sample 1:
  ```json
  {"prodId": 1234}
  ```

  Sample 2:
  ```json
  {
    "$and": [
      {
        "ctryRecCde": "CN"
      },
      {
        "grpMembrRecCde": "dummy"
      },
      {
        "prodOferChanl": {
          "$ne": "WDS"
        }
      },
      {
        "$or": [
          {
            "prodCde": "1000032676"
          },
          {
            "prodCde": "1000032677"
          },
        ]
      }
    ]
  }
  ```

- productsByNamedQuery: A named query can be created with a JSON Schema and a query template, these information will be store in the DB, user call the service with the query name and variables complied with the JSON Schema. Variables will then be merged with the query template into a real query (like the sample above). This is to lower the bar of consuming the GraphQL service, which is nice to have.

### Mutation Services

The mutation services are the only product data entry points, direct amendment of product data in MongoDB is not allowed.

- validateProducts: Validate products data.
- createProducts: Create products, apply validation before creation.
- updateProducts: Update partial information of products, apply validation before update.

### Product data validation

#### WPS validation reference

A lot of validation rules can be referenced in
```
WPS_External_Config\src\main\resources\local\validators-config.properties
com.dummy.wps.rule.impl.WpsProdExtFieldNotNullRule
com.dummy.wps.rule.impl.WpsCrossFieldValidationRule
com.dummy.wps.productdetail.ProductDetailValidator
com.dummy.wps.productdetail.ProductDetailValidatorForCommon
com.dummy.wps.productdetail.ProductDetailValidatorForProdcuts
```

Different product type in different entity may have different set of mandatory fields.

How to make the validationRules can apply to both backend and frontend validaton?

Each field has a set of validationRules, eg.

- NotNull, Apply to All
- Min(n), Apply to Numeric
- Max(n), Apply to Numeric
- MinLength(n), Apply to String
- MaxLength(n), Apply to String
- Unique(key1, key2 ...), apply to Non Scalar array, value of the combination keys has to be unique
- Past
- PastOrPresent
- Future
- FutureOrPresent
- Pattern
- NotEmpty
- SpEL, eg. for cross field checking, #{(endDay == null && startDay == null) || endDay > startDay}
- For some complicated checking, consider to use embeded javascript.

Each kind of validation rull has default message, but can be overrided by field level setting.

May need to consider add productType level, even entity level rule override.

Validation level

- Application Level

  Employ JSON Schema to do the validation work, and the JSON Schema will be generated from product metadata.

- DB Level

  When create MongoDB product collection, we can specify a validator with `$jsonSchema`, this can apply DB level data validation.

- Cross field validation

  JSON Schema can't handle cross field validation, this must be done in the application level.

- Unique keys in object sub-array

  If sub-array is consist of objects, sometimes we need to make sure one ore more keys be unique.

  This can be done in application level.

  Another approach is to define a unique index in the product collection to ensure this checking.

## Adapter Services

For legacy system support purpose, we need to develop some adapter services. These service will complied with the old interfaces.

Requests will be transform into a GraphQL serice call to the WPC GraphQL Service, response will then be transformed into the old interface format.

The adapters could be as below,

- SOAP Adapter Service
- MQ Adapter Service
- REST Adapter Service

Or any other necessary adapters per requirement.

## WPS Front End

Build with ReactJS with 

- [Matrial UI](https://material-ui.com/) for UI components.
- [Formit](https://jaredpalmer.com/formik/docs/overview) or [Redux Form](https://github.com/redux-form/redux-form) for form validation.
- [Yup](https://github.com/jquense/yup) for object schema validation.

Design the product viewer / editor in a customizable approach, so that a user can view / edit the part of data they care only.

Customizable data upload template: Provide an interface so user can select the fields they want to make change. An excel template will be generated, contains the fields user selected, besides user friendly field name, the real attribute name will be in the template but hidden. User can  then download the excel template, fill in data, and upload.

Manual data upload: through WPS upload interface directly, do not need to call a batch job to do that. (Sync or async?)

## Sytem integration (Batch Jobs)

Use `Spring Cloud Data Flow` for integration (Supports both batch and stream)

- Spring cloud data flow is mature and it has an ecosystem, such as UI interface and command line tools.
- Job can be run in docker or pcf on demand, which is more cloud native, better resource usage, no need to bound to a VM or physical machine.
- In case need to trigger a job through Ctrl-M, we can leverage the REST interface.
- Job is separated, which makes the code clear, easier to understand and maintain.
- Job can be deploy separatedly, won't affect other running jobs.
- Supports streaming data

## Product Versioning

Keep a version field in product documents, create a product with v0, every update will update the version number to v++. The `product` collection stores only the latest version, history version will be kept in `product-history` collection. To avoid `product-history` collection grown too big, we can config to keep the last `n` version or the last `n` days' version, and do housekeeping for the rest.

In case of a product being edited and needed to be approved, store in `product-pending` with the same version number.

## Product data Approval

### Strict mode (current WPC approach)

If the product version in `product` collection has no change, approval can proceed, otherwise user need to review and redo the edit, approval.

### Easy mode

One of the biggest complain from user is before approval, product data has been changed in somewhere. Even it has no conflict with the data edited, they still need to redo the edit and approval.

Comment from business
> The concurrent data update on WPC batch and WPS, so operation may get rejection when approve the data, sometime it may need to retry several times.

In easy mode, we will show user the difference between the pending approval product and the latest product, so user can decide to go or not. If user decide to go, merge data changed with no conflict. In case of there is conflict, the approval can't proceed. 

## Trace Id

Put a trace id into the http header when call graphql service, bring it to downstream for logging and tracing.

If not provided, generate one in the very begining of the graphql service, and pass as above.

## Caller Id

In order to keep track operations especially for mutations, we need to log down who did what in when. Add caller's id in the http header or parameter.

## Product ID Generation

### The old WPC approach

See `com.dummy.hbap.wpc.batch.handler.IntlSysParamTransactionHandler#generateProdId(java.lang.String, java.lang.String)`, there is a sequence for each entity.

```sql
SELECT PROD_ID_SEQ_CN_dummy.nextval FROM DUAL;
```

A segment is assigned to each entity, eg. CNdummy (1-20000000), then when create new product in CNdummy, it will take the `PROD_ID_SEQ_CN_dummy.nextval` from this segment.

Product ID is an internal ID of a product, it is transparent to customer. Customer can use the `P Code - PROD_ALT_PRIM_NUM` to identify a product, which is a combination of `CTRY_REC_CDE, GRP_MEMBR_REC_CDE, PROD_TYPE_CDE, PROD_SUBTP_CDE, PROD_ALT_PRIM_NUM` where `prod_stat_cde <> 'D'`

External vendor exchange files with WPC throug `SFG (Strelling file gateway)`.

### In MongoDB

Generate with a javascript functon and a seuence colleciton.

### About timezone

- Timezone of oracle data is in UTC;
- Migrate program, GraphQL service and Adapter service should run with `-Duser.timezone=UTC` as well.


### Data sync

Option 1: Use trigger

Create trigger on each table that needs to be sync, log insert / update / delete row into  table PROD_WATCH. Build a sync program which will watch change in PROD_WATCH, and then perform delta import for product changed.

Cons would be lead to the data update session last for more time, which will mainly impact in batch jobs.

Option 2 Database Change Notification
- With [Java](https://docs.oracle.com/cd/E11882_01/java.112/e16548/dbchgnf.htm#JJDBC28820)
- With [PL/SQL Stored Procedure](https://docs.oracle.com/cd/B19306_01/B14251_01/adfns_dcn.htm)
- https://docs.oracle.com/cd/B19306_01/appdev.102/b14251/adfns_dcn.htm
- see https://asktom.oracle.com/pls/apex/f?p=100:11:0::::P11_QUESTION_ID:3988488300346170336

Issue:

Due to sequence cache, some events may be missing when handling by sequence ID, better appraoch should be use the TS, make it index field.