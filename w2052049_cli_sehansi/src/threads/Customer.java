package threads;

import core.TicketPool;
import logging.Logger;

public class Customer implements Runnable {
    private TicketPool ticketpool;
    private final int customerID;
    private String customerName;
    private int ticketBatchSize;
    private boolean[] stopFlag;

    public Customer(TicketPool ticketpool, int id, String customerName, int ticketBatchSize, boolean[] stopFlag) {
        this.ticketpool = ticketpool;
        customerID = id;
        this.customerName = customerName;
        this.ticketBatchSize = ticketBatchSize;
        this.stopFlag = stopFlag;
    }

    public Customer(int customerID, String name, int ticketAmount) {
        this.customerID = customerID;
        customerName = name;
        ticketBatchSize = ticketAmount;

    }

    public int getCustomerID() {
        return customerID;
    }
    public String getCustomerName() {
        return customerName;
    }
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public int getTicketBatchSize() {
        return ticketBatchSize;
    }
    public void setTicketBatchSize(int ticketBatchSize) {
        this.ticketBatchSize = ticketBatchSize;
    }


    @Override
    public void run() {
        try {
            while (!stopFlag[0]) { // Keep running until stopFlag is true
                int ticketsPurchasedByBuyer = 0;

                while (ticketsPurchasedByBuyer < ticketBatchSize && !ticketpool.isSoldOut() && !stopFlag[0]) {
                    boolean removed = ticketpool.removeTicket(customerName);
                    if (removed) {
                        ticketsPurchasedByBuyer++;
                    } else {
                        break;
                    }
                    Thread.sleep(ticketpool.getCustomerRetrievalRate()); // Simulate time for ticket purchase
                }

                Logger.log(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>Summary: " + customerName + " purchased " + ticketsPurchasedByBuyer + " tickets from " + ticketBatchSize + " requested.");

                if (ticketpool.isSoldOut() || stopFlag[0]) {
                    break;
                }

            }
        } catch (InterruptedException e) {
            System.out.println(customerName + " was interrupted and is stopping.");
            System.out.println(customerName + " has stopped.");
        }
    }

}

