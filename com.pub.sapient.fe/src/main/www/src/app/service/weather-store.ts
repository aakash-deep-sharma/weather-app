import { httpResource } from '@angular/common/http';
import { Injectable, signal, computed } from '@angular/core';
import { WeatherResponseDto } from '../models/weather.model';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class WeatherStore {
  readonly apiBase = environment.apiBase;

  #city = signal('');
  #offlineMode = signal(false);

  #weatherResource = httpResource<WeatherResponseDto>(() => {
    const currentCity = this.#city().trim();
    if (!currentCity) return undefined;

    return {
      url: `${this.apiBase}/weather`,
      params: {
        city: currentCity,
        offlineMode: this.#offlineMode()
      }
    };
  });

  // Expose state as read-only signals
  get offlineMode() { return this.#offlineMode.asReadonly(); }
  get weatherResource() { return this.#weatherResource.asReadonly(); }

  // Expose easy access to backend driving actions discovered via HATEOAS
  readonly emergencyLink = computed(() => {
    return this.#weatherResource.value()?._links?.emergency_offline_view?.href ?? null;
  });

  updateCity(newCity: string) {
    if (newCity?.trim().length > 0) {
      this.#city.set(newCity.trim());
    }
  }

  toggleOfflineMode() {
    this.#offlineMode.update(currentState => !currentState);
  }
}