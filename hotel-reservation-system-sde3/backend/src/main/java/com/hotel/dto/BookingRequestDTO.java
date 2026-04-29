package com.hotel.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingRequestDTO {
    @NotBlank(message = "Guest ID required")
    @Size(min = 3, message = "Guest ID must be at least 3 characters")
    private String guestId;

    @NotNull
    @Min(value = 1, message = "Must book at least 1 room")
    @Max(value = 5, message = "Cannot book more than 5 rooms")
    private Integer roomCount;
}