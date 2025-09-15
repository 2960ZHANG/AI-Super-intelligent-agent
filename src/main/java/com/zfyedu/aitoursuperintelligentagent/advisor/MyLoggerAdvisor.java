package com.zfyedu.aitoursuperintelligentagent.advisor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;


import org.springframework.ai.chat.client.advisor.api.CallAdvisor;
import org.springframework.ai.chat.client.advisor.api.CallAdvisorChain;
import org.springframework.ai.chat.client.advisor.api.StreamAdvisor;
import org.springframework.ai.chat.client.advisor.api.StreamAdvisorChain;

import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.metadata.ChatResponseMetadata;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import reactor.core.publisher.Flux;

import java.util.List;

@Slf4j
public class MyLoggerAdvisor implements CallAdvisor, StreamAdvisor {


    @Override
    public String getName() {
        return this.getClass().getSimpleName(); // 返回 Advisor 的名称
    }

    @Override
    public int getOrder() {
        return 0; // 定义 Advisor 的执行顺序，数值小的先执行
    }



    @Override
    public ChatClientResponse adviseCall(ChatClientRequest chatClientRequest, CallAdvisorChain callAdvisorChain) {
        UserMessage userMessage = chatClientRequest.prompt().getUserMessage();
        log.info("小蒙懂的疑问：" + userMessage.toString());
        ChatClientResponse chatClientResponse =callAdvisorChain.nextCall(chatClientRequest);
        String repose = chatClientResponse.chatResponse().getResult().getOutput().getText();
        log.info("超级懂哥的解答："+repose);

        return chatClientResponse;
    }

    @Override
    public Flux<ChatClientResponse> adviseStream(ChatClientRequest chatClientRequest, StreamAdvisorChain streamAdvisorChain) {
        return null;
    }
}