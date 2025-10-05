Feature: ProbingController API test

  Background:
    * url baseUrl

  Scenario: API_probing test
    * configure headers = call read('classpath:features/karate/headers.js')
    Given url baseUrl + 'healthcheck/probing'
    When method get
    Then print 'status is: ', responseStatus
    Then print 'response is: ', response
    And status 200


  Scenario: API_checkPredictiveServer test
    * configure headers = call read('classpath:features/karate/headers.js')
    Given url baseUrl + 'healthcheck/checkPredictiveServer'
    When method get
    Then print 'status is: ', responseStatus
    Then print 'response is: ', response
    Then status 200
    And match response.name == 'predictivesearchServer'


  Scenario: API_checkPredSrch  test
    * configure headers = call read('classpath:features/karate/headers.js')
    Given url baseUrl + 'healthcheck/checkPredSrch'
    When method get
    Then print 'status is: ', responseStatus
    Then print 'response is: ', response
    Then status 200
    And match response.name == 'predictivesearchData'


  Scenario: API_listIndices test
    * configure headers = call read('classpath:features/karate/headers.js')
    Given url baseUrl + '/listIndices'
    When method get
    Then print 'status is: ', responseStatus
    Then print 'response is: ', response
    Then status 200
    And match response.size() == 1