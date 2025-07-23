import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ThemeService } from '../../services/theme.service';

@Component({
  selector: 'app-theme-toggle',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './theme-toggle.component.html',
  styleUrl: './theme-toggle.component.css'
})
export class ThemeToggleComponent {
    theme: 'light' | 'dark' = 'light';

constructor(public themeService: ThemeService) {
  if (typeof window !== 'undefined' && window.localStorage) {
    const saved = localStorage.getItem('theme') as 'light' | 'dark' | null;
    if (saved) this.setTheme(saved);
  }
}

setTheme(theme: 'light' | 'dark') {
  this.theme = theme;
  if (typeof window !== 'undefined' && window.localStorage) {
    localStorage.setItem('theme', theme);
  }
  if (typeof document !== 'undefined') {
    document.documentElement.setAttribute('data-theme', theme);
  }
}

  toggleTheme() {
    this.themeService.toggleTheme();
    this.theme = this.themeService.getTheme();
  }

}
