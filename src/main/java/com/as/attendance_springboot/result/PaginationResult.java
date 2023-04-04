package com.as.attendance_springboot.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author xulili
 * 分页响应格式
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class PaginationResult<T> extends BaseResult{
    private T data;
    private Long total;
}
