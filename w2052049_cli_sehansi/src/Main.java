import config.Configuration;
import core.TicketPool;
import input.CommandLineInput;
import threads.Customer;
import threads.Vendor;

import java.util.ArrayList;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) throws InterruptedException {

        System.out.println("Welcome to Ticketing Site!");
        Configuration info = CommandLineInput.initializeConfiguration("../config.json");

        TicketPool ticketPoolInfo = info.getTicketPool();
        TicketPool event1 = new TicketPool(ticketPoolInfo.getEvent(), ticketPoolInfo.getMaxTicketCapacity(), ticketPoolInfo.getCustomerRetrievalRate(), ticketPoolInfo.getTicketReleaseRate());

        ArrayList<Thread> threadList = new ArrayList<>();
        boolean[] stopFlag = {false};

        for (int i = 0; i < info.getVendors().size(); i++) {
            Vendor vendor = new Vendor(event1,info.getVendors().get(i).getVendorID(),info.getVendors().get(i).getVendorName(), info.getVendors().get(i).getTicketBatchSize(), stopFlag);
            Thread thread = new Thread(vendor);
            threadList.add(thread);
        }

        for (int i = 0; i < info.getCustomers().size(); i++) {
            Customer customer = new Customer(event1,info.getCustomers().get(i).getCustomerID() ,info.getCustomers().get(i).getCustomerName(), info.getCustomers().get(i).getTicketBatchSize(), stopFlag);
            Thread thread = new Thread(customer);
            threadList.add(thread);
        }

        // Thread to listen for the 'stop' command
        Thread stopListenerThread = new Thread() {
            @Override
            public void run() {
                Scanner scanner = new Scanner(System.in);
                System.out.println("Type 'x' to stop the system:");
                while (!stopFlag[0]) {
                    String command = scanner.next();
                    if (command.equalsIgnoreCase("x")) {
                        stopFlag[0] = true; // Signal threads to stop
                        System.out.println("Stopping threads...");
                    }
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
            stopListenerThread.start();
            stopListenerThread.join();
            for (Thread thread : threadList) {
                try {
                    thread.join();
                    stopListenerThread.join();
                }
                catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }

        }


    }

}
