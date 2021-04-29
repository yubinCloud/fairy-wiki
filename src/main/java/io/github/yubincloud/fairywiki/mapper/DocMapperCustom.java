package io.github.yubincloud.fairywiki.mapper;

import org.apache.ibatis.annotations.Param;

public interface DocMapperCustom {

    /**
     * view count 字段递增一次
     * @param docId 文档的 id
     */
    void increaseViewCount(@Param("id") Long docId);

    /**
     * vote count 字段递增一次
     * @param docId 文档的 id
     */
    void increaseVoteCount(@Param("id") Long docId);

    /**
     * 更新所有 Ebook 的阅读量、点赞量信息
     */
    void updateEbookFooter();
}
