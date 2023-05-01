package lando.systems.ld53.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.kotcrab.vis.ui.widget.VisImage;
import com.kotcrab.vis.ui.widget.VisLabel;
import lando.systems.ld53.Assets;
import lando.systems.ld53.Config;
import lando.systems.ld53.entities.PlayerAbility;
import lando.systems.ld53.screens.GameScreen;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BottomGameUI extends Table {
    private GameScreen screen;
    private static final float BUTTON_SIZE = 75f;
    private static final int BUTTON_COUNT = 5;
    private static final float X = Config.Screen.window_width / 2 - (BUTTON_COUNT * BUTTON_SIZE / 2);
    private static final float Y = 0f;
    private List<Table> buttonTables = new ArrayList();
    private List<PlayerAbility> abilityList = Arrays.asList(PlayerAbility.values());

    public BottomGameUI(GameScreen screen) {
        this.screen = screen;
        setPosition(X, Y);
        setSize(BUTTON_SIZE * BUTTON_COUNT, BUTTON_SIZE);
        for (int i = 0; i < BUTTON_COUNT; i++) {
            Table table = new Table();
            table.setBackground(Assets.Patch.glass.drawable);
            Stack stack = new Stack();
            VisImage icon = new VisImage(abilityList.get(i).textureRegion);
            stack.add(icon);
            VisLabel label = new VisLabel(String.valueOf(i + 1));
            label.setScale(2f);
            label.setAlignment(Align.topRight);
            stack.add(label);
            table.add(stack).growX().growY();
            buttonTables.add(table);
        }

        for (Table table : buttonTables) {
            add(table).size(BUTTON_SIZE);
        }

    }

    public void update(float delta) {
        for (Table table : buttonTables) {
            if (screen.player != null && abilityList.get(buttonTables.indexOf(table)) == screen.player.currentAbility) {
                table.setBackground(Assets.Patch.glass_yellow.drawable);
            } else {
                table.setBackground(Assets.Patch.glass.drawable);
            }
        }
    }
}
