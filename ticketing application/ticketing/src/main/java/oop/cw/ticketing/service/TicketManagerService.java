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

    ArrayList<Thread> threadList = new ArrayList<>();

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
    public void setCustomers(List<Customer> customers) {
        this.customers = customers;
    }

    public void setVendors(List<Vendor> vendors) {
        this.vendors = vendors;
    }
    public void stopThreads() {

        for (Thread thread : threadList) {
            thread.interrupt();
        }
    }

}
