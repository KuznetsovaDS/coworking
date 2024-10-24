package com.example.coworking.model.dto.request;

import com.example.coworking.model.enums.Rating;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReviewRequestDto {
    String comment;
    Rating rating;
    Long workspaceId;
    Long userId;
}
