package logging;

import exceptions.LoggerException;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.io.BufferedWriter;

/**
 * The Logger class provides functionality for logging messages to both the console and a log file.
 * It appends log messages to a file with a timestamp.
 */
public class Logger {
    /**
     * The log file where messages are appended.
     */
    private static final String LOG_FILE = "logs.txt";
    /**
     * Logs a message by printing it to the console and writing it to the log file with a timestamp.
     * If the file write operation fails, a LoggerException is thrown.
     *
     * @param message The message to be logged.
     * @throws LoggerException If an error occurs while writing to the log file.
     */
    public static void log(String message) {
        String timeStampedMessage = LocalDateTime.now() + ": " + message;
        System.out.println(timeStampedMessage);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(LOG_FILE, true))) {
            writer.write(timeStampedMessage);
            writer.newLine();
        } catch (IOException e) {
            throw new LoggerException("Could not write the logs"+ e.getMessage()); // Log the error if writing to the file fails
        }
    }
}
