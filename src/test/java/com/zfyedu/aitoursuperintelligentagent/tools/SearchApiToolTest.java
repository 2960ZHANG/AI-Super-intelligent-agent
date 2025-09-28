package com.zfyedu.aitoursuperintelligentagent.tools;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class SearchApiToolTest {
    @Value("${spring.search-api.api-key}")
    private String key;
    @Test
    void searchWeb() {


        SearchApiTool searchApiTool1 = new SearchApiTool(key);
        String s = searchApiTool1.searchWeb("2024年奥运会举办城市");
        System.out.println(s);
        Assertions.assertNotNull(s);
    }
}