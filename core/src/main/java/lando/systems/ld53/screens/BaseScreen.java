package lando.systems.ld53.screens;

import aurelienribon.tweenengine.TweenManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.kotcrab.vis.ui.VisUI;
import lando.systems.ld53.Assets;
import lando.systems.ld53.Config;
import lando.systems.ld53.Main;
import lando.systems.ld53.utils.screenshake.CameraShaker;
import lombok.var;

public abstract class BaseScreen implements Disposable {

    public final Main game;
    public final Assets assets;
    public final TweenManager tween;
    public final SpriteBatch batch;
    public final Vector3 pointerPos;
    public final OrthographicCamera windowCamera;

    public OrthographicCamera worldCamera;
    public CameraShaker screenShaker;

    protected Stage uiStage;
    protected Skin skin;

    public BaseScreen() {
        this.game = Main.game;
        this.assets = game.assets;
        this.tween = game.tween;
        this.batch = assets.batch;
        this.pointerPos = new Vector3();
        this.windowCamera = game.windowCamera;

        this.worldCamera = new OrthographicCamera();
        this.screenShaker = new CameraShaker(worldCamera);

        worldCamera.setToOrtho(false, Config.Screen.window_width, Config.Screen.window_height);
        worldCamera.update();

        initializeUI();
    }

    @Override
    public void dispose() {}

    public void update(float delta) {
        windowCamera.update();
        if (worldCamera != null) {
            worldCamera.update();
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) {
            Config.Debug.general = !Config.Debug.general;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) {
            Config.Debug.ui = !Config.Debug.ui;
        }

        screenShaker.update(delta);
        uiStage.act(delta);
    }

    public void render(SpriteBatch batch) {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);

        batch.setProjectionMatrix(worldCamera.combined);
        batch.begin();
        {
            batch.setColor(Color.SKY);
            batch.draw(assets.pixelRegion, 0, 0, worldCamera.viewportWidth, worldCamera.viewportHeight);
            batch.setColor(Color.WHITE);
        }
        batch.end();

        uiStage.draw();
    }

    protected void initializeUI() {
        skin = VisUI.getSkin();

        StretchViewport viewport = new StretchViewport(windowCamera.viewportWidth, windowCamera.viewportHeight);
        uiStage = new Stage(viewport, batch);

        // extend and setup any per-screen ui widgets in here...
    }

}