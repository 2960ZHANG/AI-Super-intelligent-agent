package com.zfyedu.aitoursuperintelligentagent.tools;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class WindowsCommandToolTest {
@Resource
WindowsCommandTool windowsCommandTool;
    @Test
    void executeWindowsCommand() {
        String s = windowsCommandTool.executeWindowsCommand("ipconfig");
        System.out.println(s);
        Assertions.assertNotNull(s);
    }
}