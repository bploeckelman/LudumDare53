package lando.systems.ld53.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import lando.systems.ld53.Config;
import lando.systems.ld53.screens.GameScreen;

public class BottomGameUI extends Table {
    private GameScreen screen;
    private static final float BUTTON_SIZE = 75f;
    private static final int BUTTON_COUNT = 6;
    private static final float X = Config.Screen.window_width / 2 - (BUTTON_COUNT * BUTTON_SIZE / 2);
    private static final float Y = 0f;

    public BottomGameUI(GameScreen screen) {
        this.screen = screen;
        setPosition(X, Y);
        setSize(BUTTON_SIZE * BUTTON_COUNT, BUTTON_SIZE);

    }
}
