package com.zfyedu.aitoursuperintelligentagent.tools;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import com.zfyedu.aitoursuperintelligentagent.constant.FilePathConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.io.File;
@Slf4j
@Component
public class ResourceDownLoadTool {
    @Tool(description = "Download a resource from given url")
    public String downloadResource(@ToolParam(description = "URL of the resource  to download") String url,

                                   @ToolParam(description = "Name of the file to save the download resource") String filename) {

        String filePath = FilePathConstant.FILE_SAVE_PATH+"/download/";
        String savePath = FilePathConstant.FILE_SAVE_PATH+"/download/"+filename;


        try {
            File file = new File(filePath);
            if (!file.exists()) {
                file.mkdirs();
            }
            HttpUtil.downloadFile(url, savePath);
            return "Resource downloaded successfully : " + savePath;
        } catch (Exception e) {
        return "Error  downloading resource : " + e.getMessage();
        }
    }
}
