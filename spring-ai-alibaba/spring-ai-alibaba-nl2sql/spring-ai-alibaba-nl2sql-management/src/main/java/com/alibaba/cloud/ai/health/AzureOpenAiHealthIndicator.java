package com.alibaba.cloud.ai.health;

import com.alibaba.cloud.ai.util.AiConfiguration;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Azure OpenAI health check: first try real connection via AzureOpenAiChatModel built by AiConfiguration.
 * If it fails, fall back to config-based diagnostics for troubleshooting.
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

	@Value("${spring.ai.azure.endpoint:}")
	private String azureEndpoint;

	@Value("${spring.ai.azure.api-version:}")
	private String azureApiVersion;

	@Value("${spring.ai.azure.deployment-name:}")
	private String azureDeploymentName;

	@Value("${spring.ai.azure.token-proxy.http:${HTTP_PROXY:}}")
	private String azureTokenHttpProxy;

	@Value("${spring.ai.azure.token-proxy.https:${HTTPS_PROXY:}}")
	private String azureTokenHttpsProxy;

	@Autowired(required = false)
	private AiConfiguration aiConfiguration;

	@Override
	public Health health() {
		String lastError = null;
		// Step 1: If Azure is configured, attempt a real minimal call via AiConfiguration's Azure model
		if (azureEnabled && StringUtils.hasText(azureTenantId) && StringUtils.hasText(azureClientId)
				&& StringUtils.hasText(azureClientSecret) && StringUtils.hasText(azureEndpoint)
				&& StringUtils.hasText(azureDeploymentName) && aiConfiguration != null) {
			try {
				ChatModel azureModel = aiConfiguration.buildAzureOpenAiChatModel();
				var response = azureModel.call(new Prompt(new UserMessage("ping")));
				boolean ok = response != null && !response.getResults().isEmpty()
						&& response.getResults().get(0).getOutput() != null
						&& StringUtils.hasText(response.getResults().get(0).getOutput().getText());
				if (ok) {
					Map<String, Object> details = new HashMap<>();
					details.put("status", "UP");
					details.put("provider", "AzureOpenAI");
					details.put("endpoint", normalizeBaseUrl(azureEndpoint));
					details.put("deploymentName", azureDeploymentName);
					return Health.up().withDetails(details).build();
				}
			}
			catch (Exception e) {
				lastError = e.getMessage();
			}
		}

		// Step 2: Fallback – provide diagnostic info to troubleshoot which config step failed
		boolean aadConfigured = azureEnabled && StringUtils.hasText(azureTenantId) && StringUtils.hasText(azureClientId)
				&& StringUtils.hasText(azureClientSecret);
		boolean tokenProxySet = StringUtils.hasText(azureTokenHttpProxy) || StringUtils.hasText(azureTokenHttpsProxy);
		boolean deploymentNameLooksLikeModel = looksLikeModelName(azureDeploymentName);
		boolean apiVersionLooksValid = apiVersionLooksValid(azureApiVersion);
		boolean endpointPlaceholder = isEndpointPlaceholder(azureEndpoint);

		String mode = "AZURE_AAD_BEARER";
		String provider = "azure_openai";
		String modeLabel = "Azure AAD Bearer";
		Map<String, Object> details = new HashMap<>();
		details.put("mode", mode);
		details.put("modeLabel", modeLabel);
		details.put("provider", provider);
		details.put("openaiBaseUrl", openAiBaseUrl);
		details.put("azureEndpoint", azureEndpoint);
		details.put("apiKeyConfigured", StringUtils.hasText(openAiApiKey));
		details.put("aadConfigured", aadConfigured);
		details.put("azureAuthorityHost", azureAuthorityHost);
		details.put("azureScope", azureScope);
		details.put("azureApiVersion", azureApiVersion);
		details.put("azureDeploymentName", azureDeploymentName);
		details.put("usingTokenProxy", tokenProxySet);
		details.put("tokenProxyHttpSet", StringUtils.hasText(azureTokenHttpProxy));
		details.put("tokenProxyHttpsSet", StringUtils.hasText(azureTokenHttpsProxy));
		// Derived checks and hints
		details.put("deploymentNameLooksLikeModel", deploymentNameLooksLikeModel);
		details.put("apiVersionLooksValid", apiVersionLooksValid);
		details.put("endpointPlaceholder", endpointPlaceholder);
		if (lastError != null) {
			details.put("error", lastError);
			details.put("hint", classifyAzureError(lastError));
		} else if (!aadConfigured) {
			if (azureEnabled) {
				details.put("hint", "AAD_Auth_Incomplete: 需要设置 tenantId/clientId/clientSecret");
			} else {
				details.put("hint", "AAD_Disabled: 请开启 spring.ai.azure.enabled=true 并设置 tenantId/clientId/clientSecret");
			}
		} else if (deploymentNameLooksLikeModel) {
			details.put("hint", "Deployment_Name_Looks_Like_Model: 请确认 deployment-name 填写的是 Azure 部署名（非模型名，例如 gpt-4o 等）");
		} else if (!apiVersionLooksValid) {
			details.put("hint", "API_Version_Format: 建议使用形如 yyyy-MM-dd[-preview] 的版本号，例如 2024-02-15-preview");
		} else if (!tokenProxySet) {
			details.put("hint", "Proxy_Not_Configured: 若网络需要代理，请设置 HTTP_PROXY/HTTPS_PROXY 或 spring.ai.azure.token-proxy.*");
		}

		return Health.down().withDetails(details).build();
	}

	private String normalizeBaseUrl(String url) {
		if (!StringUtils.hasText(url)) return url;
		return url.endsWith("/") ? url.substring(0, url.length() - 1) : url;
	}

	private boolean looksLikeModelName(String name) {
		if (!StringUtils.hasText(name)) return false;
		String n = name.toLowerCase();
		return n.startsWith("gpt") || n.contains("gpt-") || n.contains("-mini") || n.contains("-turbo")
				|| n.contains("o3") || n.contains("o1") || n.contains("text-embedding") || n.contains("whisper")
				|| n.contains("tts") || n.contains("dall-e");
	}

	private boolean apiVersionLooksValid(String v) {
		if (!StringUtils.hasText(v)) return false;
		return v.matches("\\d{4}-\\d{2}-\\d{2}.*");
	}

	private boolean isEndpointPlaceholder(String endpoint) {
		return !StringUtils.hasText(endpoint) || endpoint.contains("your-resource");
	}

	private String classifyAzureError(String msg) {
		if (msg == null) return "unknown";
		String m = msg.toLowerCase();
		if (m.contains("unauthorized") || m.contains("401") || m.contains("invalid credentials") || m.contains("aad")
				|| m.contains("authority") || m.contains("token")) {
			return "AAD_Auth_Error: 检查 tenantId/clientId/clientSecret/scope/authority-host 是否正确";
		}
		if (m.contains("unknown host") || m.contains("connection refused") || m.contains("timeout") || m.contains("ssl")
				|| m.contains("handshake") || m.contains("proxy")) {
			return "Network_Proxy_Error: 检查 azure.endpoint、网络/代理(HTTP_PROXY/HTTPS_PROXY) 与SSL";
		}
		if (m.contains("404") || m.contains("resource not found") || m.contains("deployment")
				|| m.contains("completions") || m.contains("model not found")) {
			return "Deployment_Not_Found: 检查 deployment-name 是否为 Azure 部署名（非模型名）";
		}
		if (m.contains("429") || m.contains("quota") || m.contains("rate limit") || m.contains("throttle")) {
			return "Rate_Limit_Quota: 配额或限流问题";
		}
		return "unknown";
	}
}

