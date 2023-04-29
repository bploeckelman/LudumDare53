package lando.systems.ld53;

import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;
import aurelienribon.tweenengine.primitives.MutableFloat;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.ScreenUtils;
import com.kotcrab.vis.ui.VisUI;
import lando.systems.ld53.audio.AudioManager;
import lando.systems.ld53.screens.BaseScreen;
import lando.systems.ld53.screens.TitleScreen;
import lando.systems.ld53.utils.Time;
import lando.systems.ld53.utils.accessors.*;

public class Main extends ApplicationAdapter {

    public static Main game;

    public Assets assets;
    public TweenManager tween;
    public FrameBuffer frameBuffer;
    public TextureRegion frameBufferRegion;
    public OrthographicCamera windowCamera;
    public AudioManager audioManager;

    public BaseScreen currentScreen;
    private BaseScreen nextScreen;
    private MutableFloat transitionPercent;
    private FrameBuffer transitionFBO;
    private FrameBuffer originalFBO;
    Texture originalTexture;
    Texture transitionTexture;
    ShaderProgram transitionShader;
    boolean transitioning;

    public Main() {
        Main.game = this;
    }

    @Override
    public void create() {
        Time.init();

        assets = new Assets();

        VisUI.load(assets.mgr.get("ui/uiskin.json", Skin.class));
        Skin skin = VisUI.getSkin();
        skin.getFont("default")            .setUseIntegerPositions(false);
        skin.getFont("font")               .setUseIntegerPositions(false);
        skin.getFont("list")               .setUseIntegerPositions(false);
        skin.getFont("subtitle")           .setUseIntegerPositions(false);
        skin.getFont("window")             .setUseIntegerPositions(false);
        skin.getFont("outfit-medium-10px") .setUseIntegerPositions(false);
        skin.getFont("outfit-medium-14px") .setUseIntegerPositions(false);
        skin.getFont("outfit-medium-17px") .setUseIntegerPositions(false);
        skin.getFont("outfit-medium-19px") .setUseIntegerPositions(false);
        skin.getFont("outfit-medium-20px") .setUseIntegerPositions(false);
        skin.getFont("outfit-medium-40px") .setUseIntegerPositions(false);
        skin.getFont("outfit-medium-80px") .setUseIntegerPositions(false);

        tween = new TweenManager();
        Tween.setWaypointsLimit(4);
        Tween.setCombinedAttributesLimit(4);
        Tween.registerAccessor(Color.class, new ColorAccessor());
        Tween.registerAccessor(Rectangle.class, new RectangleAccessor());
        Tween.registerAccessor(Vector2.class, new Vector2Accessor());
        Tween.registerAccessor(Vector3.class, new Vector3Accessor());
        Tween.registerAccessor(OrthographicCamera.class, new CameraAccessor());

        audioManager = new AudioManager(assets, tween);

        Pixmap.Format format = Pixmap.Format.RGBA8888;
        int width = Config.Screen.framebuffer_width;
        int height = Config.Screen.framebuffer_height;

        boolean hasDepth = true;
        frameBuffer = new FrameBuffer(format, width, height, hasDepth);
        Texture frameBufferTexture = frameBuffer.getColorBufferTexture();
        frameBufferTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        frameBufferRegion = new TextureRegion(frameBufferTexture);
        frameBufferRegion.flip(false, true);

        windowCamera = new OrthographicCamera();
        windowCamera.setToOrtho(false, Config.Screen.window_width, Config.Screen.window_height);
        windowCamera.update();

        transitionPercent = new MutableFloat(0);
        transitionFBO = new FrameBuffer(Pixmap.Format.RGB888, Config.Screen.window_width, Config.Screen.window_height, false);
        transitionTexture = transitionFBO.getColorBufferTexture();

        originalFBO = new FrameBuffer(Pixmap.Format.RGB888, Config.Screen.window_width, Config.Screen.window_height, false);
        originalTexture = originalFBO.getColorBufferTexture();

        transitioning = false;


        currentScreen = new TitleScreen();
    }

    public void update(float delta) {
        // handle top level input
        if (Gdx.input.isKeyJustPressed(Input.Keys.F1)) Config.Debug.general = !Config.Debug.general;

        // update things that must update every tick
        Time.update();
        tween.update(Time.delta);

        // handle a pause
        if (Time.pause_timer > 0) {
            Time.pause_timer -= Time.delta;
            if (Time.pause_timer <= -0.0001f) {
                Time.delta = -Time.pause_timer;
            } else {
                // skip updates if we're paused
                return;
            }
        }
        Time.millis += Time.delta;
        Time.previous_elapsed = Time.elapsed_millis();

        // update systems
        currentScreen.update(delta);
    }

    @Override
    public void render() {
        update(Time.delta);

        ScreenUtils.clear(Color.DARK_GRAY);

        currentScreen.renderFrameBuffers(assets.batch);

        if (nextScreen != null) {
            nextScreen.update(Time.delta);
            nextScreen.renderFrameBuffers(assets.batch);
            transitionFBO.begin();
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            nextScreen.render(assets.batch);
            transitionFBO.end();

            originalFBO.begin();
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            currentScreen.render(assets.batch);
            originalFBO.end();

            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

            assets.batch.setShader(transitionShader);
            assets.batch.setProjectionMatrix(windowCamera.combined);
            assets.batch.begin();
            originalTexture.bind(1);
            transitionShader.setUniformi("u_texture1", 1);
            transitionTexture.bind(0);
            transitionShader.setUniformf("u_percent", transitionPercent.floatValue());
            assets.batch.setColor(Color.WHITE);
            assets.batch.draw(transitionTexture, 0,0, Config.Screen.window_width, Config.Screen.window_height);
            assets.batch.end();
            assets.batch.setShader(null);
        } else {
            currentScreen.render(assets.batch);
        }
    }

    @Override
    public void dispose() {
        if (currentScreen != null) {
            currentScreen.dispose();
        }

        frameBuffer.dispose();
        assets.dispose();
    }

    public void setScreen(BaseScreen currentScreen) {
        setScreen(currentScreen, null, .25f);
    }

    public void setScreen(final BaseScreen newScreen, ShaderProgram transitionType, float transitionSpeed) {
        if (nextScreen != null) return;
        if (transitioning) return; // only want one transition
        if (currentScreen == null) {
            currentScreen = newScreen;
        } else {
            transitioning = true;
            if (transitionType == null) {
                transitionShader = assets.randomTransitions.get(MathUtils.random(assets.randomTransitions.size - 1));
            } else {
                transitionShader = transitionType;
            }
            transitionPercent.setValue(0);
            Timeline.createSequence()
                .pushPause(.1f)
                .push(Tween.call((i, baseTween) -> nextScreen = newScreen))
                .push(Tween.to(transitionPercent, 1, transitionSpeed)
                    .target(1))
                .push(Tween.call((i, baseTween) -> {
                    currentScreen = nextScreen;
                    nextScreen = null;
                    transitioning = false;
                }))
                .start(tween);
        }
    }

    public BaseScreen getScreen() {
        return currentScreen;
    }

}
