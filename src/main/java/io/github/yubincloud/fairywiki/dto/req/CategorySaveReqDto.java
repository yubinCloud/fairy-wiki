package io.github.yubincloud.fairywiki.dto.req;

import lombok.Data;

@Data
public class CategorySaveReqDto {
    private Long id;

    private Long parent;

    private String name;

    private Integer sort;
}