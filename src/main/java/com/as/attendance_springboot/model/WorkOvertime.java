package com.as.attendance_springboot.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author xulili
 * @version 1.0
 * @project attendance_springboot
 * @description 加班事务
 * @date 2023/4/19 15:30:41
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ToString(callSuper = true)
@NoArgsConstructor
@TableName("work_overtime")
public class WorkOvertime extends Affair {
    @TableId(value = "wv_id",type = IdType.AUTO)
    @JsonProperty(value = "wvId")
    private Integer wvId;
}
