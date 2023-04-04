package com.as.attendance_springboot.model.enum_model;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * @author xulili
 */
@Getter
public enum StaffRight {
    //员工表的权限类别枚举('normal','leader','admin')
    NORMAL(0,"普通员工"),LEADER(1,"领导"),ADMIN(2,"管理员");
    /**
     * @EnumValue 标记存储到数据库的值
     * @JsonValue 标记json返回的值
     */
    @EnumValue
    @JsonValue
    private final Integer code;
    private final String remark;
    StaffRight(Integer code , String remark) {
        this.code=code;
        this.remark = remark;
    }
}
