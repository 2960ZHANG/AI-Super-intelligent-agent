package com.zfyedu.aitoursuperintelligentagent.app;



import jakarta.annotation.PostConstruct;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Component
public class LoveApp {

    private  ChatModel dashScopeChatModel;
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
        ChatMemory chatMemory = MessageWindowChatMemory.builder().build();
        chatClient = ChatClient.builder(dashScopeChatModel)
                .defaultSystem(systemResource)
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
                .build();
    }

    public String doChat(String message,String chatId) {

        ChatResponse chatResponse = chatClient.prompt()
                .user(message)
                .advisors(
                    new SimpleLoggerAdvisor(
                            request -> "清纯少年的提问: " + request.prompt().getUserMessage(),
                            response -> "超级懂哥的回答 " + response.getResult().getOutput().getText(),
                            0

                    )
                )
                .call()
                .chatResponse();

        String resultText = chatResponse.getResult().getOutput().getText();
        return resultText;
    }

}
