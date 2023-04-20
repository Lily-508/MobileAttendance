package com.as.attendance_springboot.model;

import com.as.attendance_springboot.model.enums.VocationType;
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
 * @description 请休假事务
 * @date 2023/4/19 15:31:30
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ToString(callSuper = true)
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@TableName("take_vocation")
public class TakeVocation extends Affair {
    @TableId(value = "tk_id",type = IdType.AUTO)
    @JsonProperty(value = "tkId")
    private Integer tkId;
    @NotNull(message = "vId不为null")
    @JsonProperty(value = "vId")
    private VocationType vId;
}
