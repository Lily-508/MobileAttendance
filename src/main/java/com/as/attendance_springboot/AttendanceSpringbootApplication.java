package com.as.attendance_springboot;

import com.github.jeffreyning.mybatisplus.conf.EnableMPP;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableMPP
public class AttendanceSpringbootApplication {

    public static void main(String[] args) {
        SpringApplication.run(AttendanceSpringbootApplication.class, args);
    }

}
