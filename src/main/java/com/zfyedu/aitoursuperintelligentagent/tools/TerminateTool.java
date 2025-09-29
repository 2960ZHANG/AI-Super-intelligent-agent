package com.zfyedu.aitoursuperintelligentagent.tools;

import org.springframework.ai.tool.annotation.Tool;

/**
 * 中止工具，让智能体自行决定执行任务结束
 */
public class TerminateTool {
  
    @Tool(description = """  
            Terminate the interaction when the request is met OR if the assistant cannot proceed further with the task.  
            "When you have finished all the tasks, call this tool to end the work.  
            """)  
    public String doTerminate() {  
        return "任务结束";  
    }  
}
