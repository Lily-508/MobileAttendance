package com.as.attendance_springboot.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
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
public class Visit {
    @JsonProperty(value = "vId")
    @TableId(value = "vId", type = IdType.AUTO)
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
    @TableField(select = false)
    private Integer deleted;
}
