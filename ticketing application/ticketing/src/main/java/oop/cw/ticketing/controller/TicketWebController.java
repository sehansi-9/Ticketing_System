package oop.cw.ticketing.controller;

import oop.cw.ticketing.config.Configuration;
import oop.cw.ticketing.exceptions.LoggerException;
import oop.cw.ticketing.exceptions.ThreadManagementException;
import oop.cw.ticketing.service.TicketManagerService;
import oop.cw.ticketing.threads.Customer;
import oop.cw.ticketing.threads.Vendor;
import oop.cw.ticketing.websocket.LogWebSocketHandler;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;

/**
 * The TicketWebController class provides REST API endpoints for managing the ticketing system.
 * It includes operations for retrieving ticket pool information, adding customers and vendors,
 * starting and stopping the system
 */

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api")
public class TicketWebController {

    private final String configFile = "../config.json";
    private final Configuration config;
    private final TicketManagerService ticketManagerService;

    /**
     * Constructs a TicketWebController with the provided TicketManagerService.
     *
     * @param ticketManagerService The service responsible for managing ticketing system threads.
     */
    public TicketWebController(TicketManagerService ticketManagerService) {
        this.ticketManagerService = ticketManagerService;
        this.config = Configuration.loadFromFile(configFile);

    }

    /**
     * Returns ticket pool information such as maximum capacity, ticket release rate, and customer retrieval rate.
     *
     * @return An array of integers containing ticket pool details: [max capacity, ticket release rate, customer retrieval rate].
     */
    @GetMapping("/info")
    public int[] ticketPoolInfo (){
        if (isNull(configFile) == null) {
            System.out.println("Config file not found");
            return new int[0];
        }
        return new int[]{config.getTicketPool().getMaxTicketCapacity(),config.getTicketPool().getTicketReleaseRate(),config.getTicketPool().getCustomerRetrievalRate()};
    }
    /**
     * Returns ticket pool name or the event name
     *
     * @return a string value of the event/ticketpool name
     */
    @GetMapping("/name")
    public String ticketPoolName (){
        if (isNull(configFile) == null) {
            System.out.println("Config file not found");
            return null;
        }
        return config.getTicketPool().getEvent();
    }

    /**
     * Adds a customer to the ticketing system configuration.
     *
     * @param customerName The name of the customer to add.
     * @param tickets The number of tickets customer wishes to buy.
     * @return A success message indicating the customer has been added.
     */
    @PostMapping("/addcustomer")
    public String addCustomer( @RequestParam String customerName, @RequestParam int tickets) {
        if (isNull(configFile) == null) {
            return "Config file not found";
        }
        Customer customer = new Customer(config.getCustomers().size() + 1, customerName, tickets);
        config.addCustomer(customer);
        config.saveToFile(configFile);
        return "Customer added successfully";
    }

    /**
     * Adds a vendor to the ticketing system configuration
     *
     * @param vendorName The name of the vendor to add.
     * @param tickets The number of tickets vendor wishes to add.
     * @return A success message indicating the vendor has been added.
     */

    @PostMapping("/addvendor")
    public String addVendor( @RequestParam String vendorName, @RequestParam int tickets) {
        if (isNull(configFile) == null){
            return "Config file not found";
        }
        Vendor vendor = new Vendor(config.getVendors().size() + 1, vendorName, tickets);
        config.addVendor(vendor);
        config.saveToFile(configFile);
        return "Vendor added successfully";
    }

    /**
     * Starts the ticketing system by initializing threads for customers and vendors.
     *
     * @return A message indicating the ticketing system has been started successfully.
     * @throws ThreadManagementException If an error occurs while managing threads.
     */
    @PostMapping("/start")
    public String startSystem () throws ThreadManagementException {
        if (isNull(configFile) == null) {
            return "No details available, Enter details in cli";
        }
        Configuration updatedConfig = Configuration.loadFromFile(configFile);
        ticketManagerService.setCustomers(updatedConfig.getCustomers());
        ticketManagerService.setVendors(updatedConfig.getVendors());
        ticketManagerService.startThreads();

        try {
            LogWebSocketHandler.broadcastLog("Ticket system started!");
        } catch (IOException e) {
            throw new LoggerException("Failed to send log via WebSocket");
        }

        return "success";

    }

    /**
     * Stops the ticketing system and terminates customer and vendor threads.
     *
     * @return A message indicating the threads have been stopped successfully.
     */
    @PostMapping("/stop")
    public String stopSystem() {
        ticketManagerService.stopThreads();
        try {
            LogWebSocketHandler.broadcastLog("Ticket system stopped!");
        } catch (IOException e) {
            throw new LoggerException("Failed to send log via WebSocket");
        }
        return "Threads stopped successfully!";
    }

    /**
     * Checks if the configuration file exists and contains valid data.
     *
     * @param file The path to the configuration file.
     * @return The Configuration object if the file exists and is valid, otherwise null.
     */
    public Configuration isNull(String file) {
        Configuration updatedConfig = null;
        File configFileObj = new File(file);
        if (configFileObj.exists() && configFileObj.length() > 0) {
            return updatedConfig= Configuration.loadFromFile(file);
        }
        else return null;
    }


}
