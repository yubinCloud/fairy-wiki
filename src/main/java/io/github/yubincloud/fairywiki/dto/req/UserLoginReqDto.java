package io.github.yubincloud.fairywiki.dto.req;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Data
public class UserLoginReqDto {

    @NotEmpty(message = "【用户名】不能为空")
    private String loginName;

    @NotEmpty(message = "【密码】不能为空")
    @Pattern(regexp = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,32}$", message = "【密码】规则不正确")
    private String password;
}
