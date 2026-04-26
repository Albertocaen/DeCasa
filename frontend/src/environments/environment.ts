/**
 * Variables de entorno para DESARROLLO.
 * Angular sustituye este archivo por environment.prod.ts al hacer `ng build --configuration=production`.
 *
 * Uso: import { environment } from '../environments/environment';
 *      const url = environment.apiUrl + '/menu';
 */
export const environment = {
  production: false,
  apiUrl: 'http://localhost:8080/api'
};
