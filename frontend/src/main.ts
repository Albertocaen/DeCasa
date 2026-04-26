import { bootstrapApplication } from '@angular/platform-browser';
import { AppComponent } from './app/app.component';
import { appConfig } from './app/app.config';

/**
 * Punto de entrada de la aplicación Angular.
 *
 * bootstrapApplication() arranca la app con:
 *   - AppComponent → el componente raíz (el shell de la app)
 *   - appConfig    → providers globales (router, httpClient, etc.)
 *
 * En Angular 17+ standalone ya no hay AppModule — todo se configura aquí.
 */
bootstrapApplication(AppComponent, appConfig)
  .catch(err => console.error(err));
