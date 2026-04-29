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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final RoomRepository roomRepository;

    @Transactional
    public Reservation bookRooms(BookingRequestDTO request) {
        int roomCount = request.getRoomCount();

        // Requirement: A single guest can book up to 5 rooms at a time
        if (roomCount < 1 || roomCount > 5) {
            throw new RuntimeException("Room count must be between 1 and 5");
        }

        List<Room> available = roomRepository.findAllAvailable();

        if (available.size() < roomCount) {
            throw new RuntimeException("Not enough rooms available");
        }

        List<Room> selected = selectOptimalRooms(available, roomCount);

        int travelTime = calculateTravelTime(new ArrayList<>(selected));

        Reservation reservation = Reservation.builder()
                .guestId(request.getGuestId())
                .roomCount(roomCount)
                .status(Reservation.ReservationStatus.CONFIRMED)
                .totalTravelTime(travelTime)
                .bookingStrategy("OPTIMAL")
                .build();

        Reservation saved = reservationRepository.save(reservation);

        selected.forEach(r -> r.setStatus(Room.RoomStatus.BOOKED));
        roomRepository.saveAll(selected);

        return saved;
    }

    /**
     * Priority 1: book rooms on the same floor first.
     *             Among all floors that have enough rooms,
     *             pick the one whose best window has minimum travel time.
     *
     * Priority 2/3: no single floor has enough rooms —
     *               evaluate ALL combinations across floors and
     *               return the one with minimum combined travel time.
     */
    private List<Room> selectOptimalRooms(List<Room> available, int roomCount) {

        Map<Integer, List<Room>> byFloor = available.stream()
                .collect(Collectors.groupingBy(Room::getFloor));

        // Sort floor keys for deterministic iteration
        List<Integer> sortedFloorKeys = new ArrayList<>(byFloor.keySet());
        Collections.sort(sortedFloorKeys);

        List<Room> bestSameFloor = null;
        int bestSameFloorTime = Integer.MAX_VALUE;

        // Priority 1: try every floor that has enough available rooms
        for (Integer floorNum : sortedFloorKeys) {
            List<Room> floorRooms = byFloor.get(floorNum);
            if (floorRooms.size() < roomCount) continue;

            floorRooms.sort(Comparator.comparingInt(Room::getPositionNumber));

            List<Room> best = findBestContiguousWindow(floorRooms, roomCount);
            int time = calculateTravelTime(new ArrayList<>(best));

            if (time < bestSameFloorTime) {
                bestSameFloorTime = time;
                bestSameFloor = best;
            }
        }

        if (bestSameFloor != null) {
            return bestSameFloor;
        }

        // Priority 2 & 3: span across floors
        return findBestCrossFloorCombination(available, roomCount);
    }

    /**
     * On a single floor, find the window of k rooms (sorted by position)
     * whose span (first to last) is minimum — giving the lowest travel time.
     * When two windows tie, the one starting at the lower position wins
     * because we iterate left to right and use strict less-than.
     */
    private List<Room> findBestContiguousWindow(List<Room> sortedFloorRooms, int k) {
        List<Room> best = null;
        int bestSpan = Integer.MAX_VALUE;

        for (int i = 0; i <= sortedFloorRooms.size() - k; i++) {
            List<Room> window = sortedFloorRooms.subList(i, i + k);
            int span = window.get(k - 1).getPositionNumber()
                    - window.get(0).getPositionNumber();
            if (span < bestSpan) {
                bestSpan = span;
                best = new ArrayList<>(window);
            }
        }
        return best;
    }

    /**
     * Evaluate every possible combination of roomCount rooms
     * from the available list and return the combination with
     * the minimum calculateTravelTime.
     * Requirement: "minimize the combined vertical and horizontal travel time."
     */
    private List<Room> findBestCrossFloorCombination(List<Room> available, int roomCount) {
        List<Room> bestSelection = null;
        int bestTime = Integer.MAX_VALUE;

        List<List<Room>> allCombinations = new ArrayList<>();
        combine(available, roomCount, 0, new ArrayList<>(), allCombinations);

        for (List<Room> candidate : allCombinations) {
            int time = calculateTravelTime(new ArrayList<>(candidate));
            if (time < bestTime) {
                bestTime = time;
                bestSelection = new ArrayList<>(candidate);
            }
        }

        return bestSelection;
    }

    /**
     * Recursive combination generator.
     * Produces all C(n, k) subsets of rooms of size k.
     */
    private void combine(List<Room> rooms, int k, int start,
                         List<Room> current, List<List<Room>> result) {
        if (current.size() == k) {
            result.add(new ArrayList<>(current));
            return;
        }
        for (int i = start; i < rooms.size(); i++) {
            current.add(rooms.get(i));
            combine(rooms, k, i + 1, current, result);
            current.remove(current.size() - 1);
        }
    }

    /**
     * Travel time calculation — strictly from requirement:
     *
     * Vertical:   |maxFloor - minFloor| x 2 minutes
     * Horizontal: (maxPosition - minPosition) per floor
     *             — "1 minute per adjacent room"
     *             — "between the first and last room in the booking"
     */
    private int calculateTravelTime(List<Room> rooms) {
        if (rooms.size() <= 1) return 0;

        Map<Integer, List<Room>> byFloor = rooms.stream()
                .collect(Collectors.groupingBy(Room::getFloor));

        int minFloor = byFloor.keySet().stream()
                .mapToInt(Integer::intValue).min().getAsInt();
        int maxFloor = byFloor.keySet().stream()
                .mapToInt(Integer::intValue).max().getAsInt();

        int vertical = Math.abs(maxFloor - minFloor) * 2;

        int horizontal = 0;
        for (List<Room> floorRooms : byFloor.values()) {
            int maxPos = floorRooms.stream()
                    .mapToInt(Room::getPositionNumber).max().getAsInt();
            int minPos = floorRooms.stream()
                    .mapToInt(Room::getPositionNumber).min().getAsInt();
            horizontal += (maxPos - minPos);
        }

        return vertical + horizontal;
    }

    public Reservation getReservation(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));
    }
}