package com.dummy.wpb.product.config;

import com.dummy.wpb.product.entity.TokenizedGoldProduct;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Configure Tokenized Gold product info
 */
@Component
@ConfigurationProperties(prefix = "product.tokenized-gold")
@Data
public class TokenizedGoldProductConfig {

    private List<TokenizedGoldProduct> products;
}
