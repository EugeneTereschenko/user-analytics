import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  template: `
    <div class="container">
      <div class="row justify-content-center align-items-center min-vh-100">
        <div class="col-md-5 col-lg-4">
          <div class="card shadow-lg border-0">
            <div class="card-body p-5">
              <div class="text-center mb-4">
                <h2 class="fw-bold text-primary mb-2">Welcome Back</h2>
                <p class="text-muted">Sign in to your account</p>
              </div>

              <form [formGroup]="loginForm" (ngSubmit)="onSubmit()">
                <div class="mb-3">
                  <label for="usernameOrEmail" class="form-label">Username or Email</label>
                  <input
                    type="text"
                    class="form-control"
                    [class.is-invalid]="submitted && f['usernameOrEmail'].errors"
                    id="usernameOrEmail"
                    formControlName="usernameOrEmail"
                    placeholder="Enter your username or email"
                  />
                  <div *ngIf="submitted && f['usernameOrEmail'].errors" class="invalid-feedback">
                    <div *ngIf="f['usernameOrEmail'].errors['required']">Username or email is required</div>
                  </div>
                </div>

                <div class="mb-3">
                  <label for="password" class="form-label">Password</label>
                  <input
                    type="password"
                    class="form-control"
                    [class.is-invalid]="submitted && f['password'].errors"
                    id="password"
                    formControlName="password"
                    placeholder="Enter your password"
                  />
                  <div *ngIf="submitted && f['password'].errors" class="invalid-feedback">
                    <div *ngIf="f['password'].errors['required']">Password is required</div>
                  </div>
                </div>

                <div class="d-flex justify-content-between align-items-center mb-4">
                  <div class="form-check">
                    <input class="form-check-input" type="checkbox" id="remember">
                    <label class="form-check-label" for="remember">
                      Remember me
                    </label>
                  </div>
                  <a routerLink="/forgot-password" class="text-decoration-none small">Forgot password?</a>
                </div>

                <div *ngIf="error" class="alert alert-danger" role="alert">
                  <i class="bi bi-exclamation-triangle-fill me-2"></i>{{ error }}
                </div>

                <button type="submit" class="btn btn-primary w-100 py-2" [disabled]="loading">
                  <span *ngIf="loading" class="spinner-border spinner-border-sm me-2"></span>
                  {{ loading ? 'Signing in...' : 'Sign In' }}
                </button>
              </form>
            </div>
            <div class="card-footer bg-light text-center py-3">
              <small class="text-muted">Don't have an account? <a routerLink="/register" class="text-decoration-none fw-semibold">Sign up</a></small>
            </div>
          </div>
        </div>
      </div>
    </div>
  `,
  styles: [`
    .min-vh-100 {
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    }
    .card {
      border-radius: 1rem;
    }
  `]
})
export class LoginComponent {
  private fb = inject(FormBuilder);
  private authService = inject(AuthService);
  private router = inject(Router);

  loginForm: FormGroup;
  submitted = false;
  loading = false;
  error = '';

  constructor() {
    this.loginForm = this.fb.group({
      usernameOrEmail: ['', Validators.required],
      password: ['', Validators.required]
    });
  }

  get f() {
    return this.loginForm.controls;
  }

  onSubmit(): void {
    this.submitted = true;
    this.error = '';

    if (this.loginForm.invalid) {
      return;
    }

    this.loading = true;

    this.authService.login(this.loginForm.value).subscribe({
      next: () => {
        this.router.navigate(['/dashboard']);
      },
      error: (err) => {
        this.error = err.error?.message || 'Login failed. Please try again.';
        this.loading = false;
      }
    });
  }
}
