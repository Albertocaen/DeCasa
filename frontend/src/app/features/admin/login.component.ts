import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  template: `
    <div class="login-container">
      <div class="login-card">
        <h1>De Casa · Admin</h1>

        <form [formGroup]="loginForm" (ngSubmit)="onSubmit()">

          <div class="field">
            <label for="username">Email</label>
            <input id="username" type="email" formControlName="username"
                   placeholder="admin@decasa.be" />
            @if (loginForm.get('username')?.invalid && loginForm.get('username')?.touched) {
              <span class="field-error">Email inválido</span>
            }
          </div>

          <div class="field">
            <label for="password">Contraseña</label>
            <input id="password" type="password" formControlName="password"
                   placeholder="••••••••" />
          </div>

          @if (error()) {
            <div class="error-message">{{ error() }}</div>
          }

          <button type="submit" [disabled]="loginForm.invalid || loading()">
            {{ loading() ? 'Entrando...' : 'Entrar' }}
          </button>

        </form>
      </div>
    </div>
  `,
  styles: [`
    .login-container {
      min-height: 100vh;
      display: flex;
      align-items: center;
      justify-content: center;
      background: #1A1A1A;
    }
    .login-card {
      background: white;
      padding: 2.5rem;
      border-radius: 12px;
      width: 100%;
      max-width: 400px;
      h1 { text-align: center; margin-bottom: 1.5rem; font-size: 1.25rem; }
    }
    .field { margin-bottom: 1rem; }
    label { display: block; font-size: 0.875rem; margin-bottom: 0.25rem; color: #555; }
    input {
      width: 100%; padding: 0.75rem; border: 1px solid #ddd;
      border-radius: 6px; font-size: 1rem; box-sizing: border-box;
    }
    .field-error { color: #e53e3e; font-size: 0.75rem; }
    .error-message {
      background: #fff5f5; color: #e53e3e;
      padding: 0.75rem; border-radius: 6px; margin-bottom: 1rem; font-size: 0.875rem;
    }
    button {
      width: 100%; padding: 0.875rem; background: #C4704A; color: white;
      border: none; border-radius: 6px; font-size: 1rem; cursor: pointer;
      &:disabled { opacity: 0.6; cursor: not-allowed; }
      &:hover:not(:disabled) { background: #a85c38; }
    }
  `]
})
export class LoginComponent {

  private auth   = inject(AuthService);
  private router = inject(Router);
  private fb     = inject(FormBuilder);

  loading = signal(false);
  error   = signal<string | null>(null);

  loginForm = this.fb.group({
    username: ['', [Validators.required, Validators.email]],
    password: ['', Validators.required]
  });

  onSubmit(): void {
    if (this.loginForm.invalid) return;

    this.loading.set(true);
    this.error.set(null);

    const { username, password } = this.loginForm.value;

    this.auth.login({ username: username!, password: password! }).subscribe({
      next: () => this.router.navigate(['/admin/orders']),
      error: () => {
        this.error.set('Credenciales incorrectas');
        this.loading.set(false);
      }
    });
  }
}
