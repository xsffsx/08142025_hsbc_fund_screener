@news
Feature: News

  Scenario Outline: Customer should query news detail successfully with news id
    Given Customer has logged in
    When The following news is query in <market> market:
      | News id   | Source   |
      | <news_id> | <source> |
    Then Customer should receive the following news detail:
      | News id   | Headline   | Content   | As of date time   |
      | <news_id> | <headline> | <content> | <as_of_date_time> |
    Scenarios:
      | market | news_id                                          | source | headline                                                       | content  | as_of_date_time              |
      | US     | urn:newsml:reuters.com:20220621:nP7N2W601A:2     |        | Slovak current account deficit hits 788 million euros in April | NOT_NULL | 2022-06-21T09:49:41.000Z     |
      | HK     | 20211027461                                      | ETNet  | {Block Trade}PCCW (00008): 5.73m shares, or HK$22.82m          | NOT_NULL | 2021-10-27T15:53:00.000+0800 |

  Scenario Outline: Customer should query news detail failed with invalid news information
    Given Customer has logged in
    When The following news is query in <market> market:
      | News id   | Source   |
      | <news_id> | <source> |
    Then Customer should receive the following news detail error:
      | Status code   | Text   | Reason code   |
      | <status_code> | <text> | <reason_code> |
    Scenarios:
      | market | news_id     | source | status_code | text                                      | reason_code  |
      | US     | XXX         |        | 400         | Labci invalid response                    | MDSELBC00003 |
      | HK     | XXXXX       | ETNet  | 400         | ETNet Invalid param.                      | MDSEETN00007 |
      | HK     | 20211027461 | etnet  | 400         | ETNet Invalid param.                      | MDSEETN00007 |
      | AA     | 20211027461 | ETNet  | 400         | No available service matched this request | MDSEQTY40006 |

  Scenario Outline: Customer should query news headlines successfully with category
    Given Customer has logged in
    When Query news headlines by category <category> in the <market> market:
    Then Customer should receive news headlines
    Scenarios:
      | market | category             |
      | US     | ALL                  |
      | US     | overalleconomic      |
#      | US     | interestrate         |
      | US     | economicindicators   |
#      | US     | fund                 |
      | US     | ipodebtissue         |
#      | US     | creditrating         |
      | US     | corporateresults     |
#      | US     | ma                   |
      | US     | internationaltrade   |
#      | US     | stockmarket          |
#      | US     | moneymarket          |
#      | US     | bondmarket           |
#      | US     | energymarket         |
#      | US     | goldpreciousmetals   |
#      | US     | basicmetals          |
#      | US     | financeInsurance     |
#      | US     | realestate           |
#      | US     | automobiles          |
#      | US     | steel                |
#      | US     | electronicelectrical |
#      | US     | telecommunications   |
#      | US     | airtransportshipping |
#      | US     | chemical             |
#      | US     | food                 |
#      | US     | electricalappliances |
#      | US     | healthmedicines      |
#      | US     | internetwww          |
#      | US     | multiindustry        |
      | HK     | all                  |
      | HK     | china                |
      | HK     | blocktrade           |
      | HK     | economics            |
      | HK     | regulatory           |
      | HK     | result               |
      | HK     | business             |
      | HK     | market               |
#      | HK     | brokerage            |
      | HK     | company              |
#      | HK     | finance              |
#      | HK     | lawsuits             |
      | HK     | research             |
#      | HK     | others               |
      | HK     | ipo                  |
      | HK     | hkex                 |
#      | HK     | commentary_Benny     |
#      | HK     | commentary_Dennis    |
#      | HK     | relatednews          |

  Scenario: Customer should query news headlines successfully with related news
    Given Customer has logged in
    When Query news headlines by category relatednews in the HK market:
    And News headlines include following symbols:
      | 00005 |
      | 00006 |
      | 00007 |
    Then Customer should receive news headlines

  Scenario Outline: Customer should query news headlines successfully with single symbols
    Given Customer has logged in
    When Query news headlines by category <category> in the <market> market:
    And News headlines with product code indicator <product_code_indicator> include following symbol:
      | <symbol> |
    Then Customer should receive news headlines
    Scenarios:
      | market | category | product_code_indicator | symbol |
#      | US     | ALL      | M                      | GOOG   |
      | US     | ALL      | M                      | SHI    |
      | HK     | all      | M                      | 08003  |
      | HK     | all      | M                      | 12201  |

  Scenario Outline: Customer should query news headlines successfully with multiple symbols
    Given Customer has logged in
    When Query news headlines by category <category> in the <market> market:
    And News headlines with product code indicator <product_code_indicator> include following symbols:
      | <symbol_1> |
      | <symbol_2> |
    Then Customer should receive news headlines
    Scenarios:
      | market | category | product_code_indicator | symbol_1 | symbol_2 |
#      | US     | ALL      | M                      | GOOG     | SHI      |
      | HK     | all      | M                      | 08003    | 12201    |

  Scenario Outline: Customer should query news headlines successfully with records pagination
    Given Customer has logged in
    When Query news headlines by category <category> in the <market> market:
    And Get <page_id> page of the news headlines by <records_per_page> items per page
    Then Customer should receive news headlines less than or equal to <records_per_page> records
    Scenarios:
      | market | category | page_id | records_per_page |
      | US     | ALL      | 1       | 8                |
#      | US     | ALL      | 2       | 13               |
      | HK     | all      | 1       | 8                |
      | HK     | all      | 2       | 13               |


  Scenario Outline: Customer should query news headlines failed with invalid parameters
    Given Customer has logged in
    When Query news headlines by category <category> in the <market> market:
    And News headlines with product code indicator <product_code_indicator> include following symbol:
      | <symbol> |
    And Get <page_id> page of the news headlines by <records_per_page> items per page
    Then Customer should receive the following news headlines error:
      | Status code   | Text   | Reason code   |
      | <status_code> | <text> | <reason_code> |
    Scenarios:
      | market | category | product_code_indicator | symbol | page_id | records_per_page | status_code | text                                          | reason_code  |
      | AA     | ALL      | M                      | IBM    | 1       | 10               | 400         | No available service matched this request     | MDSEQTY40006 |
      | US     | ALL      | M                      | IBM    | A       | 10               | 400         | Request parameter is not standard JSON format | MDSEQTY40001 |
      | US     | ALL      | M                      | IBM    | 1       | AA               | 400         | Request parameter is not standard JSON format | MDSEQTY40001 |
#      | US     | ALL      | M                      | IBM    | 0       | 10               | 400         | Internal Error                                | MDSEQTY50001 |
      | HK     | ALL      | M                      | 12201  | A       | 10               | 400         | Request parameter is not standard JSON format | MDSEQTY40001 |
      | HK     | ALL      | M                      | 12201  | 1       | A                | 400         | Request parameter is not standard JSON format | MDSEQTY40001 |