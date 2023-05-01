package lando.systems.ld53.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import lando.systems.ld53.Config;
import lando.systems.ld53.audio.AudioManager;
import lando.systems.ld53.entities.*;
import lando.systems.ld53.entities.enemies.*;
import lando.systems.ld53.physics.Collidable;
import lando.systems.ld53.physics.Influencer;
import lando.systems.ld53.physics.PhysicsSystem;
import lando.systems.ld53.physics.test.TestBall;
import lando.systems.ld53.ui.*;
import lando.systems.ld53.utils.screenshake.CameraShaker;
import lando.systems.ld53.world.Map;

public class GameScreen extends BaseScreen {

    private Map map;
    public Array<Cargo> cargos;
    public Player player;
    private Array<Enemy> enemies;
    private BulletEnemy bulletEnemy;
    public int numberOfPackagesToCollect = 3; // can be broken out to each color if needed
    public int redCollected = 0;
    public int yellowCollected = 0;
    public int greenCollected = 0;
    public int blueCollected = 0;

    public final Array<Bullet> bullets;
    public final Array<Bomb> bombs;

    private final PhysicsSystem physicsSystem;
    private final Array<Collidable> physicsObjects;
    public final Array<Influencer> influencers;
    private final Array<TestBall> testBalls;

    private TopTrapezoid trapezoid;
    private SelectSkillUI selectSkillUI;
    private TopGameUI topGameUI;
    private BottomGameUI bottomGameUI;
    public boolean isSelectSkillUIShown = false;

    public boolean paused;
    public float spawnTimer;
    public Goal.Type lastSpawnedType;

    public GameScreen() {
        super();

        worldCamera.setToOrtho(false, Config.Screen.framebuffer_width, Config.Screen.framebuffer_height);
        worldCamera.update();
        screenShaker = new CameraShaker(worldCamera);

        bullets = new Array<>();
        bombs = new Array<>();
        enemies = new Array<>();

        map = new Map("maps/level1.tmx");
        player = new Player(assets, Config.Screen.window_width / 2f, Config.Screen.window_height / 2f);

        Enemy enemy = new CargoEatingEnemy(this, worldCamera.viewportWidth / 2f - 200f, worldCamera.viewportHeight * (1f / 3f));
        enemies.add(enemy);
        enemy = new RepulseMineEnemy(this, 200, 600);
        enemies.add(enemy);
        enemy = new AttractMineEnemy(this, 200, 300);
        enemies.add(enemy);
        enemy = new InvaderEnemy(this, 200, 300);
        enemies.add(enemy);


        Cargo cargo = new Cargo(assets, Goal.Type.green, worldCamera.viewportWidth / 2f, worldCamera.viewportHeight * (2f / 3f));
        cargos = new Array<>();
        cargos.add(cargo);
        bulletEnemy = new BulletEnemy(assets, this, 5, -100f);

        physicsObjects = new Array<>();
        physicsSystem = new PhysicsSystem(new Rectangle(0,0, Config.Screen.window_width, Config.Screen.window_height));

        testBalls = new Array<>();
        // to hell with your invisible balls!
//        for (int i = 0; i < 100; i++){
//            Vector2 pos = new Vector2(Gdx.graphics.getWidth() * MathUtils.random(.2f, .8f), Gdx.graphics.getHeight() * MathUtils.random(.2f, .5f));
//            Vector2 vel = new Vector2(MathUtils.random(-60f, 60f), MathUtils.random(-60f, 60f));
//            testBalls.add(new TestBall(pos, vel));
//        }

        influencers = new Array<>();
        influencers.addAll(map.goals);
        influencers.add(player.personalRepulsor);
//        influencers.add(new TestAttractor(new Vector2(400, 500)));
//        influencers.add(new TestRepulser(new Vector2(700, 450)));

        audioManager.playMusic(AudioManager.Musics.level1Full, true, true);
        this.spawnTimer = 5f;
        this.lastSpawnedType = Goal.Type.yellow;
//        audioManager.playMusic(AudioManager.Musics.level1Full);
//        audioManager.playSound(AudioManager.Sounds.coin);
        trapezoid = new TopTrapezoid(player, assets);
        selectSkillUI = new SelectSkillUI(this);
        uiStage.addActor(selectSkillUI);
        uiStage.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (isSelectSkillUIShown) {
                    if (keycode == Input.Keys.D) {
                        selectSkillUI.showNextSkill();
                    }
                    else if (keycode == Input.Keys.A) {
                        selectSkillUI.showPreviousSkill();
                    }
                    else if (keycode == Input.Keys.ESCAPE) {
                        isSelectSkillUIShown = false;
                        selectSkillUI.show(false);
                        swapMusic();
                        Gdx.input.setInputProcessor(null);
                    }
                    else if (keycode == Input.Keys.ENTER || keycode == Input.Keys.SPACE || keycode == Input.Keys.E) {
                        IndividualSkillUI ui = selectSkillUI.skillUIBeingShown;
                        if (ui.ability.isUnlocked) {
                            player.currentAbility = ui.ability;
                            hideSkillUI();
                            swapMusic();
                            return true;
                        } else {
                            ui.lock.addAction(ui.shakeLocker());
                        }

                    }
                }
                return super.keyUp(event, keycode);
            }
        });

        Gdx.input.setInputProcessor(uiStage);
    }

    public void hideSkillUI() {
        isSelectSkillUIShown = false;
        selectSkillUI.show(false);

    }

    public void swapMusic() {
        if(assets.level1Full.isPlaying()) {
            assets.level1Thin.setLooping(true);
            assets.level1Thin.play();
            assets.level1Thin.setVolume(audioManager.musicVolume.floatValue());
            assets.level1Thin.setPosition(assets.level1Full.getPosition());
            assets.level1Full.stop();

        }
        else if(assets.level1Thin.isPlaying()) {
            assets.level1Full.setLooping(true);
            assets.level1Full.play();
            assets.level1Full.setVolume(audioManager.musicVolume.floatValue());
            assets.level1Full.setPosition(assets.level1Thin.getPosition());
            assets.level1Thin.stop();
        }
    }

    @Override
    public void update(float delta) {
        super.update(delta);

        boolean pauseGame = paused || isSelectSkillUIShown;



        if(!isSelectSkillUIShown) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.I) || Gdx.input.isKeyJustPressed(Input.Keys.Q)) {
                if (!isSelectSkillUIShown) {
                    swapMusic();
                    isSelectSkillUIShown = !isSelectSkillUIShown;
                    selectSkillUI.show(isSelectSkillUIShown);
                    Gdx.input.setInputProcessor(uiStage);
                    return;
                }
            }

            if (Gdx.input.isKeyJustPressed(Input.Keys.O)) {
                if (!isSelectSkillUIShown) {
                    audioManager.stopMusic();
                    game.setScreen(new TitleScreen());
                    return;
                }
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_6)) {
            swapMusic();
//            if(assets.level1Full.isPlaying()) {
//                assets.level1Thin.setLooping(true);
//                assets.level1Thin.play();
//                assets.level1Thin.setVolume(audioManager.musicVolume.floatValue());
//                assets.level1Thin.setPosition(assets.level1Full.getPosition());
//                assets.level1Full.stop();
//
//            }
//            else if(assets.level1Thin.isPlaying()) {
//                assets.level1Full.setLooping(true);
//                assets.level1Full.play();
//                assets.level1Full.setVolume(audioManager.musicVolume.floatValue());
//                assets.level1Full.setPosition(assets.level1Thin.getPosition());
//                assets.level1Thin.stop();
//            }
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_7)) {
            cargos.add(new Cargo(assets, Goal.Type.red, worldCamera.viewportWidth / 2f, worldCamera.viewportHeight * (2f / 3f)));
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_8)) {
            cargos.add(new Cargo(assets, Goal.Type.cyan, worldCamera.viewportWidth / 2f, worldCamera.viewportHeight * (2f / 3f)));
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_9)) {
            cargos.add(new Cargo(assets, Goal.Type.green, worldCamera.viewportWidth / 2f, worldCamera.viewportHeight * (2f / 3f)));
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_0)) {
            cargos.add(new Cargo(assets, Goal.Type.yellow, worldCamera.viewportWidth / 2f, worldCamera.viewportHeight * (2f / 3f)));
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
            paused = !paused;
        }

        topGameUI.update(delta);
        bottomGameUI.update(delta);

        if (pauseGame) return;

        spawnTimer -= delta;
        if(spawnTimer < 0) {

            Goal.Type newSpawnType = Goal.Type.getRandom(lastSpawnedType);
            lastSpawnedType = newSpawnType;
            cargos.add(new Cargo(assets, newSpawnType, worldCamera.viewportWidth / 2f, worldCamera.viewportHeight * (2f / 3f)));
            spawnTimer = 5f;
            audioManager.playSound(AudioManager.Sounds.spawn, .26f);
        }

        physicsObjects.clear();

        physicsObjects.addAll(map.wallSegments);
        physicsObjects.addAll(map.pegs);
        physicsObjects.addAll(testBalls);
        physicsObjects.addAll(bullets);
        physicsObjects.addAll(bombs);
        physicsObjects.addAll(cargos);
        physicsObjects.add(player);
        physicsObjects.addAll(enemies);

        for (Bomb bomb : bombs) {
            if (!influencers.contains(bomb.repulsor, true)) {
                influencers.add(bomb.repulsor);
            }
        }

        physicsSystem.update(delta, physicsObjects, influencers);

        for (int i = bullets.size - 1; i >= 0; i--) {
            Bullet bullet = bullets.get(i);
            bullet.update(delta);

            boolean isDead = !bullet.alive;
            boolean isOffscreen = !bullet.isInside(0, 0, worldCamera.viewportWidth, worldCamera.viewportHeight);
            if (isDead || isOffscreen) {
                Bullet.pool.free(bullet);
                bullets.removeIndex(i);
            }
        }

        for (int i = bombs.size - 1; i >= 0; i--) {
            Bomb bomb = bombs.get(i);
            bomb.update(delta);
            if (!bomb.alive) {
                bombs.removeIndex(i);
                influencers.removeValue(bomb.repulsor, true);
            }
        }

        for (Cargo cargo : cargos) {
            cargo.update(delta);
        }

        bulletEnemy.update(delta);
        for (Enemy enemy : enemies) {
            enemy.update(delta);
        }
        player.update(delta);
        map.update(delta);
        for (Goal goal : map.goals){
            for (int i = cargos.size -1; i >= 0; i--){
                Cargo cargo = cargos.get(i);
                goal.tryToCollectPackage(cargo);
                if (cargo.collected) {
                    cargos.removeIndex(i);
                    switch (cargo.goalType) {
                        case red:
                            if (redCollected < numberOfPackagesToCollect) {
                                redCollected++;
                            }
                            break;
                        case yellow:
                            if (yellowCollected < numberOfPackagesToCollect) {
                                yellowCollected++;
                            }                            break;
                        case green:
                            if (greenCollected < numberOfPackagesToCollect) {
                                greenCollected++;
                            }
                            break;
                        case cyan:
                            if (blueCollected < numberOfPackagesToCollect) {
                                blueCollected++;
                            }
                            break;
                    }
                }
                else if (cargo.lifetime <= 0) {
                    cargos.removeIndex(i);
                }
            }
        }

        for (Influencer influencer : influencers){
            influencer.updateInfluence(delta);
        }

        trapezoid.update();
        uiStage.setDebugAll(Config.Debug.ui);

        screenShaker.update(delta);
    }

    @Override
    public void render(SpriteBatch batch) {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);

        map.render(batch, (OrthographicCamera) screenShaker.getViewCamera());

        batch.setProjectionMatrix(screenShaker.getCombinedMatrix());
        batch.begin();
        {
            for (Influencer influencer : influencers){
                influencer.renderInfluence(batch);
            }
            // goals before players/enemies
            for (Goal goal : map.goals) {
                goal.render(batch);
            }

            for (WallSegment segment : map.wallSegments) {
                segment.render(batch);
            }

            // players/enemies
            bulletEnemy.render(batch);
            for (Enemy enemy : enemies) {
                enemy.render(batch);
            }
            player.render(batch);

            // pegs after players/enemies
            for (Peg peg : map.pegs) {
                peg.render(batch);
            }

            // moving stuff after everything
            for (Bomb bomb : bombs) {
                bomb.render(batch);
            }

            for (Cargo cargo : cargos){
                cargo.render(batch);
            }

            for (Bullet bullet : bullets) {
                bullet.render(batch);
            }

            // debug drawing
            if (Config.Debug.general){
                for (Collidable collidable : physicsObjects) {
                    collidable.renderDebug(assets.shapes);
                }
                for (Influencer i : influencers) {
                    i.debugRender(batch);
                }
            }
        }
        batch.end();
        trapezoid.render(batch);
        uiStage.draw();
    }

    @Override
    public void initializeUI() {
        super.initializeUI();
        topGameUI = new TopGameUI(this);
        bottomGameUI = new BottomGameUI(this);
        uiStage.addActor(topGameUI);
        uiStage.addActor(bottomGameUI);
    }

}
