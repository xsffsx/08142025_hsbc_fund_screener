httpclient:
  enabled: true
  init-default-ex-code: IllegalConfiguration
  process-default-ex-code: AccessVendorError
  default-http-client-name:

connection:
  {http-client-name}:
    maxTotal:
    maxPerRoute:
    connectTimeout:
    socketTimeout:
    enableProxy:
    proxyHostname:
    proxyPort:
    retryCount:
    enableGzip: