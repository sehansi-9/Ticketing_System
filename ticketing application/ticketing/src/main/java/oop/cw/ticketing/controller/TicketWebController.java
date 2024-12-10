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

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api")
public class TicketWebController {

    private final String configFile = "../config.json";
    private final Configuration config;
    private final TicketManagerService ticketManagerService;


    public TicketWebController(TicketManagerService ticketManagerService) {
        this.ticketManagerService = ticketManagerService;
        this.config = Configuration.loadFromFile(configFile);

    }

    @GetMapping("/info")
    public int[] ticketPoolInfo (){
        if (isNull(configFile) == null) {
            System.out.println("Config file not found");
            return new int[0];
        }
        return new int[]{config.getTicketPool().getMaxTicketCapacity(),config.getTicketPool().getTicketReleaseRate(),config.getTicketPool().getCustomerRetrievalRate()};
    }
    @GetMapping("/name")
    public String ticketPoolName (){
        if (isNull(configFile) == null) {
            System.out.println("Config file not found");
            return null;
        }
        return config.getTicketPool().getEvent();
    }

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

    @PostMapping("/start")
    public String startSimulation () throws ThreadManagementException {
        if (isNull(configFile) == null) {
            return "No details available, Enter details in cli";
        }
        Configuration updatedConfig = Configuration.loadFromFile(configFile);
        ticketManagerService.setCustomers(updatedConfig.getCustomers());
        ticketManagerService.setVendors(updatedConfig.getVendors());
        ticketManagerService.startThreads();

        try {
            LogWebSocketHandler.broadcastLog("Ticket simulation started!");
        } catch (IOException e) {
            throw new LoggerException("Failed to send log via WebSocket");
        }

        return "success";

    }

    @PostMapping("/stop")
    public String stopSimulation () {
        ticketManagerService.stopThreads();
        try {
            LogWebSocketHandler.broadcastLog("Ticket simulation stopped!");
        } catch (IOException e) {
            throw new LoggerException("Failed to send log via WebSocket");
        }
        return "Threads stopped successfully!";
    }

    public Configuration isNull(String file) {
        Configuration updatedConfig = null;
        File configFileObj = new File(file);
        if (configFileObj.exists() && configFileObj.length() > 0) {
            return updatedConfig= Configuration.loadFromFile(file);
        }
        else return null;
    }


}
