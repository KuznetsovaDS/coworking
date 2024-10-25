package com.example.coworking.model.dto.response;

import com.example.coworking.model.dto.request.PaymentRequestDto;
import com.example.coworking.model.enums.PaymentStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PaymentResponseDto extends PaymentRequestDto {

    Long id;

    PaymentStatus paymentStatus;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    UserResponseDto user;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    BookingResponseDto booking;
}
