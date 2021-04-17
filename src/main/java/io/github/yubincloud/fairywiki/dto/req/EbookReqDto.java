package io.github.yubincloud.fairywiki.dto.req;

import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = true)
public class EbookReqDto extends PageReqDto {

    private Long id;

    private String name;
}
