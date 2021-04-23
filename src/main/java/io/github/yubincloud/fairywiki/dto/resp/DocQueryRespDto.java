package io.github.yubincloud.fairywiki.dto.resp;

import lombok.Data;

@Data
public class DocQueryRespDto {
    private Long id;

    private Long ebookId;

    private Long parent;

    private String name;

    private Integer sort;

    private Integer viewCount;

    private Integer voteCount;
}
