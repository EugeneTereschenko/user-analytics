import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TranslateService } from '@ngx-translate/core';

@Component({
  selector: 'app-language-switcher',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './language-switcher.component.html',
  styleUrl: './language-switcher.component.css'
})
export class LanguageSwitcherComponent {
  currentLanguage: string = 'en';

  constructor(private translateService: TranslateService) {
    translateService.addLangs(['en', 'uk', 'es', 'fr', 'de']);
    translateService.setDefaultLang('en');
    this.currentLanguage = translateService.currentLang || 'en';
  }

  switchLanguage(language: string) {
    console.log(`Switching language to: ${language}`);
    this.translateService.use(language);
    this.currentLanguage = language;
  }

  getLanguageName(code: string): string {
    const languages: { [key: string]: string } = {
      'en': 'English',
      'uk': 'Українська',
      'es': 'Español',
      'fr': 'Français',
      'de': 'Deutsch'
    };
    return languages[code] || 'English';
  }
}
