package com.as.attendance_springboot.model.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * @author xulili
 * 员工表的在职状态枚举
 */
@Getter
public enum StaffStatus {
    //员工表的在职状态枚举
    ON(0,"在职"),OFF(1,"离职");
    /**
     * @EnumValue 标记存储到数据库的值
     * @JsonValue 标记json返回的值
     */
    @EnumValue
    private final Integer code;
    @JsonValue
    private final String remark;
    StaffStatus(Integer code , String remark) {
        this.code=code;
        this.remark = remark;
    }
}
