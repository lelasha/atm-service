package com.emulator.atmservice.controller.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CashAmountDto {
    @Positive
    @Digits(integer = 6,fraction = 2,message = "wrong amount, max integer is 6 and fraction 2")
    private BigDecimal amount;
}
