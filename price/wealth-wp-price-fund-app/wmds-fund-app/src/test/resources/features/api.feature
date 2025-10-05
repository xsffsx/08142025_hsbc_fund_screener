@all @api-test
Feature: sample karate test script

  Background:    
  Scenario: retrieve UTB fundQuoteSummary
    * configure headers = read('classpath:headers.js')
    Given url 'http://localhost:3000/wmds/fundQuoteSummary'
    And param body = '{"productType":"UT","prodAltNum":"U50002","prodCdeAltClassCde":"M","market":"HK"}'
    When method get
    Then status 200
    And print 'response is: ', response
    And match response.summary contains {weekRangeCurrency:"#notnull"}
    
  Scenario: retrieve UTB quoteDetail
    * configure headers = read('classpath:headers.js')
    Given url 'http://localhost:3000/wmds/quoteDetail'
    And param body = '{"market":"HK","productType":"UT","prodCdeAltClassCde":"M","prodAltNum":"U50003","delay":true}'
    When method get
    Then status 200

  Scenario: retrieve UTB quoteList
    * configure headers = read('classpath:headers.js')
    Given url 'http://localhost:3000/wmds/quoteList'
    And param body = '{"productKeys":[{"prodCdeAltClassCde":"M","prodAltNum":"U50001","productType":"UT"},{"prodCdeAltClassCde":"M","prodAltNum":"U62281","productType":"UT"}],"market":"HK","delay":true}'
    When method get
    Then status 200

    
  Scenario: retrieve UTB fundQuickView
    * configure headers = read('classpath:headers.js')
    Given url 'http://localhost:3000/wmds/fundQuickView'
    And param body = '{"productType":"UT","returnOnlyNumberOfMatches":true,"criterias":[{"criteriaKey":"tableName","criteriaValue":"TOP5_PERFORMERS","operator":"eq"}],"restrOnlScribInd":"Y","prodStatCde":"C"}'
    When method get
    Then status 200


  Scenario: retrieve UTB fundSearchResult
    * configure headers = read('classpath:headers.js')
    Given url 'http://localhost:3000/wmds/fundSearchResult'
    And param body = '{"productType":"UT","returnOnlyNumberOfMatches":false,"sortBy":"name","sortOrder":"asc","startDetail":"1","endDetail":"10","numberOfRecords":"14","channelRestrictCode":"SI_I"}'
    When method get
    Then status 200  
    And print 'response is: ', response
   
  Scenario: retrieve UTB advanceChart
    * configure headers = read('classpath:headers.js')
    Given url 'http://localhost:3000/wmds/advanceChart'
    And param body = '{"productKeys":[{"market":"HK","prodCdeAltClassCde":"M","prodAltNum":"U50001","productType":"UT"}],"timeZone":"Asia/Hong_Kong","period":"1Y","currency":"HKD","dataType":["cumulativeReturn","navPrice"],"frequency":"daily","navForwardFill":true}'
    When method get
    Then status 200  
    And print 'response is: ', response
   
  Scenario: retrieve UTB hooldingAllocation
    * configure headers = read('classpath:headers.js')
    Given url 'http://localhost:3000/wmds/fundQuoteSummary'
    And param body = '{"market":"HK","productType":"UT","prodAltNum":"U50001","prodCdeAltClassCde":"M"}'
    When method get
    Then status 200