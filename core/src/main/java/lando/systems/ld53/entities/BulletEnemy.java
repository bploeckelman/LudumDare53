package lando.systems.ld53.entities;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.equations.Quad;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import lando.systems.ld53.Assets;
import lando.systems.ld53.Main;
import lando.systems.ld53.audio.AudioManager;
import lando.systems.ld53.screens.GameScreen;
import lando.systems.ld53.utils.accessors.RectangleAccessor;

import static lando.systems.ld53.entities.BulletEnemy.State.*;

public class BulletEnemy implements Entity {

    enum State { idle, enter, shoot, exit  }

    private final State[] stateSequence = new State[] { idle, enter, shoot, idle, shoot, idle, shoot, idle, exit };
    private int stateIndex = 0;

    private final float stateDuration = 3f;
    private float stateTime;

    private final Assets assets;
    private final GameScreen screen;
    private final Rectangle bounds;
    private final Animation<TextureRegion> animMove;
    private final Animation<TextureRegion> animFloat;

    private Animation<TextureRegion> animation;
    private TextureRegion keyframe;
    private float animTime;

    private final float scale = 1.5f;

    public BulletEnemy(Assets assets, GameScreen screen, float x, float y) {
        this.assets = assets;
        this.screen = screen;
        this.animMove = assets.bossIdle;
        this.animFloat = assets.bossShoot;
        this.animation = animFloat;
        this.animTime = 0;
        this.stateTime = 0;
        TextureRegion frame = animMove.getKeyFrame(0f);
        this.bounds = new Rectangle(x, y,  frame.getRegionWidth() * scale, frame.getRegionHeight() * scale);
    }

    @Override
    public void update(float delta) {
        State state = stateSequence[stateIndex];

        stateTime += delta;
        if (stateTime >= stateDuration) {
            stateTime -= stateDuration;

            State prevState = state;
            stateIndex = (stateIndex + 1) % stateSequence.length;
            state = stateSequence[stateIndex];

            if (prevState == shoot) {
                shoot();
            }

            if (state == enter) {
                Tween.to(bounds, RectangleAccessor.Y, stateDuration)
                    .target(screen.worldCamera.viewportHeight / 2f - 40f)
                    .ease(Quad.IN)
                    .start(screen.tween);
            } else if (state == exit) {
                Tween.to(bounds, RectangleAccessor.Y, stateDuration)
                    .target(-100f)
                    .ease(Quad.OUT)
                    .start(screen.tween);
            }
        }

        switch (state) {
            case idle:
            case shoot: {
                animation = animMove;
            } break;
            case enter:
            case exit: {
                animation = animFloat;
            } break;
        }

        animTime += delta;
        if (state == idle) {
            animTime = 0f;
        }
        keyframe = animation.getKeyFrame(animTime);

        bounds.setSize(keyframe.getRegionWidth() * scale, keyframe.getRegionHeight() * scale);
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.draw(keyframe, bounds.x, bounds.y, bounds.width, bounds.height);
    }

    private final Vector2 vel = new Vector2();
    private void shoot() {
        float speed = 350f;
        float x = bounds.x + bounds.width;
        float y = bounds.y + bounds.height / 2f;
        float radius = 12f;
        int steps = 10;
        for (int i = 0; i < steps; i++) {
            float angle = -33 + (i * (66f / steps));
            vel.set(speed, 0f).rotateDeg(angle);
            Bullet bullet = Bullet.pool.obtain().init(assets, x, y, radius, vel.x, vel.y);
            screen.bullets.add(bullet);
        }
        Main.game.audioManager.playSound(AudioManager.Sounds.cannon, .125f);
    }

}
