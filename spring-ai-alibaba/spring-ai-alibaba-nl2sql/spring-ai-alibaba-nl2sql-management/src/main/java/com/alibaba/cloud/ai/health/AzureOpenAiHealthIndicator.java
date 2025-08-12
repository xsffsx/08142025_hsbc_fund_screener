package com.alibaba.cloud.ai.health;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Expose NL2SQL OpenAI connection mode and proxy_openai token mode in actuator health.
 */
@Component
public class AzureOpenAiHealthIndicator implements HealthIndicator {

	@Value("${spring.ai.openai.base-url:}")
	private String openAiBaseUrl;

	@Value("${spring.ai.openai.api-key:}")
	private String openAiApiKey;

	@Value("${spring.ai.azure.enabled:false}")
	private boolean azureEnabled;

	@Value("${spring.ai.azure.tenant-id:}")
	private String azureTenantId;

	@Value("${spring.ai.azure.client-id:}")
	private String azureClientId;

	@Value("${spring.ai.azure.client-secret:}")
	private String azureClientSecret;

	@Value("${spring.ai.azure.scope:https://cognitiveservices.azure.com/.default}")
	private String azureScope;

	@Value("${spring.ai.azure.authority-host:https://login.microsoftonline.com}")
	private String azureAuthorityHost;

	@Value("${spring.ai.azure.token-proxy.http:${HTTP_PROXY:}}")
	private String azureTokenHttpProxy;

	@Value("${spring.ai.azure.token-proxy.https:${HTTPS_PROXY:}}")
	private String azureTokenHttpsProxy;

	@Override
	public Health health() {
		boolean aadConfigured = azureEnabled && StringUtils.hasText(azureTenantId) && StringUtils.hasText(azureClientId)
				&& StringUtils.hasText(azureClientSecret);
		boolean tokenProxySet = StringUtils.hasText(azureTokenHttpProxy) || StringUtils.hasText(azureTokenHttpsProxy);

		String mode = "AZURE_AAD_BEARER";
		String provider = "azure_openai";
		String modeLabel = "Azure AAD Bearer";
		Map<String, Object> details = new HashMap<>();
		details.put("mode", mode);
		details.put("modeLabel", modeLabel);
		details.put("provider", provider);
		details.put("openaiBaseUrl", openAiBaseUrl);
		details.put("apiKeyConfigured", StringUtils.hasText(openAiApiKey));
		details.put("aadConfigured", true);
		details.put("azureAuthorityHost", azureAuthorityHost);
		details.put("azureScope", azureScope);
		details.put("usingTokenProxy", tokenProxySet);
		details.put("tokenProxyHttpSet", StringUtils.hasText(azureTokenHttpProxy));
		details.put("tokenProxyHttpsSet", StringUtils.hasText(azureTokenHttpsProxy));

		return Health.up().withDetails(details).build();
	}

}
