package com.example;

import java.util.List;
import java.util.Stack;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;

public class Grid {
    private int rows;
    private int columns;
    private boolean supermine;
    private int left;
    private int flags;
    private int max_flags;
    private int moves;
    private boolean gameover;
    private GridPane gridPane;
    private Tile[][] tile;

    public Grid(int rows, int columns, boolean supermine) {
        this.rows = rows;
        this.columns = columns;
        this.supermine = supermine;
        gridPane = new GridPane();
        tile = new Tile[rows][columns];
        for (int y = 0; y < rows; y++)
            for (int x = 0; x < columns; x++) {
                tile[y][x] = new Tile(y, x);
                gridPane.add(tile[y][x].getImageView(), x, y);
            }
        gridPane.setAlignment(Pos.CENTER);
    }

    public void init(List<Pair<Integer, Integer>> mines) {
        left = rows * columns - mines.size();
        flags = 0;
        max_flags = mines.size();
        moves = 0;
        gameover = false;
        boolean[][] hasMine = new boolean[rows][columns];
        int[][] minesNearby = new int[rows][columns];
        for (int i = 0; i < mines.size(); i++) {
            hasMine[mines.get(i).getKey()][mines.get(i).getValue()] = true;
        }
        for (int y = 0; y < rows; y++)
            for (int x = 0; x < columns; x++) {
                if (hasMine[y][x]) {
                    minesNearby[y][x] = 9;
                } else {
                    if (y > 0 && x > 0 && hasMine[y - 1][x - 1])
                        minesNearby[y][x]++;
                    if (y > 0 && hasMine[y - 1][x])
                        minesNearby[y][x]++;
                    if (y > 0 && x < columns - 1 && hasMine[y - 1][x + 1])
                        minesNearby[y][x]++;
                    if (x > 0 && hasMine[y][x - 1])
                        minesNearby[y][x]++;
                    if (x < columns - 1 && hasMine[y][x + 1])
                        minesNearby[y][x]++;
                    if (y < rows - 1 && x > 0 && hasMine[y + 1][x - 1])
                        minesNearby[y][x]++;
                    if (y < rows - 1 && hasMine[y + 1][x])
                        minesNearby[y][x]++;
                    if (y < rows - 1 && x < columns - 1 && hasMine[y + 1][x + 1])
                        minesNearby[y][x]++;
                }
            }
        for (int y = 0; y < rows; y++)
            for (int x = 0; x < columns; x++)
                tile[y][x].intit(minesNearby[y][x]);

        for (int y = 0; y < rows; y++)
            for (int x = 0; x < columns; x++) {
                int r = y;
                int c = x;
                Tile t = tile[r][c];
                if (t.getNumber() == 0) {
                    t.getImageView().setOnMouseClicked(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent arg0) {
                            if (arg0.getButton() == MouseButton.PRIMARY) {
                                if (t.isHidden()) {
                                    Stack<Pair<Integer, Integer>> stack = new Stack<Pair<Integer, Integer>>();
                                    boolean[][] visited = new boolean[rows][columns];
                                    stack.push(new Pair<Integer, Integer>(t.getRow(), t.getColumn()));
                                    visited[t.getRow()][t.getColumn()] = true;
                                    while (!stack.empty()) {
                                        Pair<Integer, Integer> p = stack.pop();
                                        int y = p.getKey();
                                        int x = p.getValue();
                                        if (!tile[y][x].isHidden())
                                            continue;
                                        if (tile[y][x].isFlagged())
                                            flags--;
                                        tile[y][x].reveal();
                                        left--;
                                        if (tile[y][x].getNumber() == 0) {
                                            if (y > 0 && x > 0 && !visited[y - 1][x - 1]) {
                                                stack.push(new Pair<Integer, Integer>(y - 1, x - 1));
                                                visited[y - 1][x - 1] = true;
                                            }
                                            if (y > 0 && !visited[y - 1][x]) {
                                                stack.push(new Pair<Integer, Integer>(y - 1, x));
                                                visited[y - 1][x] = true;
                                            }
                                            if (y > 0 && x < columns - 1 && !visited[y - 1][x + 1]) {
                                                stack.push(new Pair<Integer, Integer>(y - 1, x + 1));
                                                visited[y - 1][x + 1] = true;
                                            }
                                            if (x > 0 && !visited[y][x - 1]) {
                                                stack.push(new Pair<Integer, Integer>(y, x - 1));
                                                visited[y][x - 1] = true;
                                            }
                                            if (x < columns - 1 && !visited[y][x + 1]) {
                                                stack.push(new Pair<Integer, Integer>(y, x + 1));
                                                visited[y][x + 1] = true;
                                            }
                                            if (y < rows - 1 && x > 0 && !visited[y + 1][x - 1]) {
                                                stack.push(new Pair<Integer, Integer>(y + 1, x - 1));
                                                visited[y + 1][x - 1] = true;
                                            }
                                            if (y < rows - 1 && !visited[y + 1][x]) {
                                                stack.push(new Pair<Integer, Integer>(y + 1, x));
                                                visited[y + 1][x] = true;
                                            }
                                            if (y < rows - 1 && x < columns - 1
                                                    && !visited[y + 1][x + 1]) {
                                                stack.push(new Pair<Integer, Integer>(y + 1, x + 1));
                                                visited[y + 1][x + 1] = true;
                                            }
                                        }
                                    }
                                    moves++;
                                }
                            }
                        }
                    });
                } else if (t.getNumber() == 9) {
                    t.getImageView().setOnMouseClicked(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent arg0) {
                            if (arg0.getButton() == MouseButton.PRIMARY) {
                                if (t.isHidden()) {
                                    t.reveal();
                                    moves++;
                                    gameover = true;
                                }
                            }
                        }
                    });
                } else {
                    t.getImageView().setOnMouseClicked(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent arg0) {
                            if (arg0.getButton() == MouseButton.PRIMARY) {
                                if (t.isHidden()) {
                                    t.reveal();
                                    left--;
                                    moves++;
                                }
                            }
                        }
                    });
                }
                if (supermine && r == mines.get(0).getKey() && c == mines.get(0).getValue()) {
                    t.getImageView().setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {
                        @Override
                        public void handle(ContextMenuEvent arg0) {
                            if (t.isHidden()) {
                                if (t.isFlagged()) {
                                    t.unflag();
                                    flags--;
                                } else if (flags < max_flags) {
                                    t.flag();
                                    flags++;
                                    if (moves <= 4) {
                                        for (int y = 0; y < rows; y++)
                                            if (y != r) {
                                                if (tile[y][c].isHidden()) {
                                                    tile[y][c].show();
                                                    if (tile[y][c].getNumber() == 9)
                                                        max_flags--;
                                                    else
                                                        left--;
                                                    if (tile[y][c].isFlagged())
                                                        flags--;
                                                }
                                            }
                                        for (int x = 0; x < columns; x++)
                                            if (x != c) {
                                                if (tile[r][x].isHidden()) {
                                                    tile[r][x].show();
                                                    if (tile[r][x].getNumber() == 9)
                                                        max_flags--;
                                                    else
                                                        left--;
                                                    if (tile[r][x].isFlagged())
                                                        flags--;
                                                }
                                            }
                                    }
                                }
                            }
                        }
                    });
                } else {
                    t.getImageView().setOnContextMenuRequested(
                            new EventHandler<ContextMenuEvent>() {
                                @Override
                                public void handle(ContextMenuEvent arg0) {
                                    if (t.isHidden()) {
                                        if (t.isFlagged()) {
                                            t.unflag();
                                            flags--;
                                        } else if (flags < max_flags) {
                                            t.flag();
                                            flags++;
                                        }
                                    }
                                }
                            });
                }
            }
    }

    public int getLeft() {
        return left;
    }

    public int getFlags() {
        return flags;
    }

    public int getMoves() {
        return moves;
    }

    public boolean isGameOver() {
        return gameover;
    }

    public void solution() {
        for (int y = 0; y < rows; y++)
            for (int x = 0; x < columns; x++)
                if (tile[y][x].isHidden() && tile[y][x].getNumber() == 9)
                    tile[y][x].show();
        gameover = true;
    }

    public GridPane getGridPane() {
        return gridPane;
    }
}