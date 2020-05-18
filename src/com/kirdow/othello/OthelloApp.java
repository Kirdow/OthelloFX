package com.kirdow.othello;

import com.kirdow.othello.events.WindowEvent;
import com.kirdow.othello.pieces.Piece;
import com.kirdow.othello.pieces.PieceBlack;
import com.kirdow.othello.util.Ref;
import com.kirdow.othello.util.Utils;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class OthelloApp extends Application {

    private Stage primaryStage;

    private BorderPane root;
    private Group contentRoot;

    private WindowEvent windowEvent;
    private Piece[] pieces;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;

        this.createWindow();
        this.createEvents();

        pieces = new Piece[8*8];

        for (int x = 0; x < 2; x++) {
            for (int y = 0; y < 2; y++) {
                boolean isBlack = (x + y) % 2 == 0;
                addPiece(x + 3, y + 3, isBlack);
            }
        }
    }

    private void createWindow() {
        Insets insets = new Insets(0.0, PADDING, PADDING, PADDING / 3.0);

        root = new BorderPane();
        contentRoot = new Group();
        root.setCenter(contentRoot);
        BorderPane.setMargin(contentRoot, insets);

        ObservableList<Node> list = contentRoot.getChildren();

        Rectangle topLeftRect = null;
        Rectangle rect;
        for (int _y = 0; _y < 8; _y++) {
            int y = _y * 100 + PADDING;
            for (int _x = 0; _x < 8; _x++) {
                int x = _x * 100 + PADDING;

                rect = new Rectangle(x, y, 100.0, 100.0);
                rect.setFill(Color.valueOf("#2EAE52"));

                list.add(rect);

                if (_x == 0 && _y == 0)
                    topLeftRect = rect;
            }
        }

        Font textFont = new Font("Arial", DPADDING / 3.0);

        for (int i = 0; i < 8; i++) {
            char horizontalChar = (char)('A' + i);
            char verticalChar = (char)('8' - i);

            Text verticalText = new Text(PADDING / 3.0, i * 100 + 50 + PADDING * 4.0 / 3.0, String.valueOf(verticalChar));
            verticalText.setFont(textFont);
            verticalText.setFill(Color.BLACK);

            Text horizontalText = new Text(i * 100 + 50 + DPADDING / 3.0, DPADDING / 3.0, String.valueOf(horizontalChar));
            horizontalText.setFont(textFont);
            horizontalText.setFill(Color.BLACK);

            list.addAll(verticalText, horizontalText);
        }

        for (int i = 0; i < 9; i++) {
            Line lineH = new Line(PADDING, extraPadding(false, TILE_PIXEL_SIZE * i), extraPadding(false, 800), extraPadding(false, TILE_PIXEL_SIZE * i));
            Line lineV = new Line(extraPadding(false, TILE_PIXEL_SIZE * i), PADDING, extraPadding(false, TILE_PIXEL_SIZE * i), extraPadding(false, 800));

            lineH.setFill(Color.BLACK);
            lineV.setFill(Color.BLACK);

            list.addAll(lineH, lineV);
        }

        for (int y = 2; y <= 6; y+= 4) {
            for (int x = 2; x <= 6; x+= 4) {
                Circle circle = new Circle(extraPadding(false, TILE_PIXEL_SIZE * x), extraPadding(false, TILE_PIXEL_SIZE * y), 4.0, Color.BLACK);
                list.add(circle);
            }
        }

        double extraWidth = 0.0;
        double extraHeight = 0.0;

        if (Utils.OS.isWin) {
            extraHeight = PADDING;
        }

        Scene scene = new Scene(root, extraPadding(true, BOARD_PIXEL_SIZE + extraWidth), extraPadding(true, BOARD_PIXEL_SIZE + extraHeight));

        primaryStage.setTitle("OthelloFX");
        primaryStage.setScene(scene);
        primaryStage.show();

        Bounds boundsInScene = topLeftRect.localToScene(topLeftRect.getBoundsInLocal());

        boardLeft = boundsInScene.getMinX();
        boardTop = boundsInScene.getMinY();

    }

    private void createEvents() {
        windowEvent = new WindowEvent(this);

        contentRoot.addEventHandler(MouseEvent.MOUSE_CLICKED, windowEvent);
    }

    public boolean isLocalTurn() {
        return true; // TODO: Correct this when adding multiplayer
    }

    public void clickTile(int x, int y) {
        if (isLocalTurn()) {
            makeTurn(x, y);
        } else {
            spectateClick(x, y);
        }
    }

    private void makeTurn(int x, int y) {
        addPiece(x, y, true);
    }

    private void spectateClick(int x, int y) {

    }

    public void addPiece(int x, int y, boolean isBlack) {
        Piece piece = getPiece(x, y);
        if (piece != null)
            return;

        piece = Piece.newPiece(isBlack);

        double baseX = PADDING + TILE_PIXEL_SIZE / 2.0;
        double baseY = PADDING + TILE_PIXEL_SIZE / 2.0;

        piece.setCenterPosition(baseX + x * TILE_PIXEL_SIZE, baseY + y * TILE_PIXEL_SIZE);
        piece.setColor();
        piece.addTo(contentRoot.getChildren());
        pieces[x + y * 8] = piece;
    }

    public Piece getPiece(int x, int y) {
        if (!isInBounds(x, y))
            return null;

        return pieces[x + y * 8];
    }

    public boolean isInBounds(int x, int y) {
        return x >= 0 && y >= 0 && x < 8 && y < 8;
    }

    public boolean hasPiece(int x, int y) {
        if (!isInBounds(x, y))
            return false;

        return pieces[x + y * 8] != null;
    }

    public static double BOARD_PIXEL_SIZE = 800.0;
    public static double TILE_PIXEL_SIZE = BOARD_PIXEL_SIZE / 8.0;

    public static final int PADDING = 24;
    public static final int DPADDING = PADDING * 2;

    private static double boardLeft, boardTop;

    public static double extraPadding(boolean isDouble, double extra) {
        return extra + (isDouble ? DPADDING : PADDING);
    }

    public static boolean isInsideBoard(double x, double y) {
        return x >= PADDING && y >= PADDING && x < extraPadding(false, BOARD_PIXEL_SIZE) && y < extraPadding(false, BOARD_PIXEL_SIZE);
    }

    public static boolean getBoardTileFromScreen(double x, double y, Ref<Integer> tileX, Ref<Integer> tileY) {
        if (tileX == null || tileY == null)
            return false;

        if (!isInsideBoard(x, y))
            return false;

        x -= boardLeft;
        y -= boardTop;

        tileX.value = (int)(x / TILE_PIXEL_SIZE);
        tileY.value = (int)(y / TILE_PIXEL_SIZE);

        return true;
    }

    static void launchApp(String[] args) {
        launch(args);
    }
}
