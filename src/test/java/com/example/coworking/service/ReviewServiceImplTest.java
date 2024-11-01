package com.example.coworking.service;

import com.example.coworking.exceptions.CustomException;
import com.example.coworking.model.dto.request.ReviewRequestDto;
import com.example.coworking.model.dto.response.ReviewResponseDto;
import com.example.coworking.model.entity.Review;
import com.example.coworking.model.entity.User;
import com.example.coworking.model.entity.Workspace;
import com.example.coworking.model.enums.ReviewStatus;
import com.example.coworking.model.repositories.ReviewRepo;
import com.example.coworking.model.repositories.UserRepo;
import com.example.coworking.model.repositories.WorkspaceRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ReviewServiceImplTest {
    @InjectMocks
    private ReviewServiceImpl reviewService;

    @Mock
    private ReviewRepo reviewRepo;

    @Mock
    private WorkspaceRepo workspaceRepo;

    @Mock
    private UserRepo userRepo;

    @Spy
    private ObjectMapper mapper;
    @Test
    public void createReview_success() {
        ReviewRequestDto reviewRequestDto = new ReviewRequestDto();
        reviewRequestDto.setUserId(1L);
        reviewRequestDto.setWorkspaceId(1L);

        User user = new User();
        user.setId(1L);
        Workspace workspace = new Workspace();
        workspace.setId(1L);
        Review review = new Review();
        review.setId(1L);
        review.setUser(user);
        review.setWorkspace(workspace);

        when(userRepo.findById(1L)).thenReturn(Optional.of(user));
        when(workspaceRepo.findById(1L)).thenReturn(Optional.of(workspace));
        when(reviewRepo.save(any(Review.class))).thenReturn(review);

        ReviewResponseDto result = reviewService.createReview(reviewRequestDto);

        assertEquals(Optional.of(1L), Optional.of(result.getId()));
        verify(reviewRepo, times(1)).save(any(Review.class));
    }
    @Test(expected = CustomException.class)
    public void createReview_userNotFound() {
        ReviewRequestDto reviewRequestDto = new ReviewRequestDto();
        reviewRequestDto.setUserId(1L);
        reviewRequestDto.setWorkspaceId(1L);

        when(userRepo.findById(1L)).thenReturn(Optional.empty());
        when(workspaceRepo.findById(1L)).thenReturn(Optional.of(new Workspace()));

        reviewService.createReview(reviewRequestDto);
    }
    @Test(expected = CustomException.class)
    public void createReview_workspaceNotFound() {
        ReviewRequestDto reviewRequestDto = new ReviewRequestDto();
        reviewRequestDto.setUserId(1L);
        reviewRequestDto.setWorkspaceId(1L);

        User user = new User();
        user.setId(1L);

        when(userRepo.findById(1L)).thenReturn(Optional.of(user));
        when(workspaceRepo.findById(1L)).thenReturn(Optional.empty());

        reviewService.createReview(reviewRequestDto);
    }
    @Test
    public void deleteReview() {
        Long reviewId = 1L;
        Review review = new Review();
        review.setId(reviewId);
        when(reviewRepo.findById(reviewId)).thenReturn(Optional.of(review));
        reviewService.deleteReview(reviewId);
        assertEquals(ReviewStatus.DELETED, review.getReviewStatus());
        verify(reviewRepo, times(1)).save(any(Review.class));
    }
    @Test(expected = CustomException.class)
    public void deleteReview_notFound() {
        Long reviewId = 1L;
        when(reviewRepo.findById(reviewId)).thenReturn(Optional.empty());
        reviewService.deleteReview(reviewId);
    }
    @Test
    public void reviewService_success() {
        Long reviewId = 1L;
        Review review = new Review();
        review.setId(reviewId);

        when(reviewRepo.findById(reviewId)).thenReturn(Optional.of(review));

        ReviewResponseDto result = reviewService.reviewService(reviewId);
        assertEquals(Optional.of(reviewId), Optional.of(result.getId()));
    }
}