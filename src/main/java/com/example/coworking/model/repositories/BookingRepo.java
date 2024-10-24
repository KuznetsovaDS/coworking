package com.example.coworking.model.repositories;

import com.example.coworking.model.dto.response.BookingResponseDto;
import com.example.coworking.model.entity.Booking;
import com.example.coworking.model.enums.BookingStatus;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepo extends JpaRepository<Booking, Long> {
    @Query("select b from Booking b where b.bookingStatus != :status")
    Page<Booking> findAllNotCancelled(Pageable request, @Param("status") BookingStatus status);
    @Query("select b from Booking b where b.workspace.id = :workspaceId and b.bookingStatus = :status and " +
            "(b.startTime < :endTime and b.endTime > :startTime)")
    List<Booking> findOverlapBookings(@Param("workspaceId") Long workspaceId,
                                      @Param("status") BookingStatus status,
                                      @Param("startTime") LocalDateTime startTime,
                                      @Param("endTime") LocalDateTime endTime);
    @Query("select b from Booking b where b.endTime < :currentTime and b.bookingStatus = :status")
    List<Booking> findPastBookings(@Param("status") BookingStatus status, @Param("currentTime") LocalDateTime currentTime);

}
