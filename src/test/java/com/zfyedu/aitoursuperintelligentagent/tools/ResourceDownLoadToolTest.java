package com.zfyedu.aitoursuperintelligentagent.tools;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class ResourceDownLoadToolTest {
@Resource
ResourceDownLoadTool resourceDownLoadTool;
    @Test
    void downloadResource() {
        String s = resourceDownLoadTool.downloadResource("https://ts1.tc.mm.bing.net/th/id/R-C.291868e0f5a49027c68330a0f55afb9b?rik=Xavsjl0gYaDOaA&riu=http%3a%2f%2f5b0988e595225.cdn.sohucs.com%2fimages%2f20200418%2f8a40f54528fe4c02b9faf81eff01558a.jpeg&ehk=Dq8JoRuf73TM1vInWzUivJ4ZKAEd4wIUZ4qx%2f6QiFD8%3d&risl=&pid=ImgRaw&r=0", "大西北.png");
        Assertions.assertNotNull(s);
    }
}