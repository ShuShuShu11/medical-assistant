package com.atguigu.java.ai.langchain4j.assistant;


import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;
import dev.langchain4j.service.spring.AiService;

import static dev.langchain4j.service.spring.AiServiceWiringMode.EXPLICIT;

//当AIService由多个组件（大模型，聊天记忆等）组成的时候，我们就可以称他为 智能体 了
@AiService(wiringMode = EXPLICIT, chatModel = "qwenChatModel", chatMemory = "chatMemory")
public interface ChatMemoryAssistant {

    @UserMessage("你是我的好朋友，请用上海话回答问题，并且添加一些表情符号。 {{it}}") //{{it}}表示这里唯一的参数的占位符
//    标上该注解的方法，只能有message一个参数
    String chat(String userMessage);

    // 使用V明确指定传递的参数名称
    @UserMessage("你是我的好朋友，请用上海话回答问题，并且添加一些表情符号。{{message}}")
    String chat1(@V("message") String userMessage);
}
