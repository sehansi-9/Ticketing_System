package oop.cw.ticketing.threads;

import oop.cw.ticketing.core.TicketPool;
import oop.cw.ticketing.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Vendor implements Runnable {
    private TicketPool ticketpool;
    private final int vendorID;
    private final String vendorName;
    private final int ticketBatchSize;

    @Autowired
    public Vendor(TicketPool ticketpool,
                  @Value("${vendor.id}") int vendorID,
                  @Value("${vendor.name}") String vendorName,
                  @Value("${vendor.ticketBatchSize}") int ticketBatchSize) {
        this.ticketpool = ticketpool;
        this.vendorID = vendorID;
        this.vendorName = vendorName;
        this.ticketBatchSize = ticketBatchSize;
    }
    public Vendor(int vendorID, String name, int ticketAmount) {
        this.vendorID = vendorID;
        vendorName = name;
        ticketBatchSize = ticketAmount;
    }

    public void setTicketPool(TicketPool ticketpool) {
        this.ticketpool = ticketpool;
    }

    @Override
    public void run() {
        int ticketsReleasedByVendor = 0;
        try {
                while (ticketsReleasedByVendor < ticketBatchSize && !ticketpool.isPoolFull()) {
                    boolean added = ticketpool.addTicket(vendorName);
                    if (added) {
                        ticketsReleasedByVendor++;
                    } else {
                        break;
                    }
                    Thread.sleep(ticketpool.getTicketReleaseRate()); // Simulate time for ticket release
                }
        } catch (InterruptedException e) {
            System.out.println(vendorName + " was interrupted and is stopping.");
            System.out.println(vendorName + " has stopped.");
        }finally{
            Logger.log(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>Summary: " + vendorName + " released " + ticketsReleasedByVendor + " from a batch of "+ ticketBatchSize);
        }
    }


}
