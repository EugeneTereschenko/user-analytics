// src/app/pages/signup/signup.component.ts
import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-signup',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <!-- MDB CSS -->
    <link href="https://cdnjs.cloudflare.com/ajax/libs/mdb-ui-kit/6.4.2/mdb.min.css" rel="stylesheet" />

    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/mdb-ui-kit/6.3.0/mdb.min.css">
    <!-- MDB JS -->
    <script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/mdb-ui-kit/6.4.2/mdb.min.js"></script>


    <section class="text-center text-lg-start">
    <style>
      .cascading-right {
        margin-right: -50px;
      }

      @media (max-width: 991.98px) {
        .cascading-right {
          margin-right: 0;
        }
      }
    </style>
    <div class="container py-4">
    <div class="row g-0 align-items-center">
      <div class="col-lg-6 mb-5 mb-lg-0">
        <div class="card cascading-right bg-body-tertiary" style="backdrop-filter: blur(30px);">
          <div class="card-body p-5 shadow-5 text-center">
            <h2 class="fw-bold mb-5">Sign up now</h2>
              <form  (ngSubmit)="signUp()" #signupForm="ngForm">
              <!-- 2 column grid layout with text inputs for the first and last names -->
              <div class="row">
                <div class="col-md-6 mb-4">
                  <div data-mdb-input-init class="form-outline">
                    <p class="text-start mb-1">First name</p>
                    <input 
                    type="text" 
                    id="form3Example1" 
                    class="form-control" 
                    [(ngModel)]="firstName"
                    name="firstName" 
                    style="border: 2px solid #007bff;" />
                  </div>
                </div>
                <div class="col-md-6 mb-4">
                  <div data-mdb-input-init class="form-outline">
                    <p class="text-start mb-1">Last name</p>
                    <input 
                    type="text" 
                    id="form3Example2" 
                    class="form-control"
                    [(ngModel)]="lastName" 
                    name="lastName" 
                    style="border: 2px solid #007bff;" />
                  </div>
                </div>
              </div>
              <!-- Email input -->
              <div data-mdb-input-init class="form-outline mb-4">
                <p class="text-start mb-1">Email address</p>
                <input 
                type="email"
                id="form3Example3" 
                [(ngModel)]="email" 
                name="email" 
                class="form-control" 
                placeholder="email" 
                required 
                style="border: 2px solid #007bff;" />
              </div>
              <!-- Password input -->
              <div data-mdb-input-init class="form-outline mb-4">
                <p class="text-start mb-1">Password</p>
                <input 
                type="password" 
                [(ngModel)]="password" 
                name="password" 
                id="form3Example4" 
                class="form-control" 
                placeholder="password" 
                required 
                style="border: 2px solid #007bff;" />
              </div>

              <!-- Confirm Password input -->
              <div data-mdb-input-init class="form-outline mb-4">
                <p class="text-start mb-1">Confirm Password:</p>
                <input 
                type="password" 
                [(ngModel)]="confirmPassword" 
                name="confirmPassword" 
                id="form3Example5" 
                class="form-control" 
                placeholder="Confirm Password:" 
                required 
                style="border: 2px solid #007bff;" />
              </div> 

              <!-- Checkbox -->
              <div class="form-check d-flex justify-content-center mb-4">
                <input class="form-check-input me-2" type="checkbox" value="" id="form2Example33" checked />
                <label class="form-check-label" for="form2Example33">
                  Subscribe to our newsletter
                </label>
              </div>

              <!-- Submit button -->
              <button type="submit" [disabled]="signupForm.invalid" data-mdb-button-init data-mdb-ripple-init class="btn btn-primary btn-block mb-4">
                Sign up
              </button>

              <!-- Register buttons -->
              <div class="text-center">
                <p>or sign up with:</p>
                <button  type="button" data-mdb-button-init data-mdb-ripple-init class="btn btn-link btn-floating mx-1">
                  <i class="fab fa-facebook-f"></i>
                </button>

                <button  type="button" data-mdb-button-init data-mdb-ripple-init class="btn btn-link btn-floating mx-1">
                  <i class="fab fa-google"></i>
                </button>

                <button  type="button" data-mdb-button-init data-mdb-ripple-init class="btn btn-link btn-floating mx-1">
                  <i class="fab fa-twitter"></i>
                </button>

                <button  type="button" data-mdb-button-init data-mdb-ripple-init class="btn btn-link btn-floating mx-1">
                  <i class="fab fa-github"></i>
                </button>
              </div>

              <!-- Sign in link -->
              <div class="text-center mt-3">
                <p>Already have an account? <a href="/signin" class="fw-bold">Sign in</a></p>
              </div>
            </form>
          </div>
        </div>
      </div>

      <div class="col-lg-6 mb-5 mb-lg-0">
        <img src="https://mdbootstrap.com/img/new/ecommerce/vertical/004.jpg" class="w-100 rounded-4 shadow-4"
          alt="" />
      </div>
    </div>
  </div>
  <!-- Jumbotron -->
</section>

    <p *ngIf="errorMessage" style="color: red;">{{ errorMessage }}</p>
    <p *ngIf="successMessage" style="color: green;">{{ successMessage }}</p>
  `
})
export class SignupComponent {
  firstName = '';
  lastName = '';
  username = '';
  email = '';
  password = '';
  confirmPassword = '';
  roles: string[] = ['ROLE_USER'];

  errorMessage = '';
  successMessage = '';
  constructor(private authService: AuthService) {}

  signUp() {
    this.errorMessage = '';
    this.successMessage = '';

    if (this.password !== this.confirmPassword) {
      this.errorMessage = 'Passwords do not match.';
      return;
    }

    if (this.firstName && this.lastName) {
      this.username = this.firstName + ' ' + this.lastName;
    }

    // Simulate API call
    console.log('Signing up user:', this.email);
    this.successMessage = 'Signup successful!';

    this.authService.signUp(this.username, this.email, this.password, undefined, undefined, this.roles).subscribe({
      next: () => {
      this.successMessage = 'Signup successful!';
      // Navigate to signin page after successful signup
      window.location.href = '/signin';
      },
      error: err => this.errorMessage = err.error.message || 'Signup failed'
    });
  }
}