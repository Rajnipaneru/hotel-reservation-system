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
    private Long id;

    @Setter
    @Column(name = "room_number", nullable = false)
    private Integer roomNumber;

    @Column(name = "floor", nullable = false)
    private Integer floor;

    @Column(name = "position_number", nullable = false)
    private Integer positionNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private RoomStatus status;

    @Column(name = "room_number_index", unique = true)
    private String roomNumberIndex;

    public enum RoomStatus {
        AVAILABLE,
        OCCUPIED,
        MAINTENANCE,
        BOOKED
    }


}