package com.atguigu.java.ai.langchain4j.controller;

import com.atguigu.java.ai.langchain4j.assistant.Assistant;
import dev.langchain4j.community.model.dashscope.QwenChatModel;
import dev.langchain4j.service.AiServices;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AIServiceController {

    @Resource
    QwenChatModel qwenChatModel;

    @GetMapping("/chat/dashScope/qwenChatModel/AIService1")
    public String func1(@RequestParam(value = "message", defaultValue = "你好") String message) {
        //创建AIService
        Assistant assistant = AiServices.create(Assistant.class, qwenChatModel);
        //调用service的接口
        String answer = assistant.chat(message);
       return answer;
    }


    @Autowired
    Assistant assistant;
    @GetMapping("/chat/dashScope/qwenChatModel/AIService2")
    public String func2(@RequestParam(value = "message", defaultValue = "嬛嬛，我要睡中间") String message) {
        //调用service的接口
        String answer = assistant.chat(message);
        return answer;
    }





}
