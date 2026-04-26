import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

/**
 * OrderFormComponent — formulario para hacer un pedido.
 * TODO: implementar el formulario completo.
 */
@Component({
  selector: 'app-order-form',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div style="padding: 2rem; text-align: center;">
      <h2>Realizar pedido</h2>
      <p>Formulario de pedido — en construcción.</p>
    </div>
  `
})
export class OrderFormComponent {}
