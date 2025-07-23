import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RolesService } from '../../services/roles.service';

@Component({
  selector: 'app-roles',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './roles.component.html',
  styleUrl: './roles.component.css'
})
export class RolesComponent implements OnInit {
  users: { id: string; name: string; role: string }[] = [];
  roles = ['admin', 'editor', 'viewer'];

  selectedUserId: string | null = null;
  selectedRole: string = '';

  constructor(private rolesService: RolesService) { }

  ngOnInit(): void {
    this.rolesService.getUsersWithRoles().subscribe(users => this.users = users);
  }

  loadUsers() {
    this.rolesService.getUsersWithRoles().subscribe(users => {
      this.users = users;
    });
  }

  onRoleChange(userId: string, role: string) {
    this.selectedUserId = userId;
    this.selectedRole = role;
  }

  updateRole(user: any) {
    this.rolesService.updateUserRole(user.id, user.role).subscribe(() => {
      alert('Role updated!');
    });
  }


}
