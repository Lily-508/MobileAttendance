package com.as.attendance_springboot.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotNull;

/**
 * @author xulili
 * @version 1.0
 * @project attendance_springboot
 * @description 补卡事务
 * @date 2023/4/19 15:32:01
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ToString(callSuper = true)
@NoArgsConstructor
@TableName("offset_attendance")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OffsetAttendance extends Affair {
    @TableId(value = "oa_id",type = IdType.AUTO)
    @JsonProperty(value = "oaId")
    private Integer oaId;
    @NotNull(message = "rId不为null")
    @JsonProperty(value = "rId")
    private Integer rId;
}
