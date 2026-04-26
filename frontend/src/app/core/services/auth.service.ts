import { Injectable, inject, signal, computed } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { tap } from 'rxjs/operators';
import { environment } from '../../../environments/environment';
import { AuthResponse, LoginRequest } from '../../shared/models/menu.models';

@Injectable({ providedIn: 'root' })
export class AuthService {

  private http   = inject(HttpClient);
  private router = inject(Router);

  private readonly TOKEN_KEY = 'decasa_jwt';

  private tokenSignal = signal<string | null>(localStorage.getItem(this.TOKEN_KEY));

  readonly isLoggedIn = computed(() => this.tokenSignal() !== null);

  readonly currentUsername = computed(() => {
    const token = this.tokenSignal();
    if (!token) return null;
    try {
      const payload = JSON.parse(atob(token.split('.')[1]));
      return payload.sub as string;
    } catch {
      return null;
    }
  });

  login(req: LoginRequest) {
    return this.http.post<AuthResponse>(`${environment.apiUrl}/auth/login`, req).pipe(
      tap(response => {
        localStorage.setItem(this.TOKEN_KEY, response.token);
        this.tokenSignal.set(response.token);
      })
    );
  }

  logout() {
    localStorage.removeItem(this.TOKEN_KEY);
    this.tokenSignal.set(null);
    this.router.navigate(['/']);
  }

  getToken(): string | null {
    return this.tokenSignal();
  }
}
