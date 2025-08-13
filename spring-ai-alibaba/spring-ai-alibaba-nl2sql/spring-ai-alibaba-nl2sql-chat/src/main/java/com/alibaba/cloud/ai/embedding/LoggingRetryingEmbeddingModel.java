package com.alibaba.cloud.ai.embedding;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.BatchingStrategy;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.embedding.EmbeddingOptions;
import org.springframework.ai.embedding.EmbeddingRequest;
import org.springframework.ai.embedding.EmbeddingResponse;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * EmbeddingModel 装饰器：
 * - 记录批次规模日志（条数、字符数、估算token）
 * - 对 429/503/网络瞬断等错误进行指数退避重试（带抖动）
 * - 不改变调用方业务逻辑
 */
public class LoggingRetryingEmbeddingModel implements EmbeddingModel {
    private static final Logger log = LoggerFactory.getLogger(LoggingRetryingEmbeddingModel.class);

    private final EmbeddingModel delegate;
    private final int maxRetries;
    private final long baseDelayMs;
    private final long jitterMs;

    public LoggingRetryingEmbeddingModel(EmbeddingModel delegate, int maxRetries, long baseDelayMs, long jitterMs) {
        this.delegate = delegate;
        this.maxRetries = Math.max(0, maxRetries);
        this.baseDelayMs = Math.max(0, baseDelayMs);
        this.jitterMs = Math.max(0, jitterMs);
    }

    @Override
    public EmbeddingResponse call(EmbeddingRequest request) {
        int size = request.getInstructions() != null ? request.getInstructions().size() : 0;
        int totalChars = 0;
        if (request.getInstructions() != null) {
            for (String s : request.getInstructions()) {
                if (s != null) totalChars += s.length();
            }
        }
        int estTokens = Math.max(1, totalChars / 4);
        log.info("[Embedding] request stats: items={}, totalChars={}, estTokens≈{}", size, totalChars, estTokens);

        for (int attempt = 1; ; attempt++) {
            try {
                return delegate.call(request);
            } catch (Exception e) {
                if (!isRetryable(e) || attempt > maxRetries) {
                    log.error("[Embedding] failed after {} attempts: {}", attempt, summarize(e));
                    throw e;
                }
                long backoff = backoffMs(attempt);
                log.warn("[Embedding] retry {}/{} in {}ms due to {}", attempt, maxRetries, backoff, summarize(e));
                sleepQuietly(backoff);
            }
        }
    }

    @Override
    public float[] embed(String text) {
        int totalChars = text != null ? text.length() : 0;
        int estTokens = Math.max(1, totalChars / 4);
        log.info("[Embedding] single text stats: chars={}, estTokens≈{}", totalChars, estTokens);
        for (int attempt = 1; ; attempt++) {
            try {
                return delegate.embed(text);
            } catch (Exception e) {
                if (!isRetryable(e) || attempt > maxRetries) {
                    log.error("[Embedding] single text failed after {} attempts: {}", attempt, summarize(e));
                    throw e;
                }
                long backoff = backoffMs(attempt);
                log.warn("[Embedding] single text retry {}/{} in {}ms due to {}", attempt, maxRetries, backoff, summarize(e));
                sleepQuietly(backoff);
            }
        }
    }

    @Override
    public float[] embed(Document document) {
        String text = document != null ? document.getText() : null;
        return embed(text);
    }

    @Override
    public List<float[]> embed(List<Document> documents, EmbeddingOptions options, BatchingStrategy batchingStrategy) {
        int n = documents != null ? documents.size() : 0;
        int totalChars = 0;
        if (documents != null) {
            for (Document d : documents) {
                String t = d != null ? d.getText() : null;
                if (t != null) totalChars += t.length();
            }
        }
        int estTokens = Math.max(1, totalChars / 4);
        log.info("[Embedding] batch stats: count={}, totalChars={}, estTokens≈{}", n, totalChars, estTokens);

        for (int attempt = 1; ; attempt++) {
            try {
                return delegate.embed(documents, options, batchingStrategy);
            } catch (Exception e) {
                if (!isRetryable(e) || attempt > maxRetries) {
                    log.error("[Embedding] batch failed after {} attempts: {}", attempt, summarize(e));
                    throw e;
                }
                long backoff = backoffMs(attempt);
                log.warn("[Embedding] batch retry {}/{} in {}ms due to {}", attempt, maxRetries, backoff, summarize(e));
                sleepQuietly(backoff);
            }
        }
    }

    private boolean isRetryable(Throwable e) {
        String msg = summarize(e).toLowerCase();
        // 常见可重试：429/503/timeout/io/netty断开/连接重置
        return msg.contains("429") || msg.contains("too many requests") ||
               msg.contains("503") || msg.contains("service unavailable") ||
               msg.contains("timeout") || msg.contains("timed out") ||
               msg.contains("connection reset") || msg.contains("rst_stream") ||
               msg.contains("goaway") || msg.contains("broken pipe") ||
               msg.contains("failure when writing tls control frames") ||
               msg.contains("sslexception");
    }

    private long backoffMs(int attempt) {
        long exp = (long) Math.pow(2, Math.max(0, attempt - 1));
        long delay = Math.min(60_000L, baseDelayMs * exp);
        long jitter = jitterMs > 0 ? ThreadLocalRandom.current().nextLong(jitterMs) : 0L;
        return delay + jitter;
    }

    private void sleepQuietly(long ms) {
        try { Thread.sleep(ms); } catch (InterruptedException ie) { Thread.currentThread().interrupt(); }
    }

    private String summarize(Throwable e) {
        String s = e.toString();
        if (s.length() > 300) return s.substring(0, 300) + "...";
        return s;
    }
}

