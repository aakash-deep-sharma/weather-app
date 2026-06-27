import { ApplicationConfig, provideBrowserGlobalErrorListeners } from '@angular/core';
import { provideRouter } from '@angular/router';
import { provideHttpClient, withFetch } from '@angular/common/http';
import { provideTranslateService } from '@ngx-translate/core';
import { provideTranslateHttpLoader } from '@ngx-translate/http-loader';

import { routes } from './app.routes';

const getSavedLanguage = (): string => {
  if (typeof window !== 'undefined' && window.localStorage) {
    return localStorage.getItem('user-lang') || 'en';
  }
  return 'en'; // Fallback if running during Server-Side Rendering (SSR)
};

export const appConfig: ApplicationConfig = {
  providers: [
    provideBrowserGlobalErrorListeners(),
    provideRouter(routes),
    provideHttpClient(withFetch()),
    provideTranslateService({
      fallbackLang: 'en',
      lang: getSavedLanguage(),         
      loader: provideTranslateHttpLoader({
        prefix: 'i18n/',
        suffix: '.json'
      })
    })
  ]
};
