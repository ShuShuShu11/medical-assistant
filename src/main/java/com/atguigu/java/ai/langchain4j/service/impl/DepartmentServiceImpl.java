package com.atguigu.java.ai.langchain4j.service.impl;

import com.atguigu.java.ai.langchain4j.entity.Department;
import com.atguigu.java.ai.langchain4j.entity.Doctor;
import com.atguigu.java.ai.langchain4j.mapper.DepartmentMapper;
import com.atguigu.java.ai.langchain4j.mapper.DoctorMapper;
import com.atguigu.java.ai.langchain4j.service.DepartmentService;

import com.atguigu.java.ai.langchain4j.service.DoctorService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;


@Service
public class DepartmentServiceImpl extends ServiceImpl<DepartmentMapper, Department>
        implements DepartmentService {

}



