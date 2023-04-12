package com.as.attendance_springboot.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotNull;

/**
 * @author xulili
 * @version 1.0
 * @project attendance_springboot
 * @description 自定义假期表
 * @date 2023/4/12 10:04:24
 */
@Data
@ToString
@NoArgsConstructor
@TableName("holiday")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Holiday {
    @JsonProperty(value = "hId")
    @TableId(value = "h_id",type = IdType.AUTO)
    private Integer hId;
    @JsonProperty(value = "hYear")
    @NotNull(message = "hYear不为null")
    private Integer hYear;
    @JsonProperty(value = "hDate")
    private String hDate;
    @TableField(select = false)
    private Integer deleted;
}
