package com.example.coworking.model.dto.response;

import com.example.coworking.model.dto.request.ReviewRequestDto;
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
public class ReviewResponseDto extends ReviewRequestDto {

    Long id;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    UserResponseDto user;
}
