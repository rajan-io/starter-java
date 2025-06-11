package org.example;

import org.example.SnakeGame2.Cell;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

class SnakeGame2Test {

    @Test
    @DisplayName("should move head and tail")
    void shouldMoveHeadAndTail() {
        //given
        SnakeGame2 board = new SnakeGame2(2,2, new Cell(0, 0), new ArrayList<>());

        //when
        int score = board.move("D");

        //then
        assertThat(score).isEqualTo(0);
        assertThat(board.getSnakeHead()).isEqualTo(new Cell(1,0));
        assertThat(board.getSnakeTail()).isEqualTo(new Cell(1,0));
    }

    @Test
    @DisplayName("should end when hits border")
    void shouldEndWhenHitsBorder() {
        //given
        SnakeGame2 board = new SnakeGame2(2,2, new Cell(0, 0), new ArrayList<>());

        //when
        board.move("R");
        int score = board.move("R");

        //then
        assertThat(score).isEqualTo(-1);
        assertThat(board.getSnakeHead()).isEqualTo(new Cell(0,1));
    }

    @Test
    @DisplayName("should grow snake when eats")
    void shouldGrowSnakeWhenEats() {
        //given
        List<Cell> foodList = List.of(new Cell(1,0));
        SnakeGame2 board = new SnakeGame2(2,2, new Cell(0, 0), foodList);

        //when
        int score = board.move("D");

        //then
        assertThat(score).isEqualTo(1);
        assertThat(board.getSnakeHead()).isEqualTo(new Cell(1,0));
        assertThat(board.getSnakeTail()).isEqualTo(new Cell(0,0));
    }

    @Test
    @DisplayName("should bytes to end game")
    void shouldBytesToEndGame() {
        //given
        List<Cell> foodList = List.of(new Cell(1,0), new Cell(2,0), new Cell(3,0), new Cell(3,1), new Cell(2,1));
        SnakeGame2 board = new SnakeGame2(4,4, new Cell(0, 0), foodList);

        board.move("D");
        board.move("D");
        board.move("D");
        board.move("R");
        board.move("U");

        //when
        int score = board.move("L");

        //then
        assertThat(score).isEqualTo(-1);
        assertThat(board.getSnakeHead()).isEqualTo(new Cell(2,1));
        assertThat(board.getSnakeTail()).isEqualTo(new Cell(0,0));
    }
}