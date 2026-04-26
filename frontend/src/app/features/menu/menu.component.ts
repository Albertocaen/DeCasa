import { Component, computed, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { toSignal } from '@angular/core/rxjs-interop';
import { catchError, of } from 'rxjs';
import { MenuService } from '../../core/services/menu.service';
import { Pack, WeeklySpecial } from '../../shared/models/menu.models';

@Component({
  selector: 'app-menu',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './menu.component.html',
  styleUrl: './menu.component.scss'
})
export class MenuComponent {

  private menuService = inject(MenuService);

  readonly menuData = toSignal(
    this.menuService.loadMenu().pipe(
      catchError(err => {
        console.error('[MenuComponent]', err);
        return of(null);
      })
    )
  );

  readonly loading  = computed(() => this.menuData() === undefined);
  readonly hasError = computed(() => this.menuData() === null);

  readonly categories = computed(() => {
    const data = this.menuData();
    if (!data) return [];
    return Object.entries(data.productsByCategory)
      .map(([name, products]) => ({ name, products }));
  });

  readonly packs          = computed(() => this.menuData()?.packs          ?? []);
  readonly weeklySpecials = computed(() => this.menuData()?.weeklySpecials ?? []);

  readonly isEmpty = computed(() => {
    const data = this.menuData();
    if (!data) return false;
    return this.categories().length === 0 && this.packs().length === 0;
  });

  readonly currentYear = new Date().getFullYear();

  formatPrice(price: number): string {
    return new Intl.NumberFormat('es-BE', { style: 'currency', currency: 'EUR' }).format(price);
  }

  formatPackPrice(pack: Pack): string {
    return pack.priceMax
      ? `€${pack.priceMin} – €${pack.priceMax}`
      : `€${pack.priceMin}`;
  }

  getWeeklyTapa(specials: WeeklySpecial[]) {
    return specials.find(s => s.type === 'TAPA');
  }

  getWeeklyMini(specials: WeeklySpecial[]) {
    return specials.find(s => s.type === 'MINI');
  }
}
