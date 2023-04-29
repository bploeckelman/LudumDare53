package lando.systems.ld53.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ScreenUtils;
import lando.systems.ld53.Config;

public class TitleScreen extends BaseScreen {

    private final Texture background;
    private TextureRegion dog;
    private TextureRegion cat;
    private TextureRegion kitten;
    private float stateTime;

    public TitleScreen() {
        super();

        worldCamera.setToOrtho(false, Config.Screen.window_width, Config.Screen.window_height);
        worldCamera.update();

        background = assets.gdx;

    }

    @Override
    public void update(float delta) {
        super.update(delta);

        stateTime += delta;
        dog = assets.asuka.getKeyFrame(stateTime);
        cat = assets.cherry.getKeyFrame(stateTime);
        kitten = assets.osha.getKeyFrame(stateTime);
        if (!dog.isFlipX()) dog.flip(true, false);
        if (!cat.isFlipX()) cat.flip(true, false);
        if (!kitten.isFlipX()) kitten.flip(true, false);

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

            batch.draw(dog,
                 width / 2,  height * 2 / 3,
                    2 * dog.getRegionWidth(), 2 * dog.getRegionHeight());
            batch.draw(cat,
                width / 3,  height * 2 / 3,
                    2 * cat.getRegionWidth(), 2 * cat.getRegionHeight());
            batch.draw(kitten,
                width * 2 / 3,  height * 2 / 3 - 10f,
                3 * cat.getRegionWidth(), 3 * cat.getRegionHeight());
        }

        batch.end();

        uiStage.draw();
    }

}
