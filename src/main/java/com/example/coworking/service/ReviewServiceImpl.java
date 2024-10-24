package com.example.coworking.service;

import com.example.coworking.exceptions.CustomException;
import com.example.coworking.model.dto.request.ReviewRequestDto;
import com.example.coworking.model.dto.response.ReviewResponseDto;
import com.example.coworking.model.entity.Review;
import com.example.coworking.model.enums.ReviewStatus;
import com.example.coworking.model.repositories.ReviewRepo;
import com.example.coworking.utils.PaginationUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private final ObjectMapper mapper;
    private final ReviewRepo reviewRepo;

    @Override
    public ReviewResponseDto createReview(ReviewRequestDto reviewRequestDto) {
        Review review = mapper.convertValue(reviewRequestDto, Review.class);
        review.setCreatedAt(LocalDateTime.now());
        Review save = reviewRepo.save(review);
        return mapper.convertValue(save, ReviewResponseDto.class);
    }

    @Override
    public ReviewResponseDto reviewService(Long id) {
        Review review = getReviewById(id);
        return mapper.convertValue(review, ReviewResponseDto.class);
    }

    private Review getReviewById(Long id) {
        return reviewRepo.findById(id)
                .orElseThrow(()->new CustomException("No review!", HttpStatus.NOT_FOUND));
    }
    @Override
    public void deleteReview(Long id) {
        Review review = getReviewById(id);
        review.setReviewStatus(ReviewStatus.DELETED);
        review.setUpdatedAt(LocalDateTime.now());
        reviewRepo.save(review);
    }

    @Override
    public Page<ReviewResponseDto> getAllReview(Integer page, Integer perPage, String sort, Sort.Direction order) {
        Pageable pageRequest = PaginationUtil.getPageRequest(page, perPage, sort, order);

        Page<Review> pageList = reviewRepo.findAllNotDeleted(pageRequest, ReviewStatus.DELETED);
        List<ReviewResponseDto> responses = pageList.getContent().stream()
                .map(p -> mapper.convertValue(p, ReviewResponseDto.class))
                .collect(Collectors.toList());

        return new PageImpl<>(responses);
    }
}
