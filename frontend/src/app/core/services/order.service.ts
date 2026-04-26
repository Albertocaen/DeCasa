import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Order, PlaceOrderRequest, OrderStatus } from '../../shared/models/menu.models';

@Injectable({ providedIn: 'root' })
export class OrderService {

  private http = inject(HttpClient);

  placeOrder(req: PlaceOrderRequest): Observable<Order> {
    return this.http.post<Order>(`${environment.apiUrl}/orders`, req);
  }

  trackOrder(orderNumber: string): Observable<Order> {
    return this.http.get<Order>(`${environment.apiUrl}/orders/${orderNumber}`);
  }

  getAllOrders(): Observable<Order[]> {
    return this.http.get<Order[]>(`${environment.apiUrl}/admin/orders`);
  }

  updateStatus(id: number, status: OrderStatus): Observable<Order> {
    return this.http.patch<Order>(`${environment.apiUrl}/admin/orders/${id}/status`, { status });
  }
}
