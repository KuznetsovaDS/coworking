package com.example.coworking.service;

import com.example.coworking.exceptions.CustomException;
import com.example.coworking.model.dto.request.BookingRequestDto;
import com.example.coworking.model.dto.request.PaymentRequestDto;
import com.example.coworking.model.dto.response.BookingResponseDto;
import com.example.coworking.model.entity.Booking;
import com.example.coworking.model.entity.Payment;
import com.example.coworking.model.entity.User;
import com.example.coworking.model.entity.Workspace;
import com.example.coworking.model.enums.BookingStatus;
import com.example.coworking.model.enums.PaymentStatus;
import com.example.coworking.model.repositories.BookingRepo;
import com.example.coworking.model.repositories.PaymentRepo;
import com.example.coworking.model.repositories.UserRepo;
import com.example.coworking.model.repositories.WorkspaceRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class BookingServiceImplTest {
    @InjectMocks
    private BookingServiceImpl bookingServiceImpl;

    @Mock
    private BookingRepo bookingRepo;

    @Mock
    private WorkspaceRepo workspaceRepo;

    @Mock
    private PaymentRepo paymentRepo;

    @Spy
    private ObjectMapper mapper;

    @Test(expected = CustomException.class)
    public void createBooking_invalidEndTime() {
        BookingRequestDto bookingRequestDto = new BookingRequestDto();
        bookingRequestDto.setWorkspaceId(1L);
        bookingRequestDto.setStartTime(LocalDateTime.of(2024, 10, 10, 12, 0));
        bookingRequestDto.setEndTime(LocalDateTime.of(2024, 10, 10, 10, 0));
        bookingRequestDto.setUserId(1L);
        bookingServiceImpl.createBooking(bookingRequestDto);
    }
    @Test(expected = CustomException.class)
    public void createBooking_workspaceUnavailable() {
        Workspace workspace = new Workspace();
        workspace.setId(1L);
        workspace.setPrice(1000.0);

        BookingRequestDto bookingRequest = new BookingRequestDto();
        bookingRequest.setWorkspaceId(1L);
        bookingRequest.setStartTime(LocalDateTime.of(2024, 10, 10, 12, 0));
        bookingRequest.setEndTime(LocalDateTime.of(2024, 10, 10, 14, 0));
        bookingRequest.setUserId(1L);

        when(workspaceRepo.findById(1L)).thenReturn(Optional.of(workspace));
        when(bookingRepo.findOverlapBookings(1L,
                BookingStatus.CONFIRMED,
                LocalDateTime.of(2024, 10, 10, 12, 0),
                LocalDateTime.of(2024, 10, 10, 14, 0)))
                .thenReturn(Collections.singletonList(new Booking()));
        bookingServiceImpl.createBooking(bookingRequest);
    }
    @Test
    public void updateExpiredBookings() {
        Booking booking = new Booking();
        booking.setId(1L);
        booking.setBookingStatus(BookingStatus.CONFIRMED);
        booking.setStartTime(LocalDateTime.now().minusHours(2));

        when(bookingRepo.findPastBookings(any(BookingStatus.class), any(LocalDateTime.class)))
                .thenReturn(Collections.singletonList(booking));

        bookingServiceImpl.updateExpiredBookings();

        assertEquals(BookingStatus.COMPLETED, booking.getBookingStatus());
        verify(bookingRepo, times(1)).save(booking);
    }

    @Test
    public void getBookingById() {
        Booking booking = new Booking();
        booking.setId(1L);
        booking.setBookingStatus(BookingStatus.CONFIRMED);
        when(bookingRepo.findById(1L)).thenReturn(Optional.of(booking));
        Booking result = bookingServiceImpl.getBookingById(1L);
        assertEquals((Long)1L, result.getId());
        assertEquals(BookingStatus.CONFIRMED, result.getBookingStatus());
    }

    @Test
    public void deleteBooking() {
        Booking booking = new Booking();
        booking.setId(1L);
        booking.setBookingStatus(BookingStatus.CONFIRMED);
        Payment payment = new Payment();
        payment.setPaymentStatus(PaymentStatus.UNPAID);
        booking.setPayment(payment);
        when(bookingRepo.findById(1L)).thenReturn(Optional.of(booking));
        bookingServiceImpl.deleteBooking(1L);

        assertEquals(BookingStatus.CANCELLED, booking.getBookingStatus());
        assertEquals(PaymentStatus.CANCELLED, payment.getPaymentStatus());
        verify(bookingRepo, times(1)).save(booking);
        verify(paymentRepo, times(1)).save(payment);
    }

    @Test
    public void getAllBooking() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Booking booking = new Booking();
        booking.setId(1L);
        booking.setBookingStatus(BookingStatus.CONFIRMED);

        Page<Booking> bookingPage = new PageImpl<>(Collections.singletonList(booking), pageable, 1);

        when(bookingRepo.findAllNotCancelled(pageable, BookingStatus.CANCELLED)).thenReturn(bookingPage);

        Page<BookingResponseDto> responsePage = bookingServiceImpl.getAllBooking(0, 10, "id", Sort.Direction.ASC);

        assertEquals(1, responsePage.getTotalElements());
        assertEquals(1L, responsePage.getContent().get(0).getId().longValue());
    }

    @Test(expected = CustomException.class)
    public void getBookingById_notFound() {
        when(bookingRepo.findById(1L)).thenReturn(Optional.empty());
        bookingServiceImpl.getBookingById(1L);
    }
}