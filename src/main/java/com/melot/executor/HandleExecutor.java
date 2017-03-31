package com.melot.executor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.google.gson.JsonObject;
import com.melot.packet.Operater;
import com.melot.packet.SocketClientSlave;

public class HandleExecutor extends Thread implements Executor {

    private static BlockingQueue<Integer> queue = new LinkedBlockingQueue<Integer>();

    private Map<Integer, String> userMap = new HashMap<Integer, String>();

    private static List<JsonObject> user = new ArrayList<JsonObject>();

    @Override
    public void execute() {
        this.start();

    }

    public void run() {
        while (true) {
            try {
                int roomId = queue.take();
                for (JsonObject usr : user) {
                    int userId = usr.get("userId").getAsInt();
                    String up = usr.get("up").getAsString();
                    String token = userMap.get(userId);
                    if (token == null) {
                        token = Operater.login(userId, up);
                        userMap.put(userId, token);
                    }
                    Set<String> set = ReapExecutor.getUserByRoomId(roomId);
                    if (set == null || !set.contains(userId + "_" + token)) {
                        String ws = Operater.getWsByRoomId(roomId);
                        SocketClientSlave.connect(userId, roomId, token, ws);
                        ReapExecutor.putUser(roomId, userId + "_" + token);
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void putRoomid(int roomId) {
        queue.add(roomId);
    }

    public static void putUser(JsonObject usr) {
        user.add(usr);
    }
}
