package com.hotel.controller;

import com.hotel.dto.RoomDTO;
import com.hotel.model.Room;
import com.hotel.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
public class RoomController {

    @Autowired
    public RoomService roomService;

    @PostMapping("/init")
    public ResponseEntity<String> initializeRooms() {
        roomService.initializeRooms();
        return ResponseEntity.ok("Rooms initialized successfully");
    }

    @GetMapping("/available")
    public ResponseEntity<List<RoomDTO>> getAvailableRooms() {
        return ResponseEntity.ok(roomService.getAvailableRooms());
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalRooms", 97);
        stats.put("available", roomService.getAvailableCount());
        stats.put("booked", roomService.getBookedCount());
        return ResponseEntity.ok(stats);
    }

    @PostMapping("/reset")
    public ResponseEntity<String> resetAllRooms() {
        roomService.resetAllRooms();
        return ResponseEntity.ok("All rooms reset to available");
    }

    @PostMapping("/add-rooms")
    public void addRooms(@RequestBody List<Room> rooms){
        this.roomService.addRooms(rooms);
    }


    @GetMapping("/booked")
    public ResponseEntity<List<RoomDTO>> getBookedRooms() {
        return ResponseEntity.ok(roomService.getBookedRooms());
    }
}