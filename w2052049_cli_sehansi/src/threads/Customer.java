package threads;

import core.TicketPool;
import logging.Logger;

/**
 * The Customer class represents a customer attempting to purchase tickets from a TicketPool.
 * This class implements the Runnable interface to enable concurrent execution of multiple customer threads.
 */
public class Customer implements Runnable {
    private TicketPool ticketpool;
    private final int customerID;
    private String customerName;
    private int ticketBatchSize;

    /**
     * Constructs a Customer object with the specified ticket pool, customer ID, name, and ticket batch size.
     *
     * @param ticketpool The TicketPool from which the customer will purchase tickets.
     * @param id The unique identifier for the customer.
     * @param customerName The name of the customer.
     * @param ticketBatchSize The number of tickets the customer intends to purchase, the batchsize
     */
    public Customer(TicketPool ticketpool, int id, String customerName, int ticketBatchSize) {
        this.ticketpool = ticketpool;
        customerID = id;
        this.customerName = customerName;
        this.ticketBatchSize = ticketBatchSize;

    }
    /**
     * Constructs a Customer object with the specified customer ID, name, and ticket batch size.
     * This constructor is used to store a customer to config file without a ticketpool at this stage
     * assuming only one pool exists (for simplification and clarity)
     *
     * @param customerID The unique identifier for the customer.
     * @param name The name of the customer.
     * @param ticketAmount The number of tickets the customer intends to purchase.
     */
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

    /**
     * Executes the customer thread, attempting to purchase the specified number of tickets from the TicketPool.
     * The customer will try to purchase tickets until the specified ticketBatchSize is reached or the pool is sold out.
     * If the customer is interrupted, they will stop purchasing tickets
     * A log message finally includes the summary of the transactions, if the intended amount is purchased, ticket pool
     * is sold out or the thread is interrupted
     */
    @Override
    public void run() {
        int ticketsPurchasedByBuyer = 0;
        try {
                while (ticketsPurchasedByBuyer < ticketBatchSize && !ticketpool.isSoldOut() ) {
                    boolean removed = ticketpool.removeTicket(customerName);
                    if (removed) {
                        ticketsPurchasedByBuyer++;
                    } else {
                        break;
                    }
                    Thread.sleep(ticketpool.getCustomerRetrievalRate()); // Simulate time for ticket purchase
                }

        } catch (InterruptedException e) {
            System.out.println(customerName + " was interrupted and has stopped.");

        }
        finally{
            Logger.log(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>Summary: " + customerName + " purchased " + ticketsPurchasedByBuyer + " tickets from " + ticketBatchSize + " requested.");
        }
    }

}

