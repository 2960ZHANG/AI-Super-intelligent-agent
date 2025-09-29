package com.zfyedu.aitoursuperintelligentagent.agent;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;
import com.zfyedu.aitoursuperintelligentagent.agent.model.AgentState;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.mbeans.UserMBean;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.ToolResponseMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.model.tool.ToolCallingManager;
import org.springframework.ai.model.tool.ToolExecutionResult;
import org.springframework.ai.tool.ToolCallback;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class ToolCallAgent extends ReActAgent{

    //可用的工具
    private ToolCallback[]  availableTools;

    //保存工具调用信息的响应
    private ChatResponse toolCallchatResponse;

    //管理工具
    private ToolCallingManager toolCallingManager;

    //禁止使用框架的工具调用
    private ChatOptions  chatOptions;

    ToolCallAgent(ToolCallback[] toolCallbacks){
        super();
        this.availableTools = toolCallbacks;
        this.toolCallingManager=ToolCallingManager.builder().build();
        //禁用SpringAI 内置的工具调用
        this.chatOptions = DashScopeChatOptions.builder()
                .withInternalToolExecutionEnabled(false)
                .build();


    }

    @Override
    public boolean think() {

        //将下一步骤添加到用户消息中
        if(getNextStepPrompt()!=null&& !getNextStepPrompt().isEmpty()){
            UserMessage userMessage = new UserMessage(getNextStepPrompt());
            getMessageList().add(userMessage);
        }

        List<Message> messageList = getMessageList();
        Prompt prompt = new Prompt(messageList, chatOptions);


        try {
            //获取带工具选项的响应
            ChatResponse chatResponse= getChatClient().prompt(prompt)
                    .system(getSystemPrompt())
                    .toolCallbacks(availableTools)
                    .call()
                    .chatResponse();
            toolCallchatResponse = chatResponse;
            AssistantMessage assistantMessage = chatResponse.getResult().getOutput();

            String text = assistantMessage.getText();
            List<AssistantMessage.ToolCall> toolCalls = assistantMessage.getToolCalls();
            log.info(getName()+"的思考："+text);
            log.info(getName() + "选择了：" + toolCalls.size() +"个工具");

            String toolCallInfo = toolCalls.stream().map(
                    toolCall ->
                            String.format("工具名称：%s,参数：%s",
                                    toolCall.name(), toolCall.arguments())

            ).collect(Collectors.joining("\n"));
            log.info(toolCallInfo);
            if(toolCalls.isEmpty()){
                getMessageList().add(assistantMessage);
                return false;
            }
            else {
                return  true;
            }
        } catch (Exception e) {
            log.error(getName()+"在思考中遇到了问题" + e.getMessage());
            getMessageList().add(new AssistantMessage("处理时遇到了错误"+e.getMessage()));
            return false;
        }

    }


    /**
     * 执行工具调用并处理结果
     *
     * @return 执行结果
     */
    @Override
    public String act() {
        if (!toolCallchatResponse.hasToolCalls()) {
            return "没有工具调用";
        }
        // 调用工具
        Prompt prompt = new Prompt(getMessageList(), chatOptions);
        ToolExecutionResult toolExecutionResult = toolCallingManager.executeToolCalls(prompt, toolCallchatResponse);
        // 记录消息上下文，conversationHistory 已经包含了助手消息和工具调用返回的结果
        setMessageList(toolExecutionResult.conversationHistory());
        // 当前工具调用的结果
        ToolResponseMessage toolResponseMessage = (ToolResponseMessage) CollUtil.getLast(toolExecutionResult.conversationHistory());
        String results = toolResponseMessage.getResponses().stream()
                .map(response -> "工具 " + response.name() + " 完成了它的任务！结果: " + response.responseData())
                .collect(Collectors.joining("\n"));


        //判断是否进行了中止工具调用
        boolean anyMatch = toolResponseMessage.getResponses().stream()
                .anyMatch(response -> "doTerminate".equals(response.name()));
        if(anyMatch){
            setState(AgentState.FINISHED);
        }
        log.info(results);
        return results;
    }


    @Override
    public void cleanup() {

    }
}
