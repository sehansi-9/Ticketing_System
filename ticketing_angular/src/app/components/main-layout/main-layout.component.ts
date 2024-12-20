import { Component, OnInit } from '@angular/core';
import { TicketService } from '../../services/ticket.service'; 
import { ConfigurationFormComponent } from '../configuration-form/configuration-form.component';

@Component({
  selector: 'app-main-layout',
  standalone: true,
  imports: [ConfigurationFormComponent],
  templateUrl: './main-layout.component.html',
  styleUrls: ['./main-layout.component.css']
})
export class MainLayoutComponent implements OnInit {
  maxTicketCapacity: number = 0;
  ticketReleaseRate: number = 0;
  customerRetrievalRate: number = 0;
  eventName!: string ;

  constructor(private ticketService: TicketService) { }

  ngOnInit(): void {
  
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
    this.ticketService.getPoolName().subscribe(
      (data) =>{
        this.eventName= data;

      },
      (error)=>{
        console.error('Error fetching ticket pool name', error);
      }
    )
  }
  
}
