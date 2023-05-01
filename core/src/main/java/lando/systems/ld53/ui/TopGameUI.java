package lando.systems.ld53.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import lando.systems.ld53.Assets;
import lando.systems.ld53.Config;
import lando.systems.ld53.screens.GameScreen;

public class TopGameUI extends Table {
    private final float UI_HEIGHT = 80f;
    private final float CELL_WIDTH = Config.Screen.window_width / 6f;

    public TopGameUI(GameScreen screen) {
        setWidth(Config.Screen.window_width);
        setHeight(UI_HEIGHT);
        setPosition(0f, Config.Screen.window_height - UI_HEIGHT);
        Table redTable = new Table();
        Table yellowTable = new Table();
        Table greenTable = new Table();
        Table blueTable = new Table();
        redTable.setBackground(Assets.Patch.glass_red.drawable);
        yellowTable.setBackground(Assets.Patch.glass_yellow.drawable);
        greenTable.setBackground(Assets.Patch.glass_green.drawable);
        blueTable.setBackground(Assets.Patch.glass_blue.drawable);
        add(redTable).width(CELL_WIDTH);
        add(yellowTable).width(CELL_WIDTH);
        add().width(CELL_WIDTH);
        add().width(CELL_WIDTH);
        add(greenTable).width(CELL_WIDTH);
        add(blueTable).width(CELL_WIDTH);
    }

    public void update() {
    }



}
