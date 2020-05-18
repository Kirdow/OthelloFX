package com.kirdow.othello.events;

import com.kirdow.othello.OthelloApp;
import com.kirdow.othello.util.Ref;
import javafx.event.EventHandler;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public class WindowEvent implements EventHandler<MouseEvent> {

    private OthelloApp othelloApp;

    public WindowEvent(OthelloApp app) {
        this.othelloApp = app;
    }

    @Override
    public void handle(MouseEvent event) {
        MouseButton button = event.getButton();
        double x = event.getSceneX();
        double y = event.getSceneY();

        switch (button) {
            case PRIMARY:
                primaryButton(x, y);
                break;
            case SECONDARY:
                secondaryButton(x, y);
                break;
            default:
                break;
        }
    }

    private void primaryButton(double x, double y) {
        Ref<Integer> tileX = new Ref<>(0);
        Ref<Integer> tileY = new Ref<>(0);

        if (OthelloApp.getBoardTileFromScreen(x, y, tileX, tileY)) {
            if (othelloApp != null)
                othelloApp.clickTile(tileX.value, tileY.value);
        }
    }

    private void secondaryButton(double x, double y) {
    }



}
