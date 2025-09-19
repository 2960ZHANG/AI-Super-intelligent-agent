package com.zfyedu.aitoursuperintelligentagent.app;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class LoveAppTest {
@Resource
private LoveApp loveApp;
    @Test
    void doChatWithReport() {
        String chatId = UUID.randomUUID().toString();
        // 第一轮
        String message = "你好，我是大学生ax，我想让另一半vvv更爱我，但我不知道该怎么做";
        LoveApp.LoveReport loveReport = loveApp.doChatWithReport(message, chatId);
        Assertions.assertNotNull(loveReport);
        // 第二轮
     message = "我是谁？";
         loveReport = loveApp.doChatWithReport(message, chatId);
        Assertions.assertNotNull(loveReport);
    }

    @Test
    void doChatWithReport2() {
        String chatId = UUID.randomUUID().toString();
        // 第一轮
        String message = "我是谁？";
        LoveApp.LoveReport loveReport = loveApp.doChatWithReport(message, chatId);
        Assertions.assertNotNull(loveReport);
    }

    @Test
    void doChatWithRag() {
        String chatId = UUID.randomUUID().toString();
        String message = "我现在是单身，给我推荐一些恋爱对象，我喜欢摄影";
        String answer =  loveApp.doChatWithRAG(message, chatId);
        Assertions.assertNotNull(answer);
    }

}