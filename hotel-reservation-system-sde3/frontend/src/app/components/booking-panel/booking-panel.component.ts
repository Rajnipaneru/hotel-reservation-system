import { Component, EventEmitter, Output } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ApiService } from '../../services/api.service';

@Component({
  selector: 'app-booking-panel',
  templateUrl: './booking-panel.component.html',
  styleUrls: ['./booking-panel.component.css']
})
export class BookingPanelComponent {
  @Output() bookingSuccess = new EventEmitter<void>();

  bookingForm: FormGroup;
  message = '';
  messageType = '';
  loading = false;

  constructor(private fb: FormBuilder, private apiService: ApiService) {
    this.bookingForm = this.fb.group({
      guestId: ['', [Validators.required, Validators.minLength(3)]],
      roomCount: [1, [Validators.required, Validators.min(1), Validators.max(5)]]
    });
  }
onRandomOccupancy() {
  this.loading = true;
  this.message = '';

  this.apiService.randomOccupancy().subscribe(
    () => {
      this.message = '✓ Random occupancy generated!';
      this.messageType = 'success';
      this.loading = false;
      this.bookingSuccess.emit();
    },
    (error) => {
      console.error('Random occupancy error:', error);
      this.message = 'Error generating random occupancy';
      this.messageType = 'error';
      this.loading = false;
    }
  );
}
  onBook() {
    if (this.bookingForm.invalid) {
      this.showMessage('Please fill form correctly', 'error');
      return;
    }

    this.loading = true;

   
    const request = {
      guestId: this.bookingForm.value.guestId,   
      roomCount: Number(this.bookingForm.value.roomCount)
    };

    this.apiService.bookRooms(request).subscribe(
      (response: any) => {
        const travelTime = response?.totalTravelTime ?? 'N/A';

        this.message = `✓ Booking successful! Travel time: ${travelTime} min`;
        this.messageType = 'success';
        this.loading = false;

        this.bookingSuccess.emit();
        this.bookingForm.reset({ roomCount: 1 });
      },
      (error) => {
        console.error('Booking error:', error);

        this.message =
          error?.error?.message ||
          error?.message ||
          'No suitable rooms available';

        this.messageType = 'error';
        this.loading = false;
      }
    );
  }

  onReset() {
    this.loading = true;

    this.apiService.resetRooms().subscribe(
      () => {
        this.message = '✓ Rooms reset!';
        this.messageType = 'info';
        this.loading = false;

        this.bookingSuccess.emit();
      },
      (error) => {
        console.error('Reset error:', error);

        this.message = 'Error resetting rooms';
        this.messageType = 'error';
        this.loading = false;
      }
    );
  }

  private showMessage(msg: string, type: string) {
    this.message = msg;
    this.messageType = type;

    setTimeout(() => {
      this.message = '';
    }, 4000);
  }
}