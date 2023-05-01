package lando.systems.ld53.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.kotcrab.vis.ui.widget.VisProgressBar;
import lando.systems.ld53.Config;
import lando.systems.ld53.screens.GameScreen;
import lando.systems.ld53.utils.Utils;

public class TopGameUI extends Group {
    private final float UI_WIDTH = 300f;
    private final float UI_HEIGHT = 105f;
    private VisProgressBar staminaBar;

    public TopGameUI(GameScreen screen) {
        VisProgressBar.ProgressBarStyle horizontalProgressBarStyle = screen.skin.get("default-horizontal", VisProgressBar.ProgressBarStyle.class);
        VisProgressBar.ProgressBarStyle staminaBarStyle = new VisProgressBar.ProgressBarStyle(horizontalProgressBarStyle);
        staminaBarStyle.knobAfter =  new TextureRegionDrawable(Utils.getColoredTextureRegion(Color.YELLOW));
        staminaBarStyle.knobBefore =  new TextureRegionDrawable(Utils.getColoredTextureRegion(Color.GREEN));
        staminaBar = new VisProgressBar(0f, 100f, .05f, false);
        staminaBar.setValue(100f);
        staminaBar.setStyle(staminaBarStyle);
        staminaBar.setPosition(Config.Screen.window_width / 2f - UI_WIDTH / 2f, Config.Screen.window_height - UI_HEIGHT);
        staminaBar.setSize(UI_WIDTH, UI_HEIGHT);
        staminaBar.setHeight(UI_HEIGHT);
        addActor(staminaBar);
    }

    public void update(float staminaPercentage) {
        staminaBar.setValue(staminaPercentage);
    }



}
