package config;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import core.TicketPool;
import exceptions.ConfigurationException;
import threads.Customer;
import threads.Vendor;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.rmi.ConnectException;
import java.util.ArrayList;
import java.util.List;

/**
 * The Configuration class basically creates the structure of the config file
 * including ticket pool, vendors, and customers. It provides methods to load,
 * save, and clear the configuration from/to a file.
 */

public class Configuration {
    private TicketPool ticketPool;
    private List<Vendor> vendors = new ArrayList<>();
    private List<Customer> customers = new ArrayList<>();

    // Getters and setters

    public TicketPool getTicketPool() {
        return ticketPool;
    }

    public void setTicketPool(TicketPool ticketPool) {
        this.ticketPool = ticketPool;
    }

    public List<Vendor> getVendors() {
        return vendors;
    }

    public void setVendors(List<Vendor> vendors) {
        this.vendors = vendors;
    }

    public List<Customer> getCustomers() {
        return customers;
    }

    public void setCustomers(List<Customer> customers) {
        this.customers = customers;
    }

    /**
     * Saves the current configuration instance to a specified file.
     *
     * @param fileName The name of the file to save the configuration to.
     * @throws ConfigurationException If an error occurs during the saving process.
     */
    public void saveToFile(String fileName) {
        Gson gson = new Gson();
        try (FileWriter writer = new FileWriter(fileName)) {
            gson.toJson(this, writer);
        } catch (IOException e) {
           throw new ConfigurationException("Saving to file failed");
        }
    }

    /**
     * Loads the configuration from a specified file.
     *
     * @param fileName The name of the file to load the configuration from.(the config file)
     * @return The loaded Configuration object in gson format to be used in java environment
     * @throws ConfigurationException If an error occurs during the loading process.
     */
    public static Configuration loadFromFile(String fileName) {
        Gson gson = new Gson();
        try (FileReader reader = new FileReader(fileName)) {
            return gson.fromJson(reader, Configuration.class);
        } catch (IOException | JsonSyntaxException e) {
            throw new ConfigurationException("Loading from file failed");
        }
    }
    /**
     * Clears the content of the specified file.
     *
     * @param fileName The name of the file to clear (this is basically for config file).
     * @throws ConfigurationException If an error occurs during the clearing process.
     */
    public void clear(String fileName) {
        try (FileWriter writer = new FileWriter(fileName)) {
            // clearing the file
        } catch (IOException e) {
            throw new ConfigurationException("Clearing the file failed");
        }
    }
}