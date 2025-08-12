package com.alibaba.cloud.ai.util.azure;

import org.springframework.ai.model.ApiKey;
import org.springframework.util.Assert;

/**
 * ApiKey implementation backed by Azure AD bearer token provider.
 */
public class AzureAdBearerTokenApiKey implements ApiKey {

	private final AzureAdTokenClient tokenClient;

	public AzureAdBearerTokenApiKey(AzureAdTokenClient tokenClient) {
		Assert.notNull(tokenClient, "tokenClient must not be null");
		this.tokenClient = tokenClient;
	}

	@Override
	public String getValue() {
		return tokenClient.getTokenBlocking();
	}

}
