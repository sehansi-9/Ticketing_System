import { Component, OnInit, OnDestroy } from '@angular/core';
import { TicketService } from '../../services/ticket.service';
import {Subscription} from 'rxjs';
import { CommonModule } from '@angular/common'; 

@Component({
  selector: 'app-ticket-status',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './ticket-status.component.html',
  styleUrl: './ticket-status.component.css'
})
export class TicketStatusComponent implements OnInit, OnDestroy{
  logs: string[] = [];
  private logsSubscription: Subscription | null = null;
  isButtonDisabled: boolean = false;

  constructor(private ticketService: TicketService) {}

  ngOnInit(): void {
    this.connectToLogs();
    this.ticketService.connectWebSocket()

}

startSystem(): void {
  this.isButtonDisabled = true;
  this.ticketService.startSystem().subscribe({
    next: (response) => {
      console.log('Simulation started:', response);
    },
    error: (err) => {
      console.error('Error starting simulation:', err);
    }
  });
}

stopSystem(): void {
  this.ticketService.stopSystem().subscribe({
    next: (response) => {
      console.log('Simulation stopped:', response);
    },
    error: (err) => {
      console.error('Error stopping simulation:', err);
    }
  });
}
connectToLogs(): void{
  this.logsSubscription = this.ticketService.getLogs().subscribe({
    next: (log) =>{
      this.logs.push(log);
    },
    error: (err)=>{
      console.log('Error recieving logs:' , err)
    }
  })
}
ngOnDestroy(): void {
    if (this.logsSubscription){
      this.logsSubscription.unsubscribe();
    }
    this.ticketService.disconnectWebSocket()
}


}
