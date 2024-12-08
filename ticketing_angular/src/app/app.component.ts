import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { ConfigurationFormComponent } from './components/configuration-form/configuration-form.component';
import { MainLayoutComponent } from './components/main-layout/main-layout.component';
import { ControlPanelComponent} from './components/control-panel/control-panel.component';
import { TicketStatusComponent } from './components/ticket-status/ticket-status.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, ConfigurationFormComponent, MainLayoutComponent, ControlPanelComponent,TicketStatusComponent], // Import child components
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'], 
})
export class AppComponent {
  title = 'ticketing_angular'; 
}
