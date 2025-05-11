package com.atguigu.java.ai.langchain4j.controller;

import com.atguigu.java.ai.langchain4j.assistant.Assistant;
import com.atguigu.java.ai.langchain4j.assistant.ChatMemoryAssistant;
import dev.langchain4j.community.model.dashscope.QwenChatModel;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.service.AiServices;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@RestController
public class ChatMemoryController {

    @Autowired
    private Assistant assistant;
    // 1、对话没有记忆
    @GetMapping("/chat/dashScope/qwenChatModel/AIService/ChatMemory1")
    public String chatMemory1() {
        String answer1 = assistant.chat("我是环环");
        String answer2 = assistant.chat("我是谁");
        return answer1+"----------------------"+answer2;
    }

    // 2、聊天记忆的简单实现
    @Resource
    private QwenChatModel qwenChatModel;

    @GetMapping("/chat/dashScope/qwenChatModel/AIService/ChatMemory2")
    public String ChatMemory2() {
        //第一轮对话
        UserMessage userMessage1 = UserMessage.userMessage("我是环环");
        ChatResponse chatResponse1 = qwenChatModel.chat(userMessage1);
        AiMessage aiMessage1 = chatResponse1.aiMessage();

        //第二轮对话
        UserMessage userMessage2 = UserMessage.userMessage("你知道我是谁吗");
        ChatResponse chatResponse2 = qwenChatModel.chat(Arrays.asList(userMessage1,
                aiMessage1, userMessage2));
        AiMessage aiMessage2 = chatResponse2.aiMessage();

        return aiMessage1.text()+"----------------------"+aiMessage2.text();
    }


    // 3、使用ChatMemory+AiService实现聊天记忆
    // 使用AIService可以封装多轮对话的复杂性，使聊天记忆功能的实现变得简单
    @GetMapping("/chat/dashScope/qwenChatModel/AIService/ChatMemory3")
    public String ChatMemory3() {

        //创建chatMemory
        MessageWindowChatMemory chatMemory = MessageWindowChatMemory.withMaxMessages(10);
        //创建AIService
        Assistant assistant = AiServices
                .builder(Assistant.class)
                .chatLanguageModel(qwenChatModel)
                .chatMemory(chatMemory)
                .build();
        //调用service的接口
        String answer1 = assistant.chat("我是环环");
        String answer2 = assistant.chat("我是谁");

        return answer1+"----------------------"+answer2;
    }

    @Autowired
    private ChatMemoryAssistant chatMemoryAssistant;
    // 4、使用AIService实现聊天记忆
    @GetMapping("/chat/dashScope/qwenChatModel/AIService/ChatMemory4")
    public String ChatMemory4() {
        //调用service的接口
        String answer1 = chatMemoryAssistant.chat("我是环环");
        String answer2 = chatMemoryAssistant.chat("我是谁");

        return answer1+"----------------------"+answer2;
    }

}
