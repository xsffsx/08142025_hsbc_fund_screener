@multi-predictive-search
Feature: Product Search

  Scenario Outline: Customer should query multi predictive search successfully with single keyword and single assetClass
    Given Customer has logged in
    When The following multi predictive search is query in <market> market:
      | Criteria key   | Criteria value   |
      | <criteria_key> | <criteria_value> |
    And multi predictive search is query by keyword:
      | <key_word> |
    And multi predictive search is query by assetClass:
      | <asset_classes> |
    Then Customer should receive the following multi predictive search detail:
      | Product type   | Country tradable code   | Exchange   |
      | <product_type> | <country_tradable_code> | <exchange> |
    Scenarios:
      | market | criteria_key        | criteria_value | key_word | asset_classes | product_type | country_tradable_code | exchange |
      | CN     | countryTradableCode | HK:CN          | 600000   | SEC           | SEC          | CN                    | SHAS     |
      | CN     | countryTradableCode | HK:CN          | 600000   | WRTS          | WRTS         | CN                    | SHAS     |
      | HK     | countryTradableCode | HK:CN          | 00005    | SEC           | SEC          | HK                    | SEHK     |
      | HK     | countryTradableCode | HK:CN          | 00005    | WRTS          | WRTS         | HK                    | SEHK     |
      | US     | countryTradableCode | HK:CN          | AAPL     | SEC           | SEC          | US                    | NASDAQ   |
      | US     | countryTradableCode | HK:CN          | AAPL     | WRTS          | SEC          | US                    | NASDAQ   |

  Scenario Outline: Customer should query multi predictive search successfully with CriteriaKey and CriteriaValue and single assetClass
    Given Customer has logged in
    When The following multi predictive search is query in <market> market:
      | Criteria key   | Criteria value   |
      | <criteria_key> | <criteria_value> |
    And multi predictive search is query by keyword:
      | <key_word> |
    And multi predictive search is query by assetClass:
      | <asset_classes> |
    Then Customer should receive the following multi predictive search detail:
      | Product type   | Country tradable code   | Exchange   |
      | <product_type> | <country_tradable_code> | <exchange> |
    Scenarios:
      | market | criteria_key        | criteria_value | key_word | asset_classes | product_type | country_tradable_code | exchange |
      | CN     | countryTradableCode | HK:CN          | 600000   | SEC           | SEC          | CN                    | SHAS     |
      | CN     | countryTradableCode | HK:CN          | 600000   | WRTS          | WRTS         | CN                    | SHAS     |
      | CN     | exchange            | SHAS:SZAS      | 600000   | SEC           | SEC          | CN                    | SHAS     |
      | CN     | exchange            | SHAS:SZAS      | 600000   | WRTS          | WRTS         | CN                    | SHAS     |
      | HK     | countryTradableCode | HK:CN          | 00005    | SEC           | SEC          | HK                    | SEHK     |
      | HK     | countryTradableCode | HK:CN          | 00005    | WRTS          | WRTS         | HK                    | SEHK     |
      | HK     | exchange            | SHAS:SZAS      | 00005    | SEC           | SEC          | HK                    | SEHK     |
      | HK     | exchange            | SHAS:SZAS      | 00005    | WRTS          | WRTS         | HK                    | SEHK     |
      | US     | countryTradableCode | HK:CN          | AAPL     | SEC           | SEC          | US                    | NASDAQ   |
      | US     | countryTradableCode | HK:CN          | AAPL     | WRTS          | SEC          | US                    | NASDAQ   |
      | US     | exchange            | SHAS:SZAS      | AAPL     | SEC           | SEC          | US                    | NASDAQ   |
      | US     | exchange            | SHAS:SZAS      | AAPL     | WRTS          | SEC          | US                    | NASDAQ   |

  Scenario Outline: Customer should query multi predictive search successfully with multiple keywords and multiple assetClasses
    Given Customer has logged in
    When The following multi predictive search is query in <market> market:
      | Operator   |
      | <operator> |
    And multi predictive search is query by keywords:
      | <key_word1> |
      | <key_word2> |
    And multi predictive search is query by assetClass:
      | <asset_classes1> |
      | <asset_classes2> |
    Then Customer should receive multi predictive search
    Scenarios:
      | market | operator | key_word1 | key_word2 | asset_classes1 | asset_classes2 |
      | CN     | gt       | 600000    | 000001    | SEC            | WRTS           |
      | CN     | ge       | 600000    | 000001    | SEC            | WRTS           |
      | CN     | eq       | 600000    | 000001    | SEC            | WRTS           |
      | CN     | lt       | 600000    | 000001    | SEC            | WRTS           |
      | CN     | le       | 600000    | 000001    | SEC            | WRTS           |
      | CN     | ne       | 600000    | 000001    | SEC            | WRTS           |
      | CN     | in       | 600000    | 000001    | SEC            | WRTS           |
      | HK     | gt       | 00005     | 000001    | SEC            | WRTS           |
      | HK     | ge       | 00005     | 000001    | SEC            | WRTS           |
      | HK     | eq       | 00005     | 000001    | SEC            | WRTS           |
      | HK     | lt       | 00005     | 000001    | SEC            | WRTS           |
      | HK     | le       | 00005     | 000001    | SEC            | WRTS           |
      | HK     | ne       | 00005     | 000001    | SEC            | WRTS           |
      | HK     | in       | 00005     | 000001    | SEC            | WRTS           |
      | US     | gt       | AAPL      | 000001    | SEC            | WRTS           |
      | US     | ge       | AAPL      | 000001    | SEC            | WRTS           |
      | US     | eq       | AAPL      | 000001    | SEC            | WRTS           |
      | US     | lt       | AAPL      | 000001    | SEC            | WRTS           |
      | US     | le       | AAPL      | 000001    | SEC            | WRTS           |
      | US     | ne       | AAPL      | 000001    | SEC            | WRTS           |
      | US     | in       | AAPL      | 000001    | SEC            | WRTS           |