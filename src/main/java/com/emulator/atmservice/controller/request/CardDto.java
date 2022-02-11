package com.emulator.atmservice.controller.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CardDto {
    @NotNull
    @NotEmpty
    @Digits(
            integer = 16,
            fraction = 0,
            message = "card number should consist of numbers with max length of 16")
    private String cardNumber;
}
