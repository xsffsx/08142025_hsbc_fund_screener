package com.alibaba.cloud.ai.health;

import org.springframework.ai.chat.metadata.ChatGenerationMetadata;
import org.springframework.ai.chat.metadata.ChatResponseMetadata;
import org.springframework.ai.chat.metadata.Usage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * AI 服务健康检查 - 检查 proxy_openai 健康 - 检查 ChatModel 最小调用 - 检查 EmbeddingModel 最小调用
 */
@Component("ai-services")
public class AiServicesHealthIndicator implements HealthIndicator {

	private final ChatModel chatModel;

	private final EmbeddingModel embeddingModel;

	@Value("${spring.ai.openai.base-url:http://localhost:8089}")
	private String openAiBaseUrl;

	@Value("${spring.ai.azure.enabled:false}")
	private boolean azureEnabled;

	@Value("${spring.ai.azure.openai.endpoint:}")
	private String azureEndpoint;

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

		@Value("${spring.ai.azure.api-version:}")
		private String azureApiVersion;

		@Value("${spring.ai.azure.deployment-name:}")
		private String azureDeploymentName;

		@Value("${spring.ai.azure.token-proxy.http:${HTTP_PROXY:}}")
		private String azureTokenHttpProxy;

		@Value("${spring.ai.azure.token-proxy.https:${HTTPS_PROXY:}}")
		private String azureTokenHttpsProxy;


	// 固定输出所有扩展诊断字段（不依赖配置）

	@Value("${spring.ai.openai.model:}")
	private String configuredChatModelName;

	@Value("${spring.ai.openai.embedding.model:}")
	private String configuredEmbeddingModelName;

	public AiServicesHealthIndicator(ChatModel chatModel, EmbeddingModel embeddingModel) {
		this.chatModel = chatModel;
		this.embeddingModel = embeddingModel;
	}

	@Override
	public Health health() {
		Map<String, Object> details = new HashMap<>();
		boolean ok = true;

		try {
			boolean llmOk = checkChatModel(details);
			ok &= llmOk;
		}
		catch (Exception e) {
			ok = false;
			details.put("llmError", e.getMessage());
		}

		try {
			boolean embOk = checkEmbedding(details);
			ok &= embOk;
		}
		catch (Exception e) {
			ok = false;
			details.put("embeddingError", e.getMessage());
		}

		return (ok ? Health.up() : Health.down()).withDetails(details).build();
	}

	private boolean checkChatModel(Map<String, Object> details) {
		long start = System.currentTimeMillis();
		try {
			ChatResponse response = chatModel.call(new Prompt(new UserMessage("ping")));
			long cost = System.currentTimeMillis() - start;
			boolean up = false;
			String text = null;
			if (response != null && response.getResults() != null && !response.getResults().isEmpty()) {
				var gen = response.getResults().get(0);
				var assistant = gen.getOutput();
				text = (assistant != null) ? assistant.getText() : null;
				up = StringUtils.hasText(text);
			}

			Map<String, Object> m = new HashMap<>();
			m.put("status", up ? "UP" : "DOWN");
			String endpoint = azureEnabled && StringUtils.hasText(azureEndpoint) ? azureEndpoint : openAiBaseUrl;
			m.put("endpoint", normalizeBaseUrl(endpoint));
			m.put("responseTime", cost + "ms");
			m.put("testResponse", up ? "SUCCESS" : "EMPTY");
			if (text != null)
				m.put("sampleText", safeTrim(text, 200));
			m.put("modelType", chatModel.getClass().getSimpleName());
			m.put("provider", azureEnabled && StringUtils.hasText(azureEndpoint) ? "AzureOpenAI" : "OpenAI");

			// 固定输出所有扩展字段（不依赖配置）
			// 模型名（优先从模型默认参数）
			try {
				var opts = chatModel.getDefaultOptions();
				if (opts != null && StringUtils.hasText(opts.getModel())) {
					m.put("modelName", opts.getModel());
				}
				else if (StringUtils.hasText(configuredChatModelName)) {
					m.put("modelName", configuredChatModelName);
				}
			}
			catch (Exception ignore) {
			}

			// finishReason 与 usage（若可获取）
			try {
				var results = response.getResults();
				if (results != null && !results.isEmpty()) {
					var gen = results.get(0);
					var meta = gen.getMetadata();
					if (meta instanceof ChatGenerationMetadata) {
						m.put("finishReason", ((ChatGenerationMetadata) meta).getFinishReason());
					}
				}
				var rmeta = response.getMetadata();
				if (rmeta instanceof ChatResponseMetadata) {
					Usage u = ((ChatResponseMetadata) rmeta).getUsage();
					if (u != null) {
						Map<String, Object> usage = new HashMap<>();
						usage.put("promptTokens", u.getPromptTokens());
						usage.put("completionTokens", u.getCompletionTokens());
						usage.put("totalTokens", u.getTotalTokens());
						m.put("usage", usage);
					}
				}
			}
			catch (Exception ignore) {
			}

			if (text != null) {
				m.put("sampleTextLen", text.length());
			}

			String endpointForMode = azureEnabled && StringUtils.hasText(azureEndpoint) ? azureEndpoint : openAiBaseUrl;
			m.put("connectionMode", connectionModeFromBaseUrl(endpointForMode));

			// Azure 诊断信息（仅在启用Azure时输出非敏感字段）
			if (azureEnabled) {
				Map<String, Object> az = new HashMap<>();
				az.put("endpoint", normalizeBaseUrl(azureEndpoint));
				az.put("deploymentName", azureDeploymentName);
				az.put("apiVersion", azureApiVersion);
				az.put("authorityHost", azureAuthorityHost);
				az.put("scope", azureScope);
				az.put("tokenProxyHttpSet", StringUtils.hasText(azureTokenHttpProxy));
				az.put("tokenProxyHttpsSet", StringUtils.hasText(azureTokenHttpsProxy));
				az.put("aadConfigured", StringUtils.hasText(azureTenantId) && StringUtils.hasText(azureClientId)
						&& StringUtils.hasText(azureClientSecret));
				m.put("azureDiagnostics", az);
			}

			details.put("llmService", m);
			return up;
		}
		catch (Exception e) {
			long cost = System.currentTimeMillis() - start;
			Map<String, Object> m = new HashMap<>();
			m.put("status", "DOWN");
			m.put("endpoint", normalizeBaseUrl(openAiBaseUrl));
			m.put("responseTime", cost + "ms");
			m.put("error", e.getMessage());
			m.put("modelType", chatModel != null ? chatModel.getClass().getSimpleName() : "N/A");
			// 固定扩展字段（值为null）
			m.put("modelName", null);
			// Azure 诊断补充（异常时）
			if (azureEnabled) {
				Map<String, Object> az = new HashMap<>();
				az.put("endpoint", normalizeBaseUrl(azureEndpoint));
				az.put("deploymentName", azureDeploymentName);
				az.put("apiVersion", azureApiVersion);
				az.put("authorityHost", azureAuthorityHost);
				az.put("scope", azureScope);
				az.put("tokenProxyHttpSet", StringUtils.hasText(azureTokenHttpProxy));
				az.put("tokenProxyHttpsSet", StringUtils.hasText(azureTokenHttpsProxy));
				az.put("aadConfigured", StringUtils.hasText(azureTenantId) && StringUtils.hasText(azureClientId)
						&& StringUtils.hasText(azureClientSecret));
				// 根据异常信息做粗分类（仅提示方向）
				String hint = classifyAzureError(e.getMessage());
				az.put("hint", hint);
				m.put("azureDiagnostics", az);
			}
			Map<String, Object> usage = new HashMap<>();
			usage.put("promptTokens", null);
			usage.put("completionTokens", null);
			usage.put("totalTokens", null);
			m.put("usage", usage);
			m.put("finishReason", null);
			m.put("sampleTextLen", null);
			m.put("sampleText", null);
			details.put("llmService", m);
			return false;
		}
	}

	private boolean checkEmbedding(Map<String, Object> details) {
		long start = System.currentTimeMillis();
		try {
			float[] vec = embeddingModel.embed("test embedding");
			long cost = System.currentTimeMillis() - start;
			boolean up = vec != null && vec.length > 0;

			Map<String, Object> m = new HashMap<>();
			m.put("status", up ? "UP" : "DOWN");
			String endpoint = azureEnabled && StringUtils.hasText(azureEndpoint) ? azureEndpoint : openAiBaseUrl;
			m.put("endpoint", normalizeBaseUrl(endpoint));
			m.put("responseTime", cost + "ms");
			m.put("vectorDimension", up ? vec.length : 0);
			m.put("modelType", embeddingModel.getClass().getSimpleName());

			// 固定输出所有扩展字段
			m.put("provider", azureEnabled && StringUtils.hasText(azureEndpoint) ? "AzureOpenAI" : "OpenAI");
			String embeddingModelName = StringUtils.hasText(configuredEmbeddingModelName) ? configuredEmbeddingModelName
					: null;
			m.put("modelName", embeddingModelName);
			// 仅预览前5个向量值，避免输出过大
			List<Float> preview = vectorPreview(vec, 5);
			m.put("responseVectorPreview", preview);

			String endpointForMode = azureEnabled && StringUtils.hasText(azureEndpoint) ? azureEndpoint : openAiBaseUrl;
			m.put("connectionMode", connectionModeFromBaseUrl(endpointForMode));

			details.put("embeddingService", m);
			return up;
		}
		catch (Exception e) {
			long cost = System.currentTimeMillis() - start;
			Map<String, Object> m = new HashMap<>();
			m.put("status", "DOWN");
			String endpoint = azureEnabled && StringUtils.hasText(azureEndpoint) ? azureEndpoint : openAiBaseUrl;
			m.put("endpoint", normalizeBaseUrl(endpoint));
			m.put("responseTime", cost + "ms");
			m.put("error", e.getMessage());
			m.put("modelType", embeddingModel != null ? embeddingModel.getClass().getSimpleName() : "N/A");
			// 固定扩展字段（值为null或空列表）
			m.put("provider", embeddingModel != null ? embeddingModel.getClass().getSimpleName() : null);
			m.put("modelName", null);
			m.put("responseVectorPreview", List.of());
			details.put("embeddingService", m);
			return false;
		}
	}

	private String normalizeBaseUrl(String baseUrl) {
		if (baseUrl == null)
			return "";
		if (baseUrl.endsWith("/")) {
			return baseUrl.substring(0, baseUrl.length() - 1);
		}
		return baseUrl;
	}

	private String safeTrim(String s, int max) {
		if (s == null)
			return null;
		if (s.length() <= max)
			return s;
		return s.substring(0, max) + "...";
	}

	// 向量预览工具（仅用于扩展诊断，限制输出长度）
	private List<Float> vectorPreview(float[] vec, int n) {
		List<Float> out = new ArrayList<>();
		if (vec == null) {
			return out;
		}
		int limit = Math.min(vec.length, Math.max(0, n));
		for (int i = 0; i < limit; i++) {
			out.add(vec[i]);
		}
		return out;
	}

	private String connectionModeFromBaseUrl(String baseUrl) {
		return "AZURE_AAD_BEARER";
	}


		private String classifyAzureError(String msg) {
			if (msg == null)
				return "unknown";
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
