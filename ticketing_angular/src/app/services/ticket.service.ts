import { Injectable, Inject, PLATFORM_ID } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { isPlatformBrowser } from '@angular/common'; //to stop httpclient from attempting to use xmlhttprequest in sprinboot server
@Injectable({
  providedIn: 'root'  // This ensures the service is available app-wide
})
export class TicketService {
  private apiUrl = 'http://localhost:8080/api/info'; 
  private addCustomerUrl = 'http://localhost:8080/api/addcustomer';  // URL to add a customer
  private addVendorUrl = 'http://localhost:8080/api/addvendor';      // URL to add a vendor

  constructor(private http: HttpClient, @Inject(PLATFORM_ID) private platformId: Object) {}

  getPoolInfo(): Observable<any> {
    // Check if the code is running in the browser
    if (isPlatformBrowser(this.platformId)) {
      return this.http.get<any>(this.apiUrl);
    }
    return new Observable();  // Return an empty Observable if not in the browser
  }

  addCustomer(customerName: string, tickets: number): Observable<any> {
    if (isPlatformBrowser(this.platformId)) {
      const url = `${this.addCustomerUrl}?customerName=${customerName}&tickets=${tickets}`;
      return this.http.post<any>(url, {}, { responseType: 'text' as 'json' });
    }
    return new Observable();
  }


  addVendor(vendorName: string, tickets: number): Observable<any> {
    if (isPlatformBrowser(this.platformId)) {
      const url = `${this.addVendorUrl}?vendorName=${vendorName}&tickets=${tickets}`;
      return this.http.post<any>(url, {}, { responseType: 'text' as 'json' }); 
    }
    return new Observable();
  }
}
