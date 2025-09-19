package com.zfyedu.aitoursuperintelligentagent.rag;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.ai.document.Document;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class LoveAppDocumentLoadTest {

    @Resource
    private LoveAppDocumentLoad loveAppDocumentLoad;
    @Test
    void loadMarkdown() {
        List<Document> documents = loveAppDocumentLoad.loadMarkdown();
        Assertions.assertNotNull(documents);

    }
}