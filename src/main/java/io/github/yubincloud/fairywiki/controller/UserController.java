package io.github.yubincloud.fairywiki.controller;

import io.github.yubincloud.fairywiki.dto.req.UserQueryReqDto;
import io.github.yubincloud.fairywiki.dto.req.UserSaveReqDto;
import io.github.yubincloud.fairywiki.dto.resp.PageRespDto;
import io.github.yubincloud.fairywiki.dto.resp.RestfulModel;
import io.github.yubincloud.fairywiki.dto.resp.UserQueryRespDto;
import io.github.yubincloud.fairywiki.service.UserService;
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
        return new RestfulModel<>(0, "", userList);
    }

    @PostMapping("/save")
    public RestfulModel<Integer> saveUser(@RequestBody @Valid UserSaveReqDto userSaveReqDto) {
        userService.save(userSaveReqDto);
        return new RestfulModel<>(0, "", 0);
    }

    @DeleteMapping("/delete/{userId}")
    public RestfulModel<Integer> deleteUser(@PathVariable Long userId) {
        userService.delete(userId);
        return new RestfulModel<>(0, "", 0);
    }
}
