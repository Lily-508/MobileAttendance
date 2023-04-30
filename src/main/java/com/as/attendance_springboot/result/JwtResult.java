package com.as.attendance_springboot.result;

import lombok.*;
import lombok.experimental.Accessors;

/**
 * @author xulili
 * @version 1.0
 * @project attendance_springboot
 * @description 带token的响应数据
 * @date 2023/4/25 17:59:10
 */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class JwtResult <T> extends BaseResult{
    private T data;
    private String token;
    @Override
    public JwtResult<T> setCode(Integer code) {
        super.setCode(code);
        return this;
    }

    @Override
    public JwtResult<T>setMsg(String msg) {
        super.setMsg(msg);
        return this;
    }
}
