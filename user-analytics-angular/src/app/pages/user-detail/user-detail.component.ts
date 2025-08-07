import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { UserService } from '../../services/user.service';
import { OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-user-detail',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './user-detail.component.html',
  styleUrl: './user-detail.component.css'
})
export class UserDetailComponent implements OnInit {
  userId!: string;
  user: any;
  users: any[] = [];

  constructor(private route: ActivatedRoute, private userService: UserService) {}

  ngOnInit() {
    this.userService.getAllUsers().subscribe(data => this.users = data);
  }

  /*ngOnInit() {
    this.userId = this.route.snapshot.paramMap.get('id')!;
    if (!this.userId) {
      console.error('User ID is not provided in the route.');
      //return;
    }
    this.userId = '1'; 
    this.userService.getUserById(this.userId).subscribe(data => this.user = data);
  }*/

  deactivateUser() {
    this.userService.deactivateUser(this.userId).subscribe(() => alert('User deactivated.'));
  }
}