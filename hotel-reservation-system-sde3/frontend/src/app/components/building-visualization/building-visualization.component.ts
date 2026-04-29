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
    
    this.apiService.getAvailableRooms().subscribe((availableRooms: Room[]) => {
      this.apiService.getBookedRooms().subscribe((bookedRooms: Room[]) => {

       
        this.rooms = [
          ...availableRooms.map(r => ({ ...r, status: 'AVAILABLE' })),
          ...bookedRooms.map(r => ({ ...r, status: 'BOOKED' }))
        ];

      }, (error) => console.error('Error loading booked rooms:', error));
    }, (error) => console.error('Error loading available rooms:', error));
  }

  getRoomClass(room: Room): string {
    return `room ${room.status.toLowerCase()}`; 
  }

  getFloorRooms(floor: number): Room[] {
    return this.rooms.filter(r => r.floor === floor);
  }
}