package lando.systems.ld53.physics.test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import lando.systems.ld53.Config;
import lando.systems.ld53.physics.Collidable;
import lando.systems.ld53.physics.PhysicsSystem;
import lando.systems.ld53.screens.BaseScreen;

public class PhysicsTestScreen extends BaseScreen {

    Array<TestBall> balls;
    TestGameArea gameArea;
    PhysicsSystem physicsSystem;
    Array<Collidable> collidables;


    public PhysicsTestScreen() {
        super();
        physicsSystem = new PhysicsSystem(new Rectangle(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        collidables = new Array<>();
        worldCamera.setToOrtho(false, Config.Screen.window_width, Config.Screen.window_height);
        worldCamera.update();

        balls = new Array<>();
        for (int i = 0; i < 300; i++){
            Vector2 pos = new Vector2(Gdx.graphics.getWidth() * MathUtils.random(.2f, .8f), Gdx.graphics.getHeight() * MathUtils.random(.2f, .5f));
            Vector2 vel = new Vector2(MathUtils.random(-60f, 60f), MathUtils.random(-60f, 60f));
            balls.add(new TestBall(pos, vel));
        }
        balls.add(new TestBall(new Vector2(500, 200), new Vector2(0,0)));
        balls.add(new TestBall(new Vector2(500, 230), new Vector2(0, -10)));
        gameArea = new TestGameArea();
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }

        collidables.clear();
        collidables.addAll(balls);
        collidables.addAll(gameArea.segments);
        physicsSystem.update(delta, collidables);
    }

    @Override
    public void render(SpriteBatch batch) {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);

        batch.setProjectionMatrix(worldCamera.combined);
        batch.begin();
        {
            for (TestBall ball : balls){
                ball.debugRender(batch);
            }
            gameArea.debugRender(batch);
            physicsSystem.debugRender(batch);
        }
        batch.end();
    }
}
