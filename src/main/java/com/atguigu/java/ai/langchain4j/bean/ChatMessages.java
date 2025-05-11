package com.atguigu.java.ai.langchain4j.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 创建实体类：映射MongoDB中的文档（相当与MySQL的表）
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document("chat_messages")   // 会自动创建chat_messages这个集合
public class ChatMessages {

    //唯一标识，映射到 MongoDB 文档的 _id 字段
    @Id
    private ObjectId id;

    private int messageId;

    private String content; //存储当前聊天记录列表的json字符串

}
