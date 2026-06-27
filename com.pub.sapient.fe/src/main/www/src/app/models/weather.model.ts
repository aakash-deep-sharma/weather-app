export interface WeatherForecastDto {
  date: string;
  maxTemp: number;
  minTemp: number;
  alerts: string[];
}

export interface WeatherResponseDto {
  city: string;
  notes: string;
  forecasts: WeatherForecastDto[];
  dataCode: string;
}