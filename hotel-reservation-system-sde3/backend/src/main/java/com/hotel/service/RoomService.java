package com.hotel.service;

import com.hotel.dto.RoomDTO;
import com.hotel.model.Room;
import com.hotel.repository.RoomRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoomService {

    @Autowired
    public RoomRepository roomRepository;

    public void initializeRooms() {
        if (roomRepository.count() > 0) return;

        List<Room> rooms = new ArrayList<>();

        // Floors 1-9: 10 rooms each
        for (int floor = 1; floor <= 9; floor++) {
            for (int pos = 1; pos <= 10; pos++) {
                int roomNumber = floor * 100 + pos;
                rooms.add(Room.builder()
                        .roomNumber(roomNumber)
                        .floor(floor)
                        .positionNumber(pos)
                        .status(Room.RoomStatus.AVAILABLE)
                        .build());
            }
        }

        // Floor 10: 7 rooms
        for (int pos = 1; pos <= 7; pos++) {
            int roomNumber = 1000 + pos;
            rooms.add(Room.builder()
                    .roomNumber(roomNumber)
                    .floor(10)
                    .positionNumber(pos)
                    .status(Room.RoomStatus.AVAILABLE)
                    .build());
        }

        roomRepository.saveAll(rooms);
    }

    public List<RoomDTO> getAvailableRooms() {
        return roomRepository.findAllAvailable().stream()
                .map(RoomDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public List<RoomDTO> getBookedRooms() {
        return roomRepository.findAllBooked().stream()
                .map(RoomDTO::fromEntity)
                .collect(Collectors.toList());
    }



    public Integer getAvailableCount() {
        return roomRepository.countAvailableRooms();
    }

    public Integer getBookedCount() {
        return roomRepository.countBookedRooms();
    }

    public void markRoomsAsBooked(List<Long> roomIds) {
        List<Room> rooms = roomRepository.findAllById(roomIds);
        rooms.forEach(r -> r.setStatus(Room.RoomStatus.BOOKED));
        roomRepository.saveAll(rooms);
    }

    public void resetAllRooms() {
        List<Room> allRooms = roomRepository.findAll();
        allRooms.forEach(r -> r.setStatus(Room.RoomStatus.AVAILABLE));
        roomRepository.saveAll(allRooms);
    }

    public void addRooms(List<Room> rooms){
        for(Room r : rooms){
            this.roomRepository.save(r);
        }

    }

    @Transactional
    public void generateRandomOccupancy() {
        List<Room> allRooms = roomRepository.findAll();
        Random random = new Random();
        allRooms.forEach(room -> {
            int chance = random.nextInt(100);
            if (chance < 15) {
                room.setStatus(Room.RoomStatus.BOOKED);
            } else {
                room.setStatus(Room.RoomStatus.AVAILABLE);
            }
        });
        roomRepository.saveAll(allRooms);
    }
}