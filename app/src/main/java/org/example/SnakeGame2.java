package org.example;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;

public class SnakeGame2 {

    public enum Status {RUNNING, ENDED}

    public class GameScore {
        private Status status;
        private int score;

        GameScore() {
            status = Status.RUNNING;
            score = 0;
        }

        public void increment() {
            if (status == Status.RUNNING) score++;
        }

        public void gameOver() {
            status = Status.ENDED;
        }

        public int response() {
            return status == Status.ENDED ? -1 : score;
        }
    }

    public record Cell(int row, int col) {
    }

    public enum Directions {
        U(-1, 0),
        D(1, 0),
        L(0, -1),
        R(0, 1);

        private final int rowOffset;
        private final int colOffset;

        Directions(int rowOffset, int colOffset) {
            this.rowOffset = rowOffset;
            this.colOffset = colOffset;
        }

        public Cell nextCell(Cell cur) {
            return new Cell(cur.row + rowOffset, cur.col + colOffset);
        }
    }

    private final Snake snake;
    private final GameScore score;
    private final OnMoveRule moveHandlerChain;

    public SnakeGame2(int width, int height, Cell initHead, List<Cell> foods) {

        snake = new Snake(new LinkedHashSet<>(List.of(initHead)));
        this.score = new GameScore();
        this.moveHandlerChain = new HandlerChainBuilder()
                .addHandler(new OnMoveBoundaryHandler(score, width, height))
                .addHandler(new EatingFoodHandler(score, snake, new Foods(foods, 0)))
                .addHandler(new ByteSelfHandler(score, snake))
                .addHandler(new OnMoveDefaultHandler(score, snake))
                .build();

    }

    static class HandlerChainBuilder {
        private OnMoveRule firstHandler;
        private OnMoveRule currentHandler;

        public HandlerChainBuilder addHandler(OnMoveRule handler) {
            if (firstHandler == null) {
                firstHandler = handler;
                currentHandler = handler;
            } else {
                currentHandler.setNext(handler);
                currentHandler = handler;
            }
            return this;
        }

        public OnMoveRule build() {
            return firstHandler;
        }
    }


    public interface OnMoveHandler {
        void onMove(Cell newHeadPos);
    }

    public static class OnMoveBoundaryHandler extends OnMoveRule {
        private final int width;
        private final int height;

        public OnMoveBoundaryHandler(GameScore score, int width, int height) {
            super(score);
            this.width = width;
            this.height = height;
        }

        @Override
        public void onMove(Cell newHeadPos) {
            if (isOutofBound(newHeadPos)) {
                score.gameOver();
            }
            performNext(newHeadPos);
        }

        private boolean isOutofBound(Cell newHeadPos) {
            return newHeadPos.col < 0 || newHeadPos.col >= this.width || newHeadPos.row < 0 || newHeadPos.row >= this.height;
        }

    }

    public static class OnMoveDefaultHandler extends OnMoveRule {
        private final Snake snake;

        public OnMoveDefaultHandler(GameScore score, Snake snake) {
            super(score);
            this.snake = snake;
        }

        @Override
        public void onMove(Cell newHeadPos) {
            if(score.status == Status.RUNNING) {
                snake.moveOrGrow(newHeadPos);
            }
            performNext(newHeadPos);
        }
    }


    private static class Snake {
        private final LinkedHashSet<Cell> snake;
        private boolean pendingGrow;

        public Snake(LinkedHashSet<Cell> snake) {
            this.snake = snake;
        }
        public void setPendingGrow(){
            pendingGrow = true;
        }

        public boolean isOverBody(Cell newHeadPos) {
            Cell curTail = snake.getLast();
            boolean onBody = snake.contains(newHeadPos);
            if (onBody && !(Objects.equals(newHeadPos, curTail) && pendingGrow)) { //ignore tail to move
               return true;
            }
            return onBody;
        }
        public void moveOrGrow(Cell newHeadPos) {
            if(!pendingGrow) {
                snake.removeLast();
            }
            snake.addFirst(newHeadPos);
        }
    }

    public static class Foods {
        private final List<Cell> foods;
        private int foodIndex;

        public Foods(List<Cell> foods, int foodIndex) {
            this.foods = foods;
            this.foodIndex = foodIndex;
        }

        boolean isAtCurrentFood(Cell newHeadPos) {
            Cell food = foodIndex < foods.size() ? foods.get(foodIndex) : null;
            boolean isEatingFood = Objects.equals(newHeadPos, food);
            return isEatingFood;
        }

        void eatFoodAndNext() {
            foodIndex++;
        }
    }

    public static class EatingFoodHandler extends OnMoveRule {
        private final Snake snake;
        private final Foods foods;


        public EatingFoodHandler(GameScore score, Snake snake, Foods foods) {
            super(score);
            this.snake = snake;
            this.foods = foods;
        }

        @Override
        public void onMove(Cell newHeadPos) {
            if (foods.isAtCurrentFood(newHeadPos)) { // eating food is growing
                foods.eatFoodAndNext();
                score.increment();
                snake.setPendingGrow();
            }
            performNext(newHeadPos);
        }

    }

    public static class ByteSelfHandler extends OnMoveRule {
        private final Snake snake;

        public ByteSelfHandler(GameScore score, Snake snake) {
            super(score);
            this.snake = snake;
        }

        @Override
        public void onMove(Cell newHeadPos) {
            if(snake.isOverBody(newHeadPos)) {
                score.gameOver();
            }
            performNext(newHeadPos);
        }


    }


    public int move(String d) {
        Directions moveDirection = Directions.valueOf(d);
        Cell newHeadPos = moveDirection.nextCell(getSnakeHead());
        moveHandlerChain.onMove(newHeadPos);
        return score.response();
    }


    Cell getSnakeHead() {
        return snake.snake.getFirst();
    }

    Cell getSnakeTail() {
        return snake.snake.getLast();
    }

    public abstract static class OnMoveRule implements OnMoveHandler {
        protected final GameScore score;
        private OnMoveHandler next;

        public OnMoveRule(GameScore score) {
            this.score = score;
        }


        public OnMoveRule setNext(OnMoveRule next) {
            this.next = next;
            return this;
        }


        public OnMoveHandler getNext() {
            return next;
        }

        public void performNext(Cell newHeadPos) {
            if(getNext()!=null) getNext().onMove(newHeadPos);
        }

        @Override
        public abstract void onMove(Cell newHeadPos);
    }
}
