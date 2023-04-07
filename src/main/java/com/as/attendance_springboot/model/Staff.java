package com.as.attendance_springboot.model;

import com.as.attendance_springboot.model.enum_model.StaffRight;
import com.as.attendance_springboot.model.enum_model.StaffSex;
import com.as.attendance_springboot.model.enum_model.StaffStatus;
import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;

/**
 * @author xulili
 */
@Data
@ToString
@NoArgsConstructor
@TableName("staff")
@Accessors(chain = true)
public class Staff implements UserDetails {
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
    @JsonProperty(value = "sPhone")
    private String sPhone;
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

    @TableField(exist = false)
    private String roles;
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return AuthorityUtils.commaSeparatedStringToAuthorityList(roles);
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
