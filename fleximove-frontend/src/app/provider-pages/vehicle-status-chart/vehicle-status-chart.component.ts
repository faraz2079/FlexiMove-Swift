import { Component, Input, SimpleChanges } from '@angular/core';
import { ChartConfiguration, ChartType } from 'chart.js';
import { ProviderVehicle } from '../provider-homepage/provider-homepage.component';

@Component({
  selector: 'app-vehicle-status-chart',
  templateUrl: './vehicle-status-chart.component.html',
  styleUrls: ['./vehicle-status-chart.component.css']
})
export class VehicleStatusChartComponent {
  @Input() vehicles: ProviderVehicle[] = [];
  pieChartLabels: string[] = ['AVAILABLE', 'BOOKED', 'IN_USE', 'MAINTENANCE', 'RETIRED'];
  pieChartData: number[] = [];
  pieChartType: ChartType = 'pie';
  pieChartOptions: ChartConfiguration['options'] = {
    responsive: true,
    plugins: {
      legend: {
        position: 'bottom'
      }
    }
  };

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['vehicles'] && this.vehicles.length > 0) {
      this.updateChartData();
    }
  }

  private updateChartData(): void {
    const statusCountMap: Record<string, number> = {};

    for (const vehicle of this.vehicles) {
      const status = vehicle.status;
      statusCountMap[status] = (statusCountMap[status] || 0) + 1;
    }

    this.pieChartLabels = Object.keys(statusCountMap);
    this.pieChartData = Object.values(statusCountMap);
  }


}
