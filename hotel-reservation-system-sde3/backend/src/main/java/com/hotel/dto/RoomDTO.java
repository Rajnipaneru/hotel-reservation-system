package com.hotel.dto;

import com.hotel.model.Room;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomDTO {
    private Long id;
    private Integer roomNumber;
    private Integer floor;
    private Integer positionNumber;
    private String status;

    public static RoomDTO fromEntity(Room room) {
        return RoomDTO.builder()
                .id(room.getId())
                .roomNumber(Integer.valueOf(room.getRoomNumber()))
                .floor(room.getFloor())
                .positionNumber(Integer.valueOf(room.getPositionNumber()))
                .status(room.getStatus().toString())
                .build();
    }
}