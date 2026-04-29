import { Component, OnInit, ViewChild } from '@angular/core';
import { ApiService } from './services/api.service';
import { BuildingVisualizationComponent } from './components/building-visualization/building-visualization.component';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  stats: any;

  @ViewChild('building')
  buildingComponent!: BuildingVisualizationComponent;

  constructor(private apiService: ApiService) {}

  ngOnInit() {
    this.initializeAndLoadStats();
  }

  initializeAndLoadStats() {
    this.apiService.initializeRooms().subscribe(() => {
      this.loadStats();
    });
  }

  loadStats() {
    this.apiService.getStats().subscribe(
      (data) => this.stats = data
    );
  }

  
 onBookingSuccess() {
  this.loadStats();
  this.buildingComponent.loadRooms();
}
}