package com.example.coworking.model.entity;

import com.example.coworking.model.enums.BookingStatus;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "bookings")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Booking {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    Long id;

    @Column(name = "created_at")
    LocalDateTime createdAt;

    @Column(name = "start_time")
    LocalDateTime startTime;

    @Column(name = "end_time")
    LocalDateTime endTime;

    @Column(name = "total_cost")
    Double totalCost;

    @Column(name = "updated_at")
    LocalDateTime updatedAt;

    BookingStatus bookingStatus;

    @ManyToOne
    @JsonBackReference(value = "user-bookings")
    User user;

    @ManyToOne
    @JsonBackReference(value = "workspace-bookings")
    Workspace workspace;

    @OneToOne(mappedBy = "booking", fetch = FetchType.EAGER)
    @JsonManagedReference
    Payment payment;


}
