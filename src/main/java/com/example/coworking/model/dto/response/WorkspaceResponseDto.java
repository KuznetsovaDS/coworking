package com.example.coworking.model.dto.response;

import com.example.coworking.model.dto.request.WorkspaceRequestDto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)
public class WorkspaceResponseDto extends WorkspaceRequestDto {
    Long id;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    List<BookingResponseDto> bookings;
}
