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
        String loveReport;
        // 第一轮
        String message = "你好，我是渣男ppp";
         loveReport = loveApp.doChatWithRAG(message, chatId);
        System.out.println(loveReport);
        // 第二轮
        message = "我的另一半叫xiaox";
        loveReport = loveApp.doChatWithRAG(message, chatId);
        System.out.println(loveReport);
        Assertions.assertNotNull(loveReport);
        // 第三轮
        message = "我的另一半叫什么来着？帮我回忆一下";
        loveReport = loveApp.doChatWithRAG(message, chatId);
        System.out.println(loveReport);
        Assertions.assertNotNull(loveReport);
    }
}