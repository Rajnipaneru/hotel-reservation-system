import { Component, OnInit } from '@angular/core';
import { ApiService } from './services/api.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  title = 'Hotel Reservation System';
  stats: any;

  constructor(private apiService: ApiService) {}

  ngOnInit() {
    this.initializeAndLoadStats();
  }

  initializeAndLoadStats() {
    this.apiService.initializeRooms().subscribe(
      () => this.loadStats(),
      (error) => console.error('Error initializing rooms:', error)
    );
  }

  loadStats() {
    this.apiService.getStats().subscribe(
      (data) => this.stats = data,
      (error) => console.error('Error loading stats:', error)
    );
  }
}