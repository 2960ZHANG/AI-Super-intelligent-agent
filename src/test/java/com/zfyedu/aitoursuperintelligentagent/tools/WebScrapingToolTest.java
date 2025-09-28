package com.zfyedu.aitoursuperintelligentagent.tools;

import com.zfyedu.aitoursuperintelligentagent.rag.love.LoveAppDocumentLoad;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class WebScrapingToolTest {
@Resource
WebScrapingTool webScrapingTool;
    @Test
    void scrapeWebPage() {
        String s = webScrapingTool.scrapeWebPage("https://docs.springframework.org.cn/spring-ai/reference/api/tools.html#_declarative_specification_tool");
        System.out.println(s);
        Assertions.assertNotNull(s);
    }
}