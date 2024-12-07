package oop.cw.ticketing.config;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import oop.cw.ticketing.core.TicketPool;
import oop.cw.ticketing.threads.Customer;
import oop.cw.ticketing.threads.Vendor;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
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

    public void saveToFile(String fileName) {
        Gson gson = new Gson();
        try (FileWriter writer = new FileWriter(fileName)) {
            gson.toJson(this, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Configuration loadFromFile(String fileName) {
        Gson gson = new Gson();
        try (FileReader reader = new FileReader(fileName)) {
            return gson.fromJson(reader, Configuration.class);
        } catch (IOException | JsonSyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }
}
