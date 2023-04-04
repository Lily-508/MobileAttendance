package com.as.attendance_springboot.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author xulili
 * 成功响应正文统一格式基类
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class DataResult<T> extends BaseResult{
    private T data;
}
