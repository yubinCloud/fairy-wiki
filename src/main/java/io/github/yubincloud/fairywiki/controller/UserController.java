package io.github.yubincloud.fairywiki.controller;

import io.github.yubincloud.fairywiki.dto.req.UserQueryReqDto;
import io.github.yubincloud.fairywiki.dto.req.UserResetPwdReqDto;
import io.github.yubincloud.fairywiki.dto.req.UserSaveReqDto;
import io.github.yubincloud.fairywiki.dto.resp.ErrorCode;
import io.github.yubincloud.fairywiki.dto.resp.PageRespDto;
import io.github.yubincloud.fairywiki.dto.resp.RestfulModel;
import io.github.yubincloud.fairywiki.dto.resp.UserQueryRespDto;
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
}
