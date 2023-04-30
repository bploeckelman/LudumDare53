package lando.systems.ld53.entities;

import aurelienribon.tweenengine.Tween;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import lando.systems.ld53.Assets;
import lando.systems.ld53.Main;
import lando.systems.ld53.utils.accessors.CircleAccessor;

public class Enemy implements Entity {

    private final Circle circle;
    private final Animation<TextureRegion> animation;

    private Vector2 target;
    private float moveTime;

    private TextureRegion keyframe;
    private float animTime;

    public Enemy(Assets assets, float x, float y) {
        this.animation = assets.gobbler;
        this.animTime = 0f;
        this.keyframe = animation.getKeyFrame(0f);
        float size = Math.max(keyframe.getRegionWidth(), keyframe.getRegionHeight());
        this.circle = new Circle(x, y, size / 2f);
        this.target = new Vector2();
        this.moveTime = 0f;
    }

    @Override
    public void update(float delta) {
        animTime += delta;
        keyframe = animation.getKeyFrame(animTime);

        moveTime -= delta;
        if (moveTime <= 0f) {
            moveTime = MathUtils.random(1f, 3f);
            float x = MathUtils.random(80, 1200);
            float y = MathUtils.random(80, 640);
            target.set(x, y);

            Tween.to(circle, CircleAccessor.XY, moveTime)
                .target(target.x, target.y)
                .start(Main.game.tween);
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.draw(keyframe,
            circle.x - circle.radius,
            circle.y - circle.radius,
            circle.radius * 2,
            circle.radius * 2);
    }

}
