package com.example.coworking.model.entity;

import com.example.coworking.model.enums.PaymentStatus;
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
@Table(name = "payments")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Payment {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    Long id;

    @Column(name = "created_at")
    LocalDateTime createdAt;

    @ManyToOne
    @JsonBackReference(value = "user-payments")
    User user;

    @Column(name = "payment_time")
    LocalDateTime paymentTime;

    PaymentStatus paymentStatus;

    @Column(name = "updated_at")
    LocalDateTime updatedAt;

    @OneToOne
    @JoinColumn(name = "booking_id")
    @JsonBackReference
    Booking booking;
}
