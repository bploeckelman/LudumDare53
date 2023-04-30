package lando.systems.ld53.ui;

import com.badlogic.gdx.math.Vector2;
import com.kotcrab.vis.ui.widget.VisWindow;
import lando.systems.ld53.Assets;
import lando.systems.ld53.Config;

public class EmptyWindow extends VisWindow {
    private final Vector2 WINDOW_SIZE = new Vector2(Config.Screen.window_width / 3, Config.Screen.window_height * 2 / 3);
    private final Vector2 WINDOW_POSITION = new Vector2(Config.Screen.window_width / 3, Config.Screen.window_height / 6);
    private final float OFFSET = 50f;
    public EmptyWindow(int indexOffset) {
        super("");
        setBackground(Assets.Patch.metal.drawable);
        setSize(WINDOW_SIZE.x - OFFSET * Math.abs(indexOffset)  , WINDOW_SIZE.y - OFFSET * Math.abs(indexOffset));
        float x = indexOffset > 0 ? WINDOW_POSITION.x + indexOffset * 2 * OFFSET : WINDOW_POSITION.x + indexOffset * OFFSET;
        setPosition(x, WINDOW_POSITION.y + Math.abs(indexOffset) * OFFSET / 2);
    }

    public void update() {

    }
}
