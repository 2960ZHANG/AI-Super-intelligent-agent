package com.zfyedu.aitoursuperintelligentagent.tools;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

@Component
public class WebScrapingTool {

    private static final int TIMEOUT_MS = 10_000; // 10秒超时
    private static final List<String> ALLOWED_PROTOCOLS = Arrays.asList("http", "https");

    /**
     * 使用 Jsoup 抓取网页内容，返回标题和正文文本（AI 可读格式）
     *
     * @param url 要抓取的网页 URL（必须是完整 URL，如 https://example.com）
     * @return 抓取结果，包含标题和正文，或错误信息
     */
    @Tool( description = "Fetches the title and main text content from a web page. Input must be a valid HTTP/HTTPS URL.")
    public String scrapeWebPage(String url) {
        if (url == null || url.isBlank()) {
            return "Error: URL is empty or null.";
        }

        try {
            // 验证 URL 格式和协议
            URL parsedUrl = new URL(url);
            if (!ALLOWED_PROTOCOLS.contains(parsedUrl.getProtocol().toLowerCase())) {
                return "Error: Only HTTP and HTTPS URLs are allowed.";
            }

            // 使用 Jsoup 抓取网页
            Document doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36")
                    .timeout(TIMEOUT_MS)
                    .followRedirects(true)
                    .get();

            String title = doc.title().trim();
            String bodyText = doc.body().text(); // 提取纯文本（去除 HTML 标签）

            if (title.isEmpty() && bodyText.isEmpty()) {
                return "Warning: Page has no readable content.";
            }

            StringBuilder result = new StringBuilder();
            if (!title.isEmpty()) {
                result.append("Title: ").append(title).append("\n\n");
            }
            if (!bodyText.isEmpty()) {
                // 截断过长内容（避免 token 超限）
                String truncatedBody = bodyText.length() > 3000 ? bodyText.substring(0, 3000) + "..." : bodyText;
                result.append("Content: ").append(truncatedBody);
            }

            return result.toString();

        } catch (MalformedURLException e) {
            return "Error: Invalid URL format - " + e.getMessage();
        } catch (IOException e) {
            return "Error: Failed to fetch webpage - " + e.getMessage();
        } catch (Exception e) {
            return "Unexpected error during scraping: " + e.getMessage();
        }
    }
}
