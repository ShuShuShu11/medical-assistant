package com.atguigu.java.ai.langchain4j.controller;


import com.atguigu.java.ai.langchain4j.assistant.Assistant;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;



@RestController
public class DeepseekController {

    @Resource
    OpenAiChatModel openAiChatModel;

    @GetMapping("/chat/open-ai/deepseek-reasoner")
    public String model(@RequestParam(value = "message", defaultValue = "你好") String message) {
        return openAiChatModel.chat(message);
    }

}