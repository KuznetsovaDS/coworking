package com.example.coworking.controllers;

import com.example.coworking.model.dto.request.BookingRequestDto;
import com.example.coworking.model.dto.response.BookingResponseDto;
import com.example.coworking.service.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Bookings")
@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    @Operation(summary =  "Create booking")
    public BookingResponseDto createBooking(@RequestBody BookingRequestDto bookingRequestDto){
        return bookingService.createBooking(bookingRequestDto);

    }
    @DeleteMapping("/{id}")
    @Operation(summary =  "Cancel booking")
    public void deleteBooking(@PathVariable Long id){
        bookingService.deleteBooking(id);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get booking")
    public BookingResponseDto getBooking(@PathVariable Long id) {
        return bookingService.getBooking(id);
    }

    @GetMapping("/all")
    @Operation(summary =  "Get all bookings")
    public Page<BookingResponseDto> getAllBooking(@RequestParam(defaultValue = "1") Integer page,
                                                  @RequestParam(defaultValue = "10")  Integer perPage,
                                                  @RequestParam(defaultValue = "bookingStatus")  String sort,
                                                  @RequestParam(defaultValue = "ASC")  Sort.Direction order){
        return bookingService.getAllBooking(page, perPage, sort, order);
    }
}

