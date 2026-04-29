package com.hotel.controller;

import com.hotel.dto.BookingRequestDTO;
import com.hotel.model.Reservation;
import com.hotel.service.ReservationService;
import com.hotel.service.RoomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {

    @Autowired
    public ReservationService reservationService;

    @Autowired
    public RoomService roomService;

    @PostMapping("/book")
    public ResponseEntity<Reservation> bookRooms(@Valid @RequestBody BookingRequestDTO request) {
        try {
            Reservation reservation = reservationService.bookRooms(request);
            return new ResponseEntity<>(reservation, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reservation> getReservation(@PathVariable Long id) {
        try {
            Reservation reservation = reservationService.getReservation(id);
            return ResponseEntity.ok(reservation);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

}