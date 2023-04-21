package com.as.attendance_springboot.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
 * @description 外派事务实体类
 * @date 2023/4/18 16:31:30
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ToString(callSuper = true)
@NoArgsConstructor
@TableName("work_outside")
public class WorkOutside extends Affair {
    @TableId(value = "wu_id",type = IdType.AUTO)
    @JsonProperty(value = "wuId")
    private Integer wuId;
    @NotNull(message = "aId不为null")
    @JsonProperty(value = "aId")
    private Integer aId;
}
