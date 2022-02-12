package com.emulator.atmservice.controller;

import com.emulator.atmservice.exception.CardNotValidException;
import com.emulator.atmservice.exception.CommonExceptionHandler;
import com.emulator.atmservice.service.UserValidationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static com.emulator.atmservice.util.FixtureHelpers.loadFixture;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// single endpoint test for demonstration
@WebMvcTest(controllers = UserValidationController.class)
class UserValidationControllerTest {
    @MockBean
    private UserValidationService userValidationService;
    @Autowired
    private MockMvc mockMvc;


    @Test
    void GivenEverythingIsOk_WhenValidatingCard_ShouldReturnResponseOK() throws Exception {
        mockMvc.perform(post("/card/validate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loadFixture("valid-card-dto.json")))
                .andExpect(status().is(200));
    }

    @Test
    void GivenExtraCardNumberSize_WhenValidatingCard_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(post("/card/validate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loadFixture("card-dto-extra-cardnumber-size.json")))
                .andExpect(status().is(400));
    }

    @Test
    void GivenLessCardNumberSize_WhenValidatingCard_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(post("/card/validate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loadFixture("card-dto-less-cardnumber-size.json")))
                .andExpect(status().is(400));
    }

    @Test
    void GivenNonDigitSymbols_WhenValidatingCard_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(post("/card/validate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loadFixture("card-dto-non-digit-symbols.json")))
                .andExpect(status().is(400));
    }

    @Test
    void GivenDigitsWithFraction_WhenValidatingCard_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(post("/card/validate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loadFixture("card-dto-digits-with-fraction.json")))
                .andExpect(status().is(400));
    }

    @Test
    void GivenEmptyCardNumber_WhenValidatingCard_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(post("/card/validate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loadFixture("card-dto-empty-cardnumber.json")))
                .andExpect(status().is(400));
    }

    @Test
    void GivenNullCardNumber_WhenValidatingCard_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(post("/card/validate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loadFixture("card-dto-null-cardnumber.json")))
                .andExpect(status().is(400));
    }

    @Test
    void GivenInvalidCardNumber_WhenValidatingCard_ShouldReturnBadRequest() throws Exception {
        when(userValidationService.validate(any())).thenThrow(CardNotValidException.class);

        mockMvc.perform(post("/card/validate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loadFixture("valid-card-dto.json")))
                .andExpect(status().is(400));
    }
}