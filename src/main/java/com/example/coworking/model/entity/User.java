package com.example.coworking.model.entity;

import com.example.coworking.model.enums.UserStatus;
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
@Table(name = "users")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    Long id;

    String email;

    String password;

    @Column(name = "first_name")
    String firstName;
    @Column(name = "last_name")
    String lastName;
    @Column(name = "created_at")
    LocalDateTime createdAt;

    @Column(name = "updated_at")
    LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    UserStatus status;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    @JsonManagedReference(value = "user-reviews")
    List<Review> reviews;
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    @JsonManagedReference(value = "user-payments")
    List<Payment> payments;
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    @JsonManagedReference(value = "user-bookings")
    List<Booking> bookings;
}
