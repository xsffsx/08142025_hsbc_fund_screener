/*
 * COPYRIGHT. hhhh HOLDINGS PLC 2018. ALL RIGHTS RESERVED.
 *
 * This software is only to be used for the purpose for which it has been
 * provided. No part of it is to be reproduced, disassembled, transmitted,
 * stored in a retrieval system nor translated in any human or computer
 * language in any way or for any other purposes whatsoever without the prior
 * written consent of hhhh Holdings plc.
 */
package com.hhhh.group.secwealth.mktdata.common.http_client_manager.properties;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hhhh.group.secwealth.mktdata.common.http_client_manager.bean.Connection;

@Component
public class ConnectionProperties {
	
	@Autowired
	private Connection connection;
	
    private Map<String, Connection> connectionMap = new HashMap<>();
    
    @PostConstruct
    public void init() {
    	connectionMap.put(connection.getDefaultHttpClientName(), connection);
    }

	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	public Map<String, Connection> getConnectionMap() {
		return connectionMap;
	}

	public void setConnectionMap(Map<String, Connection> connectionMap) {
		this.connectionMap = connectionMap;
	}
    
}
