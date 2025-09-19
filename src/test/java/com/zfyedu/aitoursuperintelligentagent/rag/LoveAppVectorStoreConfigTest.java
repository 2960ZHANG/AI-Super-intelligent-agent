package com.zfyedu.aitoursuperintelligentagent.rag;

import com.zfyedu.aitoursuperintelligentagent.rag.love.LoveAppVectorStoreConfig;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class LoveAppVectorStoreConfigTest {

    @Resource
    LoveAppVectorStoreConfig simpleVectorStore;
    @Resource
    EmbeddingModel deshSopeEmbeddingModel;

    @Test
    void loveAppVectorStore() {

        VectorStore vectorStore =simpleVectorStore.loveAppVectorStore(deshSopeEmbeddingModel);
        assertNotNull(vectorStore);


    }
}