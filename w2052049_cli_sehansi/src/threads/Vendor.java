package threads;

import core.TicketPool;
import logging.Logger;

public class Vendor implements Runnable {
    private TicketPool ticketpool;
    private final int vendorID;
    private String vendorName;
    private int ticketBatchSize;
    private boolean[] stopFlag;


    public Vendor(TicketPool ticketpool, int id, String vendorName, int ticketBatchSize, boolean[] stopFlag) {
        this.ticketpool = ticketpool;
        this.vendorID = id;
        this.vendorName = vendorName;
        this.ticketBatchSize = ticketBatchSize;
        this.stopFlag = stopFlag;
    }

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


    @Override
    public void run() {
        try {
            while (!stopFlag[0]) {
                int ticketsReleasedByVendor = 0;

                while (ticketsReleasedByVendor < ticketBatchSize && !ticketpool.isPoolFull() && !stopFlag[0]) {
                    boolean added = ticketpool.addTicket(vendorName);
                    if (added) {
                        ticketsReleasedByVendor++;
                    } else {
                        break;
                    }
                    Thread.sleep(ticketpool.getTicketReleaseRate()); // Simulate time for ticket release
                }

                Logger.log(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>Summary: " + vendorName + " released " + ticketsReleasedByVendor + " from a batch of "+ ticketBatchSize);

                if (ticketpool.isPoolFull() || stopFlag[0]) {
                    break;
                }
            }
        } catch (InterruptedException e) {
            System.out.println(vendorName + " was interrupted and is stopping.");
            System.out.println(vendorName + " has stopped.");
        }
    }

}
