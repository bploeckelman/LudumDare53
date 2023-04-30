package lando.systems.ld53.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.kotcrab.vis.ui.widget.VisProgressBar;
import com.kotcrab.vis.ui.widget.VisWindow;
import lando.systems.ld53.Assets;
import lando.systems.ld53.Config;
import lando.systems.ld53.screens.GameScreen;
import lando.systems.ld53.utils.Utils;

public class TopGameUI extends Group {
    private final float UI_WIDTH = 600f;
    private final float UI_HEIGHT = 75f;
    private VisProgressBar staminaBar;

    public TopGameUI(GameScreen screen) {
        VisWindow window = new VisWindow("");
        window.background(Assets.Patch.glass.drawable);
        window.setPosition(Config.Screen.window_width / 2f - UI_WIDTH / 2f , Config.Screen.window_height - UI_HEIGHT);
        window.setSize(UI_WIDTH, UI_HEIGHT);
        addActor(window);

        VisProgressBar.ProgressBarStyle horizontalProgressBarStyle = screen.skin.get("default-horizontal", VisProgressBar.ProgressBarStyle.class);
        VisProgressBar.ProgressBarStyle staminaBarStyle = new VisProgressBar.ProgressBarStyle(horizontalProgressBarStyle);
        staminaBarStyle.knobAfter =  new TextureRegionDrawable(Utils.getColoredTextureRegion(Color.YELLOW));
        staminaBarStyle.knobBefore =  new TextureRegionDrawable(Utils.getColoredTextureRegion(Color.GREEN));
        staminaBar = new VisProgressBar(0f, 100f, .05f, false);
        staminaBar.setValue(100f);
        staminaBar.setStyle(staminaBarStyle);
        staminaBar.setPosition(30f, 2.5f);
        staminaBar.setSize(UI_WIDTH - 60f, UI_HEIGHT);
        window.addActor(staminaBar);
    }

    public void update(float staminaPercentage) {
        staminaBar.setValue(staminaPercentage);
    }



}
