import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink, RouterLinkActive } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { User } from '../../models/user.model';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, RouterLink, RouterLinkActive],
  template: `
    <nav class="navbar navbar-expand-lg navbar-dark bg-primary shadow-sm">
      <div class="container-fluid">
        <a class="navbar-brand fw-bold" href="#">
          <i class="bi bi-hospital me-2"></i>Healthcare Portal
        </a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
          <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarNav">
          <ul class="navbar-nav ms-auto align-items-center">
            <li class="nav-item dropdown">
              <a class="nav-link dropdown-toggle" href="#" role="button" data-bs-toggle="dropdown">
                <i class="bi bi-person-circle me-1"></i>
                {{ user?.firstName }} {{ user?.lastName }}
              </a>
              <ul class="dropdown-menu dropdown-menu-end">
                <li><a class="dropdown-item" href="#"><i class="bi bi-person me-2"></i>Profile</a></li>
                <li><a class="dropdown-item" href="#"><i class="bi bi-gear me-2"></i>Settings</a></li>
                <li><hr class="dropdown-divider"></li>
                <li><a class="dropdown-item" href="#" (click)="logout()"><i class="bi bi-box-arrow-right me-2"></i>Logout</a></li>
              </ul>
            </li>
          </ul>
        </div>
      </div>
    </nav>

    <div class="container-fluid">
      <div class="row">
        <nav class="col-md-3 col-lg-2 d-md-block bg-light sidebar">
          <div class="position-sticky pt-3">
            <ul class="nav flex-column">
              <li class="nav-item">
                <a class="nav-link" routerLink="/dashboard" routerLinkActive="active" [routerLinkActiveOptions]="{exact: true}">
                  <i class="bi bi-speedometer2 me-2"></i>
                  Dashboard
                </a>
              </li>
              <li class="nav-item">
                <a class="nav-link" href="#">
                  <i class="bi bi-person me-2"></i>
                  Profile
                </a>
              </li>
              <li class="nav-item">
                <a class="nav-link" routerLink="/appointments" routerLinkActive="active">
                  <i class="bi bi-calendar-event me-2"></i>
                  Appointments
                </a>
              </li>
              <li class="nav-item">
                <a class="nav-link" routerLink="/doctors" routerLinkActive="active">
                  <i class="bi bi-hospital me-2"></i>
                  Doctors
                </a>
              </li>
              <li class="nav-item">
                <a class="nav-link" routerLink="/staff" routerLinkActive="active">
                  <i class="bi bi-people me-2"></i>
                  Staff
                </a>
              </li>
              <li class="nav-item">
                <a class="nav-link" href="#">
                  <i class="bi bi-file-medical me-2"></i>
                  Medical Records
                </a>
              </li>
              <li class="nav-item">
                <a class="nav-link" href="#">
                  <i class="bi bi-gear me-2"></i>
                  Settings
                </a>
              </li>
            </ul>
          </div>
        </nav>

        <main class="col-md-9 ms-sm-auto col-lg-10 px-md-4">
          <div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom">
            <div>
              <h1 class="h2">Welcome back, {{ user?.firstName }}!</h1>
              <p class="text-muted">Here's what's happening with your account today.</p>
            </div>
          </div>

          <div class="row g-4 mb-4">
            <div class="col-md-6 col-xl-3">
              <div class="card border-0 shadow-sm h-100">
                <div class="card-body">
                  <div class="d-flex align-items-center">
                    <div class="flex-shrink-0">
                      <div class="bg-primary bg-gradient rounded-3 p-3 text-white">
                        <i class="bi bi-person-badge fs-4"></i>
                      </div>
                    </div>
                    <div class="flex-grow-1 ms-3">
                      <h6 class="text-muted mb-1 text-uppercase small">User Type</h6>
                      <h4 class="mb-0">{{ user?.userType }}</h4>
                    </div>
                  </div>
                </div>
              </div>
            </div>

            <div class="col-md-6 col-xl-3">
              <div class="card border-0 shadow-sm h-100">
                <div class="card-body">
                  <div class="d-flex align-items-center">
                    <div class="flex-shrink-0">
                      <div class="bg-success bg-gradient rounded-3 p-3 text-white">
                        <i class="bi bi-people fs-4"></i>
                      </div>
                    </div>
                    <div class="flex-grow-1 ms-3">
                      <h6 class="text-muted mb-1 text-uppercase small">Active Roles</h6>
                      <h4 class="mb-0">{{ user?.roles?.length || 0 }}</h4>
                    </div>
                  </div>
                </div>
              </div>
            </div>

            <div class="col-md-6 col-xl-3">
              <div class="card border-0 shadow-sm h-100">
                <div class="card-body">
                  <div class="d-flex align-items-center">
                    <div class="flex-shrink-0">
                      <div class="bg-info bg-gradient rounded-3 p-3 text-white">
                        <i class="bi bi-shield-check fs-4"></i>
                      </div>
                    </div>
                    <div class="flex-grow-1 ms-3">
                      <h6 class="text-muted mb-1 text-uppercase small">Permissions</h6>
                      <h4 class="mb-0">{{ user?.permissions?.length || 0 }}</h4>
                    </div>
                  </div>
                </div>
              </div>
            </div>

            <div class="col-md-6 col-xl-3">
              <div class="card border-0 shadow-sm h-100">
                <div class="card-body">
                  <div class="d-flex align-items-center">
                    <div class="flex-shrink-0">
                      <div class="bg-warning bg-gradient rounded-3 p-3 text-white">
                        <i class="bi bi-activity fs-4"></i>
                      </div>
                    </div>
                    <div class="flex-grow-1 ms-3">
                      <h6 class="text-muted mb-1 text-uppercase small">Status</h6>
                      <h4 class="mb-0">Active</h4>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>

          <div class="row g-4">
            <div class="col-lg-12">
              <div class="card border-0 shadow-sm">
                <div class="card-header bg-white border-bottom">
                  <h5 class="mb-0"><i class="bi bi-speedometer me-2"></i>Quick Actions</h5>
                </div>
                <div class="card-body">
                  <div class="row g-3">
                    <div class="col-md-4">
                      <a routerLink="/appointments" class="btn btn-outline-primary w-100 py-3">
                        <i class="bi bi-calendar-plus fs-3 d-block mb-2"></i>
                        <span>Manage Appointments</span>
                      </a>
                    </div>
                    <div class="col-md-4">
                      <a routerLink="/doctors" class="btn btn-outline-success w-100 py-3">
                        <i class="bi bi-hospital fs-3 d-block mb-2"></i>
                        <span>Manage Doctors</span>
                      </a>
                    </div>
                    <div class="col-md-4">
                      <a routerLink="/staff" class="btn btn-outline-info w-100 py-3">
                        <i class="bi bi-people fs-3 d-block mb-2"></i>
                        <span>Manage Staff</span>
                      </a>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </main>
      </div>
    </div>
  `,
  styles: [`
    .sidebar {
      position: sticky;
      top: 0;
      height: calc(100vh - 56px);
      overflow-y: auto;
    }
    .nav-link {
      color: #333;
      padding: 0.75rem 1rem;
      border-radius: 0.25rem;
      transition: all 0.2s;
    }
    .nav-link:hover {
      background-color: rgba(0, 123, 255, 0.1);
      color: #007bff;
    }
    .nav-link.active {
      background-color: #007bff;
      color: white;
    }
    main {
      padding-top: 1rem;
      padding-bottom: 2rem;
    }
  `]
})
export class DashboardComponent implements OnInit {
  private authService = inject(AuthService);
  private router = inject(Router);
  
  user: User | null = null;

  ngOnInit(): void {
    this.authService.currentUser$.subscribe(user => {
      this.user = user;
    });
  }

  logout(): void {
    this.authService.logout();
  }
}
