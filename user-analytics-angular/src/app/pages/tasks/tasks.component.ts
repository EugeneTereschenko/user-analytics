import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

interface Task {
  id: number;
  title: string;
  done: boolean;
}

@Component({
  selector: 'app-tasks',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './tasks.component.html',
  styleUrl: './tasks.component.css'
})
export class TasksComponent {
  newTask = '';
  tasks: Task[] = [];

  addTask() {
    if (!this.newTask.trim()) return;
    const newId = this.tasks.length ? this.tasks[this.tasks.length - 1].id + 1 : 1;
    this.tasks.push({ id: newId, title: this.newTask, done: false });
    this.newTask = '';
  }

  deleteTask(id: number) {
    this.tasks = this.tasks.filter(task => task.id !== id);
  }
}
