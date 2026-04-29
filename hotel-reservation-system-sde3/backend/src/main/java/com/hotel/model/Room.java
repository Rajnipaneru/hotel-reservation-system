package com.hotel.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


import java.io.Serializable;
@Builder
@Getter
@Setter
@Entity
@Table(name = "rooms")
public class Room implements Serializable {

    // Getters and Setters
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Setter
    @Column(name = "room_number", nullable = false)
    public Integer roomNumber;

    @Column(name = "floor", nullable = false)
    public Integer floor;

    @Column(name = "position_number", nullable = false)
    public Integer positionNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    public RoomStatus status;

    @Column(name = "room_number_index", unique = true)
    public String roomNumberIndex;

    public enum RoomStatus {
        AVAILABLE,
        OCCUPIED,
        MAINTENANCE,
        BOOKED
    }

    public Room(Long id, Integer roomNumber, Integer floor, Integer positionNumber, RoomStatus status, String roomNumberIndex) {
        this.id = id;
        this.roomNumber = roomNumber;
        this.floor = floor;
        this.positionNumber = positionNumber;
        this.status = status;
        this.roomNumberIndex = roomNumberIndex;
    }

    public Room() {
    }
}