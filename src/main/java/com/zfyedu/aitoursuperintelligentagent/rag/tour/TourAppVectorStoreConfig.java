package com.zfyedu.aitoursuperintelligentagent.rag.tour;


import jakarta.annotation.Resource;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
public class TourAppVectorStoreConfig {

    @Resource
    private TourAppDocumentLoad tourAppDocumentLoad;

    @Bean
 public  VectorStore tourAppVectorStore(EmbeddingModel deshSopeEmbeddingModel) {
        SimpleVectorStore tourVectorStore = SimpleVectorStore.builder(deshSopeEmbeddingModel).build();
        //添加所有读取到的文档
        tourVectorStore.doAdd(tourAppDocumentLoad.loadMarkdown());
            return tourVectorStore;

    }
}
