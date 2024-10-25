package com.example.coworking.model.dto.response;

import com.example.coworking.model.dto.request.BookingRequestDto;
import com.example.coworking.model.enums.BookingStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)
public class BookingResponseDto extends BookingRequestDto {

    Long id;

    Double totalCost;

    BookingStatus bookingStatus;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    Long userId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    Long workspaceId;
}
