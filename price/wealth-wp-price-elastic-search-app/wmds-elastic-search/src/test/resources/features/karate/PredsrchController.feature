Feature: PredsrchController API test

  Background:
    * url baseUrl

  Scenario: API_multiPredsrch Positive test
    * configure headers = call read('classpath:features/karate/headers.js')
    Given url baseUrl + 'wealth/api/v1/market-data/product/multi-predictive-search'
    And param param = '{"keyword":["000001","60006","00005"],"assetClasses": ["SEC","WRTS"]}'
    When method get
    Then print 'status is: ', responseStatus
    Then print 'response is: ', response
    And status 200

  Scenario: API_multiPredsrch MDSEQTY40001 test
    * configure headers = call read('classpath:features/karate/headers.js')
    Given url baseUrl + 'wealth/api/v1/market-data/product/multi-predictive-search'
    And param param = '{"keywords":["000001","60006","00005"],"assetClasses": ["SEC","WRTS"]}'
    When method get
    Then print 'status is: ', responseStatus
    Then print 'response is: ', response
    Then status 400
    And match response.reasonCode == 'MDSEQTY40001'

  Scenario: API_predsrch Positive test HK stock
    * configure headers = call read('classpath:features/karate/headers.js')
    Given url baseUrl + 'wealth/api/v1/market-data/product/predictive-search'
    And param param = '{"market": "HK","keyword": "5","assetClasses": ["SEC"],"topNum": 1}'
    When method get
    Then print 'status is: ', responseStatus
    Then print 'response is: ', response
    And status 200

  Scenario: API_predsrch Positive test CN stock
    * configure headers = call read('classpath:features/karate/headers.js')
    Given url baseUrl + 'wealth/api/v1/market-data/product/predictive-search'
    And param param = '{"market": "CN","keyword": "600","assetClasses": ["SEC"],"topNum": 1}'
    When method get
    Then print 'status is: ', responseStatus
    Then print 'response is: ', response
    And status 200

  Scenario: API_predsrch Positive test US stock
    * configure headers = call read('classpath:features/karate/headers.js')
    Given url baseUrl + 'wealth/api/v1/market-data/product/predictive-search'
    And param param = '{"market": "US","keyword": "AAPL","assetClasses": ["SEC"],"topNum": 1}'
    When method get
    Then print 'status is: ', responseStatus
    Then print 'response is: ', response
    And status 200

  Scenario: API_predsrch MDSEQTY40001 test
    * configure headers = call read('classpath:features/karate/headers.js')
    Given url baseUrl + 'wealth/api/v1/market-data/product/predictive-search'
    And param param = '{"markets": "HK","keyword": "5","assetClasses": ["SEC"],"topNum": 1}'
    When method get
    Then print 'status is: ', responseStatus
    Then print 'response is: ', response
    Then status 400
    And match response.reasonCode == 'MDSEQTY40001'

  Scenario: API_predsrch MDSEQTY40003 test
    * configure headers = call read('classpath:features/karate/headers-error.js')
    Given url baseUrl + 'wealth/api/v1/market-data/product/predictive-search'
    And param param = '{"market": "HK","keyword": "5","assetClasses": ["SEC"],"topNum": 1}'
    When method get
    Then print 'status is: ', responseStatus
    Then print 'response is: ', response
    Then status 400
    And match response.reasonCode == 'MDSEQTY40003'

  Scenario: API_predsrch MDSEQTY40004 test
    * configure headers = call read('classpath:features/karate/headers.js')
    Given url baseUrl + 'wealth/api/v1/market-data/product/predictive-search'
    And param param = '{"market": "HK","keyword": "","assetClasses": ["SEC"],"topNum": 1}'
    When method get
    Then print 'status is: ', responseStatus
    Then print 'response is: ', response
    Then status 400
    And match response.reasonCode == 'MDSEQTY40004'

  Scenario: API_predsrch MDSEQTY40006 test
    * configure headers = call read('classpath:features/karate/headers-error-chnid.js')
    Given url baseUrl + 'wealth/api/v1/market-data/product/predictive-search'
    And param param = '{"market": "OT","keyword": "5","assetClasses": ["SEC"],"topNum": 1}'
    When method get
    Then print 'status is: ', responseStatus
    Then print 'response is: ', response
    Then status 400
    And match response.reasonCode == 'MDSEQTY40006'