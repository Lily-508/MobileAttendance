package com.as.attendance_springboot.service.impl;

import com.as.attendance_springboot.mapper.StaffMapper;
import com.as.attendance_springboot.model.Staff;
import com.as.attendance_springboot.model.enum_model.StaffRight;
import com.as.attendance_springboot.service.StaffService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author xulili
 */
@Service
public class StaffServiceImpl extends ServiceImpl<StaffMapper, Staff> implements StaffService, UserDetailsService {
    @Autowired
    private StaffMapper staffMapper;
    @Override
    public int saveStaff(int dId, String sName, String pwd, StaffRight staffRight){
        return 0;
    }

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        LambdaQueryWrapper<Staff> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(Staff::getSId,Integer.parseInt(userId));
        Staff staff=staffMapper.selectOne(queryWrapper);
        if(staff == null) {
            throw new UsernameNotFoundException("员工id不存在");
        }
        switch (staff.getSRight()){
            case ADMIN:
                staff.setRoles("normal,admin");
                break;
            case LEADER:
                staff.setRoles("normal,leader");
                break;
            case NORMAL:
                staff.setRoles("normal");
                break;
            default :
        }
        return staff;
    }

    /**
     * 手机号验证码登陆,待完成
     * @param phone
     * @return
     */
    public UserDetails loadUserByPhone(String phone) {
        return null;
    }

    /**
     * 第三方登陆,待完成
     * @param thirdPlatformType
     * @param thirdPlatformId
     * @return
     */
    public UserDetails loadByThirdPlatformId(String thirdPlatformType, String thirdPlatformId) {
        return null;
    }
}
