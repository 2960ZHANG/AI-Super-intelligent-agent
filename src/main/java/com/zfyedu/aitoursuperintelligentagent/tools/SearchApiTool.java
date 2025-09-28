package com.zfyedu.aitoursuperintelligentagent.tools;


import org.springframework.ai.tool.annotation.Tool;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class SearchApiTool {

    private final WebClient webClient;
    private final String apiKey;
    private final String url = "https://www.searchapi.io/api/v1/search";

    public SearchApiTool(@Value("${spring.searchapi.api-key}") String apiKey) {
        this.apiKey = apiKey;
        this.webClient = WebClient.builder()
                .baseUrl(url)
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(1024 * 1024)) // 1MB
                .build();
    }

    /**
     * 使用 SearchApi 执行联网搜索，返回最相关的摘要结果
     *
     * @param query 搜索关键词，例如 "2024年奥运会举办城市"
     * @return 搜索结果摘要（最多3条），或错误信息
     */
    @Tool(description = "Performs a real-time web search using SearchApi. Returns concise, relevant results. " +
          "Useful for answering questions about current events, facts, or recent information.")
    public String searchWeb(String query) {
        if (query == null || query.trim().isEmpty()) {
            return "Error: Search query is empty.";
        }

        try {
            // 调用 SearchApi（默认使用 Google 搜索）
            String jsonResponse = webClient
                    .get()
                    .uri(uriBuilder -> uriBuilder
                            .queryParam("engine","baidu")//百度搜索
                            .queryParam("q", query)
                            .queryParam("api_key", apiKey)
                            .build())
                    .retrieve()
                    .bodyToMono(String.class)
                    .timeout(Duration.ofSeconds(10))
                    .block(); // 阻塞等待（AI 工具通常同步）

            // 简化解析：提取 organic_results 中的 title + snippet
            // 实际项目建议使用 Jackson 反序列化为 POJO
            // 此处为简化，用字符串处理（仅演示）
            // 更健壮做法：定义 SearchApiResponse 类并用 bodyToMono(SearchApiResponse.class)

            // ⚠️ 以下为简化版解析（生产环境请用 JSON 反序列化）
            if (jsonResponse == null || jsonResponse.contains("\"organic_results\":[]")) {
                return "No relevant results found for: " + query;
            }

            // 粗略提取前3条结果（仅用于演示）
            // 实际推荐：使用 ObjectMapper 反序列化
            String[] lines = jsonResponse.split("\\},\\s*\\{");
            StringBuilder result = new StringBuilder();
            int count = 0;

            for (String line : lines) {
                if (count >= 3) break;
                if (line.contains("\"title\"") && line.contains("\"snippet\"")) {
                    String title = extractField(line, "title");
                    String snippet = extractField(line, "snippet");
                    if (title != null && snippet != null) {
                        result.append(String.format("- %s: %s\n", title, snippet));
                        count++;
                    }
                }
            }

            return result.length() > 0 ? result.toString().trim() : "Search completed but no readable results.";

        } catch (WebClientResponseException e) {
            if (e.getStatusCode().value() == 401) {
                return "Error: Invalid SearchApi API key.";
            }
            return "SearchApi error (" + e.getStatusCode() + "): " + e.getResponseBodyAsString();
        } catch (Exception e) {
            return "Failed to perform search: " + e.getMessage();
        }
    }

    // 简易 JSON 字段提取（仅用于演示，生产环境请用 Jackson）
    private String extractField(String json, String fieldName) {
        String pattern = "\"" + fieldName + "\":\\s*\"([^\"]*)\"";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(pattern);
        java.util.regex.Matcher m = p.matcher(json);
        if (m.find()) {
            return m.group(1).replaceAll("\\\\\"", "\""); // 处理转义
        }
        return null;
    }
}
