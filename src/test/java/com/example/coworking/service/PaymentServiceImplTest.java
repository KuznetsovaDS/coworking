package com.example.coworking.service;

import com.example.coworking.model.dto.request.PaymentRequestDto;
import com.example.coworking.model.dto.response.PaymentResponseDto;
import com.example.coworking.model.entity.Booking;
import com.example.coworking.model.entity.Payment;
import com.example.coworking.model.entity.User;
import com.example.coworking.model.enums.PaymentStatus;
import com.example.coworking.model.repositories.BookingRepo;
import com.example.coworking.model.repositories.PaymentRepo;
import com.example.coworking.model.repositories.UserRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.PageImpl;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class PaymentServiceImplTest {

    @InjectMocks
    private PaymentServiceImpl paymentServiceImpl;

    @Mock
    private PaymentRepo paymentRepo;
    @Mock
    private UserService userService;
    @Mock
    private BookingRepo bookingRepo;

    @Spy
    private ObjectMapper mapper;
    @Test
    public void createPayment() {
        PaymentRequestDto paymentRequestDto = new PaymentRequestDto();
        paymentRequestDto.setUserId(1L);
        paymentRequestDto.setPaymentTime(LocalDateTime.now());

        Booking savedBooking = new Booking();
        User user = new User();

        when(userService.getUserEntity(1L)).thenReturn(user);

        PaymentResponseDto paymentResponseDto = new PaymentResponseDto();
        Payment payment = new Payment();
        payment.setUser(user);
        payment.setBooking(savedBooking);

        when(paymentRepo.save(any(Payment.class))).thenReturn(payment);
        when(mapper.convertValue(payment, PaymentResponseDto.class)).thenReturn(paymentResponseDto);

        PaymentResponseDto result = paymentServiceImpl.createPayment(savedBooking, paymentRequestDto);

        assertNotNull(result);
        verify(paymentRepo).save(any(Payment.class));
        verify(userService).getUserEntity(1L);
    }
    @Test
    public void getPayment() {
        Long paymentId = 1L;
        Payment payment = new Payment();
        PaymentResponseDto paymentResponseDto = new PaymentResponseDto();

        when(paymentRepo.findById(paymentId)).thenReturn(Optional.of(payment));
        when(mapper.convertValue(payment, PaymentResponseDto.class)).thenReturn(paymentResponseDto);
        PaymentResponseDto result = paymentServiceImpl.getPayment(paymentId);
        assertEquals(paymentResponseDto, result);
    }

    @Test
    public void update() {
        Long paymentId = 1L;
        PaymentRequestDto paymentRequestDto = new PaymentRequestDto();
        paymentRequestDto.setUserId(1L);
        Payment payment = new Payment();
        User user = new User();
        Booking booking = new Booking();
        PaymentResponseDto paymentResponseDto = new PaymentResponseDto();

        when(paymentRepo.findById(paymentId)).thenReturn(Optional.of(payment));
        when(userService.getUserEntity(1L)).thenReturn(user);
        payment.setBooking(booking);
        when(paymentRepo.save(payment)).thenReturn(payment);
        when(mapper.convertValue(payment, PaymentResponseDto.class)).thenReturn(paymentResponseDto);

        PaymentResponseDto result = paymentServiceImpl.update(paymentId, paymentRequestDto);

        assertEquals(paymentResponseDto, result);
        assertEquals(PaymentStatus.PAID, payment.getPaymentStatus());
        verify(bookingRepo).save(booking);
        verify(paymentRepo).save(payment);
    }

    @Test
    public void deletePayment() {
        Long paymentId = 1L;
        Payment payment = new Payment();
        when(paymentRepo.findById(paymentId)).thenReturn(Optional.of(payment));
        paymentServiceImpl.deletePayment(paymentId);
        assertEquals(PaymentStatus.CANCELLED, payment.getPaymentStatus());
        verify(paymentRepo).save(payment);
    }
}