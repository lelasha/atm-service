package com.emulator.atmservice.service.impl;

import com.emulator.atmservice.service.CardValidationService;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
public class CardValidationServiceImpl implements CardValidationService {

    private static final String REGEX =
            "^(?:(?<visa>4[0-9]{12}(?:[0-9]{3})?)|" +
                    "(?<mastercard>5[1-5][0-9]{14})|" +
                    "(?<discover>6(?:011|5[0-9]{2})[0-9]{12})|" +
                    "(?<amex>3[47][0-9]{13})|" +
                    "(?<diners>3(?:0[0-5]|[68][0-9])?[0-9]{11})|" +
                    "(?<jcb>(?:2131|1800|35[0-9]{3})[0-9]{11}))$";


    @Override
    public boolean validateCard(String cardNumber) {
        return Pattern.compile(REGEX)
                .matcher(cardNumber)
                .matches();
    }

}
