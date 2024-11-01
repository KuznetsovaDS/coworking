package com.example.coworking.service;

import com.example.coworking.exceptions.CustomException;
import com.example.coworking.model.dto.request.BookingRequestDto;
import com.example.coworking.model.dto.request.PaymentRequestDto;
import com.example.coworking.model.dto.response.BookingResponseDto;
import com.example.coworking.model.entity.Booking;
import com.example.coworking.model.entity.Payment;
import com.example.coworking.model.entity.Workspace;
import com.example.coworking.model.enums.BookingStatus;
import com.example.coworking.model.enums.PaymentStatus;
import com.example.coworking.model.repositories.BookingRepo;
import com.example.coworking.model.repositories.PaymentRepo;
import com.example.coworking.model.repositories.WorkspaceRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import static com.example.coworking.utils.PaginationUtil.getPageRequest;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@EnableScheduling
public class BookingServiceImpl implements BookingService{

    private final ObjectMapper mapper;
    private final BookingRepo bookingRepo;
    private final UserService userService;
    private final WorkspaceRepo workspaceRepo;
    private final PaymentService paymentService;
    private final PaymentRepo paymentRepo;

    @Override
    public BookingResponseDto createBooking(BookingRequestDto bookingRequestDto) {

        Workspace workspace = workspaceRepo.findById(bookingRequestDto.getWorkspaceId())
                .orElseThrow(() -> new CustomException("Workspace not found", HttpStatus.NOT_FOUND));

        LocalDateTime startTime = bookingRequestDto.getStartTime();
        LocalDateTime endTime = bookingRequestDto.getEndTime();

        if (isWorkspaceBooked(workspace, startTime, endTime)) {
            throw new CustomException("Workspace is unavailable for bookings. Choose different time!", HttpStatus.BAD_REQUEST);
        }
        long hours = ChronoUnit.HOURS.between(startTime, endTime);
        if (hours <= 0) {
            throw new CustomException("End time must be after start time", HttpStatus.BAD_REQUEST);
        }
        double totalCost = hours * workspace.getPrice();

        Booking booking = mapper.convertValue(bookingRequestDto, Booking.class);
        booking.setStartTime(startTime);
        booking.setEndTime(endTime);
        booking.setWorkspace(workspace);
        booking.setUser(userService.getUserEntity(bookingRequestDto.getUserId()));
        booking.setBookingStatus(BookingStatus.CONFIRMED);
        booking.setCreatedAt(LocalDateTime.now());
        booking.setTotalCost(totalCost);
        Booking saveBooking = bookingRepo.save(booking);

        PaymentRequestDto paymentRequestDto = new PaymentRequestDto();
        paymentRequestDto.setPaymentTime(LocalDateTime.now());
        paymentService.createPayment(saveBooking, paymentRequestDto);

        return mapper.convertValue(saveBooking,BookingResponseDto.class);
    }
    private boolean isWorkspaceBooked(Workspace workspace, LocalDateTime startTime, LocalDateTime endTime) {
        List<Booking> activeBookings = bookingRepo.findOverlapBookings(
                workspace.getId(), BookingStatus.CONFIRMED, startTime, endTime);
        return !activeBookings.isEmpty();
    }
    @Scheduled(fixedRate = 60000)
    public void updateExpiredBookings() {
        LocalDateTime now = LocalDateTime.now();
        List<Booking> expiredBookings = bookingRepo.findPastBookings(BookingStatus.CONFIRMED, now);
        for (Booking booking : expiredBookings) {
            log.info("Updating booking ID {} status {}", booking.getId(), booking.getBookingStatus());
            booking.setBookingStatus(BookingStatus.COMPLETED);
            booking.setUpdatedAt(LocalDateTime.now());
            bookingRepo.save(booking);
            log.info("Updated booking ID: {} to status COMPLETED", booking.getId());
        }
    }
    @Override
    public Booking getBookingById(Long id) {
        return bookingRepo.findById(id)
                .orElseThrow(()->new CustomException("No booking!", HttpStatus.NOT_FOUND));
    }

    @Override
    public void deleteBooking(Long id) {
        Booking booking = getBookingById(id);
        booking.setBookingStatus(BookingStatus.CANCELLED);
        booking.setUpdatedAt(LocalDateTime.now());
        bookingRepo.save(booking);

        Payment payment = booking.getPayment();
        payment.setPaymentStatus(PaymentStatus.CANCELLED);
        payment.setPaymentStatus(PaymentStatus.CANCELLED);
        payment.setUpdatedAt(LocalDateTime.now());
        paymentRepo.save(payment);

    }
    @Override
    public Page<BookingResponseDto> getAllBooking(Integer page, Integer perPage, String sort, Sort.Direction order) {
        Pageable pageRequest = getPageRequest(page, perPage, sort, order);

        Page<Booking> pageList = bookingRepo.findAllNotCancelled(pageRequest, BookingStatus.CANCELLED);
        List<BookingResponseDto> responses = pageList.getContent().stream()
                .map(c -> mapper.convertValue(c, BookingResponseDto.class))
                .collect(Collectors.toList());
        return new PageImpl<>(responses, pageRequest, pageList.getTotalElements());
    }
    @Override
    public BookingResponseDto getBooking(Long bookingId) {
        Booking booking = bookingRepo.findById(bookingId)
                .orElseThrow(() -> new CustomException("No booking found", HttpStatus.NOT_FOUND));
        return mapper.convertValue(booking, BookingResponseDto.class);
    }
}
