import config.Configuration;
import core.TicketPool;
import exceptions.ThreadManagementException;
import input.CommandLineInput;
import threads.Customer;
import threads.Vendor;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * The Main class is the entry point of the ticketing system application.
 * It initializes and starts the ticketing system, creates customer and vendor threads, and handles the stopping of the system.
 */
public class Main {

    /**
     * The main method that runs the ticketing system. It initializes the configuration,
     * creates ticket pool and threads for vendors and customers, and listens for user input
     * to stop the system by interrupting all threads.
     *
     * @param args Command line arguments.
     * @throws ThreadManagementException If there is an error in managing the operations of threads.
     */
    public static void main(String[] args) throws ThreadManagementException {

        System.out.println("Welcome to Ticketing Site!");

        //load data from the configuration file
        Configuration info = CommandLineInput.initializeConfiguration("../config.json");

        // Create a ticket pool object based on the configuration
        TicketPool ticketPoolInfo = info.getTicketPool();
        TicketPool event1 = new TicketPool(ticketPoolInfo.getEvent(), ticketPoolInfo.getMaxTicketCapacity(), ticketPoolInfo.getCustomerRetrievalRate(), ticketPoolInfo.getTicketReleaseRate());

        // List to store threads for vendors and customers
        ArrayList<Thread> threadList = new ArrayList<>();

        // Create threads for each vendor and add them to the thread list
        for (int i = 0; i < info.getVendors().size(); i++) {
            Vendor vendor = new Vendor(event1, info.getVendors().get(i).getID(), info.getVendors().get(i).getName(), info.getVendors().get(i).getTicketBatchSize());
            Thread thread = new Thread(vendor);
            threadList.add(thread);
        }

        // Create threads for each customer and add them to the thread list
        for (int i = 0; i < info.getCustomers().size(); i++) {
            Customer customer = new Customer(event1, info.getCustomers().get(i).getID(), info.getCustomers().get(i).getName(), info.getCustomers().get(i).getTicketBatchSize());
            Thread thread = new Thread(customer);
            threadList.add(thread);
        }

        // Thread to listen for the 'stop' command
        Thread stopCommandThread = new Thread() {
            @Override
            public void run() {
                Scanner scanner = new Scanner(System.in);
                System.out.println("Type 'x' and enter key to stop the system ");
                String command = scanner.next();
                if (command.equalsIgnoreCase("x")) {
                    for (Thread thread : threadList) {
                        thread.interrupt();
                    }
                    System.out.println("Stopping threads...");
                }

            }
        };

        // Ask the user if they want to start the system
        Scanner scanner = new Scanner(System.in);
        System.out.println("Do you want to start the system? (y/n)");
        String answer = scanner.next();

        // Start the system if the user inputs 'y'
        if (answer.equals("y")) {

            // Start all customer, vendor, and the stopCommandThread and join
            for (Thread thread : threadList) {
                thread.start();
            }
            stopCommandThread.start();

            try {
                stopCommandThread.join();
            } catch (InterruptedException e) {
                throw new ThreadManagementException("Error while joining the stop command thread");
            }

            for (Thread thread : threadList) {
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new ThreadManagementException("Error while joining customer and vendor threads" + e.getMessage());
                }
            }

        }
        System.out.println("Exiting the ticketing system");
        System.exit(0);


    }


}
