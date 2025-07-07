import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../auth.service';

@Component({
  selector: 'app-logout',
  standalone: true,
  imports: [],
  templateUrl: './logout.component.html',
  styleUrl: './logout.component.css'
})
export class LogoutComponent {


  constructor(private authService: AuthService) {}

  ngOnInit() {
    this.logout();
  }

  logout() {
    this.authService.logout();
  }

}
