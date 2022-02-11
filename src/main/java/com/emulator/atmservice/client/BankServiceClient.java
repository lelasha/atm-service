package com.emulator.atmservice.client;

import com.emulator.atmservice.config.BasicAuthConfig;
import com.emulator.atmservice.controller.request.AuthMethod;
import com.emulator.atmservice.controller.response.UserTransactionDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@FeignClient(name = "BankServiceClient", url = "${bank-service.client.url}", configuration = BasicAuthConfig.class)
public interface BankServiceClient {


    @PostMapping("/deposit")
    BigDecimal deposit(@RequestParam BigDecimal amount,
                       @RequestParam String accountNumber);

    @PostMapping("/withdraw")
    BigDecimal withdraw(@RequestParam BigDecimal amount,
                        @RequestParam String accountNumber);

    @GetMapping("/balance")
    BigDecimal balance(@RequestParam String accountNumber);

    @GetMapping("/user/check")
    String doesUserExist(@RequestParam String cardNumber);

    @PostMapping("/auth")
    boolean authenticate(@RequestParam String credential,
                         @RequestParam AuthMethod authMethod,
                         @RequestParam String accountNumber);

    @GetMapping("/transactions")
    List<UserTransactionDto> printTransactions(@RequestParam String accountNumber,
                                               @RequestParam Instant startDate,
                                               @RequestParam Instant endDate);
}
