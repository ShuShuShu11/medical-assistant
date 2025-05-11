package com.atguigu.java.ai.langchain4j.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Doctor {

    @TableId(type = IdType.AUTO)
    private Long id;
    private String doctorName;
    private Long deptId;
    private String title;
    private String intro;
    private Integer status;
    private String createTime;
    private String updateTime;
}