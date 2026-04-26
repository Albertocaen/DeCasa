import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute } from '@angular/router';

/**
 * OrderTrackingComponent — muestra el estado de un pedido por número.
 * La ruta es /order/track/:orderNumber — el parámetro viene de la URL.
 * TODO: implementar la consulta al backend.
 */
@Component({
  selector: 'app-order-tracking',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div style="padding: 2rem; text-align: center;">
      <h2>Seguimiento de pedido</h2>
      <p>Número de pedido: <strong>{{ orderNumber }}</strong></p>
      <p>Seguimiento — en construcción.</p>
    </div>
  `
})
export class OrderTrackingComponent {
  private route = inject(ActivatedRoute);

  // Lee el parámetro :orderNumber de la URL
  orderNumber = this.route.snapshot.paramMap.get('orderNumber');
}
