import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../auth.service';

@Component({
  selector: 'app-signin',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
      <!-- MDB CSS -->
    <link href="https://cdnjs.cloudflare.com/ajax/libs/mdb-ui-kit/6.4.2/mdb.min.css" rel="stylesheet" />

    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/mdb-ui-kit/6.3.0/mdb.min.css">
    <!-- MDB JS -->
    <script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/mdb-ui-kit/6.4.2/mdb.min.js"></script>
      <!-- Section: Design Block -->
      <section class=" text-center text-lg-start">
        <style>
          .rounded-t-5 {
              border-top-left-radius: 0.5rem;
              border-top-right-radius: 0.5rem;
          }

          @media (min-width: 992px) {
            .rounded-tr-lg-0 {
              border-top-right-radius: 0;
          }

            .rounded-bl-lg-5 {
              border-bottom-left-radius: 0.5rem;
          }

            .signin-card-max-width {
              max-width: 1000px;
              margin-left: auto;
              margin-right: auto;
          }
        }
        </style>
        <div class="card mb-3 signin-card-max-width">
          <div class="row g-0 d-flex align-items-center">
            <div class="col-lg-4 d-none d-lg-flex">
              <img src="https://mdbootstrap.com/img/new/ecommerce/vertical/004.jpg" alt="Trendy Pants and Shoes"
                class="w-100 rounded-t-5 rounded-tr-lg-0 rounded-bl-lg-5" />
            </div>
            <div class="col-lg-8">
              <div class="card-body py-5 px-md-5">

                <form (ngSubmit)="signIn()">
                  <!-- Email input -->
                  <div data-mdb-input-init class="form-outline mb-4">
                    <p class="text-start mb-1">Email address</p>
                    <input 
                    type="email" 
                    [(ngModel)]="email" 
                    id="form2Example1" 
                    class="form-control"
                    name="email"
                    required 
                    style="border: 2px solid #007bff;" />
                  </div>

                  <!-- Password input -->
                  <div data-mdb-input-init class="form-outline mb-4">
                    <p class="text-start mb-1">Password</p>
                    <input 
                    type="password"
                    [(ngModel)]="password" 
                    id="form2Example2" 
                    class="form-control"
                    name="password"
                    required 
                    style="border: 2px solid #007bff;"  />
                  </div>

                  <!-- 2 column grid layout for inline styling -->
                  <div class="row mb-4">
                    <div class="col d-flex justify-content-center">
                      <!-- Checkbox -->
                      <div class="form-check">
                        <input class="form-check-input" type="checkbox" value="" id="form2Example31" checked />
                        <label class="form-check-label" for="form2Example31"> Remember me </label>
                      </div>
                    </div>

                    <div class="col">
                      <!-- Simple link -->
                      <a href="#!">Forgot password?</a>
                    </div>
                  </div>

                  <!-- Submit button -->
                  <button  type="submit" data-mdb-button-init data-mdb-ripple-init class="btn btn-primary btn-block mb-4">Sign in</button>

                </form>

              </div>
            </div>
          </div>
        </div>
      </section>
      <!-- Section: Design Block -->
  `
})
export class SigninComponent {
  username = '';
  email = '';
  password = '';
  roles: string[] = ['ROLE_USER']; // Default roles, can be modified as needed

    constructor(private authService: AuthService) {}

  signIn() {
    console.log('Signing in:', this.email, this.password);
    // Call AuthService.login() here

    this.authService.signIn(this.username, this.email, this.password, this.roles).subscribe({
      next: (response) => {
        console.log('Sign in successful:', response);
        // Handle successful sign-in, e.g., redirect to dashboard
      },
      error: (error) => {
        console.error('Sign in failed:', error);
        // Handle sign-in error, e.g., show an error message
      }
    });
  }
}
