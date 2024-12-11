package oop.cw.ticketing.logging;

import oop.cw.ticketing.exceptions.LoggerException;
import oop.cw.ticketing.websocket.LogWebSocketHandler;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Logger class for handling logging operations, including writing logs to console, a file and broadcasting them via WebSocket.
 *
 * The {@link Logger} class provides methods to log messages with a timestamp, write them to a log file, and broadcast
 * them to WebSocket clients in real time.
 */

public class Logger {
    private static final String LOG_FILE = "logs.txt";
    private static final ExecutorService broadcastExecutor = Executors.newSingleThreadExecutor(); // Single-threaded executor for broadcasting
    /**
     * Logs a message by printing it to the console, writing it to the log file, and broadcasting it to WebSocket clients.
     *
     * The log message is timestamped with the current date and time, then logged to both the console and a log file.
     * Additionally, the log message is broadcasted to active WebSocket client using the {@link LogWebSocketHandler}.
     *
     * @param message The log message to be recorded.
     * @throws LoggerException If an error occurs while writing the log message to the file or broadcasting it.
     */
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
