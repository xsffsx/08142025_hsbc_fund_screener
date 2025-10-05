@quotes
Feature: Quotes

  Scenario Outline: Customer should query quotes detail successfully with delay and requestType
    Given Customer has logged in
    When The following quotes is query in <market> market:
      | Delay   | RequestType   |
      | <delay> | <requestType> |
    And quotes is query by productKeys:
      | ProductType   | ProdCdeAltClassCde   | ProdAltNum   |
      | <productType> | <prodCdeAltClassCde> | <prodAltNum> |

    Then Customer should receive the following quotes detail:
      | Symbol   | ExchangeCode   | ProductSubType   | IsADR   | IsETF   |
      | <symbol> | <exchangeCode> | <productSubType> | <isADF> | <isETF> |
    Scenarios:
      | market | delay | requestType | productType | prodCdeAltClassCde | prodAltNum | symbol | exchangeCode | productSubType | isADF | isETF |
      | US     | false | 0           | SEC         | M                  | GE         | GE     | NYSE         |                | false | false |
      | US     | true  | 0           | SEC         | M                  | GE         | GE     | NYSE         |                | false | false |
      | US     | false | 0           | SEC         | M                  | SPY        | SPY    | NYARC        |                | false | true  |
      | US     | true  | 0           | SEC         | M                  | IVV        | IVV    | NYARC        |                | false | true  |
      | US     | false | 0           | SEC         | M                  | SNP        | SNP    | NYSE         |                | true  | false |
      | US     | true  | 0           | SEC         | M                  | CEA        | CEA    | NYSE         |                | true  | false |
      | HK     | false | 0           | SEC         | M                  | 00442      | 00442  | SEHK         | CLEQ           |       |       |
      | HK     | true  | 0           | SEC         | M                  | 00442      | 00442  | SEHK         | CLEQ           |       |       |
      | CN     | false | 0           | SEC         | M                  | 000858     | 000858 | SZAS         | SZEQ           |       |       |
      | CN     | true  | 0           | SEC         | M                  | 000858     | 000858 | SZAS         | SZEQ           |       |       |
      | US     | false | 10          | SEC         | M                  | GE         | GE     | NYSE         |                |       |       |
      | US     | true  | 10          | SEC         | M                  | GE         | GE     | NYSE         |                |       |       |
      | HK     | false | 10          | SEC         | M                  | 00442      | 00442  | SEHK         | CLEQ           |       |       |
      | HK     | true  | 10          | SEC         | M                  | 00442      | 00442  | SEHK         | CLEQ           |       |       |
      | CN     | false | 10          | SEC         | M                  | 000858     | 000858 | SZAS         | SZEQ           |       |       |
      | CN     | true  | 10          | SEC         | M                  | 000858     | 000858 | SZAS         | SZEQ           |       |       |
      | US     | false | 20          | SEC         | M                  | GE         | GE     | NYSE         |                |       |       |
      | US     | true  | 20          | SEC         | M                  | GE         | GE     | NYSE         |                |       |       |
      | HK     | false | 20          | SEC         | M                  | 00442      | 00442  | SEHK         | CLEQ           |       |       |
      | HK     | true  | 20          | SEC         | M                  | 00442      | 00442  | SEHK         | CLEQ           |       |       |
      | CN     | false | 20          | SEC         | M                  | 000858     | 000858 | SZAS         | SZEQ           |       |       |
      | CN     | true  | 20          | SEC         | M                  | 000858     | 000858 | SZAS         | SZEQ           |       |       |
      | CN     | false | 20          | SEC         | M                  | 510300     | 510300 | SHAS         | SHEF           |       |       |
      | CN     | true  | 20          | SEC         | M                  | 510300     | 510300 | SHAS         | SHEF           |       |       |
      | CN     | false | 20          | SEC         | M                  | 159977     | 159977 | SZAS         | SZEF           |       |       |
      | CN     | true  | 20          | SEC         | M                  | 159977     | 159977 | SZAS         | SZEF           |       |       |

  Scenario Outline: Customer should query quotes detail successfully with delay and productType
    Given Customer has logged in
    When The following quotes is query in <market> market:
      | Delay   |
      | <delay> |
    And quotes is query by productKeys:
      | ProductType   | ProdCdeAltClassCde   | ProdAltNum   |
      | <productType> | <prodCdeAltClassCde> | <prodAltNum> |

    Then Customer should receive the following quotes detail:
      | Symbol   | ExchangeCode   | ProductSubType   | IsADR   | IsETF   |
      | <symbol> | <exchangeCode> | <productSubType> | <isADF> | <isETF> |
    Scenarios:
      | market | delay | productType | prodCdeAltClassCde | prodAltNum | symbol | exchangeCode | productSubType | isADF | isETF |
      | US     | true  | SEC         | M                  | GE         | GE     | NYSE         |                | false | false |
      | US     | true  | SEC         | M                  | GE         | GE     | NYSE         |                | false | false |
      | US     | false | SEC         | M                  | SPY        | SPY    | NYARC        |                | false | true  |
      | US     | false | SEC         | M                  | IVV        | IVV    | NYARC        |                | false | true  |
      | US     | false | ALL         | M                  | SNP        | SNP    | NYSE         |                | true  | false |
      | US     | false | ALL         | M                  | CEA        | CEA    | NYSE         |                | true  | false |
      | HK     | true  | SEC         | M                  | 00442      | 00442  | SEHK         | CLEQ           |       |       |
      | HK     | false | SEC         | M                  | 00442      | 00442  | SEHK         | CLEQ           |       |       |
#      | HK     | true  | WRTS        | M                  | 51362      | 51362  | SEHK         | LCBB           |       |       |
#      | HK     | false | WRTS        | M                  | 51362      | 51362  | SEHK         | LCBB           |       |       |
      | HK     | true  | ALL         | M                  | 00442      | 00442  | SEHK         | CLEQ           |       |       |
      | HK     | false | ALL         | M                  | 00442      | 00442  | SEHK         | CLEQ           |       |       |
      | CN     | true  | SEC         | M                  | 000858     | 000858 | SZAS         | SZEQ           |       |       |
      | CN     | false | SEC         | M                  | 000858     | 000858 | SZAS         | SZEQ           |       |       |
      | CN     | true  | ALL         | M                  | 000858     | 000858 | SZAS         | SZEQ           |       |       |
      | CN     | false | ALL         | M                  | 000858     | 000858 | SZAS         | SZEQ           |       |       |


  Scenario Outline: Customer should query quotes detail failed with invalid quotes
    Given Customer has logged in
    When The following quotes is query in <market> market:
      | Delay   | RequestType   |
      | <delay> | <requestType> |
    And quotes is query by productKeys:
      | ProductType   | ProdCdeAltClassCde   | ProdAltNum   |
      | <productType> | <prodCdeAltClassCde> | <prodAltNum> |

    Then Customer should receive the following quotes detail error:
      | Status code   | Text   | Reason code   |
      | <status_code> | <text> | <reason_code> |
    Scenarios:
      | market | delay | requestType | productType | prodCdeAltClassCde | prodAltNum | status_code | text                                          | reason_code  |
      | US     | false | 0           | SEC         | XXX                | GE         | 400         | Request parameter don't match the rules       | MDSEQTY40004 |
      | HK     | true  | 0           | XXX         | M                  | 00442      | 400         | Request parameter don't match the rules       | MDSEQTY40004 |
      | CN     | XXX   | 0           | SEC         | M                  | XXX        | 400         | Request parameter is not standard JSON format | MDSEQTY40001 |
      | XXX    | XXX   | 0           | SEC         | M                  | 000858     | 400         | Request parameter is not standard JSON format | MDSEQTY40001 |