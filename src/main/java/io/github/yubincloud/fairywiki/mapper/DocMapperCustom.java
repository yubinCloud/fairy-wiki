package io.github.yubincloud.fairywiki.mapper;

import org.apache.ibatis.annotations.Param;

public interface DocMapperCustom {

    /**
     * view count 字段递增一次
     * @param docId 文档的 id
     */
    void increaseViewCount(@Param("id") Long docId);
}
