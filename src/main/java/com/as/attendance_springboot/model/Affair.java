package com.as.attendance_springboot.model;

import com.as.attendance_springboot.model.enums.AuditType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
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
 * @description 四个事务的基础类
 * @date 2023/4/20 10:17:26
 */
@Data
@ToString
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Affair {
    @NotNull(message = "申请人sId不为null")
    @JsonProperty(value = "sId")
    private Integer sId;
    @JsonProperty(value = "content")
    private String content;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JsonProperty(value = "startTime")
    @NotNull(message = "开始时间不为null")
    private LocalDateTime startTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty(value = "endTime")
    @NotNull(message = "结束时间不为null")
    private LocalDateTime endTime;
    @JsonProperty(value = "total")
    private Integer total;
    @NotNull(message = "审核人Id不为空")
    @JsonProperty(value = "reviewer")
    private Integer reviewer;
    @JsonProperty(value = "result")
    private AuditType result;
    @TableField(select = false)
    private Integer deleted;
}
