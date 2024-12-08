import { Component, Input } from '@angular/core'; 
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms'; 
import { TicketService } from '../../services/ticket.service';

@Component({
  selector: 'app-configuration-form',
  standalone: true,
  imports: [CommonModule,  FormsModule],
  templateUrl: './configuration-form.component.html',
  styleUrl: './configuration-form.component.css'
})
export class ConfigurationFormComponent {
  @Input() maxCapacity!: number;

  vendorName: string = '';
  customerName: string = '';
  vendorCapacity: number | null = null;
  customerCapacity: number | null= null;

  isVendorFormVisible: boolean = true;
  isCustomerFormVisible: boolean = false;

  formDisabled: boolean= false;

  constructor(private ticketService: TicketService) {}

  showVendorForm() {
    this.isVendorFormVisible = true;
    this.isCustomerFormVisible = false;
  }

  showCustomerForm(){
    this.isCustomerFormVisible = true;
    this.isVendorFormVisible = false;
  }

  submitForm(){
    if(this.isVendorFormVisible){
      console.log({
        vendorName: this.vendorName,
        vendorCapacity: this.vendorCapacity
      });
      this.ticketService.addVendor(this.vendorName, this.vendorCapacity!).subscribe(
        (response) => {
          alert("Your request accepted, click on 'start' to release to the pool");
          console.log(response);
        },
        (error) => {
          alert("Your request denied, please try again");
        }
      );
    } else if(this.isCustomerFormVisible){
      console.log({
        customerName: this.customerName,
        customerCapacity: this.customerCapacity
      });
      this.ticketService.addCustomer(this.customerName, this.customerCapacity!).subscribe(
        (response) => {
          alert("Your request accepted, click on 'start' to initiate buying");
          console.log(response);
        },
        (error) => {
          alert("Your request denied, please try again");
          console.log(error)
        }
      );
    }
    this.formDisabled = true;
  }
}
