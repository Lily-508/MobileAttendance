package com.as.attendance_springboot.model.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * @author xulili
 * @version 1.0
 * @project attendance_springboot
 * @description 考勤记录结果
 * @date 2023/4/13 15:52:21
 */
@Getter
public enum RecordResultType {
    //考勤记录表的考勤结果枚举
    ON(0,"考勤中"),SUCCESS(1,"考勤成功"),EXCEPTION(2,"考勤异常"),NEGLECT(3,"考勤旷工");
    /**
     * @EnumValue 标记存储到数据库的值
     * @JsonValue 标记json返回的值
     */
    @EnumValue
    private final Integer code;
    @JsonValue
    private final String remark;
    RecordResultType(Integer code , String remark) {
        this.code=code;
        this.remark = remark;
    }
}
