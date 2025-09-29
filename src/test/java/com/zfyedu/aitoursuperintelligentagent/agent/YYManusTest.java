package com.zfyedu.aitoursuperintelligentagent.agent;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest


public class YYManusTest {
   @Resource
    YYManus yYManus;

   @Test
    public void testYYManus() {
       String message="我在广州图书馆，帮我找到5公里内合适的约会地点";
       String s = yYManus.run(message);
       Assertions.assertNotNull(s);
   }
}