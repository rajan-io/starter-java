package org.example;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

class AllOneTest {

    @Test
    @DisplayName("should set word frecquency at first")
    void shouldSetWordFrecquencyAtFirst() {
        //given
        AllOne allOne = new AllOne();
        //when
        //then
        String max = allOne.getMaxKey();
        String min = allOne.getMinKey();
        assertThat(max).isEqualTo("");
        assertThat(min).isEqualTo("");
    }

    @Test
    @DisplayName("should increment word frequency")
    void shouldIncrementWordFrequency() {
        //given
        AllOne allOne = new AllOne();
        allOne.inc("hello");
        allOne.inc("world");

        //when
        allOne.inc("hello");

        //then
        String max = allOne.getMaxKey();
        String min = allOne.getMinKey();
        assertThat(max).isEqualTo("hello");
        assertThat(min).isEqualTo("world");
    }


    @Test
    @DisplayName("should decrement word frequency")
    void shouldDecrementWordFrequency() {
        //given
        AllOne allOne = new AllOne();
        allOne.inc("hello");
        allOne.inc("hello");
        allOne.inc("world");
        allOne.inc("world");

        //when
        allOne.dec("world");

        //then
        String max = allOne.getMaxKey();
        String min = allOne.getMinKey();
        assertThat(max).isEqualTo("hello");
        assertThat(min).isEqualTo("world");
    }

}