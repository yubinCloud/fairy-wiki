package io.github.yubincloud.fairywiki.controller;

import io.github.yubincloud.fairywiki.dto.resp.ErrorCode;
import io.github.yubincloud.fairywiki.dto.resp.RestfulModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class ControllerExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(ControllerExceptionHandler.class);

    /**
     * 统一处理参数校验异常
     * @param e 捕捉到的异常
     */
    @ExceptionHandler(value = BindException.class)
    @ResponseBody
    public RestfulModel<Object> validExceptionHandler(BindException e) {
        String exceptionMsg = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        LOG.warn("参数校验失败：{}", exceptionMsg);
        return new RestfulModel<>(ErrorCode.ARGS_VALIDATION_ERROR, exceptionMsg, null);
    }
}
