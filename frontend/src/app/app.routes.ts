import { Routes } from '@angular/router';
import { authGuard } from './core/guards/auth.guard';

export const routes: Routes = [
  {
    path: '',
    loadComponent: () =>
      import('./features/menu/menu.component').then(m => m.MenuComponent)
  },
  {
    path: 'order',
    loadComponent: () =>
      import('./features/order/order-form.component').then(m => m.OrderFormComponent)
  },
  {
    path: 'order/track/:orderNumber',
    loadComponent: () =>
      import('./features/order/order-tracking.component').then(m => m.OrderTrackingComponent)
  },
  {
    path: 'login',
    loadComponent: () =>
      import('./features/admin/login.component').then(m => m.LoginComponent)
  },
  {
    path: 'admin',
    canActivate: [authGuard],
    children: [
      {
        path: 'orders',
        loadComponent: () =>
          import('./features/admin/orders/admin-orders.component').then(m => m.AdminOrdersComponent)
      },
      {
        path: '',
        redirectTo: 'orders',
        pathMatch: 'full'
      }
    ]
  },
  {
    path: '**',
    redirectTo: ''
  }
];
