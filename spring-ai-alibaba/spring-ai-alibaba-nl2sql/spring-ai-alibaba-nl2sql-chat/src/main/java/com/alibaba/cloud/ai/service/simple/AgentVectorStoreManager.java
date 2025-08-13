/*
 * Copyright 2024-2025 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alibaba.cloud.ai.service.simple;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.filter.Filter;
import org.springframework.ai.vectorstore.filter.FilterExpressionBuilder;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 智能体向量存储管理器 为每个智能体提供独立的向量存储实例，确保数据隔离
 */
@Component
public class AgentVectorStoreManager {

	private static final Logger log = LoggerFactory.getLogger(AgentVectorStoreManager.class);

	private final Map<String, SimpleVectorStore> agentStores = new ConcurrentHashMap<>();

	private final EmbeddingModel embeddingModel;

	public AgentVectorStoreManager(EmbeddingModel embeddingModel) {
		long startTime = System.currentTimeMillis();
		this.embeddingModel = embeddingModel;
		log.info("🚀 AgentVectorStoreManager initialized with EmbeddingModel: {} ({}ms)",
				embeddingModel.getClass().getSimpleName(), System.currentTimeMillis() - startTime);
		log.debug("📋 Agent stores map initialized: {}", agentStores.getClass().getSimpleName());
	}

	/**
	 * 获取或创建智能体专用的向量存储
	 * @param agentId 智能体ID
	 * @return 智能体专用的SimpleVectorStore实例
	 */
	public SimpleVectorStore getOrCreateVectorStore(String agentId) {
		long startTime = System.currentTimeMillis();
		if (agentId == null || agentId.trim().isEmpty()) {
			throw new IllegalArgumentException("Agent ID cannot be null or empty");
		}

		log.debug("🔍 Getting or creating vector store for agent: {}", agentId);
		SimpleVectorStore store = agentStores.computeIfAbsent(agentId, id -> {
			log.info("🆕 Creating new vector store for agent: {}", id);
			SimpleVectorStore newStore = SimpleVectorStore.builder(embeddingModel).build();
			log.debug("✅ Vector store created for agent: {} ({}ms)", id, System.currentTimeMillis() - startTime);
			return newStore;
		});

		if (agentStores.containsKey(agentId)) {
			log.debug("♻️ Using existing vector store for agent: {} ({}ms)", agentId,
					System.currentTimeMillis() - startTime);
		}

		return store;
	}

	/**
	 * 为指定智能体添加文档
	 * @param agentId 智能体ID
	 * @param documents 要添加的文档列表
	 */
	public void addDocuments(String agentId, List<Document> documents) {
		long startTime = System.currentTimeMillis();
		if (documents == null || documents.isEmpty()) {
			log.warn("⚠️ No documents to add for agent: {}", agentId);
			return;
		}

		log.debug("📄 Adding {} documents to vector store for agent: {}", documents.size(), agentId);
		int n = documents.size();
		int totalChars = documents.stream().mapToInt(d -> d.getText() != null ? d.getText().length() : 0).sum();
		int estTokens = Math.max(1, totalChars / 4); // 粗略估算，避免0，便于排序
		log.info("[Embedding] batch stats: count={}, totalChars={}, estTokens≈{}", n, totalChars, estTokens);
		SimpleVectorStore store = getOrCreateVectorStore(agentId);
		store.add(documents);
		long duration = System.currentTimeMillis() - startTime;
		log.info("✅ Added {} documents to vector store for agent: {} ({}ms)", documents.size(), agentId, duration);
		log.debug("📊 Agent {} now has approximately {} documents", agentId, getDocumentCount(agentId));
	}

	/**
	 * 为指定智能体搜索相似文档
	 * @param agentId 智能体ID
	 * @param query 查询文本
	 * @param topK 返回结果数量
	 * @return 相似文档列表
	 */
	public List<Document> similaritySearch(String agentId, String query, int topK) {
		long startTime = System.currentTimeMillis();
		log.debug("🔍 Searching for agent: {}, query: '{}', topK: {}", agentId, query, topK);

		SimpleVectorStore store = agentStores.get(agentId);
		if (store == null) {
			log.warn("⚠️ No vector store found for agent: {}", agentId);
			log.debug("📋 Available agents: {}", agentStores.keySet());
			return Collections.emptyList();
		}

		List<Document> results = store.similaritySearch(
				org.springframework.ai.vectorstore.SearchRequest.builder().query(query).topK(topK).build());
		long duration = System.currentTimeMillis() - startTime;
		log.info("🎯 Found {} similar documents for agent: {} ({}ms)", results.size(), agentId, duration);
		log.debug("📝 Query: '{}', Results: {}", query, results.size());
		return results;
	}

	/**
	 * 为指定智能体搜索相似文档（带过滤条件）
	 * @param agentId 智能体ID
	 * @param query 查询文本
	 * @param topK 返回结果数量
	 * @param vectorType 向量类型过滤
	 * @return 相似文档列表
	 */
	public List<Document> similaritySearchWithFilter(String agentId, String query, int topK, String vectorType) {
		long startTime = System.currentTimeMillis();
		log.debug("🔍 Filtered search for agent: {}, query: '{}', topK: {}, vectorType: {}", agentId, query, topK,
				vectorType);

		SimpleVectorStore store = agentStores.get(agentId);
		if (store == null) {
			log.warn("⚠️ No vector store found for agent: {}", agentId);
			log.debug("📋 Available agents: {}", agentStores.keySet());
			return Collections.emptyList();
		}

		FilterExpressionBuilder builder = new FilterExpressionBuilder();
		Filter.Expression expression = builder.eq("vectorType", vectorType).build();

		List<Document> results = store.similaritySearch(org.springframework.ai.vectorstore.SearchRequest.builder()
			.query(query)
			.topK(topK)
			.filterExpression(expression)
			.build());

		long duration = System.currentTimeMillis() - startTime;
		log.info("🎯 Found {} filtered documents for agent: {} with vectorType: {} ({}ms)", results.size(), agentId,
				vectorType, duration);
		log.debug("📝 Query: '{}', VectorType: {}, Results: {}", query, vectorType, results.size());
		return results;
	}

	/**
	 * 删除指定智能体的所有数据
	 * @param agentId 智能体ID
	 */
	public void deleteAgentData(String agentId) {
		SimpleVectorStore removed = agentStores.remove(agentId);
		if (removed != null) {
			log.info("Deleted all vector data for agent: {}", agentId);
		}
		else {
			log.warn("No vector store found to delete for agent: {}", agentId);
		}
	}

	/**
	 * 删除指定智能体的特定文档
	 * @param agentId 智能体ID
	 * @param documentIds 要删除的文档ID列表
	 */
	public void deleteDocuments(String agentId, List<String> documentIds) {
		SimpleVectorStore store = agentStores.get(agentId);
		if (store == null) {
			log.warn("No vector store found for agent: {}", agentId);
			return;
		}

		if (documentIds != null && !documentIds.isEmpty()) {
			store.delete(documentIds);
			log.info("Deleted {} documents from vector store for agent: {}", documentIds.size(), agentId);
		}
	}

	/**
	 * 删除指定智能体的特定类型文档
	 * @param agentId 智能体ID
	 * @param vectorType 向量类型
	 */
	public void deleteDocumentsByType(String agentId, String vectorType) {
		SimpleVectorStore store = agentStores.get(agentId);
		if (store == null) {
			log.warn("No vector store found for agent: {}", agentId);
			return;
		}

		try {
			FilterExpressionBuilder builder = new FilterExpressionBuilder();
			Filter.Expression expression = builder.eq("vectorType", vectorType).build();

			List<Document> documents = store.similaritySearch(org.springframework.ai.vectorstore.SearchRequest.builder()
				.topK(Integer.MAX_VALUE)
				.filterExpression(expression)
				.build());

			if (!documents.isEmpty()) {
				List<String> documentIds = documents.stream().map(Document::getId).toList();
				store.delete(documentIds);
				log.info("Deleted {} documents of type '{}' for agent: {}", documents.size(), vectorType, agentId);
			}
			else {
				log.info("No documents of type '{}' found for agent: {}", vectorType, agentId);
			}
		}
		catch (Exception e) {
			log.error("Failed to delete documents by type for agent: {}", agentId, e);
			throw new RuntimeException("Failed to delete documents by type: " + e.getMessage(), e);
		}
	}

	/**
	 * 检查智能体是否有向量数据
	 * @param agentId 智能体ID
	 * @return 是否有数据
	 */
	public boolean hasAgentData(String agentId) {
		return agentStores.containsKey(agentId);
	}

	/**
	 * 获取智能体的文档数量（估算）
	 * @param agentId 智能体ID
	 * @return 文档数量
	 */
	public int getDocumentCount(String agentId) {
		SimpleVectorStore store = agentStores.get(agentId);
		// 空库直接返回 0；仅在 hasData=true 时执行一次非空查询
		if (store == null || !hasAgentData(agentId)) {
			return 0;
		}

		try {
			List<Document> allDocs = store.similaritySearch(org.springframework.ai.vectorstore.SearchRequest.builder()
				.query("vector-count-probe")
				.topK(10_000)
				.build());
			return allDocs.size();
		}
		catch (Exception e) {
			log.warn("Failed to get document count for agent: {}", agentId, e);
			return 0;
		}
	}

	/**
	 * 获取所有有数据的智能体ID
	 * @return 智能体ID集合
	 */
	public Set<String> getAllAgentIds() {
		return Set.copyOf(agentStores.keySet());
	}

	/**
	 * 获取向量存储统计信息
	 * @return 统计信息
	 */
	public Map<String, Object> getStatistics() {
		Map<String, Object> stats = new ConcurrentHashMap<>();
		stats.put("totalAgents", agentStores.size());
		stats.put("agentIds", getAllAgentIds());

		Map<String, Integer> agentDocCounts = new ConcurrentHashMap<>();
		agentStores.forEach((agentId, store) -> {
			agentDocCounts.put(agentId, getDocumentCount(agentId));
		});
		stats.put("documentCounts", agentDocCounts);

		return stats;
	}

}
