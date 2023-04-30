package lando.systems.ld53.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import lando.systems.ld53.Config;
import lando.systems.ld53.entities.Bullet;
import lando.systems.ld53.entities.BulletEnemy;
import lando.systems.ld53.entities.Enemy;
import lando.systems.ld53.entities.Player;
import lando.systems.ld53.world.Map;

public class GameScreen extends BaseScreen {

    private Map map;
    private Player player;
    private Enemy enemy;
    private BulletEnemy bulletEnemy;

    public final Array<Bullet> bullets;

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
    }

    @Override
    public void update(float delta) {
        super.update(delta);

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.setScreen(new TitleScreen());
            return;
        }

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
