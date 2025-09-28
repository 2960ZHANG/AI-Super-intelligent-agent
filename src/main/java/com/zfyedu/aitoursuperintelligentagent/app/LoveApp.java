package com.zfyedu.aitoursuperintelligentagent.app;


import com.zfyedu.aitoursuperintelligentagent.advisor.MyLoggerAdvisor;
import com.zfyedu.aitoursuperintelligentagent.config.ToolRegistration;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.template.st.StTemplateRenderer;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class LoveApp {

    @jakarta.annotation.Resource
    @Qualifier("loveAppVectorStore") private VectorStore simpleVectorStore;
    private final ChatModel dashScopeChatModel;
    private ChatClient chatClient;
    @jakarta.annotation.Resource
    @Qualifier("pgVectorStore") private VectorStore pgVectorStore;

    @Value("classpath:/tem/prompts/system-message-love.txt")
    private Resource systemResource;
    //工具
    @jakarta.annotation.Resource
    private ToolCallback[] allTools;
    // 存储每个会话的 ChatMemory
    private final Map<String, ChatMemory> chatMemories = new ConcurrentHashMap<>();

    // 存储每个会话的 ChatClient
    private final Map<String, ChatClient> chatClients = new ConcurrentHashMap<>();


    SystemPromptTemplate systemPromptTemplate;

    public LoveApp(ChatModel dashScopeChatModel) {
        this.dashScopeChatModel = dashScopeChatModel;
    }

    @PostConstruct
    public void init() {
        // 确保 systemResource 不为 null
        if (systemResource == null) {
            throw new IllegalStateException("System resource is not loaded. Check if the file exists at: classpath:/tem/prompts/system-message-love.txt");
        }

        //基于文件的会话记忆
        //ChatMemory chatMemory = new FileBasedChatMemory(System.getProperty("user.dir") + "/chat-memory").builder().build();
        chatClient = ChatClient.builder(dashScopeChatModel)
                .defaultSystem(systemResource)
                .defaultAdvisors( new SimpleLoggerAdvisor(
                        request -> "清纯少年的提问: " + request.prompt().getUserMessage(),
                        response -> "超级懂哥的回答 " + response.getResult().getOutput().getText(),
                        0

                ))
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
                        //new MyLoggerAdvisor()
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
                    new SimpleLoggerAdvisor(
                            request -> "清纯少年的提问: " + request.prompt().getUserMessage(),
                            response -> "超级懂哥的回答 " + response.getResult().getOutput().getText(),
                            0

                    )
                        //Re2
                        // new ReReadingAdvisor(),
                        //自定义日志
                        //new MyLoggerAdvisor()
                )
                .call()
                .entity(LoveReport.class);
        log.info("Love report for {} is {}", chatId, report);
        return report;
    }
    PromptTemplate customPromptTemplate = PromptTemplate.builder()
            .renderer(StTemplateRenderer.builder().startDelimiterToken('<').endDelimiterToken('>').build())
            .template("""
            <query>

            Context information is below.

			---------------------
			<question_answer_context>
			---------------------

			Given the context information and no prior knowledge, answer the query.

			Follow these rules:

			1. If the answer is not in the context, just say that you don't know.
			2. Avoid statements like "Based on the context..." or "The provided information...
			3. If the user does not directly ask questions related to dating, answer based on your own knowledge base.".
            """)
            .build();

    // TODO
    //  QuestionAnswerAdvisor 对所有查询强制执行RAG检索
    //  优化方法：1.修改系统提示词
    //          2.添加判断回答逻辑-区分特定问题

    public String doChatWithRAG(String message, String chatId) {
        // 获取或创建特定会话的 ChatMemory
        ChatMemory chatMemory = chatMemories.computeIfAbsent(chatId,
                id -> MessageWindowChatMemory.builder().maxMessages(20).build());




        ChatResponse result = chatClient.prompt()
//                .system(systemResource + "每次对话后都要生成恋爱结果报告,标题为{用户名}的恋爱报告,内容为建议列表")
                .user(message)

                .advisors(
                        MessageChatMemoryAdvisor.builder(chatMemory).build(),
//                        new QuestionAnswerAdvisor(pgVectorStore),
                        QuestionAnswerAdvisor.builder(pgVectorStore).
                                promptTemplate(customPromptTemplate)
                                .build()
                )

                .call()
                .chatResponse();
       String response = result.getResult().getOutput().getText();


        return response;
    }


    public String doCharWithTools(String message, String chatId) {

        ChatMemory chatMemory = chatMemories.computeIfAbsent(chatId,
                id -> MessageWindowChatMemory.builder().maxMessages(20).build());


        ChatResponse chatResponse = chatClient.prompt()
                .user(message)
                .advisors(
                   MessageChatMemoryAdvisor.builder(chatMemory).build()
                )
                .toolCallbacks(allTools)
                .call()

                .chatResponse();

        String resultText = chatResponse.getResult().getOutput().getText();
        return resultText;
    }
}
