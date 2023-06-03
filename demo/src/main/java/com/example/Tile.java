package com.example;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Tile {
    private int row;
    private int column;
    private int number;
    private boolean hidden;
    private boolean flagged;
    private Image[] image;
    private ImageView imageView;

    /**
     * Constructs a new Tile object and loads all resources.
     *
     * @param row    the row of the tile in the Minesweeper grid
     * @param column the column of the tile in the Minesweeper grid
     */
    public Tile(int row, int column) {
        this.row = row;
        this.column = column;
        image = new Image[13];
        for (int i = 0; i <= 9; i++)
            image[i] = new Image(getClass().getResourceAsStream("tile_" + i + ".png"), 30, 30, false, false);
        image[10] = new Image(getClass().getResourceAsStream("tile_hidden.png"), 30, 30, false, false);
        image[11] = new Image(getClass().getResourceAsStream("tile_flag.png"), 30, 30, false, false);
        image[12] = new Image(getClass().getResourceAsStream("tile_mine.png"), 30, 30, false, false);
        imageView = new ImageView(image[10]);
    }

    /**
     * Initializes the tile with the specified number of neighboring mines and makes
     * it hidden.
     *
     * @param number the number of neighboring mines or 9 if it is a mine
     */
    public void intit(int number) {
        this.number = number;
        hidden = true;
        flagged = false;
        imageView.setImage(image[10]);
    }

    /**
     * Shows what is hidden in the tile.
     */
    public void show() {
        hidden = false;
        imageView.setImage(image[number]);
    }

    /**
     * Shows what is hidden in the tile except mine is hit.
     */
    public void reveal() {
        if (number == 9) {
            hidden = false;
            imageView.setImage(image[12]);
        } else
            show();
    }

    /**
     * Flags the tile as a mine.
     */
    public void flag() {
        flagged = true;
        imageView.setImage(image[11]);
    }

    /**
     * Unflags the tile, removing the mine flag.
     */
    public void unflag() {
        flagged = false;
        imageView.setImage(image[10]);
    }

    /**
     * Returns the row of the tile in the Minesweeper grid.
     *
     * @return the row of the tile
     */
    public int getRow() {
        return row;
    }

    /**
     * Returns the column of the tile in the Minesweeper grid.
     *
     * @return the column of the tile
     */
    public int getColumn() {
        return column;
    }

    /**
     * Returns the number of neighboring mines or 9 if it is a mine.
     *
     * @return the number of neighboring mines or 9 if it is a mine
     */
    public int getNumber() {
        return number;
    }

    /**
     * Returns whether the tile is currently hidden.
     *
     * @return true if the tile is hidden, false otherwise
     */
    public boolean isHidden() {
        return hidden;
    }

    /**
     * Returns whether the tile has been flagged as a mine.
     *
     * @return true if the tile is flagged, false otherwise
     */
    public boolean isFlagged() {
        return flagged;
    }

    /**
     * Returns the image view displaying the current state of the tile.
     *
     * @return the image view displaying the tile
     */
    public ImageView getImageView() {
        return imageView;
    }
}