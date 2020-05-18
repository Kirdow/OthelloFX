package com.kirdow.othello.pieces;

import com.kirdow.othello.OthelloApp;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public abstract class Piece {

    private boolean isBlack;
    private Circle circle;

    private int flipCount;

    public Piece(boolean isBlack) {
        this.isBlack = isBlack;
        this.circle = new Circle(OthelloApp.TILE_PIXEL_SIZE * 0.8 / 2);

        flipCount = 0;
    }

    public Piece setCenterPosition(double x, double y) {
        this.circle.setCenterX(x);
        this.circle.setCenterY(y);

        return this;
    }

    public Piece setColor() {
        circle.setFill(isBlack ? Color.BLACK : Color.WHITE);

        return this;
    }

    public Piece addTo(ObservableList<Node> list) {
        if (!list.contains(circle))
            list.add(circle);

        return this;
    }

    public Piece flip() {
        this.isBlack = !this.isBlack;
        this.flipCount++;

        return this.setColor();
    }

    public static Piece newPiece(boolean isBlack) {
        return isBlack ? new PieceBlack() : new PieceWhite();
    }

}
