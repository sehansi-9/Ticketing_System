package oop.cw.ticketing.service;

import jakarta.annotation.PostConstruct;
import oop.cw.ticketing.config.Configuration;
import oop.cw.ticketing.core.TicketPool;
import oop.cw.ticketing.threads.Customer;
import oop.cw.ticketing.threads.Vendor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TicketManagerService {

    private final TicketPool ticketPool;
    private List<Customer> customers;
    private List<Vendor> vendors;

    // Constructor injection for TicketPool
    public TicketManagerService(TicketPool ticketPool) {
        this.ticketPool = ticketPool;
    }

    // Step 1: Initialize by loading the configuration
    @PostConstruct
    public void init() {
        try {
            // Load the configuration from the JSON file
            Configuration config = Configuration.loadFromFile("../config.json");
            if (config != null) {
                // Set the ticket pool from config if available
                this.customers = config.getCustomers();
                this.vendors = config.getVendors();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Step 2: Start the customer and vendor threads
    public void startThreads() {
        // Start vendor threads
        for (Vendor vendor : vendors) {
            vendor.setTicketPool(ticketPool);
            new Thread(vendor).start();
        }

        // Start customer threads
        for (Customer customer : customers) {
            customer.setTicketPool(ticketPool);
            new Thread(customer).start();
        }
    }

    // Optional: Stop threads if needed
    public void stopThreads() {
        for (Customer customer : customers) {
            customer.getStopFlag()[0] = true;
        }
        for (Vendor vendor : vendors) {
            vendor.getStopFlag()[0] = true;
        }
    }
}
