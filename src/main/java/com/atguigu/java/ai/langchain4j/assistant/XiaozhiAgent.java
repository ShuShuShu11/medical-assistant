package com.atguigu.java.ai.langchain4j.assistant;


import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.spring.AiService;
import reactor.core.publisher.Flux;

import static dev.langchain4j.service.spring.AiServiceWiringMode.EXPLICIT;

@AiService(
        wiringMode = EXPLICIT,
        streamingChatModel = "qwenStreamingChatModel",    // 流式输出
        chatMemoryProvider = "chatMemoryProviderXiaozhi", //配置提示词
        tools = "appointmentTools", // 配置把挂号信息存入mysql的tools
        contentRetriever = "contentRetrieverXiaozhiPincone" //配置向量存储
)
public interface XiaozhiAgent {

    @SystemMessage(fromResource = "zhaozhi-prompt-template.txt")
    Flux<String> chat(@MemoryId Long memoryId, @UserMessage String userMessage);

}
