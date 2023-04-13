package com.as.attendance_springboot.model.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * @author xulili
 * @version 1.0
 * @project attendance_springboot
 * @description 考勤类别
 * @date 2023/4/13 10:43:56
 */
@Getter
public enum RecordCategoryType {
    //考勤记录表的考勤类别枚举
    NORMAL(0,"正常考勤"),OUTSIDE(1,"外派考勤");
    /**
     * @EnumValue 标记存储到数据库的值
     * @JsonValue 标记json返回的值
     */
    @EnumValue
    private final Integer code;
    @JsonValue
    private final String remark;
    RecordCategoryType(Integer code , String remark) {
        this.code=code;
        this.remark = remark;
    }
}
