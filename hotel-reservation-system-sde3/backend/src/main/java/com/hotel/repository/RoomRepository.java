package com.hotel.repository;

import com.hotel.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

    @Query("SELECT r FROM Room r WHERE r.status = 'AVAILABLE' ORDER BY r.floor, r.positionNumber")
    List<Room> findAllAvailable();

    @Query("SELECT COUNT(r) FROM Room r WHERE r.status = 'AVAILABLE'")
    Integer countAvailableRooms();

    @Query("SELECT COUNT(r) FROM Room r WHERE r.status = 'BOOKED'")
    Integer countBookedRooms();
}