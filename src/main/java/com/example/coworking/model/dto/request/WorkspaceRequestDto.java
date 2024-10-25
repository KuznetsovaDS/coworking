package com.example.coworking.model.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WorkspaceRequestDto {

    String name;
    String description;
    Double price;
    Integer capacity;
}
