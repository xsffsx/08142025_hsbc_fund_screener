# Product data validation

## Target
- Provide a single validation point.
- Enable `validate only` for pre data upload checking
- Response with per product validation result

## Study old WPC validation

### Validation in WPS

WPS Existing Validation Rules against product detail UI input. Deeply coupled with UI. All base on string input.

- `WpsCrossFieldValidationRule` This rule is for comparing two fields or one field and one static value provided, supports date, string and decimal
- `WpsDateFieldValidationRule` WPS date field validation
- `WpsDecimalValidationRule` for decimal total length and decimal length validation.
- `WpsDpsCurrencyRule` Special rule implementation for DPS currency validation as current validation framework cannot handle these currency fields at field level. Kind of business logic, little complicated.
- `WpsEffectiveDateValidationRule` Common rule implementation for different effective date validation. check date format, and range from (-100 years, now)
- `WpsEliUnderlyingStockRule` Special rule implementation for ELI underlying stock validation. Looks pretty complicated.
- `WpsExpressionRule` Expression rule implementation with screen error message handling for WPS. Looks for a general expression like `(wpsProd.productDetailCommonBean.allowBuyUnitInd=Y||wpsProd.productDetailCommonBean.allowBuyAmtInd=Y)`
- `WpsFieldsTogetherValidationRule` for comparing two fields or one field and one static value provided. 1 usage only. Looks just to check the avalability of some fields.
- `WpsIntegerMaxValueRule` for checking whether the input integer value is over the integer
- `WpsManageSolutionIndicatorRule` The special rule implementation for manage solution indicator field groups validation. 1 usage only. 
- `WpsMaturityDateValidationRule` WPS maturity date field validation. Just to check materity day can not exceed a given value, which is 31122399. 2 usage only.
- `WpsMultipleNumberRule` to check whether the input number is multiple of the base number. 2 usage only. 
- `WpsProdAltCodeValidationRule` Special rule implementation for product alternative codes validation. 1 usage only.
- `WpsProdArrayFieldUniqueRule` The rule implementation for checking whether the array properties are unique. 2 usage only.
- `WpsProdAssetGeoAllocationRule` Special rule implementation for GEO allocation validation. 1 usage only
- `WpsProdAssetSicAllocationRule` Special rule for SIC allocation validation. 1 usage only
- `WpsProdAssetVolatilityClassRule` 1 usage only
- `WpsProdExtComboFieldUniqueRule` Special rule for checking whether the combo extend field values are unique. 1 usage only.
- `WpsProdExtFieldNotNullRule` [ignore] no usage.
- `WpsProdFieldLengthValidationRule` The rule implementation for checking field length (max).
- `WpsProdFieldNotNullRule` Check if the field is blank.
- `WpsTimeFieldValidationRule` [ignore] Check if a valid time input.

### Validation in Batch Job


## Validation in different level

### Field level
- Data type
- Mandatory / Optional (Nullable)
- Data range / Length limitation 

### Product level
- Cross field checking, like if field A exists, then field B should be exists as well
- Sub-document checking, especially in array of sub-document, how to apply the table restriction previously applied in RDBMS table, like a field or a combination of some fields need to be unique in the table.
- FK checking, some attributes are referencing data in another collection, like a `code` which is referencing `reference data`

### Cross product level
- A field or a combination of fields has to be unique in the collection, like `prodId`

## Solutions

Explore solutions below

### Validate by GraphQL

- Data type
- Required

### Validate against Json Schema

Supports only 6 types: object, array, string, number, bolean, null

Can be used in both server / client sides.

- Data type
- Required
- Regex match

Can't validate uniqueness of an object array by key combination.

Can't validate min or max date, datetime

### MongoDB document validation

### Drools

Can used to perform product level validation, like 

- cross fields validation, 
- uniqueness of array items (by key combination).

## WPS Study

com.dummy.wps.resource.dao.ProdDetailEnqDAO#retrieveProdIdWithDelRecord
