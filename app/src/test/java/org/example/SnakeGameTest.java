package org.example;

import org.assertj.core.api.Assertions;
import org.example.SnakeGame.Direction;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.example.SnakeGame.*;
import static org.example.SnakeGame.Direction.*;
import static org.junit.jupiter.api.Assertions.*;

class SnakeGameTest {

    @Test
    @DisplayName("shouldMoveToPosition")
    void shouldMoveToPosition() {
        //given
        int[][] food = new int[][] { {1,2}, {0,1}};
        SnakeGame snakeGame = new SnakeGame(3, 2, food);
        //when
        List<String> moves = List.of("R", "D", "R", "U", "L","U");
        List<Integer> scores = new ArrayList<>();
        System.out.println("score:" + 0 + ", head:" + snakeGame.getHead() + ",tail:" + snakeGame.getTail() + ",food:" + snakeGame.getFood());

        for (String move : moves) {
            var res = snakeGame.move(move);
            System.out.println("score:" + res + ", head:" + snakeGame.getHead() + ",tail:" + snakeGame.getTail() + ",food:" + snakeGame.getFood());
            scores.add(res);
        }

        //then
        assertThat(scores).isEqualTo(List.of(0, 0,1,1,2,-1));

    }

    @Test
    @DisplayName("shouldMoveToPositionWithScore")
    void shouldMoveToPositionAdditinal() {
        //given
        int[][] food = new int[][] { {0,1}};
        SnakeGame snakeGame = new SnakeGame(2, 1, food);
        //when
        List<String> moves = List.of("R", "R");
        List<Integer> scores = new ArrayList<>();
        for (String move : moves) {
            var res = snakeGame.move(move);
            System.out.println("score:" + res + ", head:" + snakeGame.getHead() + ",tail:" + snakeGame.getTail() + ",food:" + snakeGame.getFood());
            scores.add(res);
        }

        //then
        assertThat(scores).isEqualTo(List.of(1,-1));

    }

    @Test
    @DisplayName("should move for direction")
    void shouldMoveForDirection() {
        Cell cur = new Cell(2, 2);

        //then
        assertThat(L.nextCell(cur)).isEqualTo(new Cell(2,1));
        assertThat(R.nextCell(cur)).isEqualTo(new Cell(2,3));
        assertThat(U.nextCell(cur)).isEqualTo(new Cell(1,2));
        assertThat(D.nextCell(cur)).isEqualTo(new Cell(3,2));
    }

}