@index-quotes
Feature: Index Quotes

  Scenario Outline: Customer should query index quotes successfully
    Given Customer has logged in
    When The index quotes is query in <market> market:
    And index quotes is query by symbol:
      | <symbol> |
    Then Customer should receive the index quotes list:
    Scenarios:
      | market | symbol |
      | US     | .DJI   |
      | US     | .IXIC  |
      | HK     | HSI    |
      | HK     | PSI    |
      | HK     | USI    |
      | HK     | CSI    |
      | HK     | CEI    |
      | HK     | CCI    |
      | HK     | HSC    |
      | HK     | HFI    |
      | HK     | GEM    |
      | HK     | TEH    |
      | CN     | SZSC   |
      | CN     | CSI300 |
      | CN     | SSEC   |
      | CN     | SZI    |
      | CN     | SZ100  |
      | CN     | CNT    |

  Scenario Outline: Customer should query index quotes successfully with multiple symbols
    Given Customer has logged in
    When The index quotes is query in <market> market:
    And index quotes is query by symbol:
      | <symbol_1> |
      | <symbol_2> |
    Then Customer should receive the index quotes list:
    Scenarios:
      | market | symbol_1 | symbol_2 |
      | US     | .DJI     | .IXIC    |
      | HK     | HSI      | PSI      |
      | HK     | CEI      | CCI      |
      | HK     | HSC      | HFI      |
      | HK     | GEM      | TEH      |
      | CN     | SZSC     | CSI300   |
      | CN     | SSEC     | SZI      |
      | CN     | SZ100    | CNT      |

  Scenario Outline: Customer should query charts failed with invalid index quotes
    Given Customer has logged in
    When The index quotes is query in <market> market:
    And index quotes is query by symbol:
      | <symbol_1> |
      | <symbol_2> |
    Then Customer should receive the following index quotes error:
      | Status code   | Text   | Reason code   |
      | <status_code> | <text> | <reason_code> |
    Scenarios:
      | market | symbol_1 | symbol_2 | status_code | text                                      | reason_code  |
      | CN     | INDEX    | 0        | 400         | Request parameter don't match the rules   | MDSEQTY40004 |
      | HK     | HSI1     | HSI2     | 400         | Request parameter don't match the rules   | MDSEQTY40004 |
      | US     | .DJI     | IXIC     | 400         | Request parameter don't match the rules   | MDSEQTY40004 |
      | XXX    | .DJI     | .IXIC    | 400         | No available service matched this request | MDSEQTY40006 |