import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { MenuResponse, Product, Pack, WeeklySpecial, CategoryType } from '../../shared/models/menu.models';

@Injectable({ providedIn: 'root' })
export class MenuService {

  private http = inject(HttpClient);

  loadMenu(): Observable<MenuResponse> {
    return this.http.get<MenuResponse>(`${environment.apiUrl}/menu`);
  }

  getByCategory(type: CategoryType): Observable<Product[]> {
    return this.http.get<Product[]>(`${environment.apiUrl}/products/category/${type}`);
  }

  getPacks(): Observable<Pack[]> {
    return this.http.get<Pack[]>(`${environment.apiUrl}/packs`);
  }

  getWeeklySpecials(): Observable<WeeklySpecial[]> {
    return this.http.get<WeeklySpecial[]>(`${environment.apiUrl}/weekly-specials/current`);
  }
}
