package com.example.coworking.service;

import com.example.coworking.model.dto.request.PaymentRequestDto;
import com.example.coworking.model.dto.response.PaymentResponseDto;
import com.example.coworking.model.entity.Booking;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

public interface PaymentService {

    PaymentResponseDto createPayment(Booking savedBooking, PaymentRequestDto paymentRequestDto);
    PaymentResponseDto getPayment(Long id);

    PaymentResponseDto update(Long id, PaymentRequestDto paymentRequestDto);

    void deletePayment(Long id);

    Page<PaymentResponseDto> getAllPayment(Integer page, Integer perPage, String sort, Sort.Direction order);
}
