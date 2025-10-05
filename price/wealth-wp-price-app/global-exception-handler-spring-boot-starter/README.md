global:
  exception:
    handler:
      enabled: true
      response:
        path: classpath:exception/
        mapping: exception-response-mapping.xml
        config: exception-response-config.xml
        default-ex-code: Undefined