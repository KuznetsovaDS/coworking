package com.example.coworking.model.repositories;

import com.example.coworking.model.entity.Workspace;
import com.example.coworking.model.enums.BookingStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;

@Repository
public interface WorkspaceRepo extends JpaRepository<Workspace, Long> {
    @Query("select w from Workspace w where not exists (" +
            "select b from Booking b " +
            "where b.workspace = w " +
            "and b.bookingStatus = :status " +
            "and (b.startTime < :endTime and b.endTime > :startTime))")
    Page<Workspace> findAllAvailable(Pageable pageable, @Param("status") BookingStatus status,
                                     @Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);
}
