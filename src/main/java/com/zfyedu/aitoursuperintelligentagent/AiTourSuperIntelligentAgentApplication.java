package com.zfyedu.aitoursuperintelligentagent;

import com.zfyedu.aitoursuperintelligentagent.config.PgVectorStoreConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AiTourSuperIntelligentAgentApplication {

    public static void main(String[] args) {
        SpringApplication.run(AiTourSuperIntelligentAgentApplication.class, args);
        System.out.println("AI旅游超级智能体启动启动>>>>>");
    }

}
