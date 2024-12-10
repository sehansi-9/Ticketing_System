import { Injectable, Inject, PLATFORM_ID } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, Subject } from 'rxjs';
import { isPlatformBrowser } from '@angular/common'; //to stop httpclient from attempting to use xmlhttprequest in sprinboot server
import { webSocket, WebSocketSubject } from 'rxjs/webSocket';
@Injectable({
  providedIn: 'root'  // This ensures the service is available app-wide
})
export class TicketService {
  private apiUrl = 'http://localhost:8080/api/info'; 
  private addCustomerUrl = 'http://localhost:8080/api/addcustomer'; 
  private addVendorUrl = 'http://localhost:8080/api/addvendor';   
  private startSystemUrl = 'http://localhost:8080/api/start';
  private stopSystemUrl = 'http://localhost:8080/api/stop';
  private getNameUrl = 'http://localhost:8080/api/name';

  private webSocketUrl = 'ws://localhost:8080/ws/logs'
  private logSocket$!: WebSocketSubject<string>;
  private logsSubject = new Subject<string>()

  constructor(private http: HttpClient, @Inject(PLATFORM_ID) private platformId: Object) {}

  getPoolInfo(): Observable<any> {
  
    if (isPlatformBrowser(this.platformId)) {
      return this.http.get<any>(this.apiUrl);
    }
    return new Observable(); 
  }

  getPoolName(): Observable<string> {
    if (isPlatformBrowser(this.platformId)) {
      return this.http.get(this.getNameUrl, { responseType: 'text' });
    }
    return new Observable<string>(); // Return an observable with a string type
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


connectWebSocket(): void {
  if (!this.logSocket$ || this.logSocket$.closed) {
    console.log('Attempting to connect to WebSocket...');
    
   
    this.logSocket$ = webSocket<string>({
      url: this.webSocketUrl,
      deserializer: (msg: MessageEvent) => msg.data as string  
    });

    this.logSocket$.subscribe({
      next: (message: string) => {
        console.log('Received message:', message);
        this.logsSubject.next(message); 
      },
      error: (err) => {
        console.error('WebSocket error', err);
      },
      complete: () => {
        console.warn('WebSocket connection closed');
      },
    });
  }
}

  

  getLogs(): Observable<string> {
    if (isPlatformBrowser(this.platformId)) {
      return this.logsSubject.asObservable();
    }
    return new Observable();
    
  }

  disconnectWebSocket(): void {
    if (this.logSocket$) {
      this.logSocket$.complete();
    }
  }

  startSystem(): Observable<any> {
    if (isPlatformBrowser(this.platformId)) {
      return this.http.post<any>(this.startSystemUrl, {}, { responseType: 'text' as 'json' });
    }
    return new Observable();
  }

  stopSystem(): Observable<any> {
    if (isPlatformBrowser(this.platformId)) {
      return this.http.post<any>(this.stopSystemUrl, {}, { responseType: 'text' as 'json' });
    }
    return new Observable();
  }


}
