package com.as.attendance_springboot.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalTime;

/**
 * @author xulili
 * @version 1.0
 * @project attendance_springboot
 * @description 考勤规则表实体
 * @date 2023/4/13 10:17:22
 */
@Data
@ToString
@NoArgsConstructor
@TableName("attendance_rule")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AttendanceRule {
    @JsonProperty(value = "aId")
    @TableId(value = "a_id", type = IdType.AUTO)
    private Integer aId;
    @JsonProperty(value = "cId")
    @NotNull(message = "cId不为null")
    private Integer cId;
    @JsonProperty(value = "aStart")
    @DateTimeFormat(pattern = "HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm", timezone = "GMT+8")
    private LocalTime aStart;
    @JsonProperty(value = "aEnd")
    @DateTimeFormat(pattern = "HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm", timezone = "GMT+8")
    private LocalTime aEnd;
    @JsonProperty(value = "locationRange")
    private Integer locationRange;
    @JsonProperty(value = "exceptionRange")
    private Integer exceptionRange;
    @JsonProperty(value = "neglectRange")
    private Integer neglectRange;
    @TableField(select = false)
    private Integer deleted;
}
