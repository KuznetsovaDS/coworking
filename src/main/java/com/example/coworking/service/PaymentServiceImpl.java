package com.example.coworking.service;

import com.example.coworking.exceptions.CustomException;
import com.example.coworking.model.dto.request.PaymentRequestDto;
import com.example.coworking.model.dto.response.PaymentResponseDto;
import com.example.coworking.model.entity.Booking;
import com.example.coworking.model.entity.Payment;
import com.example.coworking.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.example.coworking.model.enums.PaymentStatus;
import com.example.coworking.model.repositories.BookingRepo;
import com.example.coworking.model.repositories.PaymentRepo;
import com.example.coworking.utils.PaginationUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService{
    private final ObjectMapper mapper;
    private final PaymentRepo paymentRepo;
    private final UserService userService;
    private final BookingRepo bookingRepo;

    @Override
    public PaymentResponseDto createPayment(Booking savedBooking, PaymentRequestDto paymentRequestDto) {
        Payment payment = new Payment();
        payment.setCreatedAt(LocalDateTime.now());
        payment.setPaymentStatus(PaymentStatus.UNPAID);
        payment.setPaymentTime(paymentRequestDto.getPaymentTime());
        User user = userService.getUserEntity(paymentRequestDto.getUserId());
        payment.setUser(user);
        payment.setBooking(savedBooking);
        Payment save = paymentRepo.save(payment);
        return mapper.convertValue(save, PaymentResponseDto.class);
    }

    @Override
    public PaymentResponseDto getPayment(Long id) {
        Payment payment = getPaymentById(id);
        return mapper.convertValue(payment, PaymentResponseDto.class);
    }

    @Override
    public PaymentResponseDto update(Long id, PaymentRequestDto paymentRequestDto) {
        Payment payment = getPaymentById(id);
        payment.setPaymentStatus(PaymentStatus.PAID);
        payment.setUser(userService.getUserEntity(paymentRequestDto.getUserId()));
        Booking booking = payment.getBooking();
        booking.setPayment(payment);
        bookingRepo.save(booking);
        Payment save = paymentRepo.save(payment);
        return mapper.convertValue(save, PaymentResponseDto.class);
    }
    private Payment getPaymentById(Long id) {
        return paymentRepo.findById(id)
                .orElseThrow(() -> new CustomException("Payment is not found", HttpStatus.NOT_FOUND));
    }
    @Override
    public void deletePayment(Long id) {
        Payment payment = getPaymentById(id);
        payment.setPaymentStatus(PaymentStatus.CANCELLED);
        payment.setUpdatedAt(LocalDateTime.now());
        paymentRepo.save(payment);
    }

    @Override
    public Page<PaymentResponseDto> getAllPayment(Integer page, Integer perPage, String sort, Sort.Direction order) {
        Pageable pageRequest = PaginationUtil.getPageRequest(page, perPage, sort, order);
        Page<Payment> pageList = paymentRepo.findAllNotCancelled(pageRequest, PaymentStatus. CANCELLED);
        List<PaymentResponseDto> responses = pageList.getContent().stream()
                .map(p -> mapper.convertValue(p, PaymentResponseDto.class))
                .collect(Collectors.toList());

        return new PageImpl<>(responses);
    }
}
