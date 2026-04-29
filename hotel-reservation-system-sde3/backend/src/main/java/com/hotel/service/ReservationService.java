package com.hotel.service;

import com.hotel.dto.BookingRequestDTO;
import com.hotel.model.Reservation;
import com.hotel.model.Room;
import com.hotel.repository.ReservationRepository;
import com.hotel.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final RoomRepository roomRepository;

    @Transactional
    public Reservation bookRooms(BookingRequestDTO request) {
        List<Room> available = roomRepository.findAllAvailable();

        if (available.size() < request.getRoomCount()) {
            throw new RuntimeException("Not enough rooms available");
        }

        // Simple allocation: take first N available rooms
        List<Room> selected = available.stream()
                .limit(request.getRoomCount())
                .toList();

        int travelTime = calculateTravelTime(new ArrayList<>(selected));

        Reservation reservation = Reservation.builder()
                .guestId(request.getGuestId())
                .roomCount(request.getRoomCount())
                .status(Reservation.ReservationStatus.CONFIRMED)
                .totalTravelTime(travelTime)
                .bookingStrategy("OPTIMAL")
                .build();

        Reservation saved = reservationRepository.save(reservation);

        // Mark rooms as booked
        selected.forEach(r -> r.setStatus(Room.RoomStatus.BOOKED));
        roomRepository.saveAll(new ArrayList<>(selected));

        return saved;
    }

    private int calculateTravelTime(List<Room> rooms) {
        if (rooms.size() <= 1) return 0;

        // Sort rooms by floor, then by position number
        rooms.sort(Comparator.comparingInt(Room::getFloor)
                .thenComparingInt(Room::getPositionNumber));

        int totalTime = 0;

        // Calculate vertical travel (2 minutes per floor)
        int minFloor = rooms.get(0).getFloor();
        int maxFloor = rooms.get(rooms.size() - 1).getFloor();
        totalTime += Math.abs(maxFloor - minFloor) * 2;

        // Calculate horizontal travel (1 minute per adjacent room)
        // Group rooms by floor
        Map<Integer, List<Room>> roomsByFloor = new HashMap<>();
        for (Room room : rooms) {
            roomsByFloor.computeIfAbsent(room.getFloor(), k -> new ArrayList<>())
                    .add(room);
        }

        // For each floor, calculate horizontal distance
        for (List<Room> floorRooms : roomsByFloor.values()) {
            if (floorRooms.size() > 1) {
                // Sort by position number
                floorRooms.sort(Comparator.comparingInt(Room::getPositionNumber));

                // Distance from first to last room on this floor
                int minPos = floorRooms.get(0).getPositionNumber();
                int maxPos = floorRooms.get(floorRooms.size() - 1).getPositionNumber();
                totalTime += Math.abs(maxPos - minPos);
            }
        }

        return totalTime;
    }

    public Reservation getReservation(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));
    }
}