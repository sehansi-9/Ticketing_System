package oop.cw.ticketing.logging;

import oop.cw.ticketing.exceptions.LoggerException;
import oop.cw.ticketing.websocket.LogWebSocketHandler;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Logger {
    private static final String LOG_FILE = "logs.txt";
    private static final ExecutorService broadcastExecutor = Executors.newSingleThreadExecutor(); // Single-threaded executor for broadcasting

    public static void log(String message) {
        String timeStampedMessage = LocalDateTime.now() + ": " + message;
        System.out.println(timeStampedMessage);

        // Write to log file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(LOG_FILE, true))) {
            writer.write(timeStampedMessage);
            writer.newLine();
        } catch (IOException e) {
            throw new LoggerException(e.getMessage()); // Log the error if writing to the file fails
        }

        // Submit broadcasting task to the executor
        broadcastExecutor.submit(() -> {
            try {
                LogWebSocketHandler.broadcastLog(timeStampedMessage);
            } catch (Exception e) {
                System.err.println("Error broadcasting log: " + e.getMessage());
            }
        });
    }
}
