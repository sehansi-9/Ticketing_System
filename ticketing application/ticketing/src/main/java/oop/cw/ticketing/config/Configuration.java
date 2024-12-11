package oop.cw.ticketing.config;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import oop.cw.ticketing.core.TicketPool;
import oop.cw.ticketing.exceptions.ConfigurationException;
import oop.cw.ticketing.threads.Customer;
import oop.cw.ticketing.threads.Vendor;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * The Configuration class manages the configuration of the ticketing system, including the ticket pool,
 * vendors, and customers. It provides methods to add vendors and customers, and save/load the configuration
 * from/to a JSON file.
 */
public class Configuration {
    private TicketPool ticketPool;
    private final List<Vendor> vendors = new ArrayList<>();
    private final List<Customer> customers = new ArrayList<>();

    // Getters and setters

    public TicketPool getTicketPool() {
        return ticketPool;
    }

    public List<Vendor> getVendors() {
        return vendors;
    }

    public List<Customer> getCustomers() {
        return customers;
    }

    public void addVendor(Vendor vendor) {
        this.vendors.add(vendor);
    }

    public void addCustomer(Customer customer) {
        this.customers.add(customer);
    }

    /**
     * Saves the current configuration (ticket pool, vendors, and customers) to a file in JSON format.
     *
     * @param fileName The name of the file where the configuration will be saved.
     * @throws ConfigurationException If an error occurs during the file writing process.
     */
    public void saveToFile(String fileName)  {
        Gson gson = new Gson();
        try (FileWriter writer = new FileWriter(fileName)) {
            gson.toJson(this, writer);
        } catch (IOException e) {
            throw new ConfigurationException("Reading to file failed");
        }
    }
    /**
     * Loads the configuration from a JSON file.
     *
     * @param fileName The name of the file from which the configuration will be loaded.
     * @return The Configuration instance populated with the data from the file.
     * @throws ConfigurationException If an error occurs during the file reading or parsing process.
     */
    public static Configuration loadFromFile(String fileName)  {
        Gson gson = new Gson();
        try (FileReader reader = new FileReader(fileName)) {
            return gson.fromJson(reader, Configuration.class);
        } catch (IOException | JsonSyntaxException e) {
            throw new ConfigurationException("File loading failed");
        }
    }
}
