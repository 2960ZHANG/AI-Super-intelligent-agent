package com.zfyedu.aitoursuperintelligentagent.rag;

import com.zfyedu.aitoursuperintelligentagent.app.LoveApp;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class LoveAppVectorStoreConfigTest {

    @Resource
    LoveAppVectorStoreConfig loveAppVectorStoreConfig;
    @Resource
    EmbeddingModel deshSopeEmbeddingModel;

    @Test
    void loveAppVectorStore() {
        VectorStore vectorStore = loveAppVectorStoreConfig.loveAppVectorStore(deshSopeEmbeddingModel);
        assertNotNull(vectorStore);


    }
}