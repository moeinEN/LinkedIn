package com.app.aut;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

import static com.app.aut.DbController.createTableChats;

public class ChatServer {
    private static final int PORT = 12345;
    private static Map<String, ChatRoom> chatRooms = new HashMap<>();

    public static void main(String[] args) throws SQLException {
        createTableChats();
        System.out.println("Chat server started...");
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                new Thread(new ClientHandler(clientSocket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static synchronized ChatRoom getOrCreateChatRoom(String roomName) {
        if (!chatRooms.containsKey(roomName)) {
            ChatRoom chatRoom = new ChatRoom(roomName);
            List<Message> messages = DbController.getAllChats(roomName);
            chatRoom.setMessages(messages);
            chatRooms.put(roomName, chatRoom);
            return chatRoom;
        } else {
            ChatRoom chatRoom = chatRooms.get(roomName);
            return chatRoom;
        }
    }

    static synchronized void removeClientFromRoom(String roomName, ClientHandler clientHandler) {
        ChatRoom chatRoom = chatRooms.get(roomName);
        if (chatRoom != null) {
            chatRoom.removeClient(clientHandler);
            if (chatRoom.isEmpty()) {
                DbController.insertAllChats(roomName, chatRoom.getMessages());
                chatRooms.remove(roomName);
            }
        }
    }
}

class ChatRoom {
    private String roomName;
    private Set<ClientHandler> clients = new HashSet<>();
    private List<Message> messages;


    public ChatRoom(String roomName) {
        this.roomName = roomName;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public Set<ClientHandler> getClients() {
        return clients;
    }

    public synchronized void addClient(ClientHandler client) {
        clients.add(client);
        broadcastMessage(new Message("System", client.getClientName() + " has joined the chat"), client);
    }

    public synchronized void removeClient(ClientHandler client) {
        clients.remove(client);
        broadcastMessage(new Message("System", client.getClientName() + " has left the chat"), client);
    }

    public synchronized void broadcastMessage(Message message, ClientHandler excludeClient) {
        if (!message.getSender().equals("System")){
            messages.add(message);
        }
        for (ClientHandler client : clients) {
            if (client != excludeClient) {
                client.sendMessage(message.toString());
            }
        }
    }

    public boolean isEmpty() {
        return clients.isEmpty();
    }

    public String getRoomName() {
        return roomName;
    }

    public List<Message> getMessages() {
        return new ArrayList<>(messages);
    }
}

class ClientHandler implements Runnable {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private String clientName;
    private ChatRoom chatRoom;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    public String getClientName() {
        return clientName;
    }

    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            out.println("Enter your name: ");
            clientName = in.readLine();
            if (clientName == null || clientName.isEmpty()) {
                System.out.println("Client disconnected before providing a name.");
                socket.close();
                return;
            }
            out.println("Enter your chatroom name: ");
            String roomName = in.readLine();
            if (roomName == null || roomName.isEmpty()) {
                socket.close();
                return;
            }
            chatRoom = ChatServer.getOrCreateChatRoom(roomName);
            chatRoom.addClient(this);
            for (Message message : chatRoom.getMessages()) {
                out.println(message.toString());
            }
            String message;
            while ((message = in.readLine()) != null) {
                if (message.equalsIgnoreCase("/exit")) {
                    out.println("You have left the chat room.");
                    break;
                }
                chatRoom.broadcastMessage(new Message(clientName, message), this);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (chatRoom != null) {
                ChatServer.removeClientFromRoom(chatRoom.getRoomName(), this);
            }
        }
    }

    void sendMessage(String message) {
        out.println(message);
    }
}
