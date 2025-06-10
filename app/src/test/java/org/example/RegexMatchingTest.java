package org.example;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class RegexMatchingTest {

    @Test
    @DisplayName("should match characters")
    void shouldMatchCharacters() {
        //given
        RegexMatching matching = new RegexMatching();
        String text = "text";
        String pattern = "text";

        //when
        boolean res = matching.isMatch(text, pattern);

        //then
        assertThat(res).isTrue();

    }

    @Test
    @DisplayName("should match with dot")
    void shouldMatchWithDot() {
        //given
        RegexMatching matching = new RegexMatching();
        String text = "text";
        String pattern = "te.t";

        //when
        boolean res = matching.isMatch(text, pattern);

        //then
        assertThat(res).isTrue();
    }

    @Test
    @DisplayName("should match with star")
    void shouldMatchWithStar() {
        //given
        RegexMatching matching = new RegexMatching();
        String text = "texxxt";
        String pattern = "tex*t";

        //when
        boolean res = matching.isMatch(text, pattern);

        //then
        assertThat(res).isTrue();

    }

}