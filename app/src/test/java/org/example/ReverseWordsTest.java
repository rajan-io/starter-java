package org.example;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

class ReverseWordsTest {
    @Test
    @DisplayName("should reverse words")
    void shouldReverseWords() {
        //given
        var input = "the sky is blue";
        var expected = "blue is sky the";

        //when
        ReverseWords reverseWords = new ReverseWords();
        String result = reverseWords.reverseWords(input);

        //then
        assertThat(result).isEqualTo(expected);
    }

    @Test
    @DisplayName("should reverse words removing extra spaces")
    void shouldReverseWordsRemovingExtraSpaces() {
        //given
        var input = "a good   example";
        var expected = "example good a";

        //when
        ReverseWords reverseWords = new ReverseWords();
        String result = reverseWords.reverseWords(input);

        //then
        assertThat(result).isEqualTo(expected);
    }

    @Test
    @DisplayName("should reverse words removing leading and trailing spaces")
    void shouldReverseWordsRemovingLeadingAndTrailingSpaces() {
        //given
        var input = "  hello world   ";
        var expected = "world hello";

        //when
        ReverseWords reverseWords = new ReverseWords();
        String result = reverseWords.reverseWords(input);

        //then
        assertThat(result).isEqualTo(expected);
    }

}