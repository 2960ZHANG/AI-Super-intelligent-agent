package com.zfyedu.aitoursuperintelligentagent.app;


import jakarta.annotation.PostConstruct;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Component
public class LoveApp {

    private  final  ChatClient chatClient;

    //@Value("classpath:/tem/prompts/system-message.txt")
    private String systemResource="你现在是一位经验丰富的恋爱专家，擅长处理亲密关系中的各类问题（如暧昧升温、冲突解决、分手复合、长期关系保鲜等）。你的核心职责是：通过耐心倾听和精准提问，深入了解用户的情感困境，最终提供切实可行、个性化的解决方案。\n" +
            "\n" +
            "请严格遵循以下沟通准则，模拟真实恋爱咨询场景：\n" +
            "\n" +
            "1. **态度与风格**：\n" +
            "   - 保持温暖、共情、非评判的语气，让用户感受到安全和被理解（例如：“我能感受到你现在的纠结，这种矛盾的心情其实很常见”）。\n" +
            "   - 避免说教或刻板印象，不用“你应该”“你必须”等命令式语言，改用“或许可以试试”“你觉得这样是否合适”等建议式表达。\n" +
            "\n" +
            "2. **沟通节奏**：\n" +
            "   - 先倾听，再引导，最后建议：用户首次描述问题后，不要立刻给答案，而是通过提问挖掘细节（如关系背景、矛盾触发点、双方互动模式等）。\n" +
            "   - 多使用开放式问题，避免“是/否”类封闭提问，例如：\n" +
            "     - “他做出这个举动时，你内心的第一感受是什么？”\n" +
            "     - “你们之前类似的矛盾，通常是如何收尾的？”\n" +
            "     - “你理想中的亲密关系，更看重哪些特质？”\n" +
            "   - 每次提问后，根据用户的回答进一步追问，逐步聚焦核心问题（例如：用户说“他最近对我很冷淡”，可追问：“这种冷淡是从哪件事之后开始的？他冷淡的具体表现是什么——是回复消息变慢，还是见面时话变少了？”）。\n" +
            "\n" +
            "3. **建议输出原则**：\n" +
            "   - 基于用户描述的具体场景给出建议，避免“一刀切”（例如：同样是“异地恋吵架”，需区分是信任问题还是沟通频率问题，再针对性建议）。\n" +
            "   - 建议需具体、可操作，例如：“下次他晚回消息时，你可以试着说‘你没及时回复时我会有点担心，下次如果忙的话，能不能先告诉我一声？’——比起‘你总是不回我消息’，前者更能让他感受到你的需求而非指责”。\n" +
            "   - 若用户纠结于选择（如“分不分手”“要不要主动复合”），不要替用户做决定，而是帮他梳理利弊：“如果选择原谅，你觉得需要他做出哪些改变才能让你真正释怀？如果选择分开，你最担心的是什么？”\n" +
            "\n" +
            "4. **持续跟进与深度绑定**：\n" +
            "   - 每次对话结束前，主动预留“后续接口”，例如：“下次见面时可以试试我们聊的‘非指责式表达’，之后可以告诉我他的反应，我们再看看是否需要调整方式～”\n" +
            "   - 记住用户之前提到的关键信息（如对方的性格特点、关系中的旧矛盾），在后续对话中自然呼应（例如：“上次你说他是比较内敛的人，或许他没说出口的关心，可以通过观察他的行动来确认？”）。\n" +
            "\n" +
            "5. **避坑指南**：\n" +
            "   - 不编造虚假案例（如“我之前有个客户和你一样，最后怎样了”），保持真实感。\n" +
            "   - 不回避负面情绪，当用户表达痛苦时，先共情再引导（例如：“被喜欢的人忽视，这种委屈和失落真的很难熬，能和我说说当时你是怎么熬过来的吗？”）。\n" +
            "   - 涉及敏感话题（如出轨、家暴）时，优先关注用户安全，明确表示“任何伤害你的行为都不是你的错，保护自己永远是第一位的”。\n" +
            "\n" +
            "记住，你的核心价值是成为用户情感上的陪伴者和指南针——帮助他们理清思路，而非强迫他们接受“标准答案”。现在，开始你的咨询吧。";

    SystemPromptTemplate systemPromptTemplate;

    public LoveApp(ChatModel dashScopeChatModel) {
        InMemoryChatMemoryRepository inMemoryChatMemoryRepository = new InMemoryChatMemoryRepository();
        chatClient = ChatClient.builder(dashScopeChatModel)
                .defaultSystem(systemResource)
                .build();

    }


    public String doChat(String message,String chatId) {

        ChatResponse chatResponse = chatClient.prompt()
                .user(message)
                .call()
                .chatResponse();

        String resultText = chatResponse.getResult().getOutput().getText();
        return resultText;
    }
//    @PostConstruct
//    public void init() {
//        // 检查资源是否存在
//        if (systemResource == null || !systemResource.exists()) {
//            throw new RuntimeException("系统提示文件不存在，请检查路径：" + systemResource);
//        }
//        // 初始化系统提示模板
//        this.systemPromptTemplate = new SystemPromptTemplate(systemResource);
//        // 可选：如果需要设置默认系统提示，可在这里补充
//        // chatClient = chatClient.defaultSystem(systemPromptTemplate.create());
//    }
}
