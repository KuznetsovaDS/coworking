package com.example.coworking.service;

import com.example.coworking.model.dto.request.ReviewRequestDto;
import com.example.coworking.model.dto.response.ReviewResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

public interface ReviewService {

    ReviewResponseDto createReview(ReviewRequestDto paymentRequestDto);

    ReviewResponseDto reviewService(Long id);

    void deleteReview(Long id);

    Page<ReviewResponseDto> getAllReview(Integer page, Integer perPage, String sort, Sort.Direction order);
}
