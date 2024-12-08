import { bootstrapApplication } from '@angular/platform-browser';
import { AppComponent } from './app/app.component';
import { provideHttpClient } from '@angular/common/http'; // <-- Use provideHttpClient

const bootstrap = () =>
  bootstrapApplication(AppComponent, {
    providers: [
      provideHttpClient(), // <-- Use the new API to configure HttpClient
    ],
  });

export default bootstrap;
