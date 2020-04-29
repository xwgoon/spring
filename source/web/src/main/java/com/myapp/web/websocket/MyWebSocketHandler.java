package com.myapp.web.websocket;

import net.sf.json.JSONObject;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.HashMap;
import java.util.Map;

public class MyWebSocketHandler extends TextWebSocketHandler {

    private static final Map<Long, WebSocketSession> ONLINE_USERS = new HashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("调用afterConnectionEstablished");
        System.out.println(session.getAttributes());
        Long userId = (Long) session.getAttributes().get("userId");
        System.out.println("用户【" + userId + "】连接成功");
        session.sendMessage(new TextMessage("用户【" + userId + "】你好，我是服务器"));
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
        ONLINE_USERS.put(userId, session);
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        System.out.println("调用handleMessage");
        Long userId = (Long) session.getAttributes().get("userId");
        System.out.println("用户【" + userId + "】发送消息：" + message.getPayload());

        JSONObject jsonObject = JSONObject.fromObject(message.getPayload().toString());
        Long toUserId = jsonObject.getLong("to");
        WebSocketSession toUserSession = ONLINE_USERS.get(toUserId);
        if (toUserSession != null && toUserSession.isOpen()) {
            JSONObject result = new JSONObject();
            result.put("from", userId);
            result.put("msg", jsonObject.getString("msg"));
            toUserSession.sendMessage(new TextMessage(result.toString()));
        }

    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        exception.printStackTrace();
        System.out.println("调用handleTransportError");
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        System.out.println("调用afterConnectionClosed");
        Long userId = (Long) session.getAttributes().get("userId");
        System.out.println("用户【" + userId + "】连接关闭");
        ONLINE_USERS.remove(userId);
    }

    public static void main(String[] args) {
        String json = "{\n" +
                "  \"code\": 1,\n" +
                "  \"msg\": \"成功\",\n" +
                "  \"data\": {\n" +
                "    \"filePath\": \"/vanke/Upload/Other/20180918/vanke20180918095939219572.xlsx\",\n" +
                "    \"fileName\": \"vanke20180918095939219572.xlsx\",\n" +
                "    \"fileSize\": null,\n" +
                "    \"domain\": \"https://survey.adas.com:8056//Upload\",\n" +
                "    \"compressFilePath\": null,\n" +
                "    \"qrCodePath\": null\n" +
                "  }\n" +
                "}";

        JSONObject jsonObject = JSONObject.fromObject(json);
        String to = jsonObject.getString("code");
        System.out.println(to);

    }

}
