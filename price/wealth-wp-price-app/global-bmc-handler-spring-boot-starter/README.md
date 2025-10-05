global:
  bmc:
    handler:
      enabled: true
      path: classpath:bmc/
      mapping: bmc-mapping.xml
      config: bmc-config.xml
      entity-key: SITE
      default-entity-name: Default
      prefix-message: BMC_LOG=
      intercept-response: com.hhhh.group.secwealth.mktdata.starter.global_exception_handler.response.ExResponse
      intercept-key: exception,exCode,traceCode
      default-ex-key: Undefined