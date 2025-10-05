### How to use it
* Import maven dependency
```java
<dependency>
	<groupId>com.hhhh.group.secwealth.mktdata.starter</groupId>
	<artifactId>cache-distribute-handler-spring-boot-starter</artifactId>
	<version>1.0.0-SNAPSHOT</version>
</dependency>
```
* Config application.properties
```java
cache.distribute.enabled=true
cache.distribute.uri=https://cache-distributed-domain/cache/
```
* Usage
```java
@RestController
@SpringBootApplication
public class DemoApplication {

    public static void main(final String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @GetMapping(value = "/wealth/api/v1/market-data/demo")
    public void demo() {
        /**
         * Before get result from the Cache Distribute, you can do some other
         * business logic
         */
        CacheDistributeResult result = CacheDistributeHolder.getCacheDistribute();
        CacheDistributeResultStateEnum resultState = result.getResultState();
        if (resultState == CacheDistributeResultStateEnum.OK) {
            CacheDistributeResponse response = result.getResponse();
            String value = response.getValue();
            // Get the value you are interested in
            ObjectMapper mapper = new ObjectMapper();
            try {
                JsonNode node = mapper.readTree(value);
                String customerId = node.get("permID").asText();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            if (resultState == CacheDistributeResultStateEnum.INVALID_PARAMETERS) {
                // Bad Request
            }
            if (resultState == CacheDistributeResultStateEnum.UNCACHED_RECORD) {
                // The Cache Distribute don't contains the key you sent
            }
            if (resultState == CacheDistributeResultStateEnum.INVALID_HTTP_STATUS) {
                // Get response from the Cache Distribute encounter error
            }
        }
    }

}
```
* History
  * 1.0.1-SNAPSHOT
    Add `cache.distribute.enabled=true` to activate this component explicitly.
    If you don't have any properties starting with `cache.distribute` or `cache.distribute.enabled=false`, will not activate this component.
  * 1.0.0-SNAPSHOT
