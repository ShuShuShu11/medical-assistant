package com.atguigu.java.ai.langchain4j.tools;

import com.atguigu.java.ai.langchain4j.entity.Appointment;
import com.atguigu.java.ai.langchain4j.entity.Department;
import com.atguigu.java.ai.langchain4j.entity.Doctor;
import com.atguigu.java.ai.langchain4j.service.AppointmentService;
import com.atguigu.java.ai.langchain4j.service.DepartmentService;
import com.atguigu.java.ai.langchain4j.service.DoctorService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AppointmentTools {
    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private DoctorService doctorService;


//    @Tool(name = "预约挂号", value = "根据参数，先执行工具方法queryDepartment查询是否可预约，并直接给用户回答是否可预约，并让用户确认所有预约信息，用户确认后再进行预约。")
    @Tool(name="预约挂号", value = "根据参数，先执行工具方法queryDepartment查询是否可预约，并直接给用户回答是否可预约，并让用户确认所有预约信息，用户确认后再进行预约。如果用户没有提供具体的医生姓名，请从向量存储中找到一位医生。")
    public String bookAppointment(Appointment appointment) {
        //查找数据库中是否包含对应的预约记录
        Appointment appointmentDB = appointmentService.getOne(appointment);
        if (appointmentDB == null) {
            appointment.setId(null);//防止大模型幻觉设置了id
            if (appointmentService.save(appointment)) {
                return "预约成功，并返回预约详情";
            } else {
                return "预约失败";
            }
        }
        return "您在相同的科室和时间已有预约";
    }

    @Tool(name = "取消预约挂号", value = "根据参数，查询预约是否存在，如果存在则删除预约记录并返回取 消预约成功，否则返回取消预约失败")
    public String cancelAppointment(Appointment appointment) {
        Appointment appointmentDB = appointmentService.getOne(appointment);
        if (appointmentDB != null) {
            //删除预约记录
            if (appointmentService.removeById(appointmentDB.getId())) {
                return "取消预约成功";
            } else {
                return "取消预约失败";
            }
        }
        //取消失败
        return "您没有预约记录，请核对预约科室和时间";
    }

    @Tool(name = "查询否有挂号号源服务", value = "根据科室名称，日期，时间和医生姓名查询是否有号源")
    public boolean queryDepartment(
            @P(value = "科室名称") String name,
            @P(value = "日期") String date,
            @P(value = "时间，可选值：上午、下午") String time,
            @P(value = "医生姓名", required = false) String doctorName
    ) {
        System.out.println("查询是否有号源");
        System.out.println("科室名称：" + name);
        System.out.println("日期：" + date);
        System.out.println("时间：" + time);
        System.out.println("医生名称：" + doctorName);

        // 1、是否有该科室
        LambdaQueryWrapper<Department> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Department::getDeptName,name);
        if(departmentService.count(wrapper) == 0){return false;}

        //2、判断是否有该医生
        LambdaQueryWrapper<Doctor> wrapper1 = new LambdaQueryWrapper<>();
        wrapper1.eq(Doctor::getDoctorName,doctorName);
        if(doctorService.count(wrapper1) == 0){return false;}

        //3、判断该医生是否还有排班
        LambdaQueryWrapper<Appointment> wrapper2 = new LambdaQueryWrapper<>();
        wrapper2.eq(Appointment::getDepartment,name);
        wrapper2.eq(Appointment::getDoctorName,doctorName);
        wrapper2.eq(Appointment::getDate,date);
        wrapper2.eq(Appointment::getTime,time);
        // 每个医生只有5个号
        if (appointmentService.count(wrapper2) >= 5){
            return false;
        }

        return true;

        // TODO 没有输入医生姓名，还没有处理
    }


    @Tool(name = "查询科室有哪些就诊医生", value = "根据科室名称，查看该科室有哪些就诊医生")
    public String queryDoctor(@P(value = "科室名称") String name) {

        LambdaQueryWrapper<Doctor> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Doctor::getStatus,1)
                .select(Doctor::getDoctorName);
        List<Doctor> doctorList = doctorService.list(wrapper);

        String names = doctorList.stream()
                .map(Doctor::getDoctorName) // 提取每个 Doctor 的 name
                .collect(Collectors.joining(", ")); // 用逗号加空格连接成字符串

        return names;
    }



}
