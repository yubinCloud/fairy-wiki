package io.github.yubincloud.fairywiki.dto.req;

import lombok.Data;

@Data
public class EbookSaveReqDto {
    private Long id;

    private String name;

    private Long category1Id;

    private Long category2Id;

    private String description;

    private String cover;

    private Integer docCount = 0;

    private Integer viewCount = 0;

    private Integer voteCount = 0;
}
