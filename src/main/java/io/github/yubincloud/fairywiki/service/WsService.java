package io.github.yubincloud.fairywiki.service;

import io.github.yubincloud.fairywiki.websocket.WebSocketServer;
import org.slf4j.MDC;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class WsService {

    @Resource
    private WebSocketServer webSocketServer;

    /**
     * 以异步的方式将信息通过 websocket 发送给前端
     * @param info 所要发送的信息
     * @param logId LOG 的 id，使得本次异步所开的线程也能与原先的线程记录在同一个 LOG 号下，方便运维查找
     */
    @Async
    public void sendInfo(String info, String logId) {
        MDC.put("LOG_ID", logId);
        webSocketServer.sendInfo(info);
    }
}
