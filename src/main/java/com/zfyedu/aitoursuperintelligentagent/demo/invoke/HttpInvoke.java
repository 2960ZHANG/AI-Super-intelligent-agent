package com.zfyedu.aitoursuperintelligentagent.demo.invoke;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;


/**
 * 阿里云DashScope API调用工具类（基于Hutool封装）
 */
@Deprecated
public class HttpInvoke {

    // API请求地址
    private static final String API_URL = "https://dashscope.aliyuncs.com/api/v1/services/aigc/text-generation/generation";
    
    // API密钥（建议从环境变量或配置文件读取）
    private final String apiKey;

    public HttpInvoke(String apiKey) {
        if (StrUtil.isBlank(apiKey)) {
            throw new IllegalArgumentException("API密钥不能为空");
        }
        this.apiKey = apiKey;
    }

    /**
     * 发送对话请求
     * @param model 模型名称（如qwen-plus、qwen-turbo等）
     * @param messages 对话消息列表（包含角色和内容）
     * @return 模型响应内容
     */
    public String chat(String model, JSONArray messages) {
        // 构建请求参数
        JSONObject requestBody = JSONUtil.createObj()
                .set("model", model)
                .set("input", JSONUtil.createObj().set("messages", messages))
                .set("parameters", JSONUtil.createObj().set("result_format", "message"));

        // 发送POST请求
        try (HttpResponse response = HttpRequest.post(API_URL)
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .body(requestBody.toString())
                .execute()) {

            // 处理响应
            if (response.isOk()) {
                JSONObject responseJson = JSONUtil.parseObj(response.body());
                // 解析响应结果（根据API返回格式调整）
                return responseJson.getByPath("output.choices.0.message.content", String.class);
            } else {
                throw new RuntimeException("API请求失败: " + response.sync() + "，响应内容: " + response.body());
            }
        } catch (Exception e) {
            throw new RuntimeException("调用DashScope API出错", e);
        }
    }

    /**
     * 快速创建消息对象
     * @param role 角色（system/user/assistant）
     * @param content 内容
     * @return 消息JSONObject
     */
    public static JSONObject createMessage(String role, String content) {
        return JSONUtil.createObj()
                .set("role", role)
                .set("content", content);
    }

    // 测试示例
    public static void main(String[] args) {
        // 从环境变量获取API密钥（实际项目建议用配置文件）
        String apiKey = "TestApiKey.API_KEY";
        if (StrUtil.isBlank(apiKey)) {
            System.out.println("请先设置环境变量DASHSCOPE_API_KEY");
            return;
        }

        // 初始化客户端
        HttpInvoke client = new HttpInvoke(apiKey);

        // 构建对话消息
        JSONArray messages = JSONUtil.createArray();
        messages.add(createMessage("system", "你是一个 helpful 的助手"));
        messages.add(createMessage("user", "你是谁？"));

        // 调用API并打印结果
        try {
            String result = client.chat("qwen-plus", messages);
            System.out.println("响应结果: " + result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}