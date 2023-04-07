package com.as.attendance_springboot.result;

import lombok.*;
import lombok.experimental.Accessors;

/**
 * @author xulili
 * 分页响应格式
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class PaginationResult<T> extends BaseResult{
    private T data;
    private Long total;
    @Override
    public PaginationResult<T> setCode(Integer code) {
        super.setCode(code);
        return this;
    }

    @Override
    public PaginationResult<T>setMsg(String msg) {
        super.setMsg(msg);
        return this;
    }
}
