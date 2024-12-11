package oop.cw.ticketing.threads;

import oop.cw.ticketing.core.Participant;
import oop.cw.ticketing.core.TicketPool;
import oop.cw.ticketing.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Represents a Customer in the ticketing system who attempts to purchase tickets from the {@link TicketPool}.
 * {@link Customer} class extends {@link Participant} abstract class
 *
 *
 * This class is designed to work in a Spring Boot context, with dependency injection for the ticket pool, customer ID,
 * name, and ticket batch size using {@link Autowired} and {@link Value} annotations for configuration properties.
 */
@Component
public class Customer extends Participant {

    /**
     * Constructs a new {@link Customer} instance.
     * This constructor is used by Spring for dependency injection and initializes the customer with the provided
     * {@link TicketPool}, customer ID, name, and ticket batch size.
     *
     * @param ticketpool The {@link TicketPool} from which the customer will attempt to purchase tickets.
     * @param customerID The unique ID of the customer.
     * @param customerName The name of the customer.
     * @param ticketBatchSize The number of tickets the customer wishes to purchase in a single transaction.
     */
    @Autowired
    public Customer(TicketPool ticketpool,
                    @Value("${customer.id}") int customerID,
                    @Value("${customer.name}") String customerName,
                    @Value("${customer.ticketBatchSize}") int ticketBatchSize)  {
        super(ticketpool, customerID, customerName, ticketBatchSize);
    }

    /**
     * Constructs a new {@link Customer} instance with the given parameters. This constructor is typically used when
     * creating customers programmatically without Spring's dependency injection, when adding a customer to config file specially
     *
     * @param customerID The unique ID of the customer.
     * @param customerName The name of the customer.
     * @param ticketAmount The number of tickets the customer wants to purchase.
     */
    public Customer(int customerID, String customerName, int ticketAmount) {
        super(customerID, customerName, ticketAmount);

    }
    /**
     * Runs the ticket purchasing operation for this customer.
     * The customer will attempt to purchase tickets from the {@link TicketPool} by removing tickets in a loop.
     * The loop runs until the customer has either purchased all their requested tickets or the pool is sold out.
     * The process simulates time delays using {@link Thread#sleep(long)} to mimic real-world ticket retrieval time.
     * In case of an interruption, the customer will stop the process, and the exception will be handled by printing
     * an appropriate message to the console.
     * After the purchasing process completes, a summary of the customer's ticket purchase is logged using the
     * {@link Logger} class.
     */
    @Override
    public void run() {
        int ticketsPurchasedByBuyer = 0;
        try {
                while (ticketsPurchasedByBuyer < ticketBatchSize && !ticketpool.isSoldOut()) {
                    boolean removed = ticketpool.removeTicket(name);
                    if (removed) {
                        ticketsPurchasedByBuyer++;
                    } else {
                        break;
                    }
                    Thread.sleep(ticketpool.getCustomerRetrievalRate()); // Simulate time for ticket purchase
                }

        } catch (InterruptedException e) {
            System.out.println(name + " was interrupted and has stopped.");
        }
        finally{
            Logger.log(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>Summary: " + name + " purchased " + ticketsPurchasedByBuyer + " tickets from " + ticketBatchSize + " requested.");
        }

    }




}
