package threads;

import core.Participant;
import core.TicketPool;
import logging.Logger;

/**
 * The Customer class represents a customer attempting to purchase tickets from a TicketPool.
 * This class extends the Participant Abstract class
 * The Customer class interacts with the TicketPool to try purchasing a batch of tickets and logs the result of the transaction.
 */
public class Customer extends Participant {
    /**
     * Constructs a Customer instance with the specified ticket pool, customer ID, name, and ticket batch size.
     *
     * @param ticketpool      The TicketPool from which the customer will purchase tickets.
     * @param customerID      The unique identifier for the customer.
     * @param customerName    The name of the customer.
     * @param ticketBatchSize The number of tickets the customer intends to purchase.
     */
    public Customer(TicketPool ticketpool, int customerID, String customerName, int ticketBatchSize) {
        super(ticketpool, customerID, customerName, ticketBatchSize);

    }

    /**
     * Constructs a Customer instance with the specified customer ID, name, and ticket batch size.
     * This constructor is used to store a customer in the configuration file without associating them with a ticket pool at this stage,
     * assuming only one pool exists for simplification.
     *
     * @param customerID      The unique identifier for the customer.
     * @param name            The name of the customer.
     * @param ticketBatchSize The number of tickets the customer intends to purchase.
     */
    public Customer(int customerID, String name, int ticketBatchSize) {
        super(customerID, name, ticketBatchSize);

    }

    /**
     * Executes the customer thread, attempting to purchase the specified number of tickets from the TicketPool.
     * The customer will try to purchase tickets until the specified ticketBatchSize is reached or the pool is sold out.
     * If the customer is interrupted, they will stop purchasing tickets
     * A log message finally includes the summary of the transactions, if the intended amount is purchased, ticket pool
     * is sold out or the thread is interrupted
     */

    public void run() {
        int ticketsPurchasedByBuyer = 0;
        try {
            while (ticketsPurchasedByBuyer < ticketBatchSize && !ticketpool.isSoldOut()) {
                boolean removed = super.ticketpool.removeTicket(name);
                if (removed) {
                    ticketsPurchasedByBuyer++;
                } else {
                    break;
                }
                Thread.sleep(ticketpool.getCustomerRetrievalRate()); // Simulate time for ticket purchase
            }

        } catch (InterruptedException e) {
            System.out.println(name + " was interrupted and has stopped.");

        } finally {
            Logger.log(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>Summary: " + name + " purchased " + ticketsPurchasedByBuyer + " tickets from " + ticketBatchSize + " requested.");
        }
    }

}

