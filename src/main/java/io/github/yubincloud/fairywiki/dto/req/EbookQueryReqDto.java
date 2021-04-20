package io.github.yubincloud.fairywiki.dto.req;

import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = true)
public class EbookQueryReqDto extends PageReqDto {

    private String name;

    private Long CategoryId2;
}
