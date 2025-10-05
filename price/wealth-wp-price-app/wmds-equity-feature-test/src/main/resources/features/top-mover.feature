@top-mover
Feature: Query top mover

  Scenario Outline: Customer should query top mover successfully
    Given Customer has logged in
    When The following top mover is query in <market> market:
      | ProductType   | ExchangeCode   | MoverType   | BoardType   | Delay   | TopNum   |
      | <productType> | <exchangeCode> | <moverType> | <boardType> | <delay> | <topNum> |
    Then Customer should receive the following top mover:
    Scenarios:
      | market | productType | exchangeCode | moverType | boardType | delay | topNum |
      | US     | SEC         | NYSE         | VOL       |           | true  | 2      |
      | US     | SEC         | NASDAQ       | VOL       |           | true  | 2      |
      | US     | SEC         | AMEX         | VOL       |           | true  | 2      |
      | US     | SEC         | NYSEARCA     | VOL       |           | true  | 2      |
      | US     | SEC         | NYSE         | GAINPCT   |           | true  | 2      |
      | US     | SEC         | NASDAQ       | GAINPCT   |           | true  | 2      |
      | US     | SEC         | AMEX         | GAINPCT   |           | true  | 2      |
      | US     | SEC         | NYSEARCA     | GAINPCT   |           | true  | 2      |
      | US     | SEC         | NYSE         | LOSEPCT   |           | true  | 2      |
      | US     | SEC         | NASDAQ       | LOSEPCT   |           | true  | 2      |
      | US     | SEC         | AMEX         | LOSEPCT   |           | true  | 2      |
      | US     | SEC         | NYSEARCA     | LOSEPCT   |           | true  | 2      |
      | US     | SEC         | NYSE         | RATEUP    |           | true  | 2      |
      | US     | SEC         | NASDAQ       | RATEUP    |           | true  | 2      |
      | US     | SEC         | AMEX         | RATEUP    |           | true  | 2      |
      | US     | SEC         | NYSEARCA     | RATEUP    |           | true  | 2      |
#      | HK     | SEC         | HKEX         | TURN      | MAIN      | true  | 2      |
#      | HK     | SEC         | HKEX         | VOL       | MAIN      | true  | 2      |
#      | HK     | SEC         | HKEX         | GAINPCT   | MAIN      | true  | 2      |
#      | HK     | SEC         | HKEX         | LOSEPCT   | MAIN      | true  | 2      |
#      | HK     | SEC         | HKEX         | TURN      | WARRANT   | true  | 2      |
#      | HK     | SEC         | HKEX         | VOL       | WARRANT   | true  | 2      |
#      | HK     | SEC         | HKEX         | GAINPCT   | WARRANT   | true  | 2      |
 #     | HK     |    SEC     |    HKEX      |    LOSEPCT    |   WARRANT    | true |  2   |
#      | HK     | SEC         | HKEX         | TURN      | GEM       | true  | 2      |
#      | HK     | SEC         | HKEX         | VOL       | GEM       | true  | 2      |
#      | HK     | SEC         | HKEX         | GAINPCT   | GEM       | true  | 2      |
#      | HK     | SEC         | HKEX         | LOSEPCT   | GEM       | true  | 2      |
#      | HK     | SEC         | HKEX         | TURN      | MAIN      | false | 2      |
#      | HK     | SEC         | HKEX         | VOL       | MAIN      | false | 2      |
#      | HK     | SEC         | HKEX         | GAINPCT   | MAIN      | false | 2      |
#      | HK     | SEC         | HKEX         | LOSEPCT   | MAIN      | false | 2      |
#      | HK     | SEC         | HKEX         | TURN      | WARRANT   | false | 2      |
#      | HK     | SEC         | HKEX         | VOL       | WARRANT   | false | 2      |
#      | HK     | SEC         | HKEX         | GAINPCT   | WARRANT   | false | 2      |
#      | HK     |    SEC     |    HKEX      |    LOSEPCT    |   WARRANT    | false |  2   |
#      | HK     | SEC         | HKEX         | TURN      | GEM       | false | 2      |
#      | HK     | SEC         | HKEX         | VOL       | GEM       | false | 2      |
#      | HK     | SEC         | HKEX         | GAINPCT   | GEM       | false | 2      |
#      | HK     | SEC         | HKEX         | LOSEPCT   | GEM       | false | 2      |
#      | CN     | SEC         | SZSE         | VOL       |           | true  | 2      |
#      | CN     | SEC         | SHAS         | VOL       |           | true  | 2      |
#      | CN     | SEC         | SZSE         | GAINPCT   |           | true  | 2      |
#      | CN     | SEC         | SHAS         | GAINPCT   |           | true  | 2      |
#      | CN     | SEC         | SZSE         | LOSEPCT   |           | true  | 2      |
#      | CN     | SEC         | SHAS         | LOSEPCT   |           | true  | 2      |


  Scenario Outline: Customer should receive the following top mover detail error:
    Given Customer has logged in
    When The following top mover is query in <market> market:
      | ProductType   | ExchangeCode   | MoverType   | BoardType   | Delay   | TopNum   |
      | <productType> | <exchangeCode> | <moverType> | <boardType> | <delay> | <topNum> |
    Then Customer should receive the following news detail error:
      | Status code   | Text   | Reason code   |
      | <status_code> | <text> | <reason_code> |
    Scenarios:
      | market | productType | exchangeCode | moverType | boardType | delay | topNum | status_code | text                                      | reason_code  |
      | AAA    | SEC         | NYSE         | VOL       |           | true  | 2      | 400         | No available service matched this request | MDSEQTY40006 |
      | US     | SECS        | HKEX         | VOL       |           | true  | 2      | 400         | No available service matched this request | MDSEQTY40006 |