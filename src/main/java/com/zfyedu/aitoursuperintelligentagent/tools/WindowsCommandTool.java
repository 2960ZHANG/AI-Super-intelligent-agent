package com.zfyedu.aitoursuperintelligentagent.tools;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

@Component
public class WindowsCommandTool {

    // ✅ 安全命令白名单（仅允许这些命令）
    private static final List<String> ALLOWED_COMMANDS = Arrays.asList(
        "dir", "cd", "echo", "hostname", "ipconfig", "ping", "tracert", "nslookup",
        "systeminfo", "tasklist", "netstat", "whoami", "ver", "vol"
    );

    private static final int TIMEOUT_SECONDS = 10; // 命令执行超时

    /**
     * 在 Windows 系统上安全地执行终端命令（仅限白名单命令）
     *
     * @param command 要执行的命令，例如 "ipconfig" 或 "ping google.com"
     * @return 命令执行结果或错误信息
     */
    @Tool(description = "Executes a safe Windows command-line command (e.g., ipconfig, ping, dir). " +
          "Only predefined safe commands are allowed for security reasons.")
    public String executeWindowsCommand(String command) {
        if (command == null || command.trim().isEmpty()) {
            return "Error: Command is empty.";
        }

        // 拆分命令（简单按空格分割，仅用于白名单校验）
        String[] parts = command.trim().split("\\s+", 2);
        String cmdName = parts[0].toLowerCase();

        // 🔒 白名单校验：只允许安全命令
        if (!ALLOWED_COMMANDS.contains(cmdName)) {
            return "Error: Command '" + cmdName + "' is not allowed. " +
                   "Allowed commands: " + String.join(", ", ALLOWED_COMMANDS);
        }

        // 构建完整命令（使用 cmd /c）
        ProcessBuilder pb = new ProcessBuilder("cmd", "/c", command);
        pb.redirectErrorStream(true); // 合并 stderr 到 stdout

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<String> future = executor.submit(() -> {
            try {
                Process process = pb.start();
                StringBuilder output = new StringBuilder();
                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(process.getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        output.append(line).append("\n");
                    }
                }
                int exitCode = process.waitFor();
                if (exitCode != 0 && output.length() == 0) {
                    return "Command failed with exit code: " + exitCode;
                }
                return output.length() > 0 ? output.toString().trim() : "(no output)";
            } catch (IOException | InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(e);
            }
        });

        try {
            // ⏱️ 带超时执行
            String result = future.get(TIMEOUT_SECONDS, TimeUnit.SECONDS);
            return result.length() > 2000 ? result.substring(0, 2000) + "\n...(truncated)" : result;
        } catch (TimeoutException e) {
            future.cancel(true);
            return "Error: Command timed out after " + TIMEOUT_SECONDS + " seconds.";
        } catch (Exception e) {
            return "Error executing command: " + e.getMessage();
        } finally {
            executor.shutdown();
        }
    }
}
