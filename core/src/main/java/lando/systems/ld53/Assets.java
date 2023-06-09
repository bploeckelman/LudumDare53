package lando.systems.ld53;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.I18NBundle;
import lando.systems.ld53.assets.InputPrompts;
import lando.systems.ld53.entities.Goal;
import lando.systems.ld53.entities.PlayerAbility;
import space.earlygrey.shapedrawer.ShapeDrawer;
import text.formic.Stringf;

public class Assets implements Disposable {

    public enum Load { ASYNC, SYNC }

    public boolean initialized;

    public SpriteBatch batch;
    public ShapeDrawer shapes;
    public GlyphLayout layout;
    public GlyphLayout genieLayout;
    public AssetManager mgr;
    public TextureAtlas atlas;
    public I18NBundle strings;
    public Particles particles;
    public Array<Animation<TextureRegion>> numberParticles;


    public BitmapFont font;
    public BitmapFont smallFont;
    public BitmapFont largeFont;

    public Texture pixel;
    public Texture gdx;
    public Texture titleScreen;
    public Texture titleMask;
    public Texture trapezoidTexture;
    public Texture trapezoidBorder;

    public Animation<TextureRegion> titleGenie;
    public Animation<TextureRegion> titlePc;
    public Animation<TextureRegion> titleSleep;
    public Animation<TextureRegion> titleTextCrunchTime;
    public Animation<TextureRegion> titleTextGame;
    public Animation<TextureRegion> titleTextGenie;
    public Animation<TextureRegion> titleZZZ;

    public TextureRegion ring;
    public TextureRegion lock;
    public TextureRegion pixelRegion;

    public Animation<TextureRegion> asuka;
    public Animation<TextureRegion> cherry;
    public Animation<TextureRegion> osha;

    public Animation<TextureRegion> playerIdleDown;
    public Animation<TextureRegion> playerIdleUp;
    public Animation<TextureRegion> playerIdleLeft;
    public Animation<TextureRegion> playerIdleRight;
    public Animation<TextureRegion> playerWalkLeft;
    public Animation<TextureRegion> playerWalkRight;
    public Animation<TextureRegion> playerWalkUp;
    public Animation<TextureRegion> playerWalkDown;
    public Animation<TextureRegion> playerSlashLeft;
    public Animation<TextureRegion> playerSlashRight;
    public Animation<TextureRegion> playerSlashUp;
    public Animation<TextureRegion> playerSlashDown;
    public Animation<TextureRegion> playerSlash360;
    public Animation<TextureRegion> playerSlashOverlayLeft;
    public Animation<TextureRegion> playerSlashOverlayRight;
    public Animation<TextureRegion> playerSlashOverlayUp;
    public Animation<TextureRegion> playerSlashOverlayDown;
    public Animation<TextureRegion> playerSlashOverlay360;
    public Animation<TextureRegion> playerStun;
    public Animation<TextureRegion> playerTalk;

    public Animation<TextureRegion> bullet;
    public Animation<TextureRegion> ball;
    public Animation<TextureRegion> cargoRed;
    public Animation<TextureRegion> cargoCyan;
    public Animation<TextureRegion> cargoYellow;
    public Animation<TextureRegion> cargoGreen;
    public Animation<TextureRegion> peg;
    public Animation<TextureRegion> bomb;
    public Animation<TextureRegion> shield;
    public Animation<TextureRegion> abilityIcons;

    public Animation<TextureRegion> gobbler;
    public Animation<TextureRegion> thief;
    public Animation<TextureRegion> reapo;
    public Animation<TextureRegion> turtle;
    public Animation<TextureRegion> etWalk;
    public Animation<TextureRegion> etFloat;
    public Animation<TextureRegion> bossIdle;
    public Animation<TextureRegion> bossShoot;

    public Animation<TextureRegion> goalPanelBase;
//    public Animation<TextureRegion> goalPanelColor;
    public Animation<TextureRegion> goalShimmer;

    public Array<ShaderProgram> randomTransitions;
    public ShaderProgram starWarsShader;
    public ShaderProgram blindsShader;
    public ShaderProgram fadeShader;
    public ShaderProgram radialShader;
    public ShaderProgram doomShader;
    public ShaderProgram pizelizeShader;
    public ShaderProgram doorwayShader;
    public ShaderProgram crosshatchShader;
    public ShaderProgram rippleShader;
    public ShaderProgram heartShader;
    public ShaderProgram stereoShader;
    public ShaderProgram circleCropShader;
    public ShaderProgram cubeShader;
    public ShaderProgram dreamyShader;
    public ShaderProgram flameShader;
    public ShaderProgram cooldownShader;
    public ShaderProgram influencerShader;
    public ShaderProgram goalShader;

    public InputPrompts inputPrompts;

    public Music level1Thin;
    public Music level1Full;
    public Music introMusic;

    public Sound coin1;
    public Sound swoosh1;
    public Sound bigSwoosh1;
    public Sound grunt1;
    public Sound grunt2;
    public Sound grunt3;
    public Sound grunt4;
    public Sound grunt5;
    public Sound grunt6;
    public Sound grunt7;
    public Sound pop1;
    public Sound pop2;
    public Sound pop3;
    public Sound pop4;
    public Sound impact1;
    public Sound error1;
    public Sound thud1;
    public Sound bodyHit;
    public Sound gobble1;
    public Sound gobble2;
    public Sound gobble3;
    public Sound gobble4;
    public Sound gobble5;
    public Sound ticktock;
    public Sound zap1;
    public Sound zap2;
    public Sound zap3;
    public Sound zap4;
    public Sound zap5;
    public Sound zap6;
    public Sound zap7;
    public Sound zap8;
    public Sound zap9;
    public Sound zap10;
    public Sound zap11;
    public Sound zap12;
    public Sound collect1;
    public Sound cannon1;
    public Sound spawn1;
    public Sound giggle1;
    public Sound giggle2;
    public Sound giggle3;
    public Sound giggle4;
    public Sound giggle5;

    public enum Patch {
        debug, panel, metal, glass,
        glass_green, glass_yellow, glass_red, glass_blue, glass_dim, glass_active;
        public NinePatch ninePatch;
        public NinePatchDrawable drawable;
    }

    public static class Particles {
        public TextureRegion circle;
        public TextureRegion sparkle;
        public TextureRegion smoke;
        public TextureRegion ring;
        public TextureRegion dollar;
        public TextureRegion blood;
        public TextureRegion sparks;
        public TextureRegion line;
    }

    public static class NinePatches {
        public static NinePatch plain;
        public static NinePatch plain_dim;
        public static NinePatch plain_gradient;
        public static NinePatch plain_gradient_highlight_yellow;
        public static NinePatch plain_gradient_highlight_green;
        public static NinePatch plain_gradient_highlight_red;
        public static NinePatch glass;
        public static NinePatch glass_active;
        public static NinePatch glass_blue;
        public static NinePatch glass_light_blue;
        public static NinePatch glass_corner_bl;
        public static NinePatch glass_corner_br;
        public static NinePatch glass_corner_tl;
        public static NinePatch glass_corner_tr;
        public static NinePatch glass_corners;
        public static NinePatch glass_red;
        public static NinePatch glass_yellow;
        public static NinePatch glass_green;
        public static NinePatch glass_tab;
        public static NinePatch glass_dim;
        public static NinePatch metal;
        public static NinePatch metal_blue;
        public static NinePatch metal_green;
        public static NinePatch metal_yellow;
        public static NinePatch shear;
    }

    public Assets() {
        this(Load.SYNC);
    }

    public Assets(Load load) {
        initialized = false;

        // create a single pixel texture and associated region
        Pixmap pixmap = new Pixmap(2, 2, Pixmap.Format.RGBA8888);
        {
            pixmap.setColor(Color.WHITE);
            pixmap.drawPixel(0, 0);
            pixmap.drawPixel(1, 0);
            pixmap.drawPixel(0, 1);
            pixmap.drawPixel(1, 1);
            pixel = new Texture(pixmap);
        }
        pixmap.dispose();
        pixelRegion = new TextureRegion(pixel);

        batch = new SpriteBatch();
        shapes = new ShapeDrawer(batch, pixelRegion);
        layout = new GlyphLayout();
        genieLayout = new GlyphLayout();

        mgr = new AssetManager();
        {
            mgr.load("sprites/sprites.atlas", TextureAtlas.class);
            mgr.load("ui/uiskin.json", Skin.class);
            mgr.load("i18n/strings", I18NBundle.class);

            mgr.load("images/libgdx.png", Texture.class);
            mgr.load("images/title-screen.png", Texture.class);
            mgr.load("images/trapezoid.png", Texture.class);
            mgr.load("images/trapezoid-border.png", Texture.class);

            mgr.load("images/title-blank/title-blank_00.png", Texture.class);
            mgr.load("images/title-blank/title-top-mask_00.png", Texture.class);

            mgr.load("fonts/outfit-medium-20px.fnt", BitmapFont.class);
            mgr.load("fonts/outfit-medium-40px.fnt", BitmapFont.class);
            mgr.load("fonts/outfit-medium-80px.fnt", BitmapFont.class);

            mgr.load("audio/sounds/coin1.ogg", Sound.class);
            mgr.load("audio/sounds/swoosh1.ogg", Sound.class);
            mgr.load("audio/sounds/bigswoosh1.ogg", Sound.class);
            mgr.load("audio/sounds/grunt1.ogg", Sound.class);
            mgr.load("audio/sounds/grunt2.ogg", Sound.class);
            mgr.load("audio/sounds/grunt3.ogg", Sound.class);
            mgr.load("audio/sounds/grunt4.ogg", Sound.class);
            mgr.load("audio/sounds/grunt5.ogg", Sound.class);
            mgr.load("audio/sounds/grunt6.ogg", Sound.class);
            mgr.load("audio/sounds/grunt7.ogg", Sound.class);
            mgr.load("audio/sounds/pop1.ogg", Sound.class);
            mgr.load("audio/sounds/pop2.ogg", Sound.class);
            mgr.load("audio/sounds/pop3.ogg", Sound.class);
            mgr.load("audio/sounds/pop4.ogg", Sound.class);
            mgr.load("audio/sounds/impact1.ogg", Sound.class);
            mgr.load("audio/sounds/error1.ogg", Sound.class);
            mgr.load("audio/sounds/thud1.ogg", Sound.class);
            mgr.load("audio/sounds/bodyhit1.ogg", Sound.class);
            mgr.load("audio/sounds/gobble1.ogg", Sound.class);
            mgr.load("audio/sounds/gobble2.ogg", Sound.class);
            mgr.load("audio/sounds/gobble3.ogg", Sound.class);
            mgr.load("audio/sounds/gobble4.ogg", Sound.class);
            mgr.load("audio/sounds/gobble5.ogg", Sound.class);
            mgr.load("audio/sounds/ticktock1.ogg", Sound.class);
            mgr.load("audio/sounds/zap1.ogg", Sound.class);
            mgr.load("audio/sounds/zap2.ogg", Sound.class);
            mgr.load("audio/sounds/zap3.ogg", Sound.class);
            mgr.load("audio/sounds/zap4.ogg", Sound.class);
            mgr.load("audio/sounds/zap5.ogg", Sound.class);
            mgr.load("audio/sounds/zap6.ogg", Sound.class);
            mgr.load("audio/sounds/zap7.ogg", Sound.class);
            mgr.load("audio/sounds/zap8.ogg", Sound.class);
            mgr.load("audio/sounds/zap9.ogg", Sound.class);
            mgr.load("audio/sounds/zap10.ogg", Sound.class);
            mgr.load("audio/sounds/zap11.ogg", Sound.class);
            mgr.load("audio/sounds/zap12.ogg", Sound.class);
            mgr.load("audio/sounds/collect1.ogg", Sound.class);
            mgr.load("audio/sounds/cannon1.ogg", Sound.class);
            mgr.load("audio/sounds/spawn1.ogg", Sound.class);
            mgr.load("audio/sounds/giggle1.ogg", Sound.class);
            mgr.load("audio/sounds/giggle2.ogg", Sound.class);
            mgr.load("audio/sounds/giggle3.ogg", Sound.class);
            mgr.load("audio/sounds/giggle4.ogg", Sound.class);
            mgr.load("audio/sounds/giggle5.ogg", Sound.class);

            mgr.load("audio/music/level1-thin.ogg", Music.class);
            mgr.load("audio/music/level1-full.ogg", Music.class);
            mgr.load("audio/music/introMusic.ogg", Music.class);
        }

        if (load == Load.SYNC) {
            mgr.finishLoading();
            updateLoading();
        }
    }

    public float updateLoading() {
        if (!mgr.update()) return mgr.getProgress();
        if (initialized) return 1;

        atlas = mgr.get("sprites/sprites.atlas");


        ring = atlas.findRegion("ring");

        strings = mgr.get("i18n/strings", I18NBundle.class);

        gdx = mgr.get("images/libgdx.png");
        titleScreen = mgr.get("images/title-blank/title-blank_00.png", Texture.class);
        titleMask = mgr.get("images/title-blank/title-top-mask_00.png", Texture.class);

        trapezoidBorder = mgr.get("images/trapezoid-border.png", Texture.class);
        trapezoidTexture = mgr.get("images/trapezoid.png", Texture.class);

        smallFont = mgr.get("fonts/outfit-medium-20px.fnt");
        font      = mgr.get("fonts/outfit-medium-40px.fnt");
        font.setUseIntegerPositions(false);
        largeFont = mgr.get("fonts/outfit-medium-80px.fnt");

        inputPrompts = new InputPrompts(this);
        lock = new TextureRegion(atlas.findRegion("icons/lock"));

        cherry = new Animation<>(0.1f, atlas.findRegions("pets/cat"), Animation.PlayMode.LOOP);
        asuka = new Animation<>(0.1f, atlas.findRegions("pets/dog"), Animation.PlayMode.LOOP);
        osha = new Animation<>(.1f, atlas.findRegions("pets/kitten"), Animation.PlayMode.LOOP);

        titleGenie = new Animation<>(.15f, atlas.findRegions("title-screen/title-genie/title-genie"), Animation.PlayMode.LOOP);
        titlePc = new Animation<>(.15f, atlas.findRegions("title-screen/title-pc/title-pc"), Animation.PlayMode.LOOP);
        titleSleep = new Animation<>(.2f, atlas.findRegions("title-screen/title-sleep/title-sleep"), Animation.PlayMode.LOOP);
        titleTextCrunchTime = new Animation<>(.1f, atlas.findRegions("title-screen/title-text-crunchtime/title-text-crunchtime"), Animation.PlayMode.LOOP);
        titleTextGame = new Animation<>(.1f, atlas.findRegions("title-screen/title-text-game/title-text-game"), Animation.PlayMode.LOOP);
        titleTextGenie = new Animation<>(.1f, atlas.findRegions("title-screen/title-text-genie/title-text-genie"), Animation.PlayMode.LOOP);
        titleZZZ = new Animation<>(.1f, atlas.findRegions("title-screen/title-zzz/title-zzz"), Animation.PlayMode.LOOP);

        playerIdleDown   = new Animation<>(.1f, atlas.findRegions("player/jeff-idle-down/jeff-idle-down"), Animation.PlayMode.LOOP);
        playerIdleUp     = new Animation<>(.1f, atlas.findRegions("player/jeff-idle-up/jeff-idle-up"), Animation.PlayMode.LOOP);
        playerIdleLeft   = new Animation<>(.1f, atlas.findRegions("player/jeff-idle-left/jeff-idle-left"), Animation.PlayMode.LOOP);
        playerIdleRight  = new Animation<>(.1f, atlas.findRegions("player/jeff-idle-right/jeff-idle-right"), Animation.PlayMode.LOOP);
        playerWalkLeft   = new Animation<>(.1f, atlas.findRegions("player/jeff-walk-left/jeff-walk-left"), Animation.PlayMode.LOOP);
        playerWalkRight  = new Animation<>(.1f, atlas.findRegions("player/jeff-walk-right/jeff-walk-right"), Animation.PlayMode.LOOP);
        playerWalkUp     = new Animation<>(.1f, atlas.findRegions("player/jeff-walk-up/jeff-walk-up"), Animation.PlayMode.LOOP);
        playerWalkDown   = new Animation<>(.1f, atlas.findRegions("player/jeff-walk-down/jeff-walk-down"), Animation.PlayMode.LOOP);
        playerSlashLeft  = new Animation<>(.04f, atlas.findRegions("player/jeff-slash-left/jeff-slash-left"), Animation.PlayMode.NORMAL);
        playerSlashRight = new Animation<>(.04f, atlas.findRegions("player/jeff-slash-right/jeff-slash-right"), Animation.PlayMode.NORMAL);
        playerSlashUp    = new Animation<>(.04f, atlas.findRegions("player/jeff-slash-up/jeff-slash-up"), Animation.PlayMode.NORMAL);
        playerSlashDown  = new Animation<>(.04f, atlas.findRegions("player/jeff-slash-down/jeff-slash-down"), Animation.PlayMode.NORMAL);
        playerSlash360   = new Animation<>(.066f, atlas.findRegions("player/jeff-slash-360/jeff-slash-360"), Animation.PlayMode.NORMAL);
        playerStun       = new Animation<>(.02f, atlas.findRegions("player/jeff-stun/jeff-stun"), Animation.PlayMode.NORMAL);
        playerTalk       = new Animation<>(.1f,  atlas.findRegions("player/jeff-talking/jeff-talking"), Animation.PlayMode.LOOP);
        playerSlashOverlayLeft  = new Animation<>(.04f, atlas.findRegions("player/swipes/swipe-left"), Animation.PlayMode.NORMAL);
        playerSlashOverlayRight = new Animation<>(.04f, atlas.findRegions("player/swipes/swipe-right"), Animation.PlayMode.NORMAL);
        playerSlashOverlayUp    = new Animation<>(.04f, atlas.findRegions("player/swipes/swipe-up"), Animation.PlayMode.NORMAL);
        playerSlashOverlayDown  = new Animation<>(.04f, atlas.findRegions("player/swipes/swipe-down"), Animation.PlayMode.NORMAL);
        playerSlashOverlay360   = new Animation<>(.066f, atlas.findRegions("player/zero-360/zero-360"), Animation.PlayMode.NORMAL);


        Array<TextureRegion> regions = new Array<>();
        regions.addAll(
              inputPrompts.get(InputPrompts.Type.light_circle_center)
            , inputPrompts.get(InputPrompts.Type.light_circle_up)
            , inputPrompts.get(InputPrompts.Type.light_circle_right)
            , inputPrompts.get(InputPrompts.Type.light_circle_down)
            , inputPrompts.get(InputPrompts.Type.light_circle_left)
        );
        bullet = new Animation<>(0.08f, regions, Animation.PlayMode.LOOP);
        regions.clear();
        regions.addAll(
              inputPrompts.get(InputPrompts.Type.yellow_arrow_circle_right)
            , inputPrompts.get(InputPrompts.Type.yellow_arrow_circle_down)
            , inputPrompts.get(InputPrompts.Type.yellow_arrow_circle_left)
            , inputPrompts.get(InputPrompts.Type.yellow_arrow_circle_up)
        );
//        ball = new Animation<>(0.07f, regions, Animation.PlayMode.LOOP);
//        ball = new Animation<>(0.07f, atlas.findRegions("objects/cargo-cyan/cargo-cyan"), Animation.PlayMode.LOOP);
//        ball = new Animation<>(0.07f, atlas.findRegions("objects/cargo-yellow/cargo-yellow"), Animation.PlayMode.LOOP);
        ball = new Animation<>(0.07f, atlas.findRegions("objects/cargo-red/cargo-red"), Animation.PlayMode.LOOP);
        cargoRed = new Animation<>(0.07f, atlas.findRegions("objects/cargo-red/cargo-red"), Animation.PlayMode.LOOP);
        cargoYellow = new Animation<>(0.07f, atlas.findRegions("objects/cargo-yellow/cargo-yellow"), Animation.PlayMode.LOOP);
        cargoCyan = new Animation<>(0.07f, atlas.findRegions("objects/cargo-cyan/cargo-cyan"), Animation.PlayMode.LOOP);
        cargoGreen = new Animation<>(0.07f, atlas.findRegions("objects/cargo-green/cargo-green"), Animation.PlayMode.LOOP);

        peg = new Animation<>(0.1f, atlas.findRegions("objects/peg/spinner"), Animation.PlayMode.LOOP);
        bomb = new Animation<>(0.1f, atlas.findRegions("objects/bomb/bomb"), Animation.PlayMode.NORMAL);
        shield = new Animation<>(0.1f, atlas.findRegions("objects/shield/shield"), Animation.PlayMode.LOOP);
        abilityIcons = new Animation<>(1f, atlas.findRegions("objects/ability-icons/ability-icon"), Animation.PlayMode.NORMAL);

        gobbler = new Animation<>(0.1f, atlas.findRegions("creatures/gobbler/gobbler-idle"), Animation.PlayMode.LOOP_PINGPONG);
        thief = new Animation<>(0.1f, atlas.findRegions("creatures/thief/thief"), Animation.PlayMode.LOOP);
        reapo = new Animation<>(0.1f, atlas.findRegions("creatures/reapo-man/reapo-idle-a"), Animation.PlayMode.LOOP);
        turtle = new Animation<>(0.1f, atlas.findRegions("creatures/turtle/turtle"), Animation.PlayMode.LOOP);
        etWalk = new Animation<>(0.1f, atlas.findRegions("creatures/et/et-walk"), Animation.PlayMode.LOOP);
        etFloat = new Animation<>(0.1f, atlas.findRegions("creatures/et/et-float"), Animation.PlayMode.LOOP_PINGPONG);
        bossIdle = new Animation<>(0.1f, atlas.findRegions("creatures/chromo-boss/boss-idle-a"), Animation.PlayMode.LOOP);
        bossShoot = new Animation<>(0.1f, atlas.findRegions("creatures/chromo-boss/boss-attack-punch"), Animation.PlayMode.NORMAL);

        goalPanelBase = new Animation<>(0.1f, atlas.findRegions("objects/goal-base"), Animation.PlayMode.LOOP);
        goalShimmer = new Animation<>(0.1f, atlas.findRegions("objects/goal-shimmer"), Animation.PlayMode.LOOP);


        // initialize Goal animations for each type
        for (Goal.Type type : Goal.Type.values()) {
            String regionsPath = Stringf.format("objects/goal-panel-%s/goal-panel-%s", type.name(), type.name());
            type.anim = new Animation<>(0.1f, atlas.findRegions(regionsPath), Animation.PlayMode.LOOP);
            type.baseAnim = new Animation<>(0.2f, atlas.findRegions("objects/goal-base/goal-base"), Animation.PlayMode.LOOP);
            type.shimmerAnim = new Animation<>(0.2f, atlas.findRegions("objects/goal-shimmer/goal-shimmer"), Animation.PlayMode.LOOP);
            switch(type) {
                case red:
                    type.icon = cargoRed.getKeyFrame(1);
                    type.iconAnimation = cargoRed;
                    break;
                case cyan:
                    type.icon = cargoCyan.getKeyFrame(0);
                    type.iconAnimation = cargoCyan;
                    break;
                case green:
                    type.icon = cargoGreen.getKeyFrame(0);
                    type.iconAnimation = cargoGreen;
                    break;
                case yellow:
                    type.icon = cargoYellow.getKeyFrame(0);
                    type.iconAnimation = cargoYellow;
                    break;
                default:
                    break;
            }
        }

        // initialize player ability textureRegion values
//        PlayerAbility.bomb_throw.textureRegion = new TextureRegion(bomb.getKeyFrame(0));

        PlayerAbility.speed_up.textureRegion = new TextureRegion(abilityIcons.getKeyFrame(0));
        PlayerAbility.shield_360.textureRegion = new TextureRegion(abilityIcons.getKeyFrame(1f));
        PlayerAbility.repulse.textureRegion = new TextureRegion(abilityIcons.getKeyFrame(2f));
        PlayerAbility.bomb_throw.textureRegion = new TextureRegion(abilityIcons.getKeyFrame(3));;
        PlayerAbility.fetch.textureRegion = new TextureRegion(asuka.getKeyFrame(0));


        // initialize patch values
        Patch.debug.ninePatch        = new NinePatch(atlas.findRegion("ninepatch/debug"), 2, 2, 2, 2);
        Patch.panel.ninePatch        = new NinePatch(atlas.findRegion("ninepatch/panel"), 15, 15, 15, 15);
        Patch.glass.ninePatch        = new NinePatch(atlas.findRegion("ninepatch/glass"), 8, 8, 8, 8);
        Patch.glass_green.ninePatch  = new NinePatch(atlas.findRegion("ninepatch/glass-green"), 8, 8, 8, 8);
        Patch.glass_yellow.ninePatch = new NinePatch(atlas.findRegion("ninepatch/glass-yellow"), 8, 8, 8, 8);
        Patch.glass_red.ninePatch  = new NinePatch(atlas.findRegion("ninepatch/glass-red"), 8, 8, 8, 8);
        Patch.glass_blue.ninePatch = new NinePatch(atlas.findRegion("ninepatch/glass-blue"), 8, 8, 8, 8);
        Patch.glass_dim.ninePatch    = new NinePatch(atlas.findRegion("ninepatch/glass-dim"), 8, 8, 8, 8);
        Patch.glass_active.ninePatch = new NinePatch(atlas.findRegion("ninepatch/glass-active"), 8, 8, 8, 8);
        Patch.metal.ninePatch        = new NinePatch(atlas.findRegion("ninepatch/metal"), 12, 12, 12, 12);

        Patch.debug.drawable        = new NinePatchDrawable(Patch.debug.ninePatch);
        Patch.panel.drawable        = new NinePatchDrawable(Patch.panel.ninePatch);
        Patch.glass.drawable        = new NinePatchDrawable(Patch.glass.ninePatch);
        Patch.glass_green.drawable  = new NinePatchDrawable(Patch.glass_green.ninePatch);
        Patch.glass_yellow.drawable = new NinePatchDrawable(Patch.glass_yellow.ninePatch);
        Patch.glass_dim.drawable    = new NinePatchDrawable(Patch.glass_dim.ninePatch);
        Patch.glass_active.drawable = new NinePatchDrawable(Patch.glass_active.ninePatch);
        Patch.metal.drawable        = new NinePatchDrawable(Patch.metal.ninePatch);
        Patch.glass_red.drawable    = new NinePatchDrawable(Patch.glass_red.ninePatch);
        Patch.glass_blue.drawable   = new NinePatchDrawable(Patch.glass_blue.ninePatch);

        NinePatches.plain_dim                       = new NinePatch(atlas.findRegion("ninepatch/plain-dim"),               12, 12, 12, 12);
        NinePatches.plain_gradient                  = new NinePatch(atlas.findRegion("ninepatch/plain-gradient"),           2,  2,  2,  2);
        NinePatches.plain_gradient_highlight_yellow = new NinePatch(atlas.findRegion("ninepatch/plain-gradient-highlight-yellow"), 2,  2,  2,  2);
        NinePatches.plain_gradient_highlight_green  = new NinePatch(atlas.findRegion("ninepatch/plain-gradient-highlight-green"), 2,  2,  2,  2);
        NinePatches.plain_gradient_highlight_red    = new NinePatch(atlas.findRegion("ninepatch/plain-gradient-highlight-red"), 2,  2,  2,  2);
        NinePatches.glass                           = new NinePatch(atlas.findRegion("ninepatch/glass"),                   12, 12, 12, 12);
        NinePatches.glass_blue                      = new NinePatch(atlas.findRegion("ninepatch/glass-blue"),              12, 12, 12, 12);
        NinePatches.glass_light_blue                = new NinePatch(atlas.findRegion("ninepatch/glass"),                   12, 12, 12, 12);
        NinePatches.glass_active                    = new NinePatch(atlas.findRegion("ninepatch/glass-active"),            12, 12, 12, 12);
        NinePatches.glass_corner_bl                 = new NinePatch(atlas.findRegion("ninepatch/glass-corner-bl"),         12, 12, 12, 12);
        NinePatches.glass_corner_br                 = new NinePatch(atlas.findRegion("ninepatch/glass-corner-br"),         12, 12, 12, 12);
        NinePatches.glass_corner_tl                 = new NinePatch(atlas.findRegion("ninepatch/glass-corner-tl"),         12, 12, 12, 12);
        NinePatches.glass_corner_tr                 = new NinePatch(atlas.findRegion("ninepatch/glass-corner-tr"),         12, 12, 12, 12);
        NinePatches.glass_corners                   = new NinePatch(atlas.findRegion("ninepatch/glass-corners"),           12, 12, 12, 12);
        NinePatches.glass_red                       = new NinePatch(atlas.findRegion("ninepatch/glass-red"),               12, 12, 12, 12);
        NinePatches.glass_yellow                    = new NinePatch(atlas.findRegion("ninepatch/glass-yellow"),            12, 12, 12, 12);
        NinePatches.glass_green                     = new NinePatch(atlas.findRegion("ninepatch/glass-green"),             12, 12, 12, 12);
        NinePatches.glass_tab                       = new NinePatch(atlas.findRegion("ninepatch/glass-tab"),               12, 12, 22, 12);
        NinePatches.glass_dim                       = new NinePatch(atlas.findRegion("ninepatch/glass-dim"),               12, 12, 22, 12);
        NinePatches.metal                           = new NinePatch(atlas.findRegion("ninepatch/metal"),                   12, 12, 12, 12);
        NinePatches.metal_blue                      = new NinePatch(atlas.findRegion("ninepatch/metal-blue"),              12, 12, 12, 12);
        NinePatches.metal_green                     = new NinePatch(atlas.findRegion("ninepatch/metal-green"),             12, 12, 12, 12);
        NinePatches.metal_yellow                    = new NinePatch(atlas.findRegion("ninepatch/metal-yellow"),            12, 12, 12, 12);
        NinePatches.shear                           = new NinePatch(atlas.findRegion("ninepatch/shear"),                   75, 75, 12, 12);

        // Particles
        // initialize particle images
        particles = new Particles();
        particles.circle  = atlas.findRegion("particles/circle");
        particles.ring    = atlas.findRegion("particles/ring");
        particles.smoke   = atlas.findRegion("particles/smoke");
        particles.sparkle = atlas.findRegion("particles/sparkle");
        particles.dollar  = atlas.findRegion("particles/dollars");
        particles.blood   = atlas.findRegion("characters/blood-stain");
        particles.sparks  = atlas.findRegion("particles/sparks");
        particles.line    = atlas.findRegion("particles/line");
        numberParticles = new Array<>();
        for (int i = 0; i <= 9; ++i) {
            numberParticles.add(new Animation<>(0.1f, atlas.findRegions("particles/font-points-" + i)));
        }

        //Shaders
        randomTransitions = new Array<>();
        blindsShader = loadShader("shaders/transitions/default.vert", "shaders/transitions/blinds.frag");
        fadeShader = loadShader("shaders/transitions/default.vert", "shaders/transitions/dissolve.frag");
        radialShader = loadShader("shaders/transitions/default.vert", "shaders/transitions/radial.frag");
        doomShader = loadShader("shaders/transitions/default.vert", "shaders/transitions/doomdrip.frag");
        pizelizeShader = loadShader("shaders/transitions/default.vert", "shaders/transitions/pixelize.frag");
        doorwayShader = loadShader("shaders/transitions/default.vert", "shaders/transitions/doorway.frag");
        crosshatchShader = loadShader("shaders/transitions/default.vert", "shaders/transitions/crosshatch.frag");
        rippleShader = loadShader("shaders/transitions/default.vert", "shaders/transitions/ripple.frag");
        heartShader = loadShader("shaders/transitions/default.vert", "shaders/transitions/heart.frag");
        stereoShader = loadShader("shaders/transitions/default.vert", "shaders/transitions/stereo.frag");
        circleCropShader = loadShader("shaders/transitions/default.vert", "shaders/transitions/circlecrop.frag");
        cubeShader = loadShader("shaders/transitions/default.vert", "shaders/transitions/cube.frag");
        dreamyShader = loadShader("shaders/transitions/default.vert", "shaders/transitions/dreamy.frag");
        flameShader = loadShader("shaders/default.vert", "shaders/flame.frag");
        starWarsShader = loadShader("shaders/default.vert", "shaders/starwars.frag");
        cooldownShader = loadShader("shaders/default.vert", "shaders/cooldown.frag");
        influencerShader = loadShader("shaders/default.vert", "shaders/influencer.frag");
        goalShader = loadShader("shaders/default.vert", "shaders/goal.frag");

        randomTransitions.add(radialShader);
        randomTransitions.add(pizelizeShader);

        // Sounds
        coin1 = mgr.get("audio/sounds/coin1.ogg", Sound.class);
        swoosh1 = mgr.get("audio/sounds/swoosh1.ogg", Sound.class);
        bigSwoosh1 = mgr.get("audio/sounds/bigswoosh1.ogg", Sound.class);
        grunt1 = mgr.get("audio/sounds/grunt1.ogg", Sound.class);
        grunt2 = mgr.get("audio/sounds/grunt2.ogg", Sound.class);
        grunt3 = mgr.get("audio/sounds/grunt3.ogg", Sound.class);
        grunt4 = mgr.get("audio/sounds/grunt4.ogg", Sound.class);
        grunt5 = mgr.get("audio/sounds/grunt5.ogg", Sound.class);
        grunt6 = mgr.get("audio/sounds/grunt6.ogg", Sound.class);
        grunt7 = mgr.get("audio/sounds/grunt7.ogg", Sound.class);
        pop1 = mgr.get("audio/sounds/pop1.ogg", Sound.class);
        pop2 = mgr.get("audio/sounds/pop2.ogg", Sound.class);
        pop3 = mgr.get("audio/sounds/pop3.ogg", Sound.class);
        pop4 = mgr.get("audio/sounds/pop4.ogg", Sound.class);
        impact1 = mgr.get("audio/sounds/impact1.ogg", Sound.class);
        error1 = mgr.get("audio/sounds/error1.ogg", Sound.class);
        thud1 = mgr.get("audio/sounds/thud1.ogg", Sound.class);
        bodyHit = mgr.get("audio/sounds/bodyhit1.ogg", Sound.class);
        gobble1 = mgr.get("audio/sounds/gobble1.ogg", Sound.class);
        gobble2 = mgr.get("audio/sounds/gobble2.ogg", Sound.class);
        gobble3 = mgr.get("audio/sounds/gobble3.ogg", Sound.class);
        gobble4 = mgr.get("audio/sounds/gobble4.ogg", Sound.class);
        gobble5 = mgr.get("audio/sounds/gobble5.ogg", Sound.class);
        ticktock = mgr.get("audio/sounds/ticktock1.ogg", Sound.class);
         zap1 = mgr.get("audio/sounds/zap1.ogg", Sound.class);
         zap2 = mgr.get("audio/sounds/zap2.ogg", Sound.class);
         zap3 = mgr.get("audio/sounds/zap3.ogg", Sound.class);
         zap4 = mgr.get("audio/sounds/zap4.ogg", Sound.class);
         zap5 = mgr.get("audio/sounds/zap5.ogg", Sound.class);
         zap6 = mgr.get("audio/sounds/zap6.ogg", Sound.class);
         zap7 = mgr.get("audio/sounds/zap7.ogg", Sound.class);
         zap8 = mgr.get("audio/sounds/zap8.ogg", Sound.class);
         zap9 = mgr.get("audio/sounds/zap9.ogg", Sound.class);
         zap10 = mgr.get("audio/sounds/zap10.ogg", Sound.class);
         zap11 = mgr.get("audio/sounds/zap11.ogg", Sound.class);
         zap12 = mgr.get("audio/sounds/zap12.ogg", Sound.class);
         collect1 = mgr.get("audio/sounds/collect1.ogg", Sound.class);
         cannon1 = mgr.get("audio/sounds/cannon1.ogg", Sound.class);
         spawn1 = mgr.get("audio/sounds/spawn1.ogg", Sound.class);
         giggle1 = mgr.get("audio/sounds/giggle1.ogg", Sound.class);
         giggle2 = mgr.get("audio/sounds/giggle2.ogg", Sound.class);
         giggle3 = mgr.get("audio/sounds/giggle3.ogg", Sound.class);
         giggle4 = mgr.get("audio/sounds/giggle4.ogg", Sound.class);
         giggle5 = mgr.get("audio/sounds/giggle5.ogg", Sound.class);
//         = mgr.get("audio/sounds/.ogg", Sound.class);

        // Music
        level1Thin = mgr.get("audio/music/level1-thin.ogg", Music.class);
        level1Full = mgr.get("audio/music/level1-full.ogg", Music.class);
        introMusic = mgr.get("audio/music/introMusic.ogg", Music.class);

        initialized = true;
        return 1;
    }

    @Override
    public void dispose() {
        mgr.dispose();
        batch.dispose();
        pixel.dispose();
    }

    public static ShaderProgram loadShader(String vertSourcePath, String fragSourcePath) {
        ShaderProgram.pedantic = false;
        ShaderProgram shaderProgram = new ShaderProgram(
            Gdx.files.internal(vertSourcePath),
            Gdx.files.internal(fragSourcePath));
        String log = shaderProgram.getLog();

        if (!shaderProgram.isCompiled()) {
            Gdx.app.error("LoadShader", "compilation failed:\n" + log);
            throw new GdxRuntimeException("LoadShader: compilation failed:\n" + log);
        } else if (Config.Debug.shaders) {
            Gdx.app.debug("LoadShader", "ShaderProgram compilation log: " + log);
        }

        return shaderProgram;
    }

}
