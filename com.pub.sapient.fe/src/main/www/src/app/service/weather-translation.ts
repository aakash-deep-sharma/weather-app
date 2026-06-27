import { inject, Injectable } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';

@Injectable({
  providedIn: 'root',
})
export class WeatherTranslation {

  #translate = inject(TranslateService);

  get translate(){
    return this.#translate;
  }

  initialiseLanguage() {
    const savedLang = localStorage.getItem('user-lang');

    if (savedLang && ['en', 'de', 'it', 'hi'].includes(savedLang)) {
      this.#translate.use(savedLang);
    } else {
      const browserLang = this.#translate.getBrowserLang();

      if (browserLang && ['en', 'de', 'it', 'hi'].includes(browserLang)) {
        this.#translate.use(browserLang);
        localStorage.setItem('user-lang', browserLang);
      } else {
        this.#translate.use('en');
      }
    }
  }


  switchLanguage(lang: string) {
    this.#translate.use(lang);
    localStorage.setItem('user-lang', lang);
  }

}
