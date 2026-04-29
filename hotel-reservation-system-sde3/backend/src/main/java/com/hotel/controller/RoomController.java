package com.hotel.controller;

import com.hotel.dto.RoomDTO;
import com.hotel.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

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
}