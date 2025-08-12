/*
 * Copyright 2024-2025 the original author or authors.
 */
package com.alibaba.cloud.ai.util;

import com.alibaba.cloud.ai.dashscope.api.DashScopeApi;
import com.alibaba.cloud.ai.dashscope.embedding.DashScopeEmbeddingModel;
import com.alibaba.cloud.ai.dashscope.embedding.DashScopeEmbeddingOptions;
import com.alibaba.cloud.ai.util.azure.AzureAdBearerTokenApiKey;
import com.alibaba.cloud.ai.util.azure.AzureAdTokenClient;
import com.azure.ai.openai.OpenAIClientBuilder;
import com.azure.identity.ClientSecretCredentialBuilder;

import com.azure.core.http.ProxyOptions;
import com.azure.core.http.netty.NettyAsyncHttpClientBuilder;
import org.springframework.ai.azure.openai.AzureOpenAiChatModel;
import org.springframework.ai.azure.openai.AzureOpenAiChatOptions;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.document.MetadataMode;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.OpenAiEmbeddingModel;
import org.springframework.ai.openai.OpenAiEmbeddingOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.util.StringUtils;
import java.net.InetSocketAddress;
import java.net.URI;

@Configuration
public class AiConfiguration {

	@Value("${spring.ai.openai.api-key:}")
	private String openAiApiKey;

	@Value("${spring.ai.openai.base-url:}")
	private String baseUrl;

	@Value("${spring.ai.openai.model:}")
	private String model;

	@Value("${spring.ai.dashscope.api-key:}")
	private String dashScopeApiKey;

	@Value("${spring.ai.dashscope.embedding.model:text-embedding-v2}")
	private String dashScopeEmbeddingModel;

	@Value("${spring.ai.openai.embedding.model:text-embedding-ada-002}")
	private String openAiEmbeddingModel;

	@Value("${spring.ai.openai.embedding.embeddings-path:}")
	private String openAiEmbeddingsPath;

	@Value("${spring.ai.openai.completions-path:}")
	private String openAiCompletionsPath;

	// Azure AAD optional (disabled by default for proxy_openai mode)
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

	@Value("${spring.ai.azure.api-version:}")
	private String azureApiVersion;

	@Value("${spring.ai.azure.endpoint:}")
	private String azureEndpoint;

	@Value("${spring.ai.azure.deployment-name:}")
	private String azureDeploymentName;


	@Value("${spring.ai.azure.token-proxy.http:${HTTP_PROXY:}}")
	private String azureTokenHttpProxy;

	@Value("${spring.ai.azure.token-proxy.https:${HTTPS_PROXY:}}")
	private String azureTokenHttpsProxy;

	@Bean
	public ChatModel chatModel() {
		// 优先走 AzureOpenAiChatModel：当显式配置了 Azure AAD 与 Azure 部署必要参数
		if (azureEnabled && StringUtils.hasText(azureTenantId) && StringUtils.hasText(azureClientId)
				&& StringUtils.hasText(azureClientSecret) && StringUtils.hasText(azureEndpoint)
				&& StringUtils.hasText(azureDeploymentName)) {
			return buildAzureOpenAiChatModel();
		}
		// 否则使用标准 OpenAI 客户端（兼容 proxy_openai 等 /v1 风格端点）
		OpenAiApi openAiApi = OpenAiApi.builder().apiKey(openAiApiKey).baseUrl(baseUrl).build();
		OpenAiChatOptions openAiChatOptions = OpenAiChatOptions.builder().model(model).temperature(0.7).build();
		return OpenAiChatModel.builder().openAiApi(openAiApi).defaultOptions(openAiChatOptions).build();
	}

	@Bean
	public ChatClient chatClient(@Qualifier("chatModel") ChatModel chatModel) {
		return ChatClient.create(chatModel);
	}

	@Bean("dashScopeEmbeddingModel")
	@ConditionalOnProperty(name = "spring.ai.dashscope.api-key")
	public EmbeddingModel dashScopeEmbeddingModel() {
		DashScopeApi dashScopeApi = DashScopeApi.builder().apiKey(dashScopeApiKey).build();
		DashScopeEmbeddingOptions options = DashScopeEmbeddingOptions.builder()
			.withModel(dashScopeEmbeddingModel)
			.build();
		return new DashScopeEmbeddingModel(dashScopeApi, MetadataMode.EMBED, options);
	}

	@Bean("embeddingModel")
	@ConditionalOnProperty(name = "spring.ai.openai.embedding.enabled", havingValue = "true", matchIfMissing = true)
	@ConditionalOnMissingBean(EmbeddingModel.class)
	public EmbeddingModel customOpenAiEmbeddingModel() {
		OpenAiApi.Builder builder;
		if (azureEnabled && StringUtils.hasText(azureTenantId) && StringUtils.hasText(azureClientId)
				&& StringUtils.hasText(azureClientSecret)) {
			AzureAdTokenClient tokenClient = new AzureAdTokenClient(azureAuthorityHost, azureTenantId, azureClientId,
					azureClientSecret, azureScope, azureTokenHttpProxy, azureTokenHttpsProxy);
			AzureAdBearerTokenApiKey apiKey = new AzureAdBearerTokenApiKey(tokenClient);
			builder = OpenAiApi.builder().apiKey(apiKey).baseUrl(baseUrl);
		}
		else {
			if (!StringUtils.hasText(openAiApiKey)) {
				throw new IllegalStateException(
						"Either spring.ai.azure.* or spring.ai.openai.api-key must be configured");
			}
			builder = OpenAiApi.builder().apiKey(openAiApiKey).baseUrl(baseUrl);
		}
		if (StringUtils.hasText(openAiEmbeddingsPath)) {
			builder.embeddingsPath(openAiEmbeddingsPath);
		}
		if (StringUtils.hasText(openAiCompletionsPath)) {
			builder.completionsPath(openAiCompletionsPath);
		}
		OpenAiApi openAiApi = builder.build();
		OpenAiEmbeddingOptions options = OpenAiEmbeddingOptions.builder().model(openAiEmbeddingModel).build();
		return new OpenAiEmbeddingModel(openAiApi, MetadataMode.EMBED, options);
	}


		/**
		 * Build an AzureOpenAiChatModel using current Azure AAD configuration.
		 * Exposed for health checks to validate actual connectivity.
		 */
		public ChatModel buildAzureOpenAiChatModel() {
			// 仅对 AAD 令牌获取(login.microsoftonline.com)配置代理；对 Azure OpenAI 请求不走代理
			ClientSecretCredentialBuilder credentialBuilder = new ClientSecretCredentialBuilder()
					.tenantId(azureTenantId)
					.clientId(azureClientId)
					.clientSecret(azureClientSecret)
					.authorityHost(azureAuthorityHost);
			// 若设置了 token 代理，仅用于 AAD 授权
			if (StringUtils.hasText(azureTokenHttpProxy) || StringUtils.hasText(azureTokenHttpsProxy)) {
				try {
					String proxy = StringUtils.hasText(azureTokenHttpsProxy) ? azureTokenHttpsProxy : azureTokenHttpProxy;
					// 使用 Netty HttpClient 并设置代理
					com.azure.core.http.HttpClient aadHttpClient = new NettyAsyncHttpClientBuilder()
							.proxy(new ProxyOptions(ProxyOptions.Type.HTTP,
									new InetSocketAddress(URI.create(proxy).getHost(), URI.create(proxy).getPort() > 0 ? URI.create(proxy).getPort() : (proxy.startsWith("https") ? 443 : 80))))
							.build();
					credentialBuilder.httpClient(aadHttpClient);
				}
				catch (Exception ignore) {
				}
			}
			var credential = credentialBuilder.build();
			// OpenAIClientBuilder 不设置代理
			var clientBuilder = new OpenAIClientBuilder()
					.credential(credential)
					.endpoint(azureEndpoint);
			var options = AzureOpenAiChatOptions.builder()
					.deploymentName(azureDeploymentName)
					.temperature(0.7)
					.build();
			return AzureOpenAiChatModel.builder()
					.openAIClientBuilder(clientBuilder)
					.defaultOptions(options)
					.build();
		}


}
