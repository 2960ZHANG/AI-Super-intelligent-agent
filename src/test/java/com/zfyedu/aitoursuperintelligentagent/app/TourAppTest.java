package com.zfyedu.aitoursuperintelligentagent.app;

import cn.hutool.core.lang.UUID;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TourAppTest {

    @Resource
    private TourApp app;
    @Test
    void doChatWithRAG() {
        String chatid = UUID.randomUUID().toString();
        String message = "我想去香港旅行，怎么做计划";
        String result = app.doChatWithRAG(message,chatid );
        Assertions.assertNotNull(result);
    }
}