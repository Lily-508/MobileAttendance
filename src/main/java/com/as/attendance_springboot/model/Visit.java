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
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

/**
 * @author xulili
 * @version 1.0
 * @project attendance_springboot
 * @description 拜访表
 * @date 2023/4/12 13:45:29
 */
@Data
@ToString
@NoArgsConstructor
@TableName("visit")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Visit {
    @JsonProperty(value = "vId")
    @TableId(value = "v_id", type = IdType.AUTO)
    private Integer vId;
    @JsonProperty(value = "cId")
    @NotNull(message = "cId不为null")
    private Integer cId;
    @JsonProperty(value = "dId")
    @NotNull(message = "dId不为null")
    private Integer dId;
    @JsonProperty(value = "vContent")
    private String vContent;
    @JsonProperty(value = "vStart")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime vStart;
    @JsonProperty(value = "vEnd")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime vEnd;
    @JsonProperty(value = "vPunchIn")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime vPunchIn;
    @JsonProperty(value = "vPunchOut")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime vPunchOut;
    @Pattern(regexp = "^[\\-\\+]?(0(\\.\\d{1,8})?|([1-9](\\d)?)(\\.\\d{1,8})?|1[0-7]\\d(\\.\\d{1,8})?|180(([.]0{1,8})" +
            "?)),[\\-\\+]?((0|([1-8]\\d?))(\\.\\d{1,10})?|90(\\.0{1,10})?)$",message = "经纬度格式错误")
    private String punchInPlace;
    @Pattern(regexp = "^[\\-\\+]?(0(\\.\\d{1,8})?|([1-9](\\d)?)(\\.\\d{1,8})?|1[0-7]\\d(\\.\\d{1,8})?|180(([" +
            ".]0{1,8})?)),[\\-\\+]?((0|([1-8]\\d?))(\\.\\d{1,10})?|90(\\.0{1,10})?)$", message = "经纬度格式错误")
    private String punchOutPlace;
    @TableField(select = false)
    private Integer deleted;
}
