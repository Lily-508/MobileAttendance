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
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

/**
 * @author xulili
 * @version 1.0
 * @project attendance_springboot
 * @description 部门表实体类
 * @date 2023/4/11 09:45:18
 */
@Data
@ToString
@NoArgsConstructor
@TableName("department")
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Department {
    @TableId(value = "d_id", type = IdType.AUTO)
    @JsonProperty(value = "dId")
    private Integer dId;
    @JsonProperty(value = "dName")
    @NotBlank(message = "部门名称不能为null")
    private String dName;
    @JsonProperty(value = "dDetail")
    private String dDetail;
    @JsonProperty(value = "sId")
    private Integer sId;
    @TableField(select = false)
    private Integer deleted;
}
