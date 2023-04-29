package lando.systems.ld53.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import lando.systems.ld53.Main;

public class LaunchScreen extends BaseScreen {

    public LaunchScreen() {
        super();
    }

    public void update(float dt) {
        if (!exitingScreen && Gdx.input.justTouched()){
            exitingScreen = true;
            game.setScreen(new TitleScreen());
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        ScreenUtils.clear(Color.SKY);
        OrthographicCamera camera = windowCamera;
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        {
            assets.font.getData().setScale(2f);
            assets.layout.setText(assets.font, "Click to play", Color.WHITE, camera.viewportWidth, Align.center, false);
            assets.font.draw(batch, assets.layout, 0, camera.viewportHeight / 2f + assets.layout.height);
            assets.font.getData().setScale(1f);
        }
        batch.end();
    }
}
