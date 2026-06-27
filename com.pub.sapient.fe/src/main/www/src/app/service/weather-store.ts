import { httpResource } from '@angular/common/http';
import { Injectable, signal } from '@angular/core';
import { WeatherResponseDto } from '../models/weather.model';
import { environment } from '../../environments/environment';


@Injectable({
  providedIn: 'root',
})
export class WeatherStore {

  //readonly apiBase = 'http://localhost:8080/api';
  readonly apiBase = environment.apiBase;

  // 1. Core Reactive State Drivers (Writable Signals)
  #city = signal('');
  #offlineMode = signal(false);

  #weatherResource = httpResource<WeatherResponseDto>(() => {
    const currentCity = this.#city().trim();

    if (!currentCity) {
      return undefined;
    }

    return {
      url: `${this.apiBase}/weather`,
      params: {
        city: currentCity, // encodeURIComponent is handled automatically by Angular's params handler
        offlineMode: this.#offlineMode()
      }
    };
  });
 

  get offlineMode() {
    return this.#offlineMode.asReadonly();
  }

  get weatherResource(){
    return this.#weatherResource.asReadonly();
  }


  // Action Methods updating state mutations cleanly
  updateCity(newCity: string) {
    if (newCity && newCity.trim().length > 0) {
      this.#city.set(newCity.trim());
    }
  }

  toggleOfflineMode() {
    this.#offlineMode.update(currentState => !currentState);
  }


}
