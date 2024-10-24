package com.example.coworking.model.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import java.time.LocalDateTime;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingRequestDto {
    LocalDateTime startTime;
    Long userId;
    Long workspaceId;
    LocalDateTime endTime;
}
