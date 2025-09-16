package com.zfyedu.aitoursuperintelligentagent.app;

import cn.hutool.core.lang.UUID;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AiLaveAppTest {
@Resource
private LoveApp loveApp;
    @Test
    void doChat() {
        String chatId = UUID.randomUUID().toString();
        // 第一轮
        String message = "你好，我是渣男ppp";
        String answer = loveApp.doChat(message, chatId);
        System.out.println(answer);
        // 第二轮
        message = "我的另一半叫xiaox";
        answer = loveApp.doChat(message, chatId);
        System.out.println(answer);
        Assertions.assertNotNull(answer);
        // 第三轮
        message = "我的另一半叫什么来着？帮我回忆一下";
        answer = loveApp.doChat(message, chatId);
        System.out.println(answer);
        Assertions.assertNotNull(answer);
    }
}