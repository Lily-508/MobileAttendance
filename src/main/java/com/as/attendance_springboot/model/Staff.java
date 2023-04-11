package com.as.attendance_springboot.model;

import com.as.attendance_springboot.model.enum_model.StaffRight;
import com.as.attendance_springboot.model.enum_model.StaffSex;
import com.as.attendance_springboot.model.enum_model.StaffStatus;
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
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.Collection;

/**
 * 员工表实例类,安全验证依据
 * @author xulili
 * @JsonInclude(JsonInclude.Include.NON_NULL) 忽略null字段
 */
@Data
@ToString
@NoArgsConstructor
@TableName("staff")
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Staff implements UserDetails {
    @JsonProperty(value = "sId")
    @TableId(value = "s_id", type = IdType.AUTO)
    private Integer sId;
    @NotNull(message = "dId不能为null")
    @JsonProperty(value = "dId")
    private Integer dId;
    @JsonProperty(value = "sName")
    @NotBlank(message = "sName不能为null")
    @Length(max=20)
    private String sName;
    @JsonProperty(value = "sSex")
    private StaffSex sSex;
    @JsonProperty(value = "sPwd")
    @Length(min=6,message = "密码长度至少6位")
    private String sPwd;
    @JsonProperty(value = "sPhone")
    @Pattern(regexp = "1[3|4|5|7|8][0-9]{9}$", message = "手机号不合法")
    private String sPhone;
    @JsonProperty(value = "sEmail")
    @Email(message = "Email格式不正确")
    private String sEmail;
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
    @TableField(select = false)
    private Integer deleted;
    @TableField(exist = false)
    private String roles;

    public String getRoles() {
        if (this.sRight != null && this.roles == null) {
            switch (this.sRight) {
                case ADMIN:
                    this.setRoles("normal,admin");
                    break;
                case LEADER:
                    this.setRoles("normal,leader");
                    break;
                case NORMAL:
                    this.setRoles("normal");
                    break;
                default:
            }
        }
        return this.roles;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return AuthorityUtils.commaSeparatedStringToAuthorityList(getRoles());
    }

    @Override
    public String getPassword() {
        return sPwd;
    }

    @Override
    public String getUsername() {
        return sId.toString();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

