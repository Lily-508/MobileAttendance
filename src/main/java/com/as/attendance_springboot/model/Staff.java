package com.as.attendance_springboot.model;

import com.as.attendance_springboot.model.enum_model.StaffRight;
import com.as.attendance_springboot.model.enum_model.StaffSex;
import com.as.attendance_springboot.model.enum_model.StaffStatus;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
 * @author xulili
 */
@Data
@ToString
@NoArgsConstructor
@TableName("staff")
@Accessors(chain = true)
public class Staff {
    @JsonProperty(value = "sId")
    @TableId(value = "s_id", type = IdType.AUTO)
    private Integer sId;
    @JsonProperty(value = "dId")
    private Integer dId;
    @JsonProperty(value = "sName")
    private String sName;
    @JsonProperty(value = "sSex")
    private StaffSex sSex;
    @JsonProperty(value = "sPwd")
    private String sPwd;
    @JsonProperty(value = "sImei")
    private String sImei;
    @JsonProperty(value = "sStatus")
    private StaffStatus sStatus;
    @JsonProperty(value = "sRight")
    private StaffRight sRight;
    @JsonProperty(value = "sBirthday")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDate sBirthday;
    @JsonProperty(value = "sHiredate")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDate sHiredate;
}
