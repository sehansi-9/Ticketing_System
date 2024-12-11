package oop.cw.ticketing.core;

import oop.cw.ticketing.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Represents a pool of tickets for a specific event in the ticketing system. The {@link TicketPool}
 * is responsible for managing ticket availability, allowing vendors to add tickets to the pool and
 * customers to purchase tickets from it.
 *
 * The ticket pool is synchronized to allow safe concurrent access by multiple threads,
 * ensuring that the ticket addition and removal processes are thread-safe.
 *
 * This class is designed to work in a Spring Boot context, with dependency injection for the event,
 * maximum ticket capacity, customer retrieval rate, and ticket release rate using the {@link Autowired}
 * and {@link Value} annotations for configuration properties.
 */
@Component
public class TicketPool {
    private String event;
    private int maxTicketCapacity;
    private int totalTickets;
    private final List<Integer> tickets = Collections.synchronizedList(new LinkedList<>());
    private int customerRetrievalRate;
    private int ticketReleaseRate;

    /**
     * Constructor for creating a {@link TicketPool} object, with dependency injection for the event,
     * maximum ticket capacity, customer retrieval rate, and ticket release rate.
     *
     * @param event The event name for which tickets are managed.
     * @param maxTicketCapacity The maximum number of tickets that the pool can hold.
     * @param customerRetrievalRate The rate at which customers retrieve tickets (used for simulating time).
     * @param ticketReleaseRate The rate at which vendors release tickets into the pool (used for simulating time).
     */
    @Autowired
    public TicketPool(@Value("${ticketpool.event}") String event,
                      @Value("${ticketpool.maxTicketCapacity}") int maxTicketCapacity,
                      @Value("${ticketpool.customerRetrievalRate}") int customerRetrievalRate,
                      @Value("${ticketpool.ticketReleaseRate}") int ticketReleaseRate) {
        this.event = event;
        this.maxTicketCapacity = maxTicketCapacity;
        this.customerRetrievalRate = customerRetrievalRate;
        this.ticketReleaseRate = ticketReleaseRate;
        totalTickets = 0; // tracks the tickets added in total

    }

    /**
     * Attempts to add a ticket to the pool. If the pool is not full, the ticket is added to the list,
     * and the total ticket count is updated.
     *
     * @param vendor The name of the vendor releasing the ticket.
     * @return {@code true} if the ticket was successfully added to the pool, {@code false} if the pool is full.
     */
    public synchronized boolean addTicket(String vendor) {
        if (totalTickets < maxTicketCapacity) {
            tickets.add(1);
            totalTickets++;
            Logger.log(vendor + " released 1 ticket. Total tickets in pool: " + tickets.size());
            return true;
        } else {
            return false;
        }
    }

    /**
     * Attempts to remove a ticket from the pool. If no tickets are available, the method will wait
     * for tickets to become available or until the pool is sold out.
     *
     * @param buyer The name of the customer attempting to purchase a ticket.
     * @return {@code true} if a ticket was successfully purchased, {@code false} if no tickets are available.
     */
    public synchronized boolean removeTicket(String buyer) {
        while (tickets.isEmpty()) {
            try {
                Logger.log("No tickets available currently, Please wait...");
                wait(4000);
                if (isSoldOut()) {
                    return false;
                }
                if (tickets.isEmpty()) {
                    return false;
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return false;
            }
        }
        tickets.remove(0);
        Logger.log(buyer + " purchased 1 ticket. Total tickets in pool: " + tickets.size());
        if (isSoldOut()) {
            Logger.log("! ! ! ! ! ! ! ! ! ! All tickets for the event are sold out! ! ! ! ! ! ! ! ! ! ");
        }
        return true;

    }

    //getters and setters
    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public int getMaxTicketCapacity() {
        return maxTicketCapacity;
    }

    public void setMaxTicketCapacity(int maxTicketCapacity) {
        this.maxTicketCapacity = maxTicketCapacity;
    }

    public int getCustomerRetrievalRate() {
        return customerRetrievalRate;
    }

    public void setCustomerRetrievalRate(int customerRetrievalRate) {
        this.customerRetrievalRate = customerRetrievalRate;
    }

    public int getTicketReleaseRate() {
        return ticketReleaseRate;
    }

    public void setTicketReleaseRate(int ticketReleaseRate) {
        this.ticketReleaseRate = ticketReleaseRate;
    }

    /**
     * Checks if the ticket pool has reached its maximum capacity.
     *
     * @return {@code true} if the pool is full, {@code false} otherwise.
     */
    public synchronized boolean isPoolFull() {
        return totalTickets == maxTicketCapacity;
    }

    /**
     * Checks if the ticket pool is sold out, meaning there are no tickets left in the pool
     * and the maximum capacity has been reached.
     *
     * @return {@code true} if the pool is sold out, {@code false} otherwise.
     */
    public synchronized boolean isSoldOut() {
        return tickets.isEmpty() && totalTickets == maxTicketCapacity;
    }

}

