package io.github.yubincloud.fairywiki.controller;

import io.github.yubincloud.fairywiki.dto.req.UserLoginReqDto;
import io.github.yubincloud.fairywiki.dto.req.UserQueryReqDto;
import io.github.yubincloud.fairywiki.dto.req.UserResetPwdReqDto;
import io.github.yubincloud.fairywiki.dto.req.UserSaveReqDto;
import io.github.yubincloud.fairywiki.dto.resp.*;
import io.github.yubincloud.fairywiki.service.UserService;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @GetMapping("/list")
    public RestfulModel<PageRespDto<UserQueryRespDto>> list(@Valid UserQueryReqDto userQueryReqDto) {
        PageRespDto<UserQueryRespDto> userList = userService.list(userQueryReqDto);
        return new RestfulModel<>(ErrorCode.SUCCESS, "", userList);
    }

    @PostMapping("/save")
    public RestfulModel<Integer> saveUser(@RequestBody @Valid UserSaveReqDto userSaveReqDto) {
        userSaveReqDto.setPassword(
                DigestUtils.md5DigestAsHex(userSaveReqDto.getPassword().getBytes()));
        userService.save(userSaveReqDto);
        return new RestfulModel<>(ErrorCode.SUCCESS, "", 0);
    }

    @DeleteMapping("/delete/{userId}")
    public RestfulModel<Integer> deleteUser(@PathVariable Long userId) {
        userService.delete(userId);
        return new RestfulModel<>(ErrorCode.SUCCESS, "", 0);
    }

    @PostMapping("/reset-pwd")
    public RestfulModel<Integer> resetPwd(@RequestBody @Valid UserResetPwdReqDto userResetPwdReqDto) {
        userResetPwdReqDto.setPassword(DigestUtils.md5DigestAsHex(userResetPwdReqDto.getPassword().getBytes()));
        userService.resetPwd(userResetPwdReqDto);
        return new RestfulModel<>(ErrorCode.SUCCESS, "", 0);
    }

    /**
     * 用户登录接口
     * @param userLoginReqDto 用户的用户名及密码
     * @return 登录成功则返回该用户的信息，失败则返回登录失败提示
     */
    @PostMapping("/login")
    public RestfulModel<UserLoginRespDto> login(@RequestBody @Valid UserLoginReqDto userLoginReqDto) {
        userLoginReqDto.setPassword(
                DigestUtils.md5DigestAsHex(userLoginReqDto.getPassword().getBytes())
        );
        UserLoginRespDto userLoginRespDto = userService.login(userLoginReqDto);
        return new RestfulModel<>(ErrorCode.SUCCESS, "", userLoginRespDto);
    }
}
