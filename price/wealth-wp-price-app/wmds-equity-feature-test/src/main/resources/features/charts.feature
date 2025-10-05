@charts
Feature: Charts

  Scenario Outline: Customer should query charts performance successfully with single symbol and productType and period
    Given Customer has logged in
    When The following charts is query in <market> market:
      | Product type   | Period   | Int cnt   | Int type   |
      | <product_type> | <period> | <int_cnt> | <int_type> |
    And charts is query by symbol:
      | <symbol> |
    And charts is query by filters:
      | <filter_1> |
      | <filter_2> |
      | <filter_3> |
      | <filter_4> |
      | <filter_5> |
      | <filter_6> |
      | <filter_7> |
    Then Customer should receive the following charts detail:
      | Product type   | Display name   |
      | <product_type> | <display_name> |
    Scenarios:
      | market | product_type | period | int_cnt | int_type | symbol | filter_1 | filter_2 | filter_3 | filter_4 | filter_5 | filter_6 | filter_7     | product_type | display_name                     |
      | CN     | INDEX        | 0      | 5       | MINUTE   | SSEC   | DATE     | OPEN     | HIGH     | LOW      | CLOSE    | VOLUME   |              | INDEX        | SSE Composite Index              |
      | CN     | INDEX        | 1      | 5       | MINUTE   | CSI300 | DATE     | OPEN     | HIGH     | LOW      | CLOSE    | VOLUME   |              | INDEX        | CSI 300 Index                    |
      | CN     | INDEX        | 2      | 5       | MINUTE   | SZSC   | DATE     | OPEN     | HIGH     | LOW      | CLOSE    | VOLUME   |              | INDEX        | SZSE Composite Index             |
      | CN     | INDEX        | 3      | 5       | MINUTE   | SZI    | DATE     | OPEN     | HIGH     | LOW      | CLOSE    | VOLUME   |              | INDEX        | SZSE Component Index             |
      | CN     | INDEX        | 4      | 1       | DAILY    | SZ100  | DATE     | OPEN     | HIGH     | LOW      | CLOSE    | VOLUME   |              | INDEX        | SZSE 100 Index                   |
      | CN     | INDEX        | 5      | 1       | DAILY    | CNT    | DATE     | OPEN     | HIGH     | LOW      | CLOSE    | VOLUME   |              | INDEX        | ChiNext Index                    |
      | CN     | INDEX        | 6      | 1       | DAILY    | CNT    | DATE     | OPEN     | HIGH     | LOW      | CLOSE    | VOLUME   |              | INDEX        | ChiNext Index                    |
      | CN     | INDEX        | 7      | 1       | DAILY    | CNT    | DATE     | OPEN     | HIGH     | LOW      | CLOSE    | VOLUME   |              | INDEX        | ChiNext Index                    |
      | CN     | INDEX        | 8      | 1       | DAILY    | CNT    | DATE     | OPEN     | HIGH     | LOW      | CLOSE    | VOLUME   |              | INDEX        | ChiNext Index                    |
      | CN     | INDEX        | 9      | 1       | DAILY    | CNT    | DATE     | OPEN     | HIGH     | LOW      | CLOSE    | VOLUME   |              | INDEX        | ChiNext Index                    |
      | CN     | INDEX        | 10     | 1       | DAILY    | CNT    | DATE     | OPEN     | HIGH     | LOW      | CLOSE    | VOLUME   |              | INDEX        | ChiNext Index                    |
      | CN     | INDEX        | 11     | 1       | DAILY    | CNT    | DATE     | OPEN     | HIGH     | LOW      | CLOSE    | VOLUME   |              | INDEX        | ChiNext Index                    |
      | CN     | INDEX        | 12     | 1       | WEEKLY   | CNT    | DATE     | OPEN     | HIGH     | LOW      | CLOSE    | VOLUME   |              | INDEX        | ChiNext Index                    |
      | CN     | INDEX        | 13     | 1       | WEEKLY   | CNT    | DATE     | OPEN     | HIGH     | LOW      | CLOSE    | VOLUME   |              | INDEX        | ChiNext Index                    |
      | CN     | INDEX        | 14     | 1       | WEEKLY   | CNT    | DATE     | OPEN     | HIGH     | LOW      | CLOSE    | VOLUME   |              | INDEX        | ChiNext Index                    |
      | CN     | INDEX        | 15     | 1       | WEEKLY   | CNT    | DATE     | OPEN     | HIGH     | LOW      | CLOSE    | VOLUME   |              | INDEX        | ChiNext Index                    |
      | CN     | INDEX        | 16     | 1       | MONTHLY  | CNT    | DATE     | OPEN     | HIGH     | LOW      | CLOSE    | VOLUME   |              | INDEX        | ChiNext Index                    |
      | HK     | SEC          | 0      | 5       | MINUTE   | HSI    | DATE     | OPEN     | HIGH     | LOW      | CLOSE    | VOLUME   | SMA=25,50,75 | SEC          | Hang Seng Index                  |
      | HK     | INDEX        | 0      | 5       | MINUTE   | HSI    | DATE     | OPEN     | HIGH     | LOW      | CLOSE    | VOLUME   | SMA=25,50,75 | INDEX        | Hang Seng Index                  |
      | HK     | SEC          | 2      | 15      | MINUTE   | FSI    | DATE     | OPEN     | HIGH     | LOW      | CLOSE    | VOLUME   | SMA=25,50,75 | SEC          | HS Finance Sub-Index             |
      | HK     | INDEX        | 2      | 15      | MINUTE   | FSI    | DATE     | OPEN     | HIGH     | LOW      | CLOSE    | VOLUME   | SMA=25,50,75 | INDEX        | HS Finance Sub-Index             |
      | HK     | SEC          | 5      | 1       | DAILY    | PSI    | DATE     | OPEN     | HIGH     | LOW      | CLOSE    | VOLUME   | SMA=25,50,75 | SEC          | HS Properties Sub-Index          |
      | HK     | INDEX        | 5      | 1       | DAILY    | PSI    | DATE     | OPEN     | HIGH     | LOW      | CLOSE    | VOLUME   | SMA=25,50,75 | INDEX        | HS Properties Sub-Index          |
      | HK     | SEC          | 7      | 3       | DAILY    | USI    | DATE     | OPEN     | HIGH     | LOW      | CLOSE    | VOLUME   | SMA=25,50,75 | SEC          | HS Utilities Sub-Index           |
      | HK     | INDEX        | 7      | 3       | DAILY    | USI    | DATE     | OPEN     | HIGH     | LOW      | CLOSE    | VOLUME   | SMA=25,50,75 | INDEX        | HS Utilities Sub-Index           |
      | HK     | SEC          | 8      | 6       | DAILY    | CSI    | DATE     | OPEN     | HIGH     | LOW      | CLOSE    | VOLUME   | SMA=25,50,75 | SEC          | HS Commerce & Industry Sub-Index |
      | HK     | INDEX        | 8      | 6       | DAILY    | CSI    | DATE     | OPEN     | HIGH     | LOW      | CLOSE    | VOLUME   | SMA=25,50,75 | INDEX        | HS Commerce & Industry Sub-Index |
      | HK     | SEC          | 11     | 5       | DAILY    | CEI    | DATE     | OPEN     | HIGH     | LOW      | CLOSE    | VOLUME   | SMA=25,50,75 | SEC          | HS China Enterprises Index       |
      | HK     | INDEX        | 11     | 5       | DAILY    | CEI    | DATE     | OPEN     | HIGH     | LOW      | CLOSE    | VOLUME   | SMA=25,50,75 | INDEX        | HS China Enterprises Index       |
      | HK     | SEC          | 13     | 5       | DAILY    | CCI    | DATE     | OPEN     | HIGH     | LOW      | CLOSE    | VOLUME   | SMA=25,50,75 | SEC          | HS China-Aff Corporations Index  |
      | HK     | INDEX        | 13     | 5       | DAILY    | CCI    | DATE     | OPEN     | HIGH     | LOW      | CLOSE    | VOLUME   | SMA=25,50,75 | INDEX        | HS China-Aff Corporations Index  |
      | HK     | SEC          | 0      | 5       | MINUTE   | HSC    | DATE     | OPEN     | HIGH     | LOW      | CLOSE    | VOLUME   | SMA=25,50,75 | SEC          | HS Composite Index               |
      | HK     | INDEX        | 0      | 5       | MINUTE   | HSC    | DATE     | OPEN     | HIGH     | LOW      | CLOSE    | VOLUME   | SMA=25,50,75 | INDEX        | HS Composite Index               |
      | HK     | SEC          | 0      | 5       | MINUTE   | HFI    | DATE     | OPEN     | HIGH     | LOW      | CLOSE    | VOLUME   | SMA=25,50,75 | SEC          | HS China H-Financials Index      |
      | HK     | INDEX        | 0      | 5       | MINUTE   | HFI    | DATE     | OPEN     | HIGH     | LOW      | CLOSE    | VOLUME   | SMA=25,50,75 | INDEX        | HS China H-Financials Index      |
      | HK     | SEC          | 0      | 5       | MINUTE   | GEM    | DATE     | OPEN     | HIGH     | LOW      | CLOSE    | VOLUME   | SMA=25,50,75 | SEC          | S&P/HKEx GEM Index               |
      | HK     | INDEX        | 0      | 5       | MINUTE   | GEM    | DATE     | OPEN     | HIGH     | LOW      | CLOSE    | VOLUME   | SMA=25,50,75 | INDEX        | S&P/HKEx GEM Index               |
      | HK     | SEC          | 0      | 5       | MINUTE   | TEH    | DATE     | OPEN     | HIGH     | LOW      | CLOSE    | VOLUME   | SMA=25,50,75 | SEC          | Hang Seng TECH Index             |
      | HK     | INDEX        | 0      | 5       | MINUTE   | TEH    | DATE     | OPEN     | HIGH     | LOW      | CLOSE    | VOLUME   | SMA=25,50,75 | INDEX        | Hang Seng TECH Index             |
      | US     | INDEX        | 0      | 5       | MINUTE   | .DJI   | DATE     | OPEN     | HIGH     | LOW      | CLOSE    | VOLUME   | SMA=10,20,50 | SEC          | .DJI                             |
      | US     | INDEX        | 1      | 5       | MINUTE   | .IXIC  | DATE     | OPEN     | HIGH     | LOW      | CLOSE    | VOLUME   | SMA=10,20,50 | SEC          | .IXIC                            |
      | US     | INDEX        | 2      | 5       | MINUTE   | .DJI   | DATE     | OPEN     | HIGH     | LOW      | CLOSE    | VOLUME   | SMA=10,20,50 | SEC          | .DJI                             |
      | US     | INDEX        | 3      | 5       | MINUTE   | .IXIC  | DATE     | OPEN     | HIGH     | LOW      | CLOSE    | VOLUME   | SMA=10,20,50 | SEC          | .IXIC                            |
      | US     | INDEX        | 4      | 1       | DAILY    | .DJI   | DATE     | OPEN     | HIGH     | LOW      | CLOSE    | VOLUME   | SMA=10,20,50 | SEC          | .DJI                             |
      | US     | INDEX        | 5      | 1       | DAILY    | .IXIC  | DATE     | OPEN     | HIGH     | LOW      | CLOSE    | VOLUME   | SMA=10,20,50 | SEC          | .IXIC                            |
      | US     | INDEX        | 6      | 1       | DAILY    | .DJI   | DATE     | OPEN     | HIGH     | LOW      | CLOSE    | VOLUME   | SMA=10,20,50 | SEC          | .DJI                             |
      | US     | INDEX        | 7      | 1       | DAILY    | .IXIC  | DATE     | OPEN     | HIGH     | LOW      | CLOSE    | VOLUME   | SMA=10,20,50 | SEC          | .IXIC                            |
      | US     | INDEX        | 8      | 1       | DAILY    | .DJI   | DATE     | OPEN     | HIGH     | LOW      | CLOSE    | VOLUME   | SMA=10,20,50 | SEC          | .DJI                             |
      | US     | INDEX        | 9      | 1       | DAILY    | .IXIC  | DATE     | OPEN     | HIGH     | LOW      | CLOSE    | VOLUME   | SMA=10,20,50 | SEC          | .IXIC                            |
      | US     | INDEX        | 10     | 1       | DAILY    | .DJI   | DATE     | OPEN     | HIGH     | LOW      | CLOSE    | VOLUME   | SMA=10,20,50 | SEC          | .DJI                             |
      | US     | INDEX        | 11     | 1       | DAILY    | .IXIC  | DATE     | OPEN     | HIGH     | LOW      | CLOSE    | VOLUME   | SMA=10,20,50 | SEC          | .IXIC                            |
      | US     | INDEX        | 12     | 1       | WEEKLY   | .DJI   | DATE     | OPEN     | HIGH     | LOW      | CLOSE    | VOLUME   | SMA=10,20,50 | SEC          | .DJI                             |
      | US     | INDEX        | 13     | 1       | WEEKLY   | .IXIC  | DATE     | OPEN     | HIGH     | LOW      | CLOSE    | VOLUME   | SMA=10,20,50 | SEC          | .IXIC                            |
      | US     | INDEX        | 14     | 1       | WEEKLY   | .DJI   | DATE     | OPEN     | HIGH     | LOW      | CLOSE    | VOLUME   | SMA=10,20,50 | SEC          | .DJI                             |
      | US     | INDEX        | 15     | 1       | WEEKLY   | .IXIC  | DATE     | OPEN     | HIGH     | LOW      | CLOSE    | VOLUME   | SMA=10,20,50 | SEC          | .IXIC                            |
      | US     | INDEX        | 16     | 1       | WEEKLY   | .DJI   | DATE     | OPEN     | HIGH     | LOW      | CLOSE    | VOLUME   | SMA=10,20,50 | SEC          | .DJI                             |

  Scenario Outline: Customer should query charts performance successfully with single symbol and filters
    Given Customer has logged in
    When The following charts is query in <market> market:
      | Product type   | Period   | Int cnt   | Int type   |
      | <product_type> | <period> | <int_cnt> | <int_type> |
    And charts is query by symbol:
      | <symbol> |
    And charts is query by filters:
      | <filter_1> |
      | <filter_2> |
      | <filter_3> |
      | <filter_4> |
      | <filter_5> |
      | <filter_6> |
      | <filter_7> |
    Then Customer should receive the following charts detail:
      | Product type   | Display name   |
      | <product_type> | <display_name> |
    Scenarios:
      | market | product_type | period | int_cnt | int_type | symbol | filter_1 | filter_2 | filter_3 | filter_4 | filter_5 | filter_6 | filter_7                          | product_type | display_name        |
      | CN     | INDEX        | 0      | 5       | MINUTE   | SSEC   | DATE     | OPEN     | HIGH     | LOW      | CLOSE    | VOLUME   |                                   | INDEX        | SSE Composite Index |
      | HK     | INDEX        | 0      | 5       | MINUTE   | HSI    | DATE     | OPEN     | HIGH     | LOW      | CLOSE    | VOLUME   | SMA=25,50,75                      | INDEX        | Hang Seng Index     |
      | US     | INDEX        | 0      | 5       | MINUTE   | .DJI   | DATE     | OPEN     | HIGH     | LOW      | CLOSE    | VOLUME   | SMA=10,20,50                      | SEC          | .DJI                |
      | US     | INDEX        | 1      | 5       | MINUTE   | .IXIC  | DATE     | OPEN     | HIGH     | LOW      | CLOSE    | VOLUME   | WMA(25,50,75).75                  | SEC          | .IXIC               |
      | US     | INDEX        | 1      | 5       | MINUTE   | .DJI   | DATE     | OPEN     | HIGH     | LOW      | CLOSE    | VOLUME   | BB=20 : BB(20).UB                 | SEC          | .DJI                |
      | US     | INDEX        | 1      | 5       | MINUTE   | .IXIC  | DATE     | OPEN     | HIGH     | LOW      | CLOSE    | VOLUME   | BB(20), BB(20).LB                 | SEC          | .IXIC               |
      | US     | INDEX        | 1      | 5       | MINUTE   | .DJI   | DATE     | OPEN     | HIGH     | LOW      | CLOSE    | VOLUME   | FIB= : FIB.236.0                  | SEC          | .DJI                |
      | US     | INDEX        | 1      | 5       | MINUTE   | .IXIC  | DATE     | OPEN     | HIGH     | LOW      | CLOSE    | VOLUME   | FIB.382.0                         | SEC          | .IXIC               |
      | US     | INDEX        | 1      | 5       | MINUTE   | .DJI   | DATE     | OPEN     | HIGH     | LOW      | CLOSE    | VOLUME   | FIB.500.0                         | SEC          | .DJI                |
      | US     | INDEX        | 1      | 5       | MINUTE   | .IXIC  | DATE     | OPEN     | HIGH     | LOW      | CLOSE    | VOLUME   | FIB.618.0                         | SEC          | .IXIC               |
      | US     | INDEX        | 1      | 5       | MINUTE   | .DJI   | DATE     | OPEN     | HIGH     | LOW      | CLOSE    | VOLUME   | FIB.764.0                         | SEC          | .DJI                |
      | US     | INDEX        | 1      | 5       | MINUTE   | .IXIC  | DATE     | OPEN     | HIGH     | LOW      | CLOSE    | VOLUME   | RSI=9 : RSI(9)                    | SEC          | .IXIC               |
      | US     | INDEX        | 1      | 5       | MINUTE   | .DJI   | DATE     | OPEN     | HIGH     | LOW      | CLOSE    | VOLUME   | ACD= : ACD                        | SEC          | .DJI                |
      | US     | INDEX        | 1      | 5       | MINUTE   | .IXIC  | DATE     | OPEN     | HIGH     | LOW      | CLOSE    | VOLUME   | CCI=20 : CCI(20)                  | SEC          | .IXIC               |
      | US     | INDEX        | 1      | 5       | MINUTE   | .DJI   | DATE     | OPEN     | HIGH     | LOW      | CLOSE    | VOLUME   | WR=10 : WR(10)                    | SEC          | .DJI                |
      | US     | INDEX        | 1      | 5       | MINUTE   | .IXIC  | DATE     | OPEN     | HIGH     | LOW      | CLOSE    | VOLUME   | OBV= : OBV                        | SEC          | .IXIC               |
      | US     | INDEX        | 1      | 5       | MINUTE   | .DJI   | DATE     | OPEN     | HIGH     | LOW      | CLOSE    | VOLUME   | MOM=12 : MOM(12)                  | SEC          | .DJI                |
      | US     | INDEX        | 1      | 5       | MINUTE   | .IXIC  | DATE     | OPEN     | HIGH     | LOW      | CLOSE    | VOLUME   | 3 : SKD(5,3).%K                   | SEC          | .IXIC               |
      | US     | INDEX        | 1      | 5       | MINUTE   | .DJI   | DATE     | OPEN     | HIGH     | LOW      | CLOSE    | VOLUME   | SKD(5,3).%D                       | SEC          | .DJI                |
      | US     | INDEX        | 1      | 5       | MINUTE   | .DJI   | DATE     | OPEN     | HIGH     | LOW      | CLOSE    | VOLUME   | MACD=12,26,9 : MACD(12,26,9).MACD | SEC          | .DJI                |
      | US     | INDEX        | 1      | 5       | MINUTE   | .DJI   | DATE     | OPEN     | HIGH     | LOW      | CLOSE    | VOLUME   | MACD(12,26,9).Signal              | SEC          | .DJI                |
      | US     | INDEX        | 1      | 5       | MINUTE   | .DJI   | DATE     | OPEN     | HIGH     | LOW      | CLOSE    | VOLUME   | MACD(12,26,9).Histogram           | SEC          | .DJI                |
      | US     | INDEX        | 1      | 5       | MINUTE   | .DJI   | DATE     | OPEN     | HIGH     | LOW      | CLOSE    | VOLUME   | TP=3 : TP(3).Reverse              | SEC          | .DJI                |
      | US     | INDEX        | 1      | 5       | MINUTE   | .DJI   | DATE     | OPEN     | HIGH     | LOW      | CLOSE    | VOLUME   | TP(3).Extreme                     | SEC          | .DJI                |
      | US     | INDEX        | 1      | 5       | MINUTE   | .DJI   | DATE     | OPEN     | HIGH     | LOW      | CLOSE    | VOLUME   | TP(3).Trend, TP(3).High           | SEC          | .DJI                |
      | US     | INDEX        | 1      | 5       | MINUTE   | .DJI   | DATE     | OPEN     | HIGH     | LOW      | CLOSE    | VOLUME   | TP(3).Low                         | SEC          | .DJI                |

  Scenario Outline: Customer should query charts performance successfully with multiple symbols
    Given Customer has logged in
    When The following charts is query in <market> market:
      | Product type   | Period   | Int cnt   | Int type   |
      | <product_type> | <period> | <int_cnt> | <int_type> |
    And charts is query by symbols:
      | <symbol_1> |
      | <symbol_2> |
    And charts is query by filters:
      | <filter_1> |
      | <filter_2> |
      | <filter_3> |
      | <filter_4> |
      | <filter_5> |
      | <filter_6> |
      | <filter_7> |
    Then Customer should receive charts performance
    Scenarios:
      | market | product_type | period | int_cnt | int_type | symbol_1 | symbol_2 | filter_1 | filter_2 | filter_3 | filter_4 | filter_5 | filter_6 | filter_7     |
      | CN     | INDEX        | 1      | 5       | MINUTE   | CSI300   | SZSC     | DATE     | OPEN     | HIGH     | LOW      | CLOSE    | VOLUME   |              |
      | CN     | INDEX        | 2      | 5       | MINUTE   | SZ100    | SZI      | DATE     | OPEN     | HIGH     | LOW      | CLOSE    | VOLUME   |              |
      | HK     | SEC          | 0      | 5       | MINUTE   | HSI      | 00001    | DATE     | OPEN     | HIGH     | LOW      | CLOSE    | VOLUME   | SMA=25,50,75 |
      | HK     | INDEX        | 0      | 5       | MINUTE   | FSI      | USI      | DATE     | OPEN     | HIGH     | LOW      | CLOSE    | VOLUME   | SMA=25,50,75 |
      | US     | SEC          | 3      | 1       | DAILY    | .DJI     | ARKK     | DATE     | OPEN     | HIGH     | LOW      | CLOSE    | VOLUME   | SMA=10,20,50 |
      | US     | SEC          | 4      | 1       | DAILY    | .GSPC    | ARKK     | DATE     | OPEN     | HIGH     | LOW      | CLOSE    | VOLUME   | SMA=10,20,50 |
#
  Scenario Outline: Customer should query charts failed with invalid charts performance
    Given Customer has logged in
    When The following charts is query in <market> market:
      | Product type   | Period   | Int cnt   | Int type   |
      | <product_type> | <period> | <int_cnt> | <int_type> |
    And charts is query by symbols:
      | <symbol_1> |
      | <symbol_2> |
    And charts is query by filters:
      | <filter_1> |
      | <filter_2> |
      | <filter_3> |
      | <filter_4> |
      | <filter_5> |
      | <filter_6> |
      | <filter_7> |
    Then Customer should receive the following charts detail error:
      | Status code   | Text   | Reason code   |
      | <status_code> | <text> | <reason_code> |
    Scenarios:
      | market | product_type | period | int_cnt | int_type | symbol_1 | symbol_2 | filter_1 | filter_2 | filter_3 | filter_4 | filter_5 | filter_6 | filter_7     | status_code | text                                          | reason_code  |
      | CN     | INDEX        | 0      | 5       | XXX      | CSI300   | SZSC     | DATE     | OPEN     | HIGH     | LOW      | CLOSE    | VOLUME   |              | 400         | Labci Invalid Portal request received.        | MDSELBC00008 |
      | HK     | XXX          |        | 5       | MINUTE   | HSI      | 00001    | DATE     | OPEN     | HIGH     | LOW      | CLOSE    | VOLUME   | SMA=25,50,75 | 400         | ETNet Invalid param.                          | MDSEETN00007 |
      | US     | SEC          | 0      | XXX     | MINUTE   | XXX      | ARKK     | DATE     | OPEN     | HIGH     | LOW      | CLOSE    | VOLUME   | SMA=10,20,50 | 400         | Request parameter is not standard JSON format | MDSEQTY40001 |
      | XXX    | SEC          | 0      | 5       | MINUTE   | HSI      | ARKK     | DATE     | OPEN     | HIGH     | LOW      | CLOSE    | VOLUME   | SMA=10,20,50 | 400         | No available service matched this request     | MDSEQTY40006 |