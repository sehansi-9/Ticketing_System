import config.Configuration;
import core.TicketPool;
import exceptions.ThreadManagementException;
import input.CommandLineInput;
import threads.Customer;
import threads.Vendor;

import java.util.ArrayList;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) throws ThreadManagementException {

        System.out.println("Welcome to Ticketing Site!");
        Configuration info = CommandLineInput.initializeConfiguration("../config.json");

        TicketPool ticketPoolInfo = info.getTicketPool();
        TicketPool event1 = new TicketPool(ticketPoolInfo.getEvent(), ticketPoolInfo.getMaxTicketCapacity(), ticketPoolInfo.getCustomerRetrievalRate(), ticketPoolInfo.getTicketReleaseRate());

        ArrayList<Thread> threadList = new ArrayList<>();


        for (int i = 0; i < info.getVendors().size(); i++) {
            Vendor vendor = new Vendor(event1,info.getVendors().get(i).getVendorID(),info.getVendors().get(i).getVendorName(), info.getVendors().get(i).getTicketBatchSize());
            Thread thread = new Thread(vendor);
            threadList.add(thread);
        }

        for (int i = 0; i < info.getCustomers().size(); i++) {
            Customer customer = new Customer(event1,info.getCustomers().get(i).getCustomerID() ,info.getCustomers().get(i).getCustomerName(), info.getCustomers().get(i).getTicketBatchSize());
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


        Scanner scanner = new Scanner(System.in);
        System.out.println("Do you want to start the system? (y/n)");
        String answer = scanner.next();

        if (answer.equals("y")) {

            for (Thread thread : threadList) {
                thread.start();
            }

            stopCommandThread.start();
            try {
                stopCommandThread.join();
            }
            catch (InterruptedException e) {
                throw new ThreadManagementException("Error while joining the stop command thread");
            }

            for (Thread thread : threadList) {
                try {
                    thread.join();
                }
                catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new ThreadManagementException("Error while joining customer and vendor threads"+e.getMessage());
                }
            }

        }


    }



}
