import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { OrderService } from '../../../core/services/order.service';
import { AuthService } from '../../../core/services/auth.service';
import { Order, OrderStatus } from '../../../shared/models/menu.models';

@Component({
  selector: 'app-admin-orders',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="admin-layout">

      <header class="admin-header">
        <h1>De Casa · Pedidos</h1>
        <button (click)="auth.logout()">Cerrar sesión</button>
      </header>

      @if (loading()) {
        <p>Cargando pedidos...</p>
      }

      @if (orders().length > 0) {
        <table class="orders-table">
          <thead>
            <tr>
              <th>Número</th>
              <th>Cliente</th>
              <th>Entrega</th>
              <th>Total</th>
              <th>Estado</th>
              <th>Acción</th>
            </tr>
          </thead>
          <tbody>
            @for (order of orders(); track order.id) {
              <tr [class]="'status-' + order.status.toLowerCase()">
                <td><strong>{{ order.orderNumber }}</strong></td>
                <td>
                  {{ order.customerName }}<br>
                  <small>{{ order.customerPhone }}</small>
                </td>
                <td>
                  {{ order.deliveryType }}<br>
                  <small>{{ order.deliveryDate | date:'dd/MM HH:mm' }}</small>
                </td>
                <td>{{ order.total | currency:'EUR':'symbol':'1.2-2':'es' }}</td>
                <td>
                  <span [class]="'badge badge-' + order.status.toLowerCase()">
                    {{ statusLabel(order.status) }}
                  </span>
                </td>
                <td>
                  @if (order.status === 'PENDING') {
                    <button (click)="updateStatus(order.id, 'CONFIRMED')">Confirmar</button>
                  }
                  @if (order.status === 'CONFIRMED') {
                    <button (click)="updateStatus(order.id, 'IN_PREPARATION')">Preparando</button>
                  }
                  @if (order.status === 'IN_PREPARATION') {
                    <button (click)="updateStatus(order.id, 'READY')">Listo</button>
                  }
                  @if (order.status === 'READY') {
                    <button (click)="updateStatus(order.id, 'DELIVERED')">Entregado</button>
                  }
                </td>
              </tr>
            }
          </tbody>
        </table>
      } @else if (!loading()) {
        <p class="empty">No hay pedidos todavía.</p>
      }

    </div>
  `,
  styles: [`
    .admin-layout { padding: 2rem; font-family: sans-serif; }
    .admin-header {
      display: flex; justify-content: space-between; align-items: center;
      margin-bottom: 2rem; padding-bottom: 1rem; border-bottom: 2px solid #C4704A;
    }
    .orders-table {
      width: 100%; border-collapse: collapse;
      th, td { padding: 0.75rem 1rem; text-align: left; border-bottom: 1px solid #eee; }
      th { background: #1A1A1A; color: white; }
      tr:hover td { background: #f9f9f9; }
    }
    .badge {
      display: inline-block; padding: 0.2rem 0.5rem;
      border-radius: 4px; font-size: 0.75rem; font-weight: bold;
    }
    .badge-pending        { background: #fef3c7; color: #92400e; }
    .badge-confirmed      { background: #dbeafe; color: #1e40af; }
    .badge-in_preparation { background: #fde68a; color: #78350f; }
    .badge-ready          { background: #d1fae5; color: #065f46; }
    .badge-delivered      { background: #e5e7eb; color: #374151; }
    .badge-cancelled      { background: #fee2e2; color: #991b1b; }
    button {
      padding: 0.4rem 0.8rem; background: #C4704A; color: white;
      border: none; border-radius: 4px; cursor: pointer; font-size: 0.8rem;
      &:hover { background: #a85c38; }
    }
    .empty { text-align: center; color: #888; padding: 3rem; }
  `]
})
export class AdminOrdersComponent implements OnInit {

  private orderService = inject(OrderService);
  auth = inject(AuthService);

  orders  = signal<Order[]>([]);
  loading = signal(false);

  ngOnInit(): void {
    this.loadOrders();
  }

  loadOrders(): void {
    this.loading.set(true);
    this.orderService.getAllOrders().subscribe({
      next: orders => {
        this.orders.set(orders);
        this.loading.set(false);
      },
      error: () => this.loading.set(false)
    });
  }

  updateStatus(orderId: number, status: OrderStatus): void {
    this.orderService.updateStatus(orderId, status).subscribe({
      next: updated => {
        this.orders.update(orders => orders.map(o => o.id === orderId ? updated : o));
      }
    });
  }

  statusLabel(status: OrderStatus): string {
    const labels: Record<OrderStatus, string> = {
      PENDING:        'Pendiente',
      CONFIRMED:      'Confirmado',
      IN_PREPARATION: 'Preparando',
      READY:          'Listo',
      DELIVERED:      'Entregado',
      CANCELLED:      'Cancelado'
    };
    return labels[status];
  }
}
