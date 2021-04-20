package io.github.yubincloud.fairywiki.dto.req;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;

/**
 * 当需要对数据库的查询进行分页时需要继承此 Req 类
 */
@Data
public class PageReqDto {

    @NotNull(message = "【页码】不能为空")
    private int pageNum;

    @NotNull(message = "【每页条数】不能为空")
    @Max(value = 1000, message = "【每页条数】不能超过1000")
    private int pageSize;
}
