import { Component, OnInit } from '@angular/core';
import { ApiService } from '../../services/api.service';
import { Room } from '../../models/room.model';

@Component({
  selector: 'app-building-visualization',
  templateUrl: './building-visualization.component.html',
  styleUrls: ['./building-visualization.component.css']
})
export class BuildingVisualizationComponent implements OnInit {
 rooms: Room[] = [];

  constructor(private apiService: ApiService) {}

  ngOnInit() {
    this.loadRooms();
  }

  

loadRooms() {
  this.apiService.getAvailableRooms().subscribe(
    (data: Room[]) => {
      this.rooms = data;
    },
    (error) => console.error('Error loading rooms:', error)
  );
}

  getRoomClass(room: any): string {
    return `room ${room.status.toLowerCase()}`;
  }

  getFloorRooms(floor: number): any[] {
    return this.rooms.filter(r => r.floor === floor);
  }
}