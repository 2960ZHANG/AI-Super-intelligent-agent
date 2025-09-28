package com.zfyedu.aitoursuperintelligentagent.tools;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class FileToolTest {

    @Resource
    FileTool fileTool;
    @Test
    void readFile() {
        String filePath = "五零密集.txt";
        String s = fileTool.readFile(filePath);
        Assertions.assertNotNull(s);
    }

    @Test
    void writeFile() {

        String filePath = "五零密集.txt";
        String content = "猴子 海绵 章鱼";
        String s = fileTool.writeFile(filePath, content);
        Assertions.assertNotNull(s);
    }
}