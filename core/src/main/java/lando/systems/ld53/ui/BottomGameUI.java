package lando.systems.ld53.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
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
    public List<BottomGameUIButtons> buttonTables = new ArrayList();
    private List<PlayerAbility> abilityList = Arrays.asList(PlayerAbility.values());

    public BottomGameUI(GameScreen screen) {
        this.screen = screen;
        setPosition(X, Y);
        setSize(BUTTON_SIZE * BUTTON_COUNT, BUTTON_SIZE);
        for (int i = 0; i < BUTTON_COUNT; i++) {
            BottomGameUIButtons button = new BottomGameUIButtons(screen, i, abilityList);
            buttonTables.add(button);
        }

        for (Table table : buttonTables) {
            add(table).size(BUTTON_SIZE);
        }

    }

    public void update(float delta) {
        for (BottomGameUIButtons button : buttonTables) {
            if (abilityList.get(buttonTables.indexOf(button)) == screen.player.currentAbility) {
                button.setBackground(Assets.Patch.glass_yellow.drawable);
                button.lock.setVisible(false);
            }
            else if (!abilityList.get(buttonTables.indexOf(button)).isUnlocked) {
                button.setBackground(Assets.Patch.glass_dim.drawable);
                button.lock.setVisible(true);
            }
            else {
                button.setBackground(Assets.Patch.glass.drawable);
                button.lock.setVisible(false);
            }
        }
    }
}
