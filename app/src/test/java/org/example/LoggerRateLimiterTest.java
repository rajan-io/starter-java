package org.example;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class LoggerRateLimiterTest {

    private LoggerRateLimiter loggerRateLimiter;

    @BeforeEach
    void setUp() {
        loggerRateLimiter = new LoggerRateLimiter();
    }

    @Test
    @DisplayName("should print message when not seen in 10 seconds")
    void shouldPrintMessageWhenNotSeenIn10Seconds() {
        //given
        loggerRateLimiter.shouldPrintMessage(1, "foo");

        //when
        boolean shouldPrintMessage = loggerRateLimiter.shouldPrintMessage(2, "bar");

        //then
       assertThat(shouldPrintMessage).isTrue();
    }

    @Test
    @DisplayName("should not print message when seen in 10 seconds")
    void shouldNotPrintMessageWhenSeenIn10Seconds() {
        //given
        loggerRateLimiter.shouldPrintMessage(1, "foo");

        //when
        boolean shouldPrintMessage = loggerRateLimiter.shouldPrintMessage(10, "foo");

        //then
        assertThat(shouldPrintMessage).isFalse();
    }

    @Test
    @DisplayName("should print message when seen after 10 seconds")
    void shouldPrintMessageWhenSeenAfter10Seconds() {
        //given
        loggerRateLimiter.shouldPrintMessage(1, "foo");

        //when
        boolean shouldPrintMessage = loggerRateLimiter.shouldPrintMessage(11, "foo");

        //then
        assertThat(shouldPrintMessage).isTrue();

    }
}