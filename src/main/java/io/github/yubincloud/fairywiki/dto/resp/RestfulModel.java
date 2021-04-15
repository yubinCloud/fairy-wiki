package io.github.yubincloud.fairywiki.dto.resp;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RestfulModel<T> {

    /**
     * 响应的错误码
     */
    private int code;

    /**
     * 返回信息
     */
    private String msg;

    /**
     * 返回泛型数据，自定义类型
     */
    private T data;
}
