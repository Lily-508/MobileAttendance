package com.as.attendance_springboot.model;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author xulili
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
public class PayloadDto {
    @Builder.Default
    private String sub="JWT";
    private Long iat;
    private Long exp;
    private String jti;
    private String loginPlatform;
    private String username;
    private String userId;
    private String right;
}
