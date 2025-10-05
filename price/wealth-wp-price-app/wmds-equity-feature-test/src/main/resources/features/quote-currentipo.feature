@quote-currentipo
Feature: Quote-CurrentIPO

  Scenario Outline: Customer should query current IPO successfully
    Given Customer has logged in
    When The current IPO is query in <market> market
    Then Customer should receive the current IPO list
    Scenarios:
      | market |
      | HK     |

  Scenario Outline: Customer should query current IPO failed with invalid market
    Given Customer has logged in
    When The current IPO is query in <market> market
    Then Customer should receive the following current IPS error:
      | Status code   | Text   | Reason code   |
      | <status_code> | <text> | <reason_code> |
    Scenarios:
      | market | status_code | text                                      | reason_code  |
      | US     | 400         | No available service matched this request | MDSEQTY40006 |
      | CN     | 400         | No available service matched this request | MDSEQTY40006 |