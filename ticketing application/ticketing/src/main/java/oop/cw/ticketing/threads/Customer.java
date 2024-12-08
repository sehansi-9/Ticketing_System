package oop.cw.ticketing.threads;

import oop.cw.ticketing.core.TicketPool;
import oop.cw.ticketing.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Customer implements Runnable {
    private TicketPool ticketpool;
    private final int customerID;
    private final String customerName;
    private final int ticketBatchSize;

    @Autowired
    public Customer(TicketPool ticketpool,
                    @Value("${customer.id}") int customerID,
                    @Value("${customer.name}") String customerName,
                    @Value("${customer.ticketBatchSize}") int ticketBatchSize)  {
        this.ticketpool = ticketpool;
        this.customerID = customerID;
        this.customerName = customerName;
        this.ticketBatchSize = ticketBatchSize;
    }

    public Customer(int customerID, String name, int ticketAmount) {
        this.customerID = customerID;
        customerName = name;
        ticketBatchSize = ticketAmount;

    }

      public void setTicketPool(TicketPool ticketpool) {
        this.ticketpool = ticketpool;
    }

    @Override
    public void run() {
        int ticketsPurchasedByBuyer = 0;
        try {
                while (ticketsPurchasedByBuyer < ticketBatchSize && !ticketpool.isSoldOut()) {
                    boolean removed = ticketpool.removeTicket(customerName);
                    if (removed) {
                        ticketsPurchasedByBuyer++;
                    } else {
                        break;
                    }
                    Thread.sleep(ticketpool.getCustomerRetrievalRate()); // Simulate time for ticket purchase
                }

        } catch (InterruptedException e) {
            System.out.println(customerName + " was interrupted and is stopping.");
            System.out.println(customerName + " has stopped.");
        }
        finally{
            Logger.log(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>Summary: " + customerName + " purchased " + ticketsPurchasedByBuyer + " tickets from " + ticketBatchSize + " requested.");
        }

    }




}
