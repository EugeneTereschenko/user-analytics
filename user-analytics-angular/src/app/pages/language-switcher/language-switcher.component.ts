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

  constructor(private translateService: TranslateService) {
    translateService.addLangs(['en', 'uk']);
    translateService.setDefaultLang('en');
  }

  switchLanguage(language: string) {
    console.log(`Switching language to: ${language}`);
    this.translateService.use(language);
  }

}
