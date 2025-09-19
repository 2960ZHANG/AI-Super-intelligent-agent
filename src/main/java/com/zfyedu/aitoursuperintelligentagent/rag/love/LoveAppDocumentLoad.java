package com.zfyedu.aitoursuperintelligentagent.rag.love;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.markdown.MarkdownDocumentReader;
import org.springframework.ai.reader.markdown.config.MarkdownDocumentReaderConfig;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
@Component
@Slf4j
public class LoveAppDocumentLoad {


    private final ResourcePatternResolver resource;

    LoveAppDocumentLoad(ResourcePatternResolver resource) {
        this.resource = resource;
    }

    public List<Document> loadMarkdown() {

        ArrayList<Document> documents = new ArrayList<>();
        //加载全部的文档
        try {
            Resource[] resources = resource.getResources("classpath:document/love/*.md");
            //添加文档进列表
            for (Resource resource : resources) {
                String filename = resource.getFilename();

                MarkdownDocumentReaderConfig config = MarkdownDocumentReaderConfig.builder()
                        .withHorizontalRuleCreateDocument(true)
                        .withIncludeCodeBlock(false)
                        .withIncludeBlockquote(false)
                        .withAdditionalMetadata("filename", filename)//元信息（用于做更高的相似度查询）
                        .build();

                MarkdownDocumentReader reader = new MarkdownDocumentReader(resource, config);
                documents.addAll(reader.read());
            }
        } catch (IOException e) {
            log.error("Markdown文档加载失败 {} ：", e.getMessage());
        }

        return documents;

    }
}