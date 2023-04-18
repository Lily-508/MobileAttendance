package com.as.attendance_springboot.model;

import com.as.attendance_springboot.model.enums.RecordCategoryType;
import com.as.attendance_springboot.model.enums.RecordResultType;
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
import java.time.LocalDate;
import java.time.LocalDateTime;

/**考勤记录表
 * @author xulili
 * @version 1.0
 * @project attendance_springboot
 * @description 考勤记录表实体类
 * @date 2023/4/13 10:40:44
 */
@Data
@ToString
@NoArgsConstructor
@TableName("record_attendance")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RecordAttendance {
    @JsonProperty(value = "rId")
    @TableId(value = "r_id", type = IdType.AUTO)
    private Integer rId;
    @JsonProperty(value = "sId")
    @NotNull(message = "sId不为null")
    private Integer sId;
    @JsonProperty(value = "aId")
    @NotNull(message = "aId不为null")
    private Integer aId;
    @JsonProperty(value = "rCategory")
    private RecordCategoryType rCategory;
    @JsonProperty(value = "rDate")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDate rDate;
    @JsonProperty(value = "rPunchIn")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime rPunchIn;
    @JsonProperty(value = "rPunchOut")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime rPunchOut;
    @Pattern(regexp = "^[\\-\\+]?(0(\\.\\d{1,8})?|([1-9](\\d)?)(\\.\\d{1,8})?|1[0-7]\\d(\\.\\d{1,8})?|180(([.]0{1,8})" +
            "?)),[\\-\\+]?((0|([1-8]\\d?))(\\.\\d{1,10})?|90(\\.0{1,10})?)$",message = "经纬度格式错误")
    private String punchInPlace;
    @Pattern(regexp = "^[\\-\\+]?(0(\\.\\d{1,8})?|([1-9](\\d)?)(\\.\\d{1,8})?|1[0-7]\\d(\\.\\d{1,8})?|180(([" +
            ".]0{1,8})?)),[\\-\\+]?((0|([1-8]\\d?))(\\.\\d{1,10})?|90(\\.0{1,10})?)$", message = "经纬度格式错误")
    private String punchOutPlace;
    @JsonProperty(value = "rResult")
    private RecordResultType rResult;
    @TableField(select = false)
    private Integer deleted;
}
