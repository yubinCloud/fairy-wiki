package io.github.yubincloud.fairywiki.dto.req;

import lombok.Data;

/**
 * 当需要对数据库的查询进行分页时需要继承此 Req 类
 */
@Data
public class PageReqDto {

    private int pageNum;

    private int pageSize;
}
