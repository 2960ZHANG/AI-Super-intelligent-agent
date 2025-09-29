package com.zfyedu.aitoursuperintelligentagent.tools;

import org.springframework.ai.support.ToolCallbacks;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ToolRegistration {
    @Value("${spring.search-api.api-key}")
    private String apiKey;

    @Bean
    public ToolCallback[] allTools(){
        FileTool fileTool = new FileTool();
        ResourceDownLoadTool resourceDownLoadTool = new ResourceDownLoadTool();
        WebScrapingTool webScrapingTool = new WebScrapingTool();
        WindowsCommandTool windowsCommandTool = new WindowsCommandTool();
        SearchApiTool searchApiTool = new SearchApiTool(apiKey);
        TerminateTool terminateTool = new TerminateTool();

        return ToolCallbacks.from(fileTool,
                resourceDownLoadTool,
                webScrapingTool,
                windowsCommandTool,
                searchApiTool,
                terminateTool)
                ;
    }
}
