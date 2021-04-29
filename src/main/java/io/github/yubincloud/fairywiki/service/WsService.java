package io.github.yubincloud.fairywiki.service;

import io.github.yubincloud.fairywiki.websocket.WebSocketServer;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class WsService {

    @Resource
    private WebSocketServer webSocketServer;

    @Async
    public void sendInfo(String info) {
        webSocketServer.sendInfo(info);
    }
}
