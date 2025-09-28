// FileTool.java
package com.zfyedu.aitoursuperintelligentagent.tools;


import cn.hutool.core.io.FileUtil;
import com.zfyedu.aitoursuperintelligentagent.constant.FilePathConstant;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

@Component
public class FileTool {

    private static final String DATA_DIR = FilePathConstant.FILE_SAVE_PATH;

    /**
     * 读取指定路径的文本文件内容（仅限 tmp/ 目录下）
     */
    @Tool(description = "Reads the content of a text file. The filePath is relative to the 'tmp' directory.")
    public String readFile(String filePath) {
        try {
            Path resolvedPath = resolvePath(filePath);
            if (!Files.exists(resolvedPath)) {
                return "File not found: " + filePath;
            }
            return FileUtil.readUtf8String(resolvedPath.toString());
        }  catch (SecurityException e) {
            return "Access denied: invalid file path.";
        }catch (Exception e) {
            return "Error reading file '" + filePath + "': " + e.getMessage();
        }
    }

    /**
     * 将内容写入指定路径的文本文件（仅限 tmp/ 目录下）
     */
    @Tool(description = "Writes content to a text file. The filePath is relative to the 'tmp' directory.")
    public String writeFile(String filePath, String content) {
        try {
            Path resolvedPath = resolvePath(filePath);
            Files.createDirectories(resolvedPath.getParent());
            FileUtil.writeUtf8String(content,resolvedPath.toString());
            return "Successfully wrote to file: " + filePath;
        } catch (IOException e) {
            return "Error writing to file '" + filePath + "': " + e.getMessage();
        } catch (SecurityException e) {
            return "Access denied: invalid file path.";
        }
    }

    // 安全路径解析：防止路径遍历攻击
    private Path resolvePath(String filePath) {
        Path baseDir = Paths.get(DATA_DIR).toAbsolutePath().normalize();
        Path userPath = Paths.get(filePath);
        Path resolved = baseDir.resolve(userPath).normalize();

        if (!resolved.startsWith(baseDir)) {
            throw new SecurityException("Path traversal attempt detected.");
        }
        return resolved;
    }
}
