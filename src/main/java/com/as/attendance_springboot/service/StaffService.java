package com.as.attendance_springboot.service;

import com.as.attendance_springboot.model.Staff;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.OutputStream;

/**StaffService接口
 * 自定义excel导入导出接口
 * @author xulili
 */
public interface StaffService extends IService<Staff> {
    /**
     * 导入excel数据到数据库中
     * @author xulili
     * @date 14:01 2023/4/10
     * @return boolean
     **/
    boolean importStaffExcel(MultipartFile file) throws IOException;
    /**
     * 导出数据库数据到excel中
     * @author xulili
     * @date 14:01 2023/4/10
     * @return boolean
     **/
    void exportStaffExcel(OutputStream out);
}
