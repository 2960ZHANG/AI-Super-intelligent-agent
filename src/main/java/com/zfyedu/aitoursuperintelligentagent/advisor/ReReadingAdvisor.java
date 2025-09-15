package com.zfyedu.aitoursuperintelligentagent.advisor;

import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.CallAdvisor;
import org.springframework.ai.chat.client.advisor.api.CallAdvisorChain;
import org.springframework.ai.chat.client.advisor.api.StreamAdvisor;
import org.springframework.ai.chat.client.advisor.api.StreamAdvisorChain;
import reactor.core.publisher.Flux;

import java.util.HashMap;
import java.util.Map;

/**
 * 自定义 Re2 Advisor
 * 可提高大型语言模型的推理能力
 */
public class ReReadingAdvisor implements CallAdvisor, StreamAdvisor {


//    private AdvisedRequest before(AdvisedRequest advisedRequest) {
//
//        Map<String, Object> advisedUserParams = new HashMap<>(advisedRequest.userParams());
//        advisedUserParams.put("re2_input_query", advisedRequest.userText());
//
//        return AdvisedRequest.from(advisedRequest)
//                .userText("""
//                        {re2_input_query}
//                        Read the question again: {re2_input_query}
//                        """)
//                .userParams(advisedUserParams)
//                .build();
//    }
//
//    @Override
//    public AdvisedResponse aroundCall(AdvisedRequest advisedRequest, CallAroundAdvisorChain chain) {
//        return chain.nextAroundCall(this.before(advisedRequest));
//    }
//
//    @Override
//    public Flux<AdvisedResponse> aroundStream(AdvisedRequest advisedRequest, StreamAroundAdvisorChain chain) {
//        return chain.nextAroundStream(this.before(advisedRequest));
//    }

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public ChatClientResponse adviseCall(ChatClientRequest chatClientRequest, CallAdvisorChain callAdvisorChain) {
        Map<String, Object> advisedUserParams = new HashMap<>(chatClientRequest.context());
        advisedUserParams.put("re2_input_query", chatClientRequest.prompt().getUserMessage().getText());
        //请求拷贝的实现
        ChatClientRequest re2clientRequest = chatClientRequest
                .mutate()
                .prompt(chatClientRequest.prompt())
                .context(advisedUserParams).build();
//                .userText("""
//                        {re2_input_query}
//                        Read the question again: {re2_input_query}
//                        """)
//                .userParams(advisedUserParams)
//                .build();

        return callAdvisorChain.nextCall(re2clientRequest);
    }

    @Override
    public Flux<ChatClientResponse> adviseStream(ChatClientRequest chatClientRequest, StreamAdvisorChain streamAdvisorChain) {
        return null;
    }
}
