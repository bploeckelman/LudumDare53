package lando.systems.ld53.screens;

import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import lando.systems.ld53.Config;
import lando.systems.ld53.ui.TitleScreenUI;
import lando.systems.ld53.utils.accessors.Vector2Accessor;

public class TitleScreen extends BaseScreen {

    private final Texture background;
    private Vector2 titlePcPos;
    private Vector2 titleGeniePos;
    private Vector2 titleSleepPos;
    private Vector2 titleTextCrunchTimePos;
    private Vector2 titleTextGamePos;
    private Vector2 titleTextGeniePos;
    private Vector2 titleZZZPos;


    private TextureRegion titlePc = assets.titlePc.getKeyFrame(0f);
    private Animation<TextureRegion> titleGenieAnim = assets.titleGenie;
    private TextureRegion titleGenieFrame;
    private Animation<TextureRegion> titleSleepAnim = assets.titleSleep;
    private TextureRegion titleSleepFrame;
    private TextureRegion titleTextCrunchTime = assets.titleTextCrunchTime.getKeyFrame(0f);
    private TextureRegion titleTextGame = assets.titleTextGame.getKeyFrame(0f);
    private TextureRegion titleTextGenie = assets.titleTextGenie.getKeyFrame(0f);
    private TextureRegion titleZZZ = assets.titleZZZ.getKeyFrame(0f);
    private boolean drawUI = false;

    private float stateTime;

    public TitleScreen() {
        super();
        titlePcPos = new Vector2(0, 700);
        titleSleepPos = new Vector2(500, 0);
        titleTextCrunchTimePos = new Vector2(930, 250);
        titleTextGamePos = new Vector2(170, 430);
        titleTextGeniePos = new Vector2(670, 435);
        titleGeniePos = new Vector2(550, 180);
        titleZZZPos = new Vector2(0, 0);
        worldCamera.setToOrtho(false, Config.Screen.window_width, Config.Screen.window_height);
        worldCamera.update();
        Gdx.input.setInputProcessor(uiStage);
        background = assets.titleScreen;
    }

    @Override
    public void update(float delta) {
        super.update(delta);

        stateTime += delta;
        titleSleepFrame = titleSleepAnim.getKeyFrame(stateTime);
        titleGenieFrame = titleGenieAnim.getKeyFrame(stateTime);

        Timeline.createSequence()
            .push(Tween.to(titlePcPos, Vector2Accessor.Y, .2f)
                .target(-120f))
            .push(Tween.to(titleSleepPos, Vector2Accessor.X, .2f)
                .target(titleSleepPos.y))
            .pushPause(.5f)
            .push(Tween.call((type, source) -> {
                drawUI = true;
            }))
            .start(tween);

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
            float width = worldCamera.viewportWidth;
            float height = worldCamera.viewportHeight;
            batch.draw(background, 0, 0, width, height);
            batch.draw(titlePc, titlePcPos.x, titlePcPos.y, titlePc.getRegionWidth(), titlePc.getRegionHeight());
            batch.draw(titleSleepFrame, titleSleepPos.x, titleSleepPos.y, titleSleepFrame.getRegionWidth(), titleSleepFrame.getRegionHeight());
            batch.draw(titleGenieFrame, titleGeniePos.x, titleGeniePos.y, titleGenieFrame.getRegionWidth(), titleGenieFrame.getRegionHeight());
            batch.draw(titleTextGame, titleTextGamePos.x, titleTextGamePos.y, titleTextGame.getRegionWidth(), titleTextGame.getRegionHeight());
            batch.draw(titleTextGenie, titleTextGeniePos.x, titleTextGeniePos.y, titleTextGenie.getRegionWidth(), titleTextGenie.getRegionHeight());
            batch.draw(titleTextCrunchTime, titleTextCrunchTimePos.x, titleTextCrunchTimePos.y, titleTextCrunchTime.getRegionWidth(), titleTextCrunchTime.getRegionHeight());

        }

        batch.end();
        if (drawUI) {
            uiStage.draw();
        }
    }

    @Override
    public void initializeUI() {
        super.initializeUI();
        TitleScreenUI titleScreenUI = new TitleScreenUI(this);
        uiStage.addActor(titleScreenUI);
    }

}
