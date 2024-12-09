package oop.cw.ticketing.controller;

import oop.cw.ticketing.config.Configuration;
import oop.cw.ticketing.exceptions.ThreadManagementException;
import oop.cw.ticketing.service.TicketManagerService;
import oop.cw.ticketing.threads.Customer;
import oop.cw.ticketing.threads.Vendor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.File;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api")
public class TicketWebController {

    private final String configFile = "../config.json";
    private final Configuration config;
    private final TicketManagerService ticketManagerService;

    @Autowired
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
        return "Threads started successfully!";
    }

    @PostMapping("/stop")
    public String stopSimulation () {
        ticketManagerService.stopThreads();
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
