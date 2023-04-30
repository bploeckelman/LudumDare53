package lando.systems.ld53.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import lando.systems.ld53.Config;
import lando.systems.ld53.entities.Bullet;
import lando.systems.ld53.entities.BulletEnemy;
import lando.systems.ld53.entities.Enemy;
import lando.systems.ld53.entities.Player;
import lando.systems.ld53.entities.WallSegment;
import lando.systems.ld53.physics.Collidable;
import lando.systems.ld53.physics.PhysicsSystem;
import lando.systems.ld53.physics.test.TestBall;
import lando.systems.ld53.world.Map;

public class GameScreen extends BaseScreen {

    private Map map;
    private Player player;
    private Enemy enemy;
    private BulletEnemy bulletEnemy;

    public final Array<Bullet> bullets;
    private PhysicsSystem physicsSystem;
    private Array<Collidable> physicsObjects;

    Array<TestBall> testBalls;

    public GameScreen() {
        super();

        worldCamera.setToOrtho(false, Config.Screen.framebuffer_width, Config.Screen.framebuffer_height);
        worldCamera.update();

        map = new Map("maps/test-80x80.tmx");
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
    }

    @Override
    public void update(float delta) {
        super.update(delta);

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.setScreen(new TitleScreen());
            return;
        }

        physicsObjects.clear();

        physicsObjects.addAll(map.wallSegments);
        physicsObjects.addAll(testBalls);
        physicsObjects.add(player);

        physicsSystem.update(delta, physicsObjects);


        for (int i = bullets.size - 1; i >= 0; i--) {
            Bullet bullet = bullets.get(i);
            bullet.update(delta);

            boolean bulletOffscreen =
                       bullet.left()   > worldCamera.viewportWidth
                    || bullet.top()    > worldCamera.viewportHeight
                    || bullet.right()  < 0
                    || bullet.bottom() < 0;
            if (bulletOffscreen) {
                Bullet.pool.free(bullet);
                bullets.removeIndex(i);
            }
        }

        bulletEnemy.update(delta);
        enemy.update(delta);
        player.update(delta);
        map.update(delta);
    }

    @Override
    public void render(SpriteBatch batch) {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);

        map.render(batch, worldCamera);

        batch.setProjectionMatrix(worldCamera.combined);
        batch.begin();
        {
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
    }

}
