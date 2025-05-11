package com.atguigu.java.ai.langchain4j.controller;


import com.atguigu.java.ai.langchain4j.assistant.SeparateChatAssistant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

// 为每个用户的新聊天或者不同的用户区分聊天记忆
@RestController
public class ChatSeparateController {

    @Autowired
    private SeparateChatAssistant separateChatAssistant;
    // 1、隔离对话
    @GetMapping("/chat/dashScope/qwenChatModel/AIService/ChatSeparate1")
    public String chatMemory1() {
//        String answer1 = separateChatAssistant.chat(1,"我是拉拉");
//        String answer2 = separateChatAssistant.chat(1,"我是谁");
//        String answer3 = separateChatAssistant.chat(2,"我是谁");
//        String answer4 = separateChatAssistant.chat(2,"我是环环");
//        return answer1+"----------------------"+answer2+"----------------------"+
//                answer3+"----------------------"+answer4;
//        String answer1 = separateChatAssistant.chat(3,"我是拉拉");
        String answer1 = separateChatAssistant.chat(3,"今天是几号");
        return answer1;
    }

    // 2、使用V
    @GetMapping("/chat/dashScope/qwenChatModel/AIService/ChatSeparate2")
    public String chatMemory2() {
        String answer1 = separateChatAssistant.chat2(6,"今天是几号");
        return answer1;
    }


    @GetMapping("/chat/dashScope/qwenChatModel/AIService/ChatSeparate3")
    public String chatMemory3() {
        String answer = separateChatAssistant.chat3(1, "我是谁，我多大了", "翠花", 18);
        return answer;
    }

    @GetMapping("/chat/dashScope/qwenChatModel/AIService/ChatSeparate4")
    public String chatMemory4() {
        String answer = separateChatAssistant.chat4(10, "1+2等于几，475695037565的平方根是多少？");
        return answer;
    }

}
