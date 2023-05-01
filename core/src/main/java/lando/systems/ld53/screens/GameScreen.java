package lando.systems.ld53.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import lando.systems.ld53.Config;
import lando.systems.ld53.audio.AudioManager;
import lando.systems.ld53.entities.*;
import lando.systems.ld53.entities.enemies.Enemy;
import lando.systems.ld53.particles.Particles;
import lando.systems.ld53.physics.Collidable;
import lando.systems.ld53.physics.Influencer;
import lando.systems.ld53.physics.PhysicsSystem;
import lando.systems.ld53.ui.BottomGameUI;
import lando.systems.ld53.ui.SelectSkillUI;
import lando.systems.ld53.ui.TopGameUI;
import lando.systems.ld53.ui.TopTrapezoid;
import lando.systems.ld53.utils.screenshake.CameraShaker;
import lando.systems.ld53.world.Map;

import java.util.HashMap;

public class GameScreen extends BaseScreen {

    private Map map;
    public Array<Cargo> cargos;
    public Player player;
    public Array<EnemySpawner> enemySpawners;
    public Array<Enemy> enemies;
    private BulletEnemy bulletEnemy;
    public HashMap<Goal.Type, Integer> collectedMap = new HashMap<>();
    public HashMap<Goal.Type, Integer> needToCollectMap = new HashMap<>();

    public final Array<Bullet> bullets;
    public final Array<Bomb> bombs;

    private final PhysicsSystem physicsSystem;
    private final Array<Collidable> physicsObjects;
    public final Array<Influencer> influencers;

    private TopTrapezoid trapezoid;
    private SelectSkillUI selectSkillUI;
    private TopGameUI topGameUI;
    private BottomGameUI bottomGameUI;
    public boolean isSelectSkillUIShown = false;

    public boolean paused;
    public float spawnTimer;
    public Goal.Type lastSpawnedType;

    public Particles particles;

    public enum Levels {
        preview, level1, level2, level3
    }

    public GameScreen(Levels level) {
        super();
        collectedMap.put(Goal.Type.cyan, 0);
        collectedMap.put(Goal.Type.red, 0);
        collectedMap.put(Goal.Type.yellow, 0);
        collectedMap.put(Goal.Type.green, 0);

        // TODO: this should be based on the level
        needToCollectMap.put(Goal.Type.cyan, 3);
        needToCollectMap.put(Goal.Type.red, 3);
        needToCollectMap.put(Goal.Type.yellow, 5);
        needToCollectMap.put(Goal.Type.green, 3);

        worldCamera.setToOrtho(false, Config.Screen.framebuffer_width, Config.Screen.framebuffer_height);
        worldCamera.update();
        screenShaker = new CameraShaker(worldCamera);

        bullets = new Array<>();
        bombs = new Array<>();
        enemies = new Array<>();
        cargos = new Array<>();

        switch (level) {
            case preview:
                map = new Map(this, "maps/level1.tmx");
                PlayerAbility.bomb_throw.isUnlocked = true;
                PlayerAbility.speed_up.isUnlocked = true;
                PlayerAbility.shield_360.isUnlocked = true;
                PlayerAbility.repulse.isUnlocked = false;
                PlayerAbility.fetch.isUnlocked = false;
                break;
            case level1:
                map = new Map(this, "maps/level1.tmx");
                PlayerAbility.bomb_throw.isUnlocked = false;
                PlayerAbility.speed_up.isUnlocked = false;
                PlayerAbility.shield_360.isUnlocked = true;
                PlayerAbility.repulse.isUnlocked = false;
                PlayerAbility.fetch.isUnlocked = false;
                break;
            case level2:
                map = new Map(this, "maps/level2.tmx");
                PlayerAbility.bomb_throw.isUnlocked = true;
                PlayerAbility.speed_up.isUnlocked = true;
                PlayerAbility.shield_360.isUnlocked = true;
                PlayerAbility.repulse.isUnlocked = false;
                PlayerAbility.fetch.isUnlocked = false;
                break;
            case level3:
                map = new Map(this, "maps/level3.tmx");
                PlayerAbility.bomb_throw.isUnlocked = true;
                PlayerAbility.speed_up.isUnlocked = true;
                PlayerAbility.shield_360.isUnlocked = true;
                PlayerAbility.repulse.isUnlocked = true;
                PlayerAbility.fetch.isUnlocked = true;
                break;
        }
        player = new Player(this, Config.Screen.window_width / 2f, Config.Screen.window_height / 2f);

        bulletEnemy = new BulletEnemy(assets, this, 5, -100f);

        physicsObjects = new Array<>();
        physicsSystem = new PhysicsSystem(new Rectangle(0,0, Config.Screen.window_width, Config.Screen.window_height));

        influencers = new Array<>();
        influencers.addAll(map.goals);
        influencers.add(player.personalRepulsor);

        audioManager.playMusic(AudioManager.Musics.level1Full, true, true);
        this.spawnTimer = 5f;
        this.lastSpawnedType = Goal.Type.yellow;
//        audioManager.playMusic(AudioManager.Musics.level1Full);
//        audioManager.playSound(AudioManager.Sounds.coin);
        trapezoid = new TopTrapezoid(player, assets);
        selectSkillUI = new SelectSkillUI(this);
        uiStage.addActor(selectSkillUI);
//        uiStage.addListener(new InputListener() {
//            @Override
//            public boolean keyDown(InputEvent event, int keycode) {
//                if (isSelectSkillUIShown) {
//                    if (keycode == Input.Keys.D) {
//                        selectSkillUI.showNextSkill();
//                    }
//                    else if (keycode == Input.Keys.A) {
//                        selectSkillUI.showPreviousSkill();
//                    }
//                    else if (keycode == Input.Keys.ESCAPE) {
//                        isSelectSkillUIShown = false;
//                        selectSkillUI.show(false);
//                        swapMusic();
//                        Gdx.input.setInputProcessor(null);
//                    }
//                    else if (keycode == Input.Keys.ENTER || keycode == Input.Keys.SPACE || keycode == Input.Keys.E) {
//                        IndividualSkillUI ui = selectSkillUI.skillUIBeingShown;
//                        if (ui.ability.isUnlocked) {
//                            player.currentAbility = ui.ability;
//                            hideSkillUI();
//                            swapMusic();
//                            return true;
//                        } else {
//                            ui.lock.addAction(ui.shakeLocker());
//                        }
//
//                    }
//                }
//                return super.keyUp(event, keycode);
//            }
//        });

        particles = new Particles(assets);
        Gdx.input.setInputProcessor(uiStage);
    }

//    public void hideSkillUI() {
//        isSelectSkillUIShown = false;
//        selectSkillUI.show(false);
//    }

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

    public boolean isLevelDone(){
        boolean completed = true;
        for (Goal.Type type : Goal.Type.values()){
           if (collectedMap.get(type) < needToCollectMap.get(type)) completed = false;
        }
        return completed;
    }

    @Override
    public void update(float delta) {
        super.update(delta);

        boolean pauseGame = paused || isSelectSkillUIShown;



//        if(!isSelectSkillUIShown) {
//            if (Gdx.input.isKeyJustPressed(Input.Keys.I) || Gdx.input.isKeyJustPressed(Input.Keys.Q)) {
//                if (!isSelectSkillUIShown) {
//                    swapMusic();
//                    isSelectSkillUIShown = !isSelectSkillUIShown;
//                    selectSkillUI.show(isSelectSkillUIShown);
//                    Gdx.input.setInputProcessor(uiStage);
//                    return;
//                }
//            }
//
//            if (Gdx.input.isKeyJustPressed(Input.Keys.O)) {
//                if (!isSelectSkillUIShown) {
//                    audioManager.stopMusic();
//                    game.setScreen(new TitleScreen());
//                    return;
//                }
//            }
//        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1) && player.currentAbility != player.abilityList.get(0)) {
            if (player.abilityList.get(0).isUnlocked) {
                selectSkillUI.autoScrollToSkillInit(0);
                player.currentAbility = player.abilityList.get(0);
            } else {
                bottomGameUI.buttonTables.get(0).shakeLocker();
            }
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2) && player.currentAbility != player.abilityList.get(1)) {
            if (player.abilityList.get(1).isUnlocked) {
                selectSkillUI.autoScrollToSkillInit(1);
                player.currentAbility = player.abilityList.get(1);
            } else {
                bottomGameUI.buttonTables.get(1).lock.addAction(bottomGameUI.buttonTables.get(0).shakeLocker());
            }
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3) && player.currentAbility != player.abilityList.get(2)) {
            if (player.abilityList.get(2).isUnlocked) {
                selectSkillUI.autoScrollToSkillInit(2);
                player.currentAbility = player.abilityList.get(2);
            } else {
                bottomGameUI.buttonTables.get(2).lock.addAction(bottomGameUI.buttonTables.get(0).shakeLocker());
            }
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_4) && player.currentAbility != player.abilityList.get(3)) {
            if (player.abilityList.get(3).isUnlocked) {
                selectSkillUI.autoScrollToSkillInit(3);
                player.currentAbility = player.abilityList.get(3);
            } else {
                bottomGameUI.buttonTables.get(3).lock.addAction(bottomGameUI.buttonTables.get(0).shakeLocker());
            }
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_5) && player.currentAbility != player.abilityList.get(4)) {
            if (player.abilityList.get(4).isUnlocked) {
                selectSkillUI.autoScrollToSkillInit(4);
                player.currentAbility = player.abilityList.get(4);
            } else {
                bottomGameUI.buttonTables.get(4).lock.addAction(bottomGameUI.buttonTables.get(0).shakeLocker());
                selectSkillUI.abilityUIMap.get(player.abilityList.get(4)).lock.addAction(selectSkillUI.abilityUIMap.get(player.abilityList.get(4)).shakeLocker());
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
        if (selectSkillUI.isAutoScrolling) {
            selectSkillUI.autoScrollUpdate(delta);
        }

        if (pauseGame) return;



        physicsObjects.clear();

        physicsObjects.addAll(map.wallSegments);
        physicsObjects.addAll(map.pegs);
        physicsObjects.addAll(bullets);
        physicsObjects.addAll(bombs);
        physicsObjects.addAll(cargos);
        physicsObjects.addAll(enemies);
        physicsObjects.add(player);

        if (player.fetcher.isActive()) {
            physicsObjects.add(player.fetcher);
        }

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

        for (CargoSpawner cargoSpawner : map.cargoSpawners) {
            cargoSpawner.update(delta);
        }

        for (Cargo cargo : cargos) {
            cargo.update(delta);
        }

        for (EnemySpawner enemySpawner : map.enemySpawners) {
            enemySpawner.update(delta);
        }

        for (Enemy enemy : enemies) {
            enemy.update(delta);
        }

        bulletEnemy.update(delta);
        player.update(delta);
        map.update(delta);

        for (Goal goal : map.goals){
            for (int i = cargos.size -1; i >= 0; i--){
                Cargo cargo = cargos.get(i);
                goal.tryToCollectPackage(cargo);
                if (cargo.collected) {
                    cargos.removeIndex(i);
                    if (collectedMap.get(cargo.goalType) < needToCollectMap.get(cargo.goalType)){
                        collectedMap.put(cargo.goalType, collectedMap.get(cargo.goalType) + 1);
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
        particles.update(delta);
    }

    @Override
    public void render(SpriteBatch batch) {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);

        map.render(batch, (OrthographicCamera) screenShaker.getViewCamera());

        batch.setProjectionMatrix(screenShaker.getCombinedMatrix());
        batch.begin();
        {
            particles.draw(batch, Particles.Layer.background);
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

            particles.draw(batch, Particles.Layer.middle);

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

            particles.draw(batch, Particles.Layer.foreground);

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
        uiStage.getViewport().setCamera(screenShaker.getViewCamera());
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
