export interface Link {
  href: string;
}

export interface HateoasLinks {
  self: Link;
  emergency_offline_view?: Link; 
}

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
  // HATEOAS links wrapper
  _links?: HateoasLinks; 
}