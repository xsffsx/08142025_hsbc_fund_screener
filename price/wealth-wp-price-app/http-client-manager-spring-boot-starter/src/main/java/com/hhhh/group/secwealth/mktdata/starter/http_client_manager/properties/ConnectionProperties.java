/*
 */
package com.hhhh.group.secwealth.mktdata.starter.http_client_manager.properties;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.hhhh.group.secwealth.mktdata.starter.http_client_manager.bean.Connection;

import lombok.Getter;
import lombok.Setter;

@ConfigurationProperties(prefix = "")
@Getter
@Setter
public class ConnectionProperties {

    private Map<String, Connection> connection = new HashMap<>();

}
