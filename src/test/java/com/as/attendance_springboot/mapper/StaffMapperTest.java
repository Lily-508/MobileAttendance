package com.as.attendance_springboot.mapper;

import com.as.attendance_springboot.model.Staff;
import com.as.attendance_springboot.model.enum_model.StaffRight;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootTest
public class StaffMapperTest {
    @Autowired
    private StaffMapper staffMapper;
    @Test
    void saveStaff(){
        Staff staff = new Staff();
        String pwd=new BCryptPasswordEncoder().encode("123456");
        staff.setDId(101).setSName("Li Hua").setSPwd(pwd).setSRight(StaffRight.ADMIN).setRoles("normal,admin");
        System.out.println(staffMapper.insert(staff));
    }
}
