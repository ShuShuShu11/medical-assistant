package com.atguigu.java.ai.langchain4j.controller;


import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class OllamaController {

    @Resource
    OllamaChatModel ollamaChatModel;

    @GetMapping("/chat/ollama/deepseek-r1")
    public String model(@RequestParam(value = "message", defaultValue = "你好") String message) {
        return ollamaChatModel.chat(message);
    }

}