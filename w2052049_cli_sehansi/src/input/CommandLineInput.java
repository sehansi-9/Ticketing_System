package input;

import config.Configuration;
import core.TicketPool;
import threads.Customer;
import threads.Vendor;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * The CommandLineInput class provides functionality to prompt users
 * to configure the ticket pool system by collecting event details, vendor and
 * customer information, and saving them to a configuration file, inorder to simulate a multithreaded environment
 */
public class CommandLineInput {
    /**
     * A list to store the vendor details collected.
     */
    public static ArrayList<Vendor> vendorList = new ArrayList<>();
    /**
     * A list to store the customer details collected.
     */
    public static ArrayList<Customer> customerList = new ArrayList<>();

    /**
     * Initializes the configuration details by loading from an existing configuration file.
     * If the configuration file doesn't exist or if the user chooses to overwrite it,
     * it will collect the necessary input from the user and save the new configuration.
     *
     * @param fileName The name of the configuration file.
     * @return The loaded or newly created Configuration object.
     */
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
    /**
     * Collects input for event details, vendor, and customer configurations from
     * the user and saves the details to a config file.
     *
     * @param fileName The name of the configuration file.
     * @return The newly created Configuration object.
     */
    public static Configuration collectInputAndSave(String fileName) {
        Scanner scanner = new Scanner(System.in);
        String eventName = "";
        while (eventName.isEmpty()) {
            System.out.print("Enter the event name : ");
            eventName = scanner.nextLine();
        }

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
    /**
     * Validates user input to ensure it is a valid integer greater than zero.
     *
     * @param message The message to display to the user when requesting input.
     * @return A valid integer input from the user.
     */
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
                System.out.println("Please enter a valid number");
                scanner.next();
            }
        }
        return input;
    }

    /**
     * Collects the number of customers and vendors the user wish to input to run the multithreaded simulation
     * and passes the info to prompt for the details of each later
     *
     * @param maxTicketCapacity The maximum number of tickets available for the event.
     */

    private static void collectVendorAndCustomerDetails(int maxTicketCapacity) {
        int numberOfVendors = inputValidator("Enter number of vendors: ");
        int numberOfCustomers = inputValidator("Enter number of customers: ");

        personDetails("Vendor", numberOfVendors, maxTicketCapacity);
        personDetails("Customer", numberOfCustomers, maxTicketCapacity);
    }
    /**
     * Collects details for a specific type of person (either Vendor or Customer) from
     * the user, including their name and the number of tickets. The input is validated
     * to ensure that the number of tickets is within a valid range.
     *
     * @param person The type of person (Vendor or Customer).
     * @param numberOfPeople The number of people of this type to configure collected
     *                       through collectVendorAndCustomerDetails method
     * @param maxTicketCapacity The maximum number of tickets available for the event.
     */
    private static void personDetails(String person, int numberOfPeople, int maxTicketCapacity) {
        Scanner scanner = new Scanner(System.in);
        for (int i = 1; i <= numberOfPeople; i++) {
            System.out.println("Enter details for " + person + " " + i);
            String personName = "";
            while (personName.isEmpty()) {
                System.out.print("Enter " + person + " name: ");
                personName = scanner.nextLine();
            }

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
