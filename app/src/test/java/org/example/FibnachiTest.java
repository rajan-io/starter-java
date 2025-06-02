package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.params.provider.Arguments.of;

class FibnachiTest {
    Fibnachi fibGenerator;
    @BeforeEach
    void setUp() {
        fibGenerator = new Fibnachi();
    }

    static Stream<Arguments> shouldGenerateFib() {
        return Stream.of(
                of(0, 0),
                of(1, 1),
                of(2, 1),
                of(3, 2),
                of(4, 3),
                of(5, 5),
                of(6, 8)
        );
    }
    @ParameterizedTest
    @MethodSource
    void shouldGenerateFib(int pos, int expectedFib) {
        assertThat(fibGenerator.fib(pos)).isEqualTo(expectedFib);
    }


    @ParameterizedTest()
    @ArgumentsSource(FibArgsProvider.class)
    void shouldGenerateFibAtPos(int pos, int expectedFib) {
        assertThat(fibGenerator.fib(pos)).isEqualTo(expectedFib);
    }

    static class FibArgsProvider implements ArgumentsProvider {
        @Override public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
            return Stream.of(
                    of(0, 0),
                    of(1, 1),
                    of(2, 1),
                    of(3, 2),
                    of(4, 3),
                    of(5, 5),
                    of(6, 8)
            );
        }
    }

}