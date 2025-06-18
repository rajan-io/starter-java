package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

class MainTest {


    @Test
    @DisplayName("should be setup ready")
    void shouldBeSetupReady() {
        //given
        var main = new Main();

        //when
        var testSetup = true;
        //then
        assertThat(testSetup).isTrue();
    }
}
