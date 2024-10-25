package com.example.coworking.model.repositories;

import com.example.coworking.model.entity.Payment;
import com.example.coworking.model.enums.PaymentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepo extends JpaRepository<Payment, Long> {
    @Query("select p from Payment p where p.paymentStatus <> :status")
    Page<Payment> findAllNotCancelled (Pageable pageable, @Param("status") PaymentStatus status);
}
