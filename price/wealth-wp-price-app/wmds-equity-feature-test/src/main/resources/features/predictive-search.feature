@predictive-search
Feature: Predictive Search

  Scenario Outline: Customer should query predictive search successfully with single assetClass and keyWord and productType
    Given Customer has logged in
    When The following predictive search is query in <market> market:
      | Key word   | Top num   | Criteria key   | Criteria value   |
      | <key_word> | <top_num> | <criteria_key> | <criteria_value> |
    And predictive search is query by assetClass:
      | <asset_classes> |
    Then Customer should receive the following predictive search detail:
      | Product type   | Country tradable code   | Exchange   |
      | <product_type> | <country_tradable_code> | <exchange> |
    Scenarios:
      | market | key_word | top_num | criteria_key | criteria_value | asset_classes | product_type | country_tradable_code | exchange |
      | CN     | 5        | 1       | productKey   | M:00005:HK:SEC | SEC           | SEC          | CN                    | SZAS     |
      | CN     | 5        | 1       | productKey   | M:00005:HK:SEC | WRTS          | WRTS         | CN                    | SZAS     |
      | HK     | 5        | 1       | productKey   | M:00005:HK:SEC | SEC           | SEC          | HK                    | SEHK     |
      | HK     | 5        | 1       | productKey   | M:00005:HK:SEC | WRTS          | WRTS         | HK                    | SEHK     |
      | HK     | hhhh     | 1       | productKey   | M:00005:HK:SEC | SEC           | SEC          | HK                    | SEHK     |
      | HK     | hhhh     | 1       | productKey   | M:00005:HK:SEC | WRTS          | WRTS         | HK                    | SEHK     |
      | HK     | 滙豐       | 1       | productKey   | M:00005:HK:SEC | SEC           | SEC          | HK                    | SEHK     |
      | HK     | 滙豐       | 1       | productKey   | M:00005:HK:SEC | WRTS          | WRTS         | HK                    | SEHK     |
      | HK     | 汇丰       | 1       | productKey   | M:00005:HK:SEC | SEC           | SEC          | HK                    | SEHK     |
      | HK     | 汇丰       | 1       | productKey   | M:00005:HK:SEC | WRTS          | WRTS         | HK                    | SEHK     |
      | US     | 5        | 1       | productKey   | M:00005:HK:SEC | SEC           | SEC          | US                    | NASDAQ   |
      | US     | 5        | 1       | productKey   | M:00005:HK:SEC | WRTS          | SEC          | US                    | NASDAQ   |
      | US     | hhhh     | 1       | productKey   | M:00005:HK:SEC | SEC           | SEC          | US                    | NYSE   |
      | US     | hhhh     | 1       | productKey   | M:00005:HK:SEC | WRTS          | SEC          | US                    | NYSE   |

  Scenario Outline: Customer should query predictive search successfully with single assetClass and criteriaKey and productType
    Given Customer has logged in
    When The following predictive search is query in <market> market:
      | Key word   | Top num   | Criteria key   | Criteria value   |
      | <key_word> | <top_num> | <criteria_key> | <criteria_value> |
    And predictive search is query by assetClass:
      | <asset_classes> |
    Then Customer should receive the following predictive search detail:
      | Product type   | Country tradable code   | Exchange   |
      | <product_type> | <country_tradable_code> | <exchange> |
    Scenarios:
      | market | key_word | top_num | criteria_key | criteria_value | asset_classes | product_type | country_tradable_code | exchange |
      | CN     | 5        | 1       | productKey   | M:00005:HK:SEC | SEC           | SEC          | CN                    | SZAS     |
      | CN     | 5        | 1       | productKey   | M:00005:HK:SEC | WRTS          | WRTS         | CN                    | SZAS     |
      | CN     | 5        | 1       | exchange     | SHAS:SZAS      | SEC           | SEC          | CN                    | SZAS     |
      | CN     | 5        | 1       | exchange     | SHAS:SZAS      | WRTS          | WRTS         | CN                    | SZAS     |
      | HK     | 滙豐      | 1       | productKey   | M:00005:HK:SEC | SEC           | SEC          | HK                    | SEHK     |
      | HK     | 滙豐      | 1       | productKey   | M:00005:HK:SEC | WRTS          | WRTS         | HK                    | SEHK     |
      | HK     | 滙豐      | 1       | exchange     | SHAS:SZAS      | SEC           | SEC          | HK                    | SEHK     |
      | HK     | 滙豐      | 1       | exchange     | SHAS:SZAS      | WRTS          | WRTS         | HK                    | SEHK     |
      | US     | hhhh     | 1       | productKey   | M:00005:HK:SEC | SEC           | SEC          | US                    | NYSE     |
      | US     | hhhh     | 1       | productKey   | M:00005:HK:SEC | WRTS          | SEC          | US                    | NYSE     |
      | US     | hhhh     | 1       | exchange     | SHAS:SZAS      | SEC           | SEC          | US                    | NYSE     |
      | US     | hhhh     | 1       | exchange     | SHAS:SZAS      | WRTS          | SEC          | US                    | NYSE     |

  Scenario Outline: Customer should query predictive search successfully with multiple assetClasses and operator
    Given Customer has logged in
    When The following predictive search is query in <market> market:
      | Key word   | Top num   | Operator   |
      | <key_word> | <top_num> | <operator> |
    And predictive search is query by assetClass:
      | <asset_classes1> |
      | <asset_classes2> |
    Then Customer should receive the following predictive search detail:
      | Product type   | Country tradable code   | Exchange   |
      | <product_type> | <country_tradable_code> | <exchange> |
    Scenarios:
      | market | key_word | top_num | operator | asset_classes1 | asset_classes2 | product_type | country_tradable_code | exchange |
      | CN     | 5        | 1       | gt       | SEC            | WRTS           | SEC          | CN                    | SZAS     |
      | CN     | 5        | 2       | ge       | SEC            | WRTS           | SEC          | CN                    | SZAS     |
      | CN     | 5        | 3       | eq       | SEC            | WRTS           | SEC          | CN                    | SZAS     |
      | CN     | 5        | 4       | lt       | SEC            | WRTS           | SEC          | CN                    | SZAS     |
      | CN     | 5        | 5       | le       | SEC            | WRTS           | SEC          | CN                    | SZAS     |
      | CN     | 5        | 6       | ne       | SEC            | WRTS           | SEC          | CN                    | SZAS     |
      | CN     | 5        | 7       | in       | SEC            | WRTS           | SEC          | CN                    | SZAS     |
      | HK     | 滙豐       | 1       | gt       | SEC            | WRTS           | SEC          | HK                    | SEHK     |
      | HK     | 滙豐       | 2       | ge       | SEC            | WRTS           | SEC          | HK                    | SEHK     |
      | HK     | 滙豐       | 3       | eq       | SEC            | WRTS           | SEC          | HK                    | SEHK     |
      | HK     | 滙豐       | 4       | lt       | SEC            | WRTS           | SEC          | HK                    | SEHK     |
      | HK     | 滙豐       | 5       | le       | SEC            | WRTS           | SEC          | HK                    | SEHK     |
      | HK     | 滙豐       | 6       | ne       | SEC            | WRTS           | SEC          | HK                    | SEHK     |
#      | HK     | 滙豐       | 7       | in       | SEC            | SEC           | SEC          | HK                    | SEHK     |
      | US     | hhhh     | 1       | gt       | SEC            | WRTS           | SEC          | US                    | NYSE     |
      | US     | hhhh     | 2       | ge       | SEC            | WRTS           | SEC          | US                    | NYSE     |
      | US     | hhhh     | 3       | eq       | SEC            | WRTS           | SEC          | US                    | NYSE     |
      | US     | hhhh     | 4       | lt       | SEC            | WRTS           | SEC          | US                    | NYSE     |
      | US     | hhhh     | 5       | le       | SEC            | WRTS           | SEC          | US                    | NYSE     |
      | US     | hhhh     | 6       | ne       | SEC            | WRTS           | SEC          | US                    | NYSE     |
      | US     | hhhh     | 7       | in       | SEC            | WRTS           | SEC          | US                    | NYSE     |
#
  Scenario Outline: Customer should query predictive search failed with invalid predictive search
    Given Customer has logged in
    When The following predictive search is query in <market> market:
      | Key word   | Top num   | Criteria key   | Criteria value   |
      | <key_word> | <top_num> | <criteria_key> | <criteria_value> |
    And predictive search is query by assetClass:
      | <asset_classes1> |
      | <asset_classes2> |
    Then Customer should receive the following predictive search detail error:
      | Status code   | Text   | Reason code   |
      | <status_code> | <text> | <reason_code> |
    Scenarios:
      | market | key_word | top_num | criteria_key | criteria_value | asset_classes1 | asset_classes2 | status_code | text                  | reason_code |
      | CN     | 5        | XXX     | XXXX         | M:00005:HK:SEC | SEC            | WRTS           | 400         | Unexected error occur | MDSECM99    |
      | HK     | 5        | XXX     | XXXX         | M:00005:HK:SEC | SEC            | XXX            | 400         | Unexected error occur | MDSECM99    |
      | XXXX   | XXX      | XXX     | productKey   | M:00005:HK:SEC | SEC            | WRTS           | 400         | Unexected error occur | MDSECM99    |