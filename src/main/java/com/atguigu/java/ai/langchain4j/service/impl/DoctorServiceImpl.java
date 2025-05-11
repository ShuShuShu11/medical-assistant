package com.atguigu.java.ai.langchain4j.service.impl;

import com.atguigu.java.ai.langchain4j.entity.Doctor;
import com.atguigu.java.ai.langchain4j.mapper.DoctorMapper;
import com.atguigu.java.ai.langchain4j.service.DoctorService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class DoctorServiceImpl extends ServiceImpl<DoctorMapper, Doctor>
        implements DoctorService {

}
