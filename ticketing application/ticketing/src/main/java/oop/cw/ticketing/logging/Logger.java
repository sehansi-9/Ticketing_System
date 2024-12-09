package oop.cw.ticketing.logging;

import oop.cw.ticketing.exceptions.LoggerException;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.io.BufferedWriter;

public class Logger {
    private static final String LOG_FILE = "logs.txt";
    public static void log(String message) {
        String timeStampedMessage = LocalDateTime.now() + ": " + message;
        System.out.println(timeStampedMessage);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(LOG_FILE, true))) {
            writer.write(timeStampedMessage);
            writer.newLine();
        } catch (IOException e) {
           throw new LoggerException(e.getMessage()); // Log the error if writing to the file fails
        }
    }
}