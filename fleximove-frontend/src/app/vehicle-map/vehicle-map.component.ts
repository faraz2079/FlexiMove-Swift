import { AfterViewInit, Component, ElementRef, Input, OnChanges, SimpleChanges, ViewChild } from '@angular/core';
import { NearestAvailableVehicleResponse } from '../customer-pages/customer-homepage/customer-homepage.component';
import * as L from 'leaflet';

// Leaflet Marker-Fix (Pfad zu Icons setzen)
delete (L.Icon.Default.prototype as any)._getIconUrl;

L.Icon.Default.mergeOptions({
  iconRetinaUrl: 'assets/leaflet/marker-icon-2x.png',
  iconUrl: 'assets/leaflet/marker-icon.png',
  shadowUrl: 'assets/leaflet/marker-shadow.png',
});


@Component({
  selector: 'app-vehicle-map',
  templateUrl: './vehicle-map.component.html',
  styleUrls: ['./vehicle-map.component.css']
})
export class VehicleMapComponent implements AfterViewInit{
  @Input() vehicles: NearestAvailableVehicleResponse[] = [];
  private map!: L.Map;
  @ViewChild('mapContainer') mapContainer!: ElementRef;

  ngAfterViewInit(): void {
    setTimeout(() => {
      if (this.vehicles.length > 0 && this.mapContainer) {
        this.initMap();
        this.addMarkers();
      }
    }, 0);
  }

  private initMap(): void {
    this.map = L.map('map').setView([51.5136, 7.4653], 13);

    L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png', {
      maxZoom: 19,
      attribution: '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>'
    }).addTo(this.map);
  }

  private addMarkers(): void {
    if (!this.map) return;

    this.vehicles.forEach(vehicle => {
      L.marker([vehicle.latitude, vehicle.longitude])
        .addTo(this.map)
        .bindPopup(`<b>${vehicle.vehicleName}</b><br>${vehicle.address}`).openPopup();
    });
  }
}
