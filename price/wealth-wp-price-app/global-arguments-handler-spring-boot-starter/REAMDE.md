global:
  arguments:
    handler:
      enabled: true
      patterns: SITE~~X-hhhh-Chnl-CountryCode~~<_>~~X-hhhh-Chnl-Group-Member~~HEADER
      url-patterns: /wealth/api/v1/market-data/**
      default-ex-code: IllegalConfiguration