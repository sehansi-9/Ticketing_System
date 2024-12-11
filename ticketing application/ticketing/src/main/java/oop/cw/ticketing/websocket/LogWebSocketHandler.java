package oop.cw.ticketing.websocket;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * WebSocket handler for managing WebSocket connections and broadcasting log messages to client (frontend).
 *
 * The {@link LogWebSocketHandler} class extends {@link TextWebSocketHandler} to handle WebSocket connections
 * and broadcasts log messages.
 *
 * This is typically used for real-time logging, where multiple clients can receive log updates as they happen.
 */

@Component
public class LogWebSocketHandler extends TextWebSocketHandler {

    // list of active websocket sessions
    private static final List<WebSocketSession> sessions = new ArrayList<>();

    //Called when a new WebSocket connection is established. Adds the session to the list of active sessions.
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
    }

    // Called when a WebSocket connection is closed. Removes the session from the list of active sessions.
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session);
    }

    /**
     * Broadcasts log message to all active WebSocket sessions (clients).
     *
     * This method will attempt to send the provided log message to all open WebSocket sessions. If sending a message
     * to a session fails, the error is logged, but the broadcasting continues for the remaining sessions.
     *
     * @param logMessage The log message to be sent to all connected WebSocket clients.
     * @throws IOException If an error occurs while sending the message to any of the WebSocket sessions.
     */

    public static void broadcastLog(String logMessage) throws IOException {
        for (WebSocketSession session : sessions) {
            try {
                if (session.isOpen()) {
                    session.sendMessage(new TextMessage(logMessage));
                }
            } catch (IOException e) {
                System.err.println("Failed to send message: " + e.getMessage());
            }
        }
    }
}

