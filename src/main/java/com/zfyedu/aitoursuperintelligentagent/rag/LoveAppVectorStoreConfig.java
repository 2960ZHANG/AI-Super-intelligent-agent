package com.zfyedu.aitoursuperintelligentagent.rag;

import jakarta.annotation.Resource;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoveAppVectorStoreConfig {

    @Resource
    private  LoveAppDocumentLoad loveAppDocumentLoad;

    @Bean
    VectorStore loveAppVectorStore(EmbeddingModel deshSopeEmbeddingModel) {
        SimpleVectorStore vectorStore = SimpleVectorStore.builder(deshSopeEmbeddingModel).build();
        //添加所有读取到的文档
        vectorStore.doAdd(loveAppDocumentLoad.loadMarkdown());
            return vectorStore;

    }
}
