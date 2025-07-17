import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Task {
  id: number;
  title: string;
  done: boolean;
}

@Injectable({
  providedIn: 'root'
})
export class TaskService {
  private tokenKey = 'auth_token';
  private readonly baseUrl = 'http://localhost:8080/api/tasks';

  constructor(private http: HttpClient) {}

  private getAuthHeaders(): { [header: string]: string } {
    const token = localStorage.getItem(this.tokenKey);
    return token ? { Authorization: `Bearer ${token}` } : {};
  }

  createTask(task: Task): Observable<Task> {
    return this.http.post<Task>(this.baseUrl, task, {
      headers: this.getAuthHeaders()
      });
  }

  getTasks(): Observable<Task[]> {
    return this.http.get<Task[]>(this.baseUrl, {
      headers: this.getAuthHeaders()
    });
  }

}
