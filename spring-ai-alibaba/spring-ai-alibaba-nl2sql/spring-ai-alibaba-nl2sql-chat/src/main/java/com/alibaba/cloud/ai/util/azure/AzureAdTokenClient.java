package com.alibaba.cloud.ai.util.azure;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.netty.transport.ProxyProvider;

import java.net.URI;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Azure AD client credentials token retriever with simple in-memory cache. Only token
 * acquisition uses HTTP/HTTPS proxy (if provided). Downstream OpenAI calls should not use
 * proxy.
 */
public class AzureAdTokenClient {

	private static final Logger log = LoggerFactory.getLogger(AzureAdTokenClient.class);

	private final String tokenUrl; // e.g.
									// https://login.microsoftonline.com/{tenantId}/oauth2/v2.0/token

	private final String clientId;

	private final String clientSecret;

	private final String scope; // e.g. https://cognitiveservices.azure.com/.default

	private final WebClient webClient; // configured with optional proxy

	private static class TokenHolder {

		final String value;

		final Instant expiresAt;

		TokenHolder(String value, Instant expiresAt) {
			this.value = value;
			this.expiresAt = expiresAt;
		}

		boolean valid() {
			return value != null && expiresAt != null && Instant.now().isBefore(expiresAt);
		}

	}

	private final AtomicReference<TokenHolder> cache = new AtomicReference<>(null);

	public AzureAdTokenClient(String authorityHost, String tenantId, String clientId, String clientSecret, String scope,
			String httpProxy, String httpsProxy) {
		Assert.hasText(tenantId, "tenantId must not be empty");
		Assert.hasText(clientId, "clientId must not be empty");
		Assert.hasText(clientSecret, "clientSecret must not be empty");
		Assert.hasText(scope, "scope must not be empty");
		String host = (authorityHost == null || authorityHost.isBlank()) ? "https://login.microsoftonline.com"
				: authorityHost;
		this.tokenUrl = host.replaceAll("/$", "") + "/" + tenantId + "/oauth2/v2.0/token";
		this.clientId = clientId;
		this.clientSecret = clientSecret;
		this.scope = scope;

		HttpClient httpClient = HttpClient.create();
		// Configure proxy only for token acquisition
		String proxy = (httpsProxy != null && !httpsProxy.isBlank()) ? httpsProxy
				: (httpProxy != null ? httpProxy : null);
		if (proxy != null && !proxy.isBlank()) {
			try {
				URI proxyUri = URI.create(proxy);
				httpClient = httpClient.proxy(typeSpec -> {
					ProxyProvider.Builder builder = typeSpec.type(ProxyProvider.Proxy.HTTP)
						.host(proxyUri.getHost())
						.port(proxyUri.getPort() > 0 ? proxyUri.getPort()
								: ("https".equalsIgnoreCase(proxyUri.getScheme()) ? 443 : 80));
					if (proxyUri.getUserInfo() != null && !proxyUri.getUserInfo().isBlank()) {
						String[] up = proxyUri.getUserInfo().split(":", 2);
						String user = up.length > 0 ? up[0] : "";
						String pass = up.length > 1 ? up[1] : "";
						builder.username(user).password(s -> pass);
					}
				});
				log.info("AzureAdTokenClient configured with proxy {}", proxy);
			}
			catch (Exception e) {
				log.warn("Invalid proxy URL '{}', ignoring.", proxy, e);
			}
		}
		this.webClient = WebClient.builder().clientConnector(new ReactorClientHttpConnector(httpClient)).build();
	}

	public synchronized String getTokenBlocking() {
		TokenHolder current = cache.get();
		if (current != null && current.valid()) {
			return current.value;
		}
		Map<String, Object> resp = fetchToken().block(Duration.ofSeconds(30));
		Assert.state(resp != null, "Azure AD token response is null");
		String accessToken = (String) resp.get("access_token");
		Number expiresIn = (Number) resp.get("expires_in");
		Assert.hasText(accessToken, "Azure AD access_token is empty");
		long seconds = (expiresIn != null ? expiresIn.longValue() : 3600L);
		// refresh 60 seconds earlier
		Instant expiresAt = Instant.now().plusSeconds(Math.max(60, seconds - 60));
		TokenHolder next = new TokenHolder(accessToken, expiresAt);
		cache.set(next);
		return next.value;
	}

	@SuppressWarnings("unchecked")
	public Mono<Map<String, Object>> fetchToken() {
		MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
		form.add("grant_type", "client_credentials");
		form.add("client_id", clientId);
		form.add("client_secret", clientSecret);
		form.add("scope", scope);
		return (Mono<Map<String, Object>>) (Mono<?>) webClient.post()
			.uri(tokenUrl)
			.contentType(MediaType.APPLICATION_FORM_URLENCODED)
			.body(BodyInserters.fromFormData(form))
			.retrieve()
			.bodyToMono(Map.class);
	}

}
