package lando.systems.ld53;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.kotcrab.vis.ui.VisUI;
import de.damios.guacamole.gdx.graphics.NestableFrameBuffer;
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

    public BaseScreen screen;

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

        screen = new TitleScreen();
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
        screen.update(delta);
    }

    @Override
    public void render() {
        update(Time.delta);
        screen.render(assets.batch);
    }

    @Override
    public void dispose() {
        if (screen != null) {
            screen.dispose();
        }

        frameBuffer.dispose();
        assets.dispose();
    }

}
