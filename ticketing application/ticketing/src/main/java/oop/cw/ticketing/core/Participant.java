package oop.cw.ticketing.core;

/**
 * The Participant class serves as an abstract class representing a participant in the ticket pool system.
 * It holds the common properties shared by both customers and vendors, such as the ticket pool,
 * ID, name, and ticket batch size. This class is meant to be extended by specific types of participants,
 * such as customers and vendors, each implementing their own distinct behaviors of ticket transactions
 */

public abstract class Participant implements Runnable {
    protected TicketPool ticketpool;
    protected int id;
    protected String name;
    protected int ticketBatchSize;

    /**
     * Constructs a Participant instance with the specified ticket pool, ID, name, and ticket batch size.
     *
     * @param ticketpool      The TicketPool from which the participant (vendor or customer) will either release or purchase tickets.
     * @param id              The unique identifier for the participant.
     * @param name            The name of the participant (vendor or customer).
     * @param ticketBatchSize The number of tickets the participant intends to release or purchase in a batch.
     */
    public Participant(TicketPool ticketpool, int id, String name, int ticketBatchSize) {
        this.ticketpool = ticketpool;
        this.id = id;
        this.name = name;
        this.ticketBatchSize = ticketBatchSize;
    }
    /**
     * Constructs a Participant instance with the specified ID, name, and ticket batch size.
     * This constructor is used for storing the participant in a configuration file without associating them with a ticket pool at this stage.
     *
     * @param id              The unique identifier for the participant.
     * @param name            The name of the participant.
     * @param ticketBatchSize The number of tickets the participant intends to release or purchase.
     */
    public Participant(int id, String name, int ticketBatchSize) {
        this.id = id;
        this.name = name;
        this.ticketBatchSize = ticketBatchSize;
    }


    // setter to set the ticketpool
    public void setTicketPool(TicketPool ticketpool) {
        this.ticketpool = ticketpool;
    }

    /**
     * The run method is an abstract method that must be implemented by subclasses.
     * It defines the behavior of the participant (e.g., releasing tickets for vendors or purchasing tickets for customers).
     */
    public abstract void run();

}
