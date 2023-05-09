package com.as.attendance_springboot.model;

import com.as.attendance_springboot.model.enums.VocationType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.jeffreyning.mybatisplus.anno.MppMultiId;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**vDuration单位分钟
 * @author xulili
 * @version 1.0
 * @project attendance_springboot
 * @description 假期额度表,联合主键
 * @date 2023/4/12 10:34:45
 */
@Data
@ToString
@NoArgsConstructor
@TableName("vocation_quota")
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VocationQuota {
    @JsonProperty(value = "sId")
    @MppMultiId
    @NotNull(message = "sId不能为null")
    @TableField(value = "s_id")
    private Integer sId;
    @JsonProperty(value = "vId")
    @MppMultiId
    @NotNull(message = "vId不能为null")
    @TableField(value = "v_id")
    private VocationType vId;
    @JsonProperty(value = "vDuration")
    private Long vDuration;
    @TableField(select = false)
    private Integer deleted;
}
