package com.as.attendance_springboot.service.impl;

import cn.hutool.core.io.IoUtil;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.as.attendance_springboot.mapper.StaffMapper;
import com.as.attendance_springboot.mapper.VocationQuotaMapper;
import com.as.attendance_springboot.model.Staff;
import com.as.attendance_springboot.model.VocationQuota;
import com.as.attendance_springboot.model.enums.StaffSex;
import com.as.attendance_springboot.model.enums.StaffStatus;
import com.as.attendance_springboot.model.enums.VocationType;
import com.as.attendance_springboot.service.StaffService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

/**实现excel导入导出员工
 * @author xulili
 */
@Service
public class StaffServiceImpl extends ServiceImpl<StaffMapper, Staff> implements StaffService, UserDetailsService {
    @Autowired
    private StaffMapper staffMapper;
    @Autowired
    private VocationQuotaMapper vocationQuotaMapper;

    @Override
    public boolean save(Staff entity) {
        boolean success=super.save(entity);
        if(success){
            success=initVocationQuota(entity);
        }
        return success;
    }

    /**
     * excel导入员工
     * @author xulili
     * @date 10:50 2023/4/11
     * @param file MultipartFile
     * @return boolean
     **/
    @Override
    public boolean importStaffExcel(MultipartFile file) throws IOException {
        ExcelReader reader= ExcelUtil.getReader(file.getInputStream());
        reader.addHeaderAlias("员工id","sId");
        reader.addHeaderAlias("部门id","dId");
        reader.addHeaderAlias("员工姓名","sName");
        reader.addHeaderAlias("员工性别","sSex");
        reader.addHeaderAlias("员工密码","sPwd");
        reader.addHeaderAlias("员工手机号","sPhone");
        reader.addHeaderAlias("员工邮箱","sEmail");
        reader.addHeaderAlias("员工IMEI号","sImei");
        reader.addHeaderAlias("员工状态","sStatus");
        reader.addHeaderAlias("员工权限","sRight");
        reader.addHeaderAlias("员工出生日期","sBirthday");
        reader.addHeaderAlias("员工入职日期","sHiredate");
        List<Staff> staffList=reader.readAll(Staff.class);
        staffList.forEach(staff->
            staff.setSPwd(DigestUtil.md5Hex(staff.getSPwd()))
        );
        boolean success= this.saveBatch(staffList);
        if(success){
            for(Staff staff:staffList){
                boolean isSuccess=initVocationQuota(staff);
                if(!isSuccess){
                    success=false;
                    break;
                }
            }
        }
        return success;
    }
    public boolean initVocationQuota(Staff staff) {
        VocationQuota vocation = new VocationQuota().setSId(staff.getSId());
        boolean success;
        try {
            //事假20天
            vocation.setVId(VocationType.PERSONAL).setVDuration((long) (20 * 24 * 60));
            vocationQuotaMapper.insert(vocation);
//        婚假10天
            vocation.setVId(VocationType.MARRIAGE).setVDuration((long) (10*24*60));
            vocationQuotaMapper.insert(vocation);
//        调休初始为0
            vocation.setVId(VocationType.COMPENSATORY).setVDuration(0L);
            vocationQuotaMapper.insert(vocation);
//        年假5天
            vocation.setVId(VocationType.ANNUAL).setVDuration((long) (5*24*60));
            vocationQuotaMapper.insert(vocation);
//        病假3个月
            vocation.setVId(VocationType.SICK).setVDuration((long) (3*30*24*60));
            vocationQuotaMapper.insert(vocation);
//        产假98天
            vocation.setVId(VocationType.PREGNANCY).setVDuration((long) (98*24*60));
            vocationQuotaMapper.insert(vocation);
            success=true;
        }catch (Exception e){
            e.printStackTrace();
            success=false;
        }
        return success;

    }
    /**
     * excel导出员工
     * @author xulili
     * @date 10:50 2023/4/11
     * @param out OutputStream
     **/
    @Override
    public void exportStaffExcel(OutputStream out) {
        QueryWrapper<Staff> queryWrapper=new QueryWrapper<>();
        queryWrapper.select(Staff.class,
                i->!"s_pwd".equals(i.getColumn())&&!"s_right".equals(i.getColumn())&&!"deleted".equals(i.getColumn()));
        List<Map<String,Object>>staffList=staffMapper.selectMaps(queryWrapper);
        for(Map<String,Object>map :staffList){
            if(StaffSex.MAN.getCode().equals((Integer)map.get("s_sex"))){
                map.put("s_sex",StaffSex.MAN.getRemark());
            }else {
                map.put("s_sex",StaffSex.WOMAN.getRemark());
            }
            if(StaffStatus.ON.getCode().equals((Integer)map.get("s_status"))){
                map.put("s_status",StaffStatus.ON.getRemark());
            }else {
                map.put("s_status",StaffStatus.OFF.getRemark());
            }
        }
        //xlsx格式
        ExcelWriter writer=ExcelUtil.getWriter(true);
        writer.addHeaderAlias("s_id","员工id");
        writer.addHeaderAlias("d_id","部门id");
        writer.addHeaderAlias("s_name","员工姓名");
        writer.addHeaderAlias("s_sex","员工性别");
        writer.addHeaderAlias("s_phone","员工手机号");
        writer.addHeaderAlias("s_email","员工邮箱");
        writer.addHeaderAlias("s_imei","员工IMEI号");
        writer.addHeaderAlias("s_status","员工状态");
        writer.addHeaderAlias("s_birthday","员工出生日期");
        writer.addHeaderAlias("s_hiredate","员工入职日期");
        // 默认的未添加alias的属性也会写出，如果想只写出加了别名的字段，可以调用此方法排除之
        writer.setOnlyAlias(true);
        writer.write(staffList, true);
        writer.flush(out, true);
        writer.close();
        IoUtil.close(out);
    }
    /**
     * spring security自定义员工id加载
     * @author xulili
     * @date 10:51 2023/4/11
     * @param userId
     * @return org.springframework.security.core.userdetails.UserDetails
     **/
    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        LambdaQueryWrapper<Staff> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(Staff::getSId,Integer.parseInt(userId));
        Staff staff=staffMapper.selectOne(queryWrapper);
        if(staff == null) {
            throw new UsernameNotFoundException("员工id不存在");
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
