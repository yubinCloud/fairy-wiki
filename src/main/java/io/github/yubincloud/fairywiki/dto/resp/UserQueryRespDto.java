package io.github.yubincloud.fairywiki.dto.resp;

import lombok.Data;

@Data
public class UserQueryRespDto {
    private Long id;

    private String loginName;

    private String name;

    private String password;

}
