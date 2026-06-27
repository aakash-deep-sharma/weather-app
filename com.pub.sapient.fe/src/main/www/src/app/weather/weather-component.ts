import { httpResource } from '@angular/common/http';
import { Component, inject, OnInit, signal } from '@angular/core';
import { WeatherResponseDto } from '../models/weather.model';
import { CommonModule } from '@angular/common';
import { WeatherStore } from '../service/weather-store';
import { TranslatePipe, TranslateService } from '@ngx-translate/core';
import { WeatherTranslation } from '../service/weather-translation';

@Component({
  selector: 'app-weather',
  imports: [CommonModule, TranslatePipe],
  templateUrl: './weather-component.html',
  styleUrl: './weather-component.css',
})
export class WeatherComponent implements OnInit {

  readonly store = inject(WeatherStore);
  readonly weatherTranslation = inject(WeatherTranslation);
 

  ngOnInit() {
    this.weatherTranslation.initialiseLanguage();
  }

  getAlertClass(alert: string): string {
    if (alert.includes('umbrella')) return 'rain-alert';
    if (alert.includes('sunscreen')) return 'heat-alert';
    if (alert.includes('windy')) return 'wind-alert';
    if (alert.includes('Storm')) return 'storm-alert';
    return 'default-alert';
  }
}
