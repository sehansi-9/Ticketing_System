package core;

import logging.Logger;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
/**
 * The TicketPool class manages a pool of tickets for an event, supporting operations
 * like adding and removing tickets, checking ticket availability, and tracking the total
 * number of tickets available in the pool. This class is synchronized to allow thread-safe
 * access to the ticket pool.
 */
public class TicketPool {
    private String event;
    private int maxTicketCapacity;
    private int totalTickets;
    private final List<Integer> tickets = Collections.synchronizedList(new LinkedList<>());
    private int customerRetrievalRate;
    private int ticketReleaseRate;

    /**
     * Constructs a TicketPool object for the given event with specified ticket capacity
     * and rates for customer retrieval and ticket release.
     *
     * @param event The name of the event for which the ticket pool is being created.
     * @param maxTicketCapacity The maximum number of tickets that can be in the pool.
     * @param customerRetrievalRate The rate at which customers can retrieve tickets.
     * @param ticketReleaseRate The rate at which vendors can release tickets into the pool.
     */
    public TicketPool(String event, int maxTicketCapacity, int customerRetrievalRate, int ticketReleaseRate) {
        this.event = event;
        this.maxTicketCapacity = maxTicketCapacity;
        this.customerRetrievalRate = customerRetrievalRate;
        this.ticketReleaseRate = ticketReleaseRate;
        totalTickets = 0;

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
     * Adds a ticket to the pool, if the capacity allows.
     *
     * @param vendor The vendor releasing the ticket.
     * @return true if the ticket is successfully added, false if the pool is full.
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
     * Removes a ticket from the pool, allowing a customer to purchase it.
     * If no tickets are available, it will wait until one is released.
     *
     * @param buyer The customer buying the ticket.
     * @return true if the ticket is successfully purchased, false if the pool is sold out.
     */
    public synchronized boolean removeTicket(String buyer) {
        while (tickets.isEmpty()) {
            try {
                Logger.log("No tickets available currently, Please wait...");
                wait(1000);
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
    /**
     * Checks if the ticket pool is full (whether the total tickets are equal to the max capacity).
     *
     * @return true if the pool is full, false otherwise.
     */
    public synchronized boolean isPoolFull() {
        return totalTickets == maxTicketCapacity;
    }
    /**
     * Checks if the ticket pool is sold out (no tickets left in pool and the total tickets have reached the max capacity).
     *
     * @return true if the pool is sold out, false otherwise.
     */
    public synchronized boolean isSoldOut() {
        return tickets.isEmpty() && totalTickets == maxTicketCapacity;
    }





}






