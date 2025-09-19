package com.zfyedu.aitoursuperintelligentagent.app;


import com.zfyedu.aitoursuperintelligentagent.advisor.MyLoggerAdvisor;
import com.zfyedu.aitoursuperintelligentagent.chatmemory.FileBasedChatMemory;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class LoveApp {

    @jakarta.annotation.Resource
    private VectorStore simpleVectorStore;
    private final ChatModel dashScopeChatModel;
    private ChatClient chatClient;


    @Value("classpath:/tem/prompts/system-message.txt")
    private Resource systemResource;



    SystemPromptTemplate systemPromptTemplate;

    public LoveApp(ChatModel dashScopeChatModel) {
        this.dashScopeChatModel = dashScopeChatModel;
    }

    @PostConstruct
    public void init() {
        // 确保 systemResource 不为 null
        if (systemResource == null) {
            throw new IllegalStateException("System resource is not loaded. Check if the file exists at: classpath:/tem/prompts/system-message.txt");
        }
        //会话记忆
        //ChatMemory chatMemory = MessageWindowChatMemory.builder().build();
        //基于文件的会话记忆
        ChatMemory chatMemory = new FileBasedChatMemory(System.getProperty("user.dir") + "/chat-memory").builder().build();
        chatClient = ChatClient.builder(dashScopeChatModel)
                .defaultSystem(systemResource)
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
                .build();
    }

    public String doChat(String message, String chatId) {

        ChatResponse chatResponse = chatClient.prompt()
                .user(message)
                .advisors(
//                    new SimpleLoggerAdvisor(
//                            request -> "清纯少年的提问: " + request.prompt().getUserMessage(),
//                            response -> "超级懂哥的回答 " + response.getResult().getOutput().getText(),
//                            0
//
//                    )
                        //Re2
                        // new ReReadingAdvisor(),
                        //自定义日志
                        new MyLoggerAdvisor()
                )
                .call()
                .chatResponse();

        String resultText = chatResponse.getResult().getOutput().getText();
        return resultText;
    }


    /**
     * 恋爱报告类标题
     *
     * @param title      报告
     * @param suggestion 建议
     */
    public record LoveReport(String title, List<String> suggestion) {
    }

    public LoveReport doChatWithReport(String message, String chatId) {

        LoveReport report = chatClient.prompt()
                .system(systemResource + "每次对话后都要生成恋爱结果报告,标题为{用户名}的恋爱报告,内容为建议列表")
                .user(message)
                .advisors(
//                    new SimpleLoggerAdvisor(
//                            request -> "清纯少年的提问: " + request.prompt().getUserMessage(),
//                            response -> "超级懂哥的回答 " + response.getResult().getOutput().getText(),
//                            0
//
//                    )
                        //Re2
                        // new ReReadingAdvisor(),
                        //自定义日志
                        new MyLoggerAdvisor()
                )
                .call()
                .entity(LoveReport.class);
        log.info("Love report for {} is {}", chatId, report);
        return report;
    }


    public String doChatWithRAG(String message, String chatId) {
        ChatResponse chatResponse = chatClient.prompt()
                .user(message)

                .advisors(
                        new MyLoggerAdvisor(),
                        new QuestionAnswerAdvisor(simpleVectorStore)
                )
                .call()
                .chatResponse();
        String text = chatResponse.getResult().getOutput().getText();

        return text;
    }
}
