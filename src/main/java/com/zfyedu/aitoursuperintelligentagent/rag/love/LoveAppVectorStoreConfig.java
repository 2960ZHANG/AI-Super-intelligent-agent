package com.zfyedu.aitoursuperintelligentagent.rag.love;

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
 public  VectorStore loveAppVectorStore(EmbeddingModel deshSopeEmbeddingModel) {
        SimpleVectorStore loveVectorStore = SimpleVectorStore.builder(deshSopeEmbeddingModel).build();
        //添加所有读取到的文档
        loveVectorStore.doAdd(loveAppDocumentLoad.loadMarkdown());
            return loveVectorStore;

    }
}
