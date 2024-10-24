package com.example.coworking.controllers;

import com.example.coworking.model.dto.request.ReviewRequestDto;
import com.example.coworking.model.dto.response.ReviewResponseDto;
import com.example.coworking.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Reviews")
@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping
    @Operation(summary =  "Create review")
    public ReviewResponseDto createBooking(@RequestBody ReviewRequestDto paymentRequestDto){
        return reviewService.createReview(paymentRequestDto);
    }
    @GetMapping("/{id}")
    @Operation(summary =  "Get review")
    public ReviewResponseDto readReview(@PathVariable Long id){
        return reviewService.reviewService(id);
    }

    @DeleteMapping("/{id}")
    @Operation(summary =  "Delete review")
    public void deleteReview(@PathVariable Long id){
        reviewService.deleteReview(id);
    }
    @GetMapping("/all")
    public Page<ReviewResponseDto> getAllReview(@RequestParam(defaultValue = "1") Integer page,
                                                @RequestParam(defaultValue = "10")  Integer perPage,
                                                @RequestParam(defaultValue = "rating")  String sort,
                                                @RequestParam(defaultValue = "ASC")  Sort.Direction order){
        return reviewService.getAllReview(page, perPage, sort, order);

    }
}
