@quote-listedipo
Feature: Quote-ListedIPO

  Scenario Outline: Customer should query listed IPO successfully
    Given Customer has logged in
    When The listed IPO is query in <market> market:
      | recordsPerPage   | pageId   |
      | <recordsPerPage> | <pageId> |
    Then Customer should receive the Listed IPO list:
    Scenarios:
      | market | recordsPerPage | pageId |
      | HK     | 10             | 1      |
      | HK     | 1              | 10     |
      | HK     | 5              | 5      |

  Scenario Outline: Customer should query listed IPO failed with invalid market
    Given Customer has logged in
    When The listed IPO is query in <market> market:
      | recordsPerPage   | pageId   |
      | <recordsPerPage> | <pageId> |
    Then Customer should receive the following index quotes error:
      | Status code   | Text   | Reason code   |
      | <status_code> | <text> | <reason_code> |
    Scenarios:
      | market | recordsPerPage | pageId | status_code | text                                      | reason_code  |
      | US     | 10             | 1      | 400         | No available service matched this request | MDSEQTY40006 |
      | CN     | 10             | 1      | 400         | No available service matched this request | MDSEQTY40006 |