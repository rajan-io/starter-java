package org.example;

import java.util.*;

public class SnakeGame {

    public record Cell(
            int row,
            int col
    ){}
    public enum Direction {
        U(-1,0),D(1,0),L(0,-1),R(0,1);

        private final int rowOffset;
        private final int colOffset;

        Direction(int row, int col) {
            this.rowOffset = row;
            this.colOffset = col;
        }

        public Cell nextCell(Cell cur) {
            return new Cell(cur.row + this.rowOffset, cur.col + this.colOffset);
        }
    }


    private final int width;
    private final int height;
    private Deque<Cell> snake;
    private Set<Cell> snakeBodyLookup;

    private final ArrayList<Cell> foods;
    private int foodIndex;

    private int score;
    public SnakeGame(int width, int height, ArrayList<Cell> foods) {
        this.width = width;
        this.height = height;
        this.foodIndex = 0;
        this.foods = foods;
        this.score = 0;
        Cell initialHead = new Cell(0, 0);
        snakeBodyLookup = new HashSet<>();
        snakeBodyLookup.add(initialHead);
        this.snake = new LinkedList<>();
        this.snake.addFirst(initialHead);
    }
    public SnakeGame(int width, int height, int[][] food) {
        this(width, height, asArrayCells(food));
    }

    private static ArrayList<Cell> asArrayCells(int[][] food) {
        ArrayList<Cell> list = new ArrayList<>();
        for(int[] f: food) {
            list.add(new Cell(f[0], f[1]));
        }

        return list;
    }

    public Cell getHead() {
        return snake.peekFirst();
    }
    public Cell getTail() {
        return snake.peekLast();
    }
    public Cell getFood() {

        return foodIndex < foods.size() ? foods.get(foodIndex) : null;
    }

    public int move(String direction) {
        Direction moveDirection = Direction.valueOf(direction);
        if(score < 0) return score;

        Cell head = snake.getFirst();
        Cell tail = snake.getLast(); // peek tail
        Cell nextHead = moveDirection.nextCell(head);

        //head hits boundary game exit -1
        if(hasCrossBoundry(nextHead)) {
            score = -1;
            return -1;
        }
        //head hits snake body game exit -1 , next head is on tail which will move it ok
        if(snakeBodyLookup.contains(nextHead) && !Objects.equals(nextHead, tail)) {
            score = -1;
            return -1; //bites itself
        }
        //head hits food, snake move head, keeps tail
        if(isEatingFood(nextHead)) {
            score++;
            foodIndex++; //eat food
            moveHead(nextHead);
        } else {
            removeTail();
            moveHead(nextHead);
        }
        //head hit nothing, move head move tail


        return score;
    }

    private boolean isEatingFood(Cell nextHead) {
        return hasNextFood() && Objects.equals(foods.get(foodIndex), nextHead);
    }

    private void removeTail() {
        var tail = snake.removeLast();
        snakeBodyLookup.remove(tail);
    }

    private void moveHead(Cell nextHead) {
        snake.addFirst(nextHead); // move head keep tail
        snakeBodyLookup.add(nextHead);
    }

    private boolean hasNextFood() {
        return foodIndex < foods.size();
    }

    private boolean hasCrossBoundry(Cell nextHead) {
        return nextHead.col < 0 || nextHead.col >= width || nextHead.row < 0 || nextHead.row >= height;
    }
}
