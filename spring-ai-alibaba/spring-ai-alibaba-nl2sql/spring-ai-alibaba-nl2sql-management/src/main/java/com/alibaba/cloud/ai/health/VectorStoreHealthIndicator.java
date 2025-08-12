package com.alibaba.cloud.ai.health;

import com.alibaba.cloud.ai.service.simple.AgentVectorStoreManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 向量存储健康检查 - SimpleVectorStore 最小 add + search 测试 - AgentVectorStoreManager getOrCreate +
 * addDocuments + similaritySearch 测试
 */
@Component("vector-store")
public class VectorStoreHealthIndicator implements HealthIndicator {

	private static final Logger log = LoggerFactory.getLogger(VectorStoreHealthIndicator.class);

	private final EmbeddingModel embeddingModel;

	private final AgentVectorStoreManager agentVectorStoreManager;

	public VectorStoreHealthIndicator(EmbeddingModel embeddingModel, AgentVectorStoreManager agentVectorStoreManager) {
		this.embeddingModel = embeddingModel;
		this.agentVectorStoreManager = agentVectorStoreManager;
	}

	@Override
	public Health health() {
		Map<String, Object> details = new HashMap<>();
		boolean ok = true;

		try {
			boolean simpleOk = checkSimpleVectorStore(details);
			ok &= simpleOk;
		}
		catch (Exception e) {
			ok = false;
			Map<String, Object> m = new HashMap<>();
			m.put("status", "DOWN");
			m.put("error", e.getMessage());
			details.put("simpleVectorStore", m);
		}

		try {
			boolean agentOk = checkAgentVectorStore(details);
			ok &= agentOk;
		}
		catch (Exception e) {
			ok = false;
			Map<String, Object> m = new HashMap<>();
			m.put("status", "DOWN");
			m.put("error", e.getMessage());
			details.put("agentVectorStoreManager", m);
		}

		return (ok ? Health.up() : Health.down()).withDetails(details).build();
	}

	private boolean checkSimpleVectorStore(Map<String, Object> details) {
		long start = System.currentTimeMillis();
		Map<String, Object> m = new HashMap<>();
		try {
			// 构建临时内存向量库
			SimpleVectorStore store = SimpleVectorStore.builder(embeddingModel).build();
			// 构造测试文档
			String id = "hc-" + UUID.randomUUID();
			String content = "healthcheck simple vector store ping";
			Document doc = new Document(id, content, Map.of("vectorType", "healthcheck"));
			store.add(List.of(doc));

			// 进行一次相似度搜索
			List<Document> results = store.similaritySearch(SearchRequest.builder().query("ping").topK(1).build());

			long cost = System.currentTimeMillis() - start;
			boolean up = results != null && !results.isEmpty();

			m.put("status", up ? "UP" : "DOWN");
			m.put("responseTime", cost + "ms");
			m.put("testAddSuccess", true);
			m.put("testSearchResults", results == null ? 0 : results.size());
			m.put("storeType", "SimpleVectorStore");
			details.put("simpleVectorStore", m);
			return up;
		}
		catch (Exception e) {
			long cost = System.currentTimeMillis() - start;
			m.put("status", "DOWN");
			m.put("responseTime", cost + "ms");
			m.put("error", e.getMessage());
			m.put("storeType", "SimpleVectorStore");
			details.put("simpleVectorStore", m);
			return false;
		}
	}

	private boolean checkAgentVectorStore(Map<String, Object> details) {
		long start = System.currentTimeMillis();
		Map<String, Object> m = new HashMap<>();
		try {
			String agentId = "healthcheck-agent";
			// 创建或获取 agent store，并添加一条文档
			String id = "hc-agent-" + UUID.randomUUID();
			String content = "healthcheck agent vector store ping";
			Document doc = new Document(id, content, Map.of("vectorType", "healthcheck"));
			agentVectorStoreManager.addDocuments(agentId, List.of(doc));

			// 相似度搜索
			List<Document> results = agentVectorStoreManager.similaritySearch(agentId, "ping", 1);

			long cost = System.currentTimeMillis() - start;
			boolean up = results != null && !results.isEmpty();

			// 统计信息
			Map<String, Object> stats = agentVectorStoreManager.getStatistics();
			Object totalAgents = stats != null ? stats.getOrDefault("totalAgents", 0) : 0;

			m.put("status", up ? "UP" : "DOWN");
			m.put("responseTime", cost + "ms");
			m.put("testAgentCreated", true);
			m.put("testSearchResults", results == null ? 0 : results.size());
			m.put("activeAgents", totalAgents);
			details.put("agentVectorStoreManager", m);
			return up;
		}
		catch (Exception e) {
			long cost = System.currentTimeMillis() - start;
			m.put("status", "DOWN");
			m.put("responseTime", cost + "ms");
			m.put("error", e.getMessage());
			details.put("agentVectorStoreManager", m);
			return false;
		}
	}

}
