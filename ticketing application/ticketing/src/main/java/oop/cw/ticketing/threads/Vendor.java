package oop.cw.ticketing.threads;

import oop.cw.ticketing.core.Participant;
import oop.cw.ticketing.core.TicketPool;
import oop.cw.ticketing.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


/**
 * Represents a Vendor in the ticketing system who attempts to release tickets to the {@link TicketPool}.
 * {@link Vendor} class extends {@link Participant} abstract class
 *
 * This class is designed to work in a Spring Boot context, with dependency injection for the ticket pool, vendor ID,
 * name, and ticket batch size using {@link Autowired} and {@link Value} annotations for configuration properties.
 */
@Component
public class Vendor extends Participant {
    /**
     * Constructs a new {@link Vendor} instance.
     * This constructor is used by Spring for dependency injection and initializes the vendor with the provided
     * {@link TicketPool}, vendor ID, name, and ticket batch size.
     *
     * @param ticketpool The {@link TicketPool} from which the vendor will attempt to release tickets.
     * @param vendorID The unique ID of the vendor.
     * @param vendorName The name of the vendor.
     * @param ticketBatchSize The number of tickets the vendor wishes to release in a single transaction.
     */
    @Autowired
    public Vendor(TicketPool ticketpool,
                  @Value("${vendor.id}") int vendorID,
                  @Value("${vendor.name}") String vendorName,
                  @Value("${vendor.ticketBatchSize}") int ticketBatchSize) {
        super(ticketpool, vendorID, vendorName, ticketBatchSize);
    }

    /**
     * Constructs a new {@link Vendor} instance with the given parameters. This constructor is typically used when
     * creating vendors programmatically without Spring's dependency injection, when adding a vendor to config file specially
     *
     * @param vendorID The unique ID of the vendor.
     * @param  vendorName The name of the vendor.
     * @param ticketAmount The number of tickets the vendor wants to release.
     */
    public Vendor(int vendorID, String vendorName, int ticketAmount) {
        super(vendorID, vendorName, ticketAmount);
    }

    /**
     * Runs the ticket releasing operation for this vendor overriding the
     * The vendor will attempt to release tickets to the {@link TicketPool} by adding tickets in a loop.
     * The loop runs until the vendor has either released all their requested tickets or the pool is full.
     * The process simulates time delays using {@link Thread#sleep(long)} to mimic real-world ticket releasing time.
     * In case of an interruption, the vendor will stop the process, and the exception will be handled by printing
     * an appropriate message to the console.
     * After the releasing process completes, a summary of the vendor's ticket purchase is logged using the
     * {@link Logger} class.
     */
    @Override
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
            Logger.log(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>Summary: " + name + " released " + ticketsReleasedByVendor + " from a batch of " + ticketBatchSize);
        }
    }


}
