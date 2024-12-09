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

    public void saveToFile(String fileName) {
        Gson gson = new Gson();
        try (FileWriter writer = new FileWriter(fileName)) {
            gson.toJson(this, writer);
        } catch (IOException e) {
           throw new ConfigurationException("Saving to file failed");
        }
    }

    public static Configuration loadFromFile(String fileName) {
        Gson gson = new Gson();
        try (FileReader reader = new FileReader(fileName)) {
            return gson.fromJson(reader, Configuration.class);
        } catch (IOException | JsonSyntaxException e) {
            throw new ConfigurationException("Loading from file failed");
        }
    }

    public void clear(String fileName) {
        try (FileWriter writer = new FileWriter(fileName)) {
            // clearing the file
        } catch (IOException e) {
            throw new ConfigurationException("Clearing the file failed");
        }
    }
}