package threads;

import core.Participant;
import core.TicketPool;
import logging.Logger;

/**
 * The Vendor class represents a vendor attempting to release tickets to a TicketPool.
 * This class extends the Participant Abstract class
 * The Vendor class interacts with the TicketPool to try releasing a batch of tickets and logs the result of the transaction.
 */
public class Vendor extends Participant {

    /**
     * Constructs a Vendor instance with the specified ticket pool, vendor ID, name, and ticket batch size.
     *
     * @param ticketpool      The TicketPool to which the vendor will release tickets.
     * @param vendorID        The unique identifier for the vendor.
     * @param vendorName      The name of the vendor.
     * @param ticketBatchSize The number of tickets the vendor intends to release.
     */
    public Vendor(TicketPool ticketpool, int vendorID, String vendorName, int ticketBatchSize) {
        super(ticketpool, vendorID, vendorName, ticketBatchSize);

    }

    /**
     * Constructs a Vendor instance with the specified vendor ID, name, and ticket batch size.
     * This constructor is used to store a vendor in the configuration file without associating them with a ticket pool at this stage,
     * assuming only one pool exists for simplification.
     *
     * @param vendorID        The unique identifier for the vendor.
     * @param vendorName      The name of the vendor.
     * @param ticketBatchSize The number of tickets the vendor intends to release.
     */

    public Vendor(int vendorID, String vendorName, int ticketBatchSize) {
        super(vendorID, vendorName, ticketBatchSize);
    }


    /**
     * Executes the vendor thread, attempting to release the specified number of tickets to the TicketPool.
     * The vendor will try to release tickets until the specified ticketBatchSize is reached or the pool
     * is full (totaltickets has reached maxcpacity).
     * If the vendor is interrupted, they will stop releasing tickets
     * A log message finally includes the summary of the transactions, if the intended amount is released, ticket pool
     * is full out or the thread is interrupted
     */

    public void run() {
        int ticketsReleasedByVendor = 0;
        try {

            while (ticketsReleasedByVendor < ticketBatchSize && !ticketpool.isPoolFull()) {
                boolean added = ticketpool.addTicket(name);
                if (added) {
                    ticketsReleasedByVendor++;
                } else {
                    break;
                }
                Thread.sleep(ticketpool.getTicketReleaseRate()); // Simulate time for ticket release
            }


        } catch (InterruptedException e) {
            System.out.println(name + " was interrupted and has stopped.");
        } finally {
            Logger.log(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>Summary: " + name + " released " + ticketsReleasedByVendor + " tickets from a batch of " + ticketBatchSize);
        }
    }

}
