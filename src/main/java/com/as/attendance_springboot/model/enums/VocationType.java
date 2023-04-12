package com.as.attendance_springboot.model.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**前端传递remark
 * @author xulili
 * @version 1.0
 * @project attendance_springboot
 * @description 假期类型
 * @date 2023/4/12 13:26:15
 */
@Getter
public enum VocationType {
    //假期额度表的假期类别枚举
    PERSONAL(0, "事假"),
    MARRIAGE(1, "婚假"),
    COMPENSATORY(2, "调休"),
    ANNUAL(3,"年假"),
    SICK(4,"病假"),
    PREGNANCY(5,"孕假");

    /**
     * @EnumValue 标记存储到数据库的值
     * @JsonValue 标记json返回的值
     */
    @EnumValue
    private final Integer code;
    @JsonValue
    private final String remark;
    VocationType(Integer code, String remark) {
        this.code = code;
        this.remark = remark;
    }
}
