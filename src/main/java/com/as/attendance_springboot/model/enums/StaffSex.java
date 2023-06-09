package com.as.attendance_springboot.model.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**员工表的性别枚举
 * @author xulili
 */
@Getter
public enum StaffSex {
    //员工表的性别枚举
    MAN(0,"男"),WOMAN(1,"女");

    @EnumValue
    private final Integer code;
    @JsonValue
    private final String remark;
    StaffSex(int code, String remark){
        this.code = code;
        this.remark = remark;
    }
}
