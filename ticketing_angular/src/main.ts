import { provideHttpClient, withFetch } from '@angular/common/http';
import { bootstrapApplication } from '@angular/platform-browser';
import { appConfig } from './app/app.config';
import { AppComponent } from './app/app.component';
import 'whatwg-fetch';  // Polyfill for fetch

// Modify the bootstrap call to include `withFetch()`
bootstrapApplication(AppComponent, {
  ...appConfig,
  providers: [
    provideHttpClient(withFetch())  // Ensure that withFetch is correctly provided
  ]
})
.catch((err) => console.error('Error during bootstrap:', err));
