package io.github.yubincloud.fairywiki.controller;

import com.alibaba.fastjson.JSONObject;
import io.github.yubincloud.fairywiki.dto.req.UserLoginReqDto;
import io.github.yubincloud.fairywiki.dto.req.UserQueryReqDto;
import io.github.yubincloud.fairywiki.dto.req.UserResetPwdReqDto;
import io.github.yubincloud.fairywiki.dto.req.UserSaveReqDto;
import io.github.yubincloud.fairywiki.dto.resp.*;
import io.github.yubincloud.fairywiki.service.UserService;
import io.github.yubincloud.fairywiki.utils.SnowFlake;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.concurrent.TimeUnit;


@Api("用户相关接口")
@RestController
@RequestMapping("/user")
public class UserController {

    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);

    @Resource
    private UserService userService;

    @Resource
    private SnowFlake snowFlake;

    @Resource
    private RedisTemplate<String, String> redisTemplate;

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

        String token = Long.toString(snowFlake.nextId());
        LOG.info("生成单点登录token：{}，并放入redis中", token);
        userLoginRespDto.setToken(token);
        redisTemplate.opsForValue().set(token, JSONObject.toJSONString(userLoginRespDto), 3600 * 24, TimeUnit.SECONDS);
        return new RestfulModel<>(ErrorCode.SUCCESS, "", userLoginRespDto);
    }

    @RequestMapping("/redis/set/{key}/{value}")
    public String set(@PathVariable Long key, @PathVariable String value) {
        redisTemplate.opsForValue().set(Long.toString(key), value, 3600, TimeUnit.SECONDS);
        LOG.info("key: {}, value: {}", key, value);
        return "success";
    }

    @RequestMapping("/redis/get/{key}")
    public Object get(@PathVariable String key) {
        Object object = redisTemplate.opsForValue().get(key);
        LOG.info("key: {}, value: {}", key, object);
        return object;
    }

    @ApiOperation(value = "退出登录",
            notes = "该接口会清除掉存放于 redis 中所传入的 token")
    @GetMapping("/logout/{userToken}")
    public RestfulModel<Integer> logout(@PathVariable String userToken) {
        redisTemplate.delete(userToken);
        LOG.info("redis 中清除 token：{}", userToken);
        return new RestfulModel<>(ErrorCode.SUCCESS, "", 0);
    }
}
