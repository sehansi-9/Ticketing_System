package oop.cw.ticketing.service;

import jakarta.annotation.PostConstruct;
import oop.cw.ticketing.config.Configuration;
import oop.cw.ticketing.core.TicketPool;
import oop.cw.ticketing.exceptions.ConfigurationException;
import oop.cw.ticketing.exceptions.ThreadManagementException;
import oop.cw.ticketing.threads.Customer;
import oop.cw.ticketing.threads.Vendor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Service class that manages the ticket pool and coordinates the threads for vendors and customers.
 *
 * The {@link TicketManagerService} is responsible for initializing the ticket pool configuration,
 * starting the vendor and customer threads, and stopping those threads when necessary.
 *
 * This service uses Spring's {@link Service} annotation to handle the ticket pool management
 * and interact with vendors and customers through thread management.
 */
@Service
public class TicketManagerService {

    private final TicketPool ticketPool;
    private List<Customer> customers;
    private List<Vendor> vendors;

    // Constructor injection for TicketPool
    @Autowired
    public TicketManagerService(TicketPool ticketPool) {

        this.ticketPool = ticketPool;
    }

    //thread list to store initaited threads
    ArrayList<Thread> threadList = new ArrayList<>();

    //setter methods
    public void setCustomers(List<Customer> customers) {
        this.customers = customers;
    }

    public void setVendors(List<Vendor> vendors) {
        this.vendors = vendors;
    }

    /**
     * Initializes the ticket pool configuration by loading it from a configuration file and
     * setting up the event, ticket capacity, customer retrieval rate, and ticket release rate.
     * It also loads the customer and vendor lists from the configuration.
     *
     * @throws ConfigurationException if the configuration loading fails.
     */
    @PostConstruct
    public void init() {
        try {
            Configuration config = Configuration.loadFromFile("../config.json");
            if (config != null) {
                ticketPool.setEvent(config.getTicketPool().getEvent());
                ticketPool.setMaxTicketCapacity(config.getTicketPool().getMaxTicketCapacity());
                ticketPool.setCustomerRetrievalRate(config.getTicketPool().getCustomerRetrievalRate());
                ticketPool.setTicketReleaseRate(config.getTicketPool().getTicketReleaseRate());
                this.customers = config.getCustomers();
                this.vendors = config.getVendors();

            }
        } catch (Exception e) {
            throw new ConfigurationException(e.getMessage());
        }
    }
    /**
     * Starts the threads for all vendors and customers. Each vendor and customer is assigned a thread
     * to simulate their actions, and all threads are started concurrently.
     *
     * @throws ThreadManagementException if there is an issue with managing the threads.
     */
        public void startThreads() throws ThreadManagementException {

            for (Vendor vendor : vendors) {
                vendor.setTicketPool(ticketPool);
                Thread thread = new Thread(vendor);
                threadList.add(thread);
            }

            for (Customer customer : customers) {
                customer.setTicketPool(ticketPool);
                Thread thread = new Thread(customer);
                threadList.add(thread);
            }

            for (Thread thread : threadList) {
                thread.start();
            }

    }
    //stops all threads by interrupting them
    public void stopThreads() {

        for (Thread thread : threadList) {
            thread.interrupt();
        }
    }

}
