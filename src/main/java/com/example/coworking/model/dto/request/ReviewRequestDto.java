package com.example.coworking.model.dto.request;

import com.example.coworking.model.enums.Rating;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReviewRequestDto {

    String comment;
    Rating rating;
    Long workspaceId;
    Long userId;
}
