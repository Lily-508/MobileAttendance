package com.as.attendance_springboot.model.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * @author xulili
 * @version 1.0
 * @project attendance_springboot
 * @description 事务的审查结果枚举
 * @date 2023/4/18 16:54:50
 */
@Getter
public enum AuditType {
//    审查结果枚举
    DISAGREE(0,"未通过"),AGREE(1,"通过"),ON(2,"审核中");
    @EnumValue
    private final Integer code;
    @JsonValue
    private final String remark;
    AuditType(Integer code, String remark) {
        this.code = code;
        this.remark = remark;
    }
}
