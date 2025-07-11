import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { UserService } from '../../user.service';
import { OnInit } from '@angular/core';

@Component({
  selector: 'app-user-detail',
  standalone: true,
  imports: [],
  templateUrl: './user-detail.component.html',
  styleUrl: './user-detail.component.css'
})
export class UserDetailComponent implements OnInit {
  userId!: string;
  user: any;

  constructor(private route: ActivatedRoute, private userService: UserService) {}

  ngOnInit() {
    this.userId = this.route.snapshot.paramMap.get('id')!;
    this.userService.getUser(this.userId).subscribe(data => this.user = data);
  }

  deactivateUser() {
    this.userService.deactivateUser(this.userId).subscribe(() => alert('User deactivated.'));
  }
}