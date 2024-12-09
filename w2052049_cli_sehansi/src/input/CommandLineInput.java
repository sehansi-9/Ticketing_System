package input;

import config.Configuration;
import core.TicketPool;
import threads.Customer;
import threads.Vendor;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class CommandLineInput {
    public static ArrayList<Vendor> vendorList = new ArrayList<>();
    public static ArrayList<Customer> customerList = new ArrayList<>();

    public static Configuration initializeConfiguration(String fileName) {
        File configFile = new File(fileName);

        if (configFile.exists()) {
            Configuration config = Configuration.loadFromFile(fileName);
            if (config != null) {
                System.out.println("Configuration file found. Loading...");
                Scanner scanner = new Scanner(System.in);
                System.out.println("Do you need to enter new details instead? (y/n)");
                String option = scanner.nextLine();
                if (option.equals("y")) {
                    config.clear(fileName);
                    return collectInputAndSave(fileName);
                }
                return config;
            } else {
                return collectInputAndSave(fileName);
            }
        } else {
            return collectInputAndSave(fileName);
        }

    }

    public static Configuration collectInputAndSave(String fileName) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the event name : ");
        String eventName = scanner.nextLine();
        int maxTicketCapacity = inputValidator("Enter the maximum ticket capacity : ");
        int customerRetrievalRate = inputValidator("Enter customer retrieval rate : ");
        int ticketReleaseRate = inputValidator("Enter ticket release rate: ");

        TicketPool ticketpool = new TicketPool(eventName, maxTicketCapacity, customerRetrievalRate, ticketReleaseRate);
        Configuration config = new Configuration();
        config.setTicketPool(ticketpool);


        collectVendorAndCustomerDetails(maxTicketCapacity);
        config.setVendors(new ArrayList<>(vendorList));
        config.setCustomers(new ArrayList<>(customerList));

        config.saveToFile(fileName);
        System.out.println("Configuration saved to file: " + fileName);

        return config;

    }

    private static int inputValidator(String message) {
        Scanner scanner = new Scanner(System.in);
        int input = -1;
        while (input <= 0) {
            System.out.print(message);
            if (scanner.hasNextInt()) {
                input = scanner.nextInt();
                if (input <= 0) {
                    System.out.println("Please enter a valid number greater than zero");
                }
            } else {
                System.out.println("Please enter a number");
                scanner.next();
            }
        }
        return input;
    }

    private static void collectVendorAndCustomerDetails(int maxTicketCapacity) {
        int numberOfVendors = inputValidator("Enter number of vendors: ");
        int numberOfCustomers = inputValidator("Enter number of customers: ");

        personDetails("Vendor", numberOfVendors, maxTicketCapacity);
        personDetails("Customer", numberOfCustomers, maxTicketCapacity);
    }

    private static void personDetails(String person, int numberOfPeople, int maxTicketCapacity) {
        Scanner scanner = new Scanner(System.in);
        for (int i = 1; i <= numberOfPeople; i++) {
            System.out.println("Enter details for " + person + " " + i);
            // Read vendor name
            System.out.print("Enter " + person + " name: ");
            String personName = scanner.nextLine();

            int personTickets = -1; // Initialize to an invalid value

            while (personTickets <= 0 || personTickets > maxTicketCapacity) {
                System.out.print("Enter ticket amount for " + personName + " (must not exceed " + maxTicketCapacity + "): ");

                if (scanner.hasNextInt()) {
                    personTickets = scanner.nextInt();

                    if (personTickets <= 0 || personTickets > maxTicketCapacity) {
                        System.out.println("Error: Please enter a number between 1 and " + maxTicketCapacity + ".");
                    }
                    scanner.nextLine(); // Consume the leftover newline
                } else {
                    System.out.println("Error: Invalid input. Please enter a valid number.");
                    scanner.next(); // Consume invalid input
                }
            }
            if (person.equals("Vendor")) {
                Vendor vendor = new Vendor(i, personName, personTickets);
                vendorList.add(vendor);
            } else {
                Customer customer = new Customer(i, personName, personTickets);
                customerList.add(customer);
            }

        }

    }


}
