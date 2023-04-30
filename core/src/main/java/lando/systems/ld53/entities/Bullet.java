package lando.systems.ld53.entities;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;
import lando.systems.ld53.Assets;

// TODO(brian) - implement collidable
public class Bullet implements Entity, Pool.Poolable {

    public static final Pool<Bullet> pool = Pools.get(Bullet.class, 100);

    public final Circle circle = new Circle();
    private final Vector2 velocity = new Vector2();

    private Animation<TextureRegion> animation;
    private TextureRegion keyframe;
    private float animTime;

    public Bullet init(Assets assets, float x, float y, float radius, float velX, float velY) {
        circle.set(x, y, radius);
        velocity.set(velX, velY);
        animation = assets.bullet;
        animTime = 0;
        keyframe = animation.getKeyFrame(0f);
        return this;
    }

    public float left() {
        return circle.x - circle.radius;
    }

    public float right() {
        return circle.x + circle.radius;
    }

    public float top() {
        return circle.y + circle.radius;
    }

    public float bottom() {
        return circle.y - circle.radius;
    }

    @Override
    public void reset() {
        circle.set(0, 0, 0);
        velocity.setZero();
        animTime = 0;
    }

    @Override
    public void update(float delta) {
        float newX = circle.x + velocity.x * delta;
        float newY = circle.y + velocity.y * delta;
        circle.setPosition(newX, newY);

        animTime += delta;
        keyframe = animation.getKeyFrame(animTime);
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
