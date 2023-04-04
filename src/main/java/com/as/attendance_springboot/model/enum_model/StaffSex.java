package com.as.attendance_springboot.model.enum_model;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * @author xulili
 */

@Getter
public enum StaffSex {
    //员工表的性别枚举
    MAN(0,"男"),WOMAN(1,"女");
    @JsonValue
    @EnumValue
    private final int code;
    private final String remark;
    StaffSex(int code, String remark){
        this.code = code;
        this.remark = remark;
    }
}
