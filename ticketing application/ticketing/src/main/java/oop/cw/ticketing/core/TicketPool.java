package oop.cw.ticketing.core;

import oop.cw.ticketing.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@Component
public class TicketPool {
    private String event;
    private int maxTicketCapacity;
    private int totalTickets;
    private final List<Integer> tickets = Collections.synchronizedList(new LinkedList<>());
    private int customerRetrievalRate;
    private int ticketReleaseRate;

    @Autowired
    public TicketPool(@Value("${ticketpool.event}") String event,
                      @Value("${ticketpool.maxTicketCapacity}") int maxTicketCapacity,
                      @Value("${ticketpool.customerRetrievalRate}") int customerRetrievalRate,
                      @Value("${ticketpool.ticketReleaseRate}") int ticketReleaseRate) {
        this.event = event;
        this.maxTicketCapacity = maxTicketCapacity;
        this.customerRetrievalRate = customerRetrievalRate;
        this.ticketReleaseRate = ticketReleaseRate;
        totalTickets = 0;

    }

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

    public synchronized boolean removeTicket(String buyer) {
        while (tickets.isEmpty()) {
            try {
                Logger.log("No tickets available currently, Waiting...");
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

    public synchronized boolean isPoolFull() {
        return totalTickets == maxTicketCapacity;
    }

    public synchronized boolean isSoldOut() {
        return tickets.isEmpty() && totalTickets == maxTicketCapacity;
    }

}
