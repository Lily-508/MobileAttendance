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
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

/**员工表实体类
 * @author xulili
 */
@ToString
@Data
@NoArgsConstructor
@TableName("notice")
public class Notice {
    @JsonProperty(value = "nId")
    @TableId(value = "n_id", type = IdType.AUTO)
    private Integer nId;
    @NotBlank(message = "标题不能为空")
    @Length(max = 30, message = "标题最大长度为30")
    @JsonProperty(value = "nTitle")
    private String nTitle;
    @NotBlank(message = "内容不能为空")
    @JsonProperty(value = "nContent")
    private String nContent;
    @JsonProperty(value = "nDatetime")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime nDatetime;
    @TableField(select = false)
    private Integer deleted;
}
