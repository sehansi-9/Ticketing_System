package threads;

import core.TicketPool;
import logging.Logger;

/**
 * The Vendor class represents a vendor attempting to release tickets to a TicketPool.
 * This class implements the Runnable interface to enable concurrent execution of multiple vendor threads.
 */
public class Vendor implements Runnable {
    private TicketPool ticketpool;
    private final int vendorID;
    private String vendorName;
    private int ticketBatchSize;

    /**
     * Constructs a Vendor object with the specified ticket pool, vendor ID, name, and ticket batch size.
     *
     * @param ticketpool The TicketPool from which the vendor will purchase tickets.
     * @param id The unique identifier for the vendor.
     * @param vendorName The name of the vendor.
     * @param ticketBatchSize The number of tickets the vendor intends to release, the batchsize
     */
    public Vendor(TicketPool ticketpool, int id, String vendorName, int ticketBatchSize) {
        this.ticketpool = ticketpool;
        this.vendorID = id;
        this.vendorName = vendorName;
        this.ticketBatchSize = ticketBatchSize;

    }

    /**
     * Constructs a Vendor object with the specified customer ID, name, and ticket batch size.
     * This constructor is used to store a vendor to config file without a ticketpool at this stage
     * assuming only one pool exists (for simplification and clarity)
     *
     * @param vendorID The unique identifier for the vendor.
     * @param name The name of the vendor.
     * @param ticketAmount The number of tickets the vendor intends to release.
     */

    public Vendor(int vendorID, String name, int ticketAmount) {
        this.vendorID = vendorID;
        vendorName = name;
        ticketBatchSize = ticketAmount;
    }

    public int getVendorID() {
        return vendorID;
    }
    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }
    public int getTicketBatchSize() {
        return ticketBatchSize;
    }
    public void setTicketBatchSize(int ticketBatchSize) {
        this.ticketBatchSize = ticketBatchSize;
    }


    /**
     * Executes the vendor thread, attempting to release the specified number of tickets to the TicketPool.
     * The vendor will try to release tickets until the specified ticketBatchSize is reached or the pool
     * is full (totaltickets has reached maxcpacity).
     * If the vendor is interrupted, they will stop releasing tickets
     * A log message finally includes the summary of the transactions, if the intended amount is released, ticket pool
     * is full out or the thread is interrupted
     */
    @Override
    public void run() {
        int ticketsReleasedByVendor = 0;
        try {

                while (ticketsReleasedByVendor < ticketBatchSize && !ticketpool.isPoolFull() ) {
                    boolean added = ticketpool.addTicket(vendorName);
                    if (added) {
                        ticketsReleasedByVendor++;
                    } else {
                        break;
                    }
                    Thread.sleep(ticketpool.getTicketReleaseRate()); // Simulate time for ticket release
                }


        } catch (InterruptedException e) {
            System.out.println(vendorName + " was interrupted and has stopped.");
        }
        finally{
            Logger.log(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>Summary: " + vendorName + " released " + ticketsReleasedByVendor + " tickets from a batch of " + ticketBatchSize);
        }
    }

}
