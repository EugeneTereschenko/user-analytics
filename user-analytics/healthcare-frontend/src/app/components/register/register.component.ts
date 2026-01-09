import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { UserType } from '../../models/user.model';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  template: `
    <div class="container">
      <div class="row justify-content-center align-items-center min-vh-100 py-5">
        <div class="col-md-7 col-lg-6">
          <div class="card shadow-lg border-0">
            <div class="card-body p-5">
              <div class="text-center mb-4">
                <h2 class="fw-bold text-primary mb-2">Create Account</h2>
                <p class="text-muted">Sign up to get started</p>
              </div>

              <form [formGroup]="registerForm" (ngSubmit)="onSubmit()">
                <div class="row">
                  <div class="col-md-6 mb-3">
                    <label for="firstName" class="form-label">First Name</label>
                    <input
                      type="text"
                      class="form-control"
                      [class.is-invalid]="submitted && f['firstName'].errors"
                      id="firstName"
                      formControlName="firstName"
                      placeholder="John"
                    />
                    <div *ngIf="submitted && f['firstName'].errors" class="invalid-feedback">
                      <div *ngIf="f['firstName'].errors['required']">First name is required</div>
                    </div>
                  </div>

                  <div class="col-md-6 mb-3">
                    <label for="lastName" class="form-label">Last Name</label>
                    <input
                      type="text"
                      class="form-control"
                      [class.is-invalid]="submitted && f['lastName'].errors"
                      id="lastName"
                      formControlName="lastName"
                      placeholder="Doe"
                    />
                    <div *ngIf="submitted && f['lastName'].errors" class="invalid-feedback">
                      <div *ngIf="f['lastName'].errors['required']">Last name is required</div>
                    </div>
                  </div>
                </div>

                <div class="mb-3">
                  <label for="username" class="form-label">Username</label>
                  <input
                    type="text"
                    class="form-control"
                    [class.is-invalid]="submitted && f['username'].errors"
                    id="username"
                    formControlName="username"
                    placeholder="johndoe"
                  />
                  <div *ngIf="submitted && f['username'].errors" class="invalid-feedback">
                    <div *ngIf="f['username'].errors['required']">Username is required</div>
                    <div *ngIf="f['username'].errors['minlength']">Username must be at least 3 characters</div>
                  </div>
                </div>

                <div class="mb-3">
                  <label for="email" class="form-label">Email</label>
                  <input
                    type="email"
                    class="form-control"
                    [class.is-invalid]="submitted && f['email'].errors"
                    id="email"
                    formControlName="email"
                    placeholder="john@example.com"
                  />
                  <div *ngIf="submitted && f['email'].errors" class="invalid-feedback">
                    <div *ngIf="f['email'].errors['required']">Email is required</div>
                    <div *ngIf="f['email'].errors['email']">Email must be valid</div>
                  </div>
                </div>

                <div class="mb-3">
                  <label for="phoneNumber" class="form-label">Phone Number <span class="text-muted">(Optional)</span></label>
                  <input
                    type="tel"
                    class="form-control"
                    id="phoneNumber"
                    formControlName="phoneNumber"
                    placeholder="+1234567890"
                  />
                </div>

                <div class="mb-3">
                  <label for="userType" class="form-label">User Type</label>
                  <select 
                    class="form-select" 
                    [class.is-invalid]="submitted && f['userType'].errors"
                    id="userType" 
                    formControlName="userType"
                  >
                    <option value="">Select user type</option>
                    <option *ngFor="let type of userTypes" [value]="type">{{ type }}</option>
                  </select>
                  <div *ngIf="submitted && f['userType'].errors" class="invalid-feedback">
                    <div *ngIf="f['userType'].errors['required']">User type is required</div>
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
                    placeholder="••••••••"
                  />
                  <div *ngIf="submitted && f['password'].errors" class="invalid-feedback">
                    <div *ngIf="f['password'].errors['required']">Password is required</div>
                    <div *ngIf="f['password'].errors['minlength']">Password must be at least 8 characters</div>
                  </div>
                </div>

                <div class="mb-4">
                  <label for="confirmPassword" class="form-label">Confirm Password</label>
                  <input
                    type="password"
                    class="form-control"
                    [class.is-invalid]="submitted && (f['confirmPassword'].errors || registerForm.errors?.['passwordMismatch'])"
                    id="confirmPassword"
                    formControlName="confirmPassword"
                    placeholder="••••••••"
                  />
                  <div *ngIf="submitted && f['confirmPassword'].errors" class="invalid-feedback">
                    <div *ngIf="f['confirmPassword'].errors['required']">Please confirm your password</div>
                  </div>
                  <div *ngIf="submitted && registerForm.errors?.['passwordMismatch']" class="invalid-feedback d-block">
                    Passwords do not match
                  </div>
                </div>

                <div *ngIf="error" class="alert alert-danger" role="alert">
                  <i class="bi bi-exclamation-triangle-fill me-2"></i>{{ error }}
                </div>

                <button type="submit" class="btn btn-primary w-100 py-2" [disabled]="loading">
                  <span *ngIf="loading" class="spinner-border spinner-border-sm me-2"></span>
                  {{ loading ? 'Creating account...' : 'Create Account' }}
                </button>
              </form>
            </div>
            <div class="card-footer bg-light text-center py-3">
              <small class="text-muted">Already have an account? <a routerLink="/login" class="text-decoration-none fw-semibold">Sign in</a></small>
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
export class RegisterComponent {
  private fb = inject(FormBuilder);
  private authService = inject(AuthService);
  private router = inject(Router);

  registerForm: FormGroup;
  submitted = false;
  loading = false;
  error = '';
  userTypes = Object.values(UserType);

  constructor() {
    this.registerForm = this.fb.group({
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      username: ['', [Validators.required, Validators.minLength(3)]],
      email: ['', [Validators.required, Validators.email]],
      phoneNumber: [''],
      userType: ['', Validators.required],
      password: ['', [Validators.required, Validators.minLength(8)]],
      confirmPassword: ['', Validators.required]
    }, { validators: this.passwordMatchValidator });
  }

  get f() {
    return this.registerForm.controls;
  }

  passwordMatchValidator(formGroup: FormGroup) {
    const password = formGroup.get('password');
    const confirmPassword = formGroup.get('confirmPassword');
    
    if (password && confirmPassword && password.value !== confirmPassword.value) {
      return { passwordMismatch: true };
    }
    return null;
  }

  onSubmit(): void {
    this.submitted = true;
    this.error = '';

    if (this.registerForm.invalid) {
      return;
    }

    this.loading = true;

    const { confirmPassword, ...registerData } = this.registerForm.value;

    this.authService.register(registerData).subscribe({
      next: () => {
        this.router.navigate(['/dashboard']);
      },
      error: (err) => {
        this.error = err.error?.message || 'Registration failed. Please try again.';
        this.loading = false;
      }
    });
  }
}
