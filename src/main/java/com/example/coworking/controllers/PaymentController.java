package com.example.coworking.controllers;

import com.example.coworking.model.dto.request.PaymentRequestDto;
import com.example.coworking.model.dto.response.PaymentResponseDto;
import com.example.coworking.model.entity.Booking;
import com.example.coworking.service.BookingService;
import com.example.coworking.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Payments")
@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;
    private final BookingService bookingService;

    @PostMapping
    @Operation(summary =  "Create payment")
    public PaymentResponseDto createPayment(@PathVariable Long bookingId, @RequestBody PaymentRequestDto paymentRequestDto) {
        Booking booking = bookingService.getBookingById(bookingId);
        return paymentService.createPayment(booking, paymentRequestDto);
    }
    @GetMapping("/{id}")
    @Operation(summary =  "Get payment")
    public PaymentResponseDto readPayment(@PathVariable Long id){
        return paymentService.getPayment(id);
    }
    @PutMapping("/{id}")
    @Operation(summary =  "Update payment")
    public PaymentResponseDto updatePayment(@PathVariable Long id, @RequestBody PaymentRequestDto paymentRequestDto){
        return paymentService.update(id, paymentRequestDto);
    }
    @DeleteMapping("/{id}")
    @Operation(summary =  "Cancel payment")
    public void deletePayment(@PathVariable Long id){
        paymentService.deletePayment(id);
    }
    @GetMapping("/all")
    @Operation(summary =  "Get all payments")
    public Page<PaymentResponseDto> getAllPayment(@RequestParam(defaultValue = "1") Integer page,
                                                  @RequestParam(defaultValue = "10")  Integer perPage,
                                                  @RequestParam(defaultValue = "paymentStatus")  String sort,
                                                  @RequestParam(defaultValue = "ASC")  Sort.Direction order){
        return paymentService.getAllPayment(page, perPage, sort, order);
    }
}

