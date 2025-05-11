package com.atguigu.java.ai.langchain4j.config;

import com.atguigu.java.ai.langchain4j.store.MongoChatMemoryStore;
import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.store.memory.chat.InMemoryChatMemoryStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SeparateChatAssistantConfig {

    //注入持久化对象
    @Autowired
    private MongoChatMemoryStore mongoChatMemoryStore;

    @Bean
    ChatMemoryProvider chatMemoryProvider() {
        return memoryId -> MessageWindowChatMemory.builder()
                .id(memoryId)
                .maxMessages(10)
                // 1、默认是 SingleSlotChatMemoryStore，两者都是把记忆数据存放在内存中
                // 2、还有InMemoryChatMemoryStore这种实现
//                .chatMemoryStore(new InMemoryChatMemoryStore())
                // 3、自定义实现ChatMemoryStore接口，然后传入自定义对象
                .chatMemoryStore(mongoChatMemoryStore)//配置持久化对象
                .build();
    }
}