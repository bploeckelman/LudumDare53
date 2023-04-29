package lando.systems.ld53.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import lando.systems.ld53.Config;

public class TitleScreen extends BaseScreen {

    private final Texture background;

    public TitleScreen() {
        super();

        worldCamera.setToOrtho(false, Config.Screen.window_width, Config.Screen.window_height);
        worldCamera.update();

        background = assets.gdx;
    }

    @Override
    public void update(float delta) {
        super.update(delta);

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);

        batch.setProjectionMatrix(worldCamera.combined);
        batch.begin();
        {
            float w = background.getWidth();
            float h = background.getHeight();
            float width = worldCamera.viewportWidth;
            float height = worldCamera.viewportHeight;
            batch.draw(background, (width - w) / 2f, (height - h) / 2f);
        }
        batch.end();

        uiStage.draw();
    }

}
