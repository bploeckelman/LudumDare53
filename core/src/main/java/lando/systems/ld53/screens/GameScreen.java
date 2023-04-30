package lando.systems.ld53.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import lando.systems.ld53.Config;
import lando.systems.ld53.audio.AudioManager;
import lando.systems.ld53.entities.*;
import lando.systems.ld53.physics.Collidable;
import lando.systems.ld53.physics.PhysicsSystem;
import lando.systems.ld53.physics.test.TestBall;
import lando.systems.ld53.ui.TopGameUI;
import lando.systems.ld53.world.Map;

public class GameScreen extends BaseScreen {

    private Map map;
    private Ball ball;
    private Player player;
    private Enemy enemy;
    private BulletEnemy bulletEnemy;

    public final Array<Bullet> bullets;
    private PhysicsSystem physicsSystem;
    private Array<Collidable> physicsObjects;
    private TopGameUI topGameUI;

    Array<TestBall> testBalls;

    public GameScreen() {
        super();

        worldCamera.setToOrtho(false, Config.Screen.framebuffer_width, Config.Screen.framebuffer_height);
        worldCamera.update();

        map = new Map("maps/test-80x80.tmx");
        ball = new Ball(assets, worldCamera.viewportWidth / 2f, worldCamera.viewportHeight * (2f / 3f));
        player = new Player(assets);
        enemy = new Enemy(assets, player.position.x - 200f, player.position.y + 80f);
        bulletEnemy = new BulletEnemy(assets, this, 5, -100f);
        bullets = new Array<>();

        Gdx.input.setInputProcessor(uiStage);
        physicsObjects = new Array<>();
        physicsSystem = new PhysicsSystem(new Rectangle(0,0, Config.Screen.window_width, Config.Screen.window_height));

        testBalls = new Array<>();
        for (int i = 0; i < 200; i++){
            Vector2 pos = new Vector2(Gdx.graphics.getWidth() * MathUtils.random(.2f, .8f), Gdx.graphics.getHeight() * MathUtils.random(.2f, .5f));
            Vector2 vel = new Vector2(MathUtils.random(-60f, 60f), MathUtils.random(-60f, 60f));
            testBalls.add(new TestBall(pos, vel));
        }

        audioManager.playMusic(AudioManager.Musics.level1Thin);
//        audioManager.playMusic(AudioManager.Musics.level1Full);
//        audioManager.playSound(AudioManager.Sounds.coin);
    }

    @Override
    public void update(float delta) {
        super.update(delta);

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            audioManager.stopMusic();
            game.setScreen(new TitleScreen());
            return;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_6)) {
            if(assets.level1Full.isPlaying()) {
                assets.level1Thin.play();
                assets.level1Thin.setPosition(assets.level1Full.getPosition());
                assets.level1Full.stop();
            }
            else if(assets.level1Thin.isPlaying()) {
                assets.level1Full.play();
                assets.level1Full.setPosition(assets.level1Thin.getPosition());
                assets.level1Thin.stop();
            }

        }

        physicsObjects.clear();

        physicsObjects.addAll(map.wallSegments);
        physicsObjects.addAll(map.pegs);
        physicsObjects.addAll(testBalls);
        physicsObjects.addAll(bullets);
        physicsObjects.add(ball);
        physicsObjects.add(player);
        physicsObjects.add(enemy);

        physicsSystem.update(delta, physicsObjects);

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

        for (Peg peg : map.pegs) {
            peg.update(delta);
        }

        ball.update(delta);
        bulletEnemy.update(delta);
        enemy.update(delta);
        player.update(delta);
        map.update(delta);
        topGameUI.update(player.getStaminaPercentage());
    }

    @Override
    public void render(SpriteBatch batch) {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);

        map.render(batch, worldCamera);

        batch.setProjectionMatrix(worldCamera.combined);
        batch.begin();
        {
            for (Peg peg : map.pegs) {
                peg.render(batch);
            }

            bulletEnemy.render(batch);
            enemy.render(batch);
            player.render(batch);

            if (Config.Debug.general){
                for (WallSegment segment : map.wallSegments){
                    segment.getCollisionShape().debugRender(batch);
                }
                for (TestBall ball : testBalls){
                    ball.debugRender(batch);
                }
            }
            if (Config.Debug.ui){
                uiStage.setDebugAll(true);
            } else {
                uiStage.setDebugAll(false);
            }

            ball.render(batch);

            for (Bullet bullet : bullets) {
                bullet.render(batch);
            }
        }
        batch.end();

        uiStage.draw();
    }

    @Override
    public void initializeUI() {
        super.initializeUI();
        topGameUI = new TopGameUI(this);
        uiStage.addActor(topGameUI);
    }

}
