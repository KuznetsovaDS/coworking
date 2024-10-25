package com.example.coworking.model.repositories;
import com.example.coworking.model.entity.Review;
import com.example.coworking.model.enums.ReviewStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepo extends JpaRepository<Review, Long> {
   @Query("select r from Review r where r.reviewStatus <> :status")
   Page<Review> findAllNotDeleted (Pageable pageable, @Param("status") ReviewStatus status);
}
