package lando.systems.ld53.entities;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;
import lando.systems.ld53.Assets;
import lando.systems.ld53.physics.Collidable;
import lando.systems.ld53.physics.CollisionShape;
import lando.systems.ld53.physics.CollisionShapeCircle;

public class Bullet implements Entity, Collidable, Pool.Poolable {

    public static final Pool<Bullet> pool = Pools.get(Bullet.class, 100);

    private final Rectangle bounds = new Rectangle();
    private final CollisionShapeCircle circle = new CollisionShapeCircle(0, 0, 0);
    private final Vector2 velocity = new Vector2();

    private Animation<TextureRegion> animation;
    private TextureRegion keyframe;
    private float animTime;

    public float mass = 1f;
    public float friction = 1f;
    public boolean alive = false;

    public Bullet init(Assets assets, float x, float y, float radius, float velX, float velY) {
        bounds.set(x - radius, y - radius, radius * 2, radius * 2);
        circle.set(radius, x, y);
        velocity.set(velX, velY);
        animation = assets.bullet;
        animTime = 0;
        keyframe = animation.getKeyFrame(0f);
        alive = true;
        return this;
    }

    public float left() {
        return circle.center.x - circle.radius;
    }

    public float right() {
        return circle.center.x + circle.radius;
    }

    public float top() {
        return circle.center.y + circle.radius;
    }

    public float bottom() {
        return circle.center.y - circle.radius;
    }

    public boolean isInside(float x, float y, float w, float h) {
        return left() > (x + w)
            || top()  > (y + h)
            || right()  < x
            || bottom() < y;
    }

    @Override
    public void reset() {
        bounds.set(0, 0, 0, 0);
        circle.set(0, 0, 0);
        velocity.setZero();
        animTime = 0;
        alive = false;
    }

    @Override
    public void update(float delta) {
        animTime += delta;
        keyframe = animation.getKeyFrame(animTime);
    }

    @Override
    public void render(SpriteBatch batch) {
        if (!alive) return;

        batch.draw(keyframe,
            circle.center.x - circle.radius,
            circle.center.y - circle.radius,
            circle.radius * 2,
            circle.radius * 2);
    }

    @Override
    public float getMass() {
        return mass;
    }

    @Override
    public float getFriction() {
        return friction;
    }

    @Override
    public Vector2 getVelocity() {
        return velocity;
    }

    @Override
    public void setVelocity(Vector2 newVel) {
        setVelocity(newVel.x, newVel.y);
    }

    @Override
    public void setVelocity(float x, float y) {
        velocity.set(x, y);
    }

    @Override
    public Vector2 getPosition() {
        return circle.center;
    }

    @Override
    public void setPosition(float x, float y) {
        circle.set(x, y);
        bounds.setPosition(x - circle.radius, y - circle.radius);
    }

    @Override
    public void setPosition(Vector2 newPos) {
        setPosition(newPos.x, newPos.y);
    }

    @Override
    public Rectangle getCollisionBounds() {
        return bounds;
    }

    @Override
    public CollisionShape getCollisionShape() {
        return circle;
    }

    @Override
    public void collidedWith(Collidable object) {
        if (!alive) return;

        // TODO - trigger a particle effect and sound

        boolean isPlayer = (object instanceof Player);
        boolean isEnemy = (object instanceof Enemy);
//        boolean isPeg = (object instanceof Peg); // TODO(brian) - maybe the pegs should bounce the bullets instead of removing them, only removed when hitting a 'creature' or whatever
        if (isPlayer || isEnemy) {
            alive = false;
        }
    }

    @Override
    public boolean shouldCollideWith(Collidable object) {
        if (object instanceof WallSegment) {
            return false;
        }
        return true;
    }
}
