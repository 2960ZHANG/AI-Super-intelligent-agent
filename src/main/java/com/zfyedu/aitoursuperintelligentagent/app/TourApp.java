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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.util.List;

//@Component
@Slf4j
public class TourApp {

    @jakarta.annotation.Resource
    @Qualifier("tourAppVectorStore")  private VectorStore simpleVectorStore;
    private final ChatModel dashScopeChatModel;
    private ChatClient chatClient;


    @Value("classpath:/tem/prompts/system-message-tour.txt")
    private Resource systemResource;



    SystemPromptTemplate systemPromptTemplate;

    public TourApp(ChatModel dashScopeChatModel) {
        this.dashScopeChatModel = dashScopeChatModel;
    }

    @PostConstruct
    public void init() {
        // 确保 systemResource 不为 null
        if (systemResource == null) {
            throw new IllegalStateException("System resource is not loaded. Check if the file exists at: classpath:/tem/prompts/system-message-Tour.txt");
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
     * 旅行计划报告类标题
     *
     * @param title      报告
     * @param suggestion 建议
     */
    public record TourReport(String title, List<String> suggestion) {
    }

    public TourReport doChatWithReport(String message, String chatId) {

        TourApp.TourReport report = chatClient.prompt()
                .system(systemResource + "每次对话后都要生成旅行计划结果报告,标题为{用户名}的旅行计划报告,内容为建议列表")
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
                .entity(TourApp.TourReport.class);
        log.info("Tour report for {} is {}", chatId, report);
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
