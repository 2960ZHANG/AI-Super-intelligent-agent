package com.zfyedu.aitoursuperintelligentagent.agent;

import cn.hutool.core.util.StrUtil;
import com.zfyedu.aitoursuperintelligentagent.agent.model.AgentState;
import com.zfyedu.aitoursuperintelligentagent.exception.ThrowUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * 抽象代理类
 * 内存管理，状态转换，基于步骤的循环
 */
@Slf4j
@Data
public abstract class BaseAgent {

    //核心属性
    private String name;

    //提示

    private String systemPrompt;
    private String nextStepPrompt;
    //状态
    private AgentState state = AgentState.IDLE;
    //循环限制
    private int maxSteps=10;
    private int currentStep=0;
    //LLM

    private ChatClient  chatClient;

    //Memory
    private List<Message> messageList =  new ArrayList<>();

    /**
     *
     * @return
     */
    public String run(String userPrompt) {
        if(state != AgentState.IDLE){
            throw new RuntimeException("Cannot run agent from state " + state);
        }

        ThrowUtils.throwIf(StrUtil.isBlank(nextStepPrompt),new RuntimeException("nextPrompt is null or empty"));

        state = AgentState.RUNNING;

        //记录消息的上下文
        messageList.add(new UserMessage(userPrompt));
        //保存结果列表
        ArrayList<String> results = new ArrayList<>();
        try {
            for (int i = 0; i < maxSteps && state!=AgentState.FINISHED; i++) {
                    int stepNum = i+1;
                    currentStep = stepNum;

                String stepResult = step();
                String result = "Step" + stepNum + ":" +stepResult;

                results.add(result);

            }

            //循环检查
            if(currentStep >= maxSteps){
                state = AgentState.FINISHED;
                results.add("Terminated:Reached max steps("+maxSteps+")");

            }

            return String.join("\n", results);
        }catch (Exception e){
            state = AgentState.ERROR;
            log.error("Error executing agent",e );
            return "执行错误" + e.getMessage();
        } finally {
            cleanup();
        }
    }

    /**
     *
     * @return 步骤执行结果
     */
    public abstract String step();

    /**
     *  清理资源
     */
    public abstract void cleanup();
}
