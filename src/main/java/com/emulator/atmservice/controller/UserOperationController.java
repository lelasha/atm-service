package com.emulator.atmservice.controller;

import com.emulator.atmservice.annotation.CheckToken;
import com.emulator.atmservice.controller.request.CashAmountDto;
import com.emulator.atmservice.service.UserOperationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.time.Instant;

@RestController
@RequestMapping("/operation")
@RequiredArgsConstructor
public class UserOperationController {
    private final UserOperationService userOperationService;


    @PostMapping("/deposit")
    @CheckToken
    public ResponseEntity<BigDecimal> depositCash(@RequestHeader("Authorization") String token,
                                                  @RequestBody @Valid CashAmountDto cashAmountDto) {
        return ResponseEntity.ok(userOperationService.deposit(cashAmountDto.getAmount(), token));
    }

    @PostMapping("/withdraw")
    @CheckToken
    public ResponseEntity<BigDecimal> withdrawCash(@RequestHeader("Authorization") String token,
                                                   @RequestBody @Valid CashAmountDto cashAmountDto) {
        return ResponseEntity.ok(userOperationService.withdraw(cashAmountDto.getAmount(), token));
    }

    @GetMapping("/balance")
    @CheckToken
    public ResponseEntity<BigDecimal> checkBalance(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(userOperationService.balance(token));
    }

    @GetMapping("/print")
    @CheckToken
    public ResponseEntity<Object> printReceipt(@RequestHeader("Authorization") String token,
                                               @RequestParam Instant startDate,
                                               @RequestParam Instant endDate) {
        return ResponseEntity.ok(userOperationService.print(token,startDate,endDate));
    }
}
