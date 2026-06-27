import { Component, signal } from '@angular/core';

import {  WeatherComponent } from './weather/weather-component';

@Component({
  selector: 'app-root',
  imports: [WeatherComponent],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  protected readonly title = signal('weather-app');
}
