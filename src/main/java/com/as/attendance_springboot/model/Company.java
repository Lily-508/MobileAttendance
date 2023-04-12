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

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * @author xulili
 * @version 1.0
 * @project attendance_springboot
 * @description 公司表实体类
 * @date 2023/4/11 17:38:01
 */
@Data
@ToString
@NoArgsConstructor
@TableName("company")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Company {
    @TableId(value = "c_id", type = IdType.AUTO)
    @JsonProperty(value = "cId")
    private Integer cId;
    @NotBlank(message = "公司名不能为空")
    @JsonProperty(value = "cName")
    private String cName;
    @JsonProperty(value = "cContent")
    private String cContent;
    @JsonProperty(value = "cPlace")
    @Pattern(regexp = "^[\\-\\+]?(0(\\.\\d{1,8})?|([1-9](\\d)?)(\\.\\d{1,8})?|1[0-7]\\d{1}(\\.\\d{1,8})?|180(([" +
            ".]0{1,8})?)),[\\-\\+]?((0|([1-8]\\d?))(\\.\\d{1,10})?|90(\\.0{1,10})?)$")
    private String cPlace;
    @TableField(select = false)
    private Integer deleted;
}
