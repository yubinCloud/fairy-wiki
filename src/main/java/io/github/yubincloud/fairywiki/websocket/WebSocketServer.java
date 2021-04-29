package io.github.yubincloud.fairywiki.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;

@Component
@ServerEndpoint("/ws/{token}")
public class WebSocketServer {
    private static final Logger LOG = LoggerFactory.getLogger(WebSocketServer.class);

    /**
     * 每个客户端一个token
     */
    private String clientToken = "";

    private static final HashMap<String, Session> sessionMap = new HashMap<>();

    /**
     * 连接成功
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("token") String token) {
        sessionMap.put(token, session);
        this.clientToken = token;
        LOG.info("有新连接：token：{}，session id：{}，当前连接数：{}", token, session.getId(), sessionMap.size());
    }

    /**
     * 连接关闭
     */
    @OnClose
    public void onClose(Session session) {
        sessionMap.remove(this.clientToken);
        LOG.info("连接关闭，token：{}，session id：{}！当前连接数：{}", this.clientToken, session.getId(), sessionMap.size());
    }

    /**
     * 收到消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        LOG.info("收到消息：{}，内容：{}", clientToken, message);
    }

    /**
     * 连接错误
     */
    @OnError
    public void onError(Session session, Throwable error) {
        LOG.error("发生错误", error);
    }

    /**
     * 群发消息
     */
    public void sendInfo(String message) {
        for (String token : sessionMap.keySet()) {
            Session session = sessionMap.get(token);
            try {
                session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                LOG.error("推送消息失败：{}，内容：{}", token, message);
            }
            LOG.info("推送消息：{}，内容：{}", token, message);
        }
    }

}
