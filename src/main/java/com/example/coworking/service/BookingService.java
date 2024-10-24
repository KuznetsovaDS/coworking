package com.example.coworking.service;

import com.example.coworking.model.dto.request.BookingRequestDto;
import com.example.coworking.model.dto.response.BookingResponseDto;
import com.example.coworking.model.entity.Booking;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

public interface BookingService {

    BookingResponseDto createBooking(BookingRequestDto bookingRequestDto);

    void deleteBooking(Long id);

    Page<BookingResponseDto> getAllBooking(Integer page, Integer perPage, String sort, Sort.Direction order);

    BookingResponseDto getBooking(Long bookingId);

    Booking getBookingById(Long bookingId);

}
