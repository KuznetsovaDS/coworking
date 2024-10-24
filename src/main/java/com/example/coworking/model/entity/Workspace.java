package com.example.coworking.model.entity;

import com.example.coworking.model.enums.UserStatus;
import com.example.coworking.model.enums.WorkspaceStatus;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "workspace")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Workspace {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    Long id;
    @Column(name = "created_at")
    LocalDateTime createdAt;
    String name;

    String description;

    Double price;

    Integer capacity;
    WorkspaceStatus status;
    @OneToMany(mappedBy = "workspace", fetch = FetchType.EAGER)
    @JsonManagedReference(value = "workspace-bookings")
    List<Booking> bookings;
    @OneToMany(mappedBy = "workspace")
    @JsonManagedReference(value = "workspace-reviews")
    List<Review> reviews;


}
