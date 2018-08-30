package com.myapp.web.websocket;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import org.springframework.web.socket.*;

import java.util.ArrayList;
import java.util.List;

public class MyWebSocketHandler implements WebSocketHandler {

    private static final List<WebSocketSession> onlineUsers = new ArrayList<>();
    private static final JsonParser parser = new JsonParser();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("用户【" + session.getId() + "】连接成功");
        session.sendMessage(new TextMessage("用户【" + session.getId() + "】你好，我是服务器"));
//        new Thread(() -> {
//            while (true) {
//                try {
//                    Thread.sleep(3000);
//                    if (!session.isOpen()) break;
//                    session.sendMessage(new TextMessage("用户【" + session.getId() + "】你好，我是服务器"));
//                } catch (InterruptedException | IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();
        onlineUsers.add(session);
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        System.out.println("用户【" + session.getId() + "】发送消息：" + message.getPayload());

        JsonObject jsonObject = parser.parse(message.getPayload().toString()).getAsJsonObject();
        String to = jsonObject.get("to").getAsString();
        for (WebSocketSession user : onlineUsers) {
            if (user.getId().equals(to)) {
                JsonObject result = new JsonObject();
                result.add("from", new JsonPrimitive(session.getId()));
                result.add("msg", jsonObject.get("msg"));
                if (user.isOpen()) {
                    user.sendMessage(new TextMessage(result.toString()));
                }
                break;
            }
        }

    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        System.out.println("触发handleTransportError()");
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        System.out.println("用户【" + session.getId() + "】连接关闭");
        onlineUsers.removeIf(it -> it.getId().equals(session.getId()));
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

}
