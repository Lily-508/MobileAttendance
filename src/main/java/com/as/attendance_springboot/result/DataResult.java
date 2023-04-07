package com.as.attendance_springboot.result;

import lombok.*;
import lombok.experimental.Accessors;

/**
 * @author xulili
 * 成功响应正文统一格式基类
 */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class DataResult<T> extends BaseResult{
    private T data;

    @Override
    public DataResult<T> setCode(Integer code) {
        super.setCode(code);
        return this;
    }

    @Override
    public DataResult<T>setMsg(String msg) {
        super.setMsg(msg);
        return this;
    }
}
