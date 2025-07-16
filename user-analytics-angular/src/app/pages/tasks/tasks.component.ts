import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { TaskService } from '../../task.service';
import { RouterModule } from '@angular/router';

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

  constructor(private taskService: TaskService) { }


  ngOnInit() {
    console.log('TasksComponent initialized');
    this.refresh();
  }

  addTask() {
    if (!this.newTask.trim()) return;
    const newId = this.tasks.length ? this.tasks[this.tasks.length - 1].id + 1 : 1;
    this.tasks.push({ id: newId, title: this.newTask, done: false });
    //this.newTask = '';
    this.taskService.createTask({ id: newId, title: this.newTask, done: false }).subscribe({
      next: (task: Task) => {
        console.log('Task created successfully', task);
      },
      error: (err: any) => {
        console.error('Failed to create task', err);
      }
    });
  }

  deleteTask(id: number) {
    this.tasks = this.tasks.filter(task => task.id !== id);
  }

  refresh() {
    this.taskService.getTasks().subscribe({ 
      next: (tasks: Task[]) => {
        this.tasks = tasks;
      },
      error: (err: any) => {
        console.error('Failed to fetch tasks', err);
      }
    });
  }
}
