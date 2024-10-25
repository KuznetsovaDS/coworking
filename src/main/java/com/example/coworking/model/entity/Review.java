package com.example.coworking.model.entity;

import com.example.coworking.model.enums.Rating;
import com.example.coworking.model.enums.ReviewStatus;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "reviews")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Review {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    Long id;

    @Column(name = "created_at")
    LocalDateTime createdAt;

    String comment;

    Rating rating;

    ReviewStatus reviewStatus;

    @ManyToOne
    @JsonBackReference(value = "user-reviews")
    User user;

    @ManyToOne
    @JsonBackReference(value = "workspace-reviews")
    Workspace workspace;

    @Column(name = "updated_at")
    LocalDateTime updatedAt;
}
