import { Component, OnInit } from '@angular/core';
import { TicketService } from '../../services/ticket.service'; 
import { ConfigurationFormComponent } from '../configuration-form/configuration-form.component';
import { TicketStatusComponent } from '../ticket-status/ticket-status.component';
@Component({
  selector: 'app-main-layout',
  standalone: true,
  imports: [ConfigurationFormComponent, TicketStatusComponent],
  templateUrl: './main-layout.component.html',
  styleUrls: ['./main-layout.component.css']
})
export class MainLayoutComponent implements OnInit {
  maxTicketCapacity: number = 0;
  ticketReleaseRate: number = 0;
  customerRetrievalRate: number = 0;

  constructor(private ticketService: TicketService) { }

  ngOnInit(): void {
    // Fetch the data using the service
    this.ticketService.getPoolInfo().subscribe(
      (data) => {
        
          this.maxTicketCapacity = data[0];
          this.ticketReleaseRate = data[1]/1000;
          this.customerRetrievalRate = data[2]/1000;
      },
      (error) => {
        console.error('Error fetching ticket pool info', error);
      }
    );
  }
  
}
