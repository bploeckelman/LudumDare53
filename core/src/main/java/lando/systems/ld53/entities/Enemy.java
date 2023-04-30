package lando.systems.ld53.entities;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import lando.systems.ld53.Assets;
import lando.systems.ld53.physics.Collidable;
import lando.systems.ld53.physics.CollisionShape;
import lando.systems.ld53.physics.CollisionShapeCircle;

public class Enemy implements Entity, Collidable {

    private final Vector2 velocity;
    private final Rectangle bounds;
    private final CollisionShapeCircle circle;
    private final Animation<TextureRegion> animation;

    // TODO(brian) - use target to set velocity? need to add behavioral code
    private Vector2 target;
    private float moveTime;

    private TextureRegion keyframe;
    private float animTime;

    public float mass = 10f;
    public float friction = 0.5f;

    public Enemy(Assets assets, float x, float y) {
        this.animation = assets.gobbler;
        this.animTime = 0f;
        this.keyframe = animation.getKeyFrame(0f);
        float size = 0.5f * Math.max(keyframe.getRegionWidth(), keyframe.getRegionHeight());
        this.circle = new CollisionShapeCircle(size / 2f, x, y);
        this.bounds = new Rectangle(x - circle.radius, y - circle.radius, size, size);
        this.velocity = new Vector2();
        this.target = new Vector2();
        this.moveTime = 0f;
    }

    @Override
    public void update(float delta) {
        animTime += delta;
        keyframe = animation.getKeyFrame(animTime);

        // TODO - custom behavior code should go here,
        //  but don't set positions directly, the physics system has to do that
        //  we just need to influence
        moveTime -= delta;
        if (moveTime <= 0f) {
            moveTime = MathUtils.random(1f, 3f);
            float x = MathUtils.random(80, 1200);
            float y = MathUtils.random(80, 640);
            target.set(x, y);

//            Tween.to(circle, CircleAccessor.XY, moveTime)
//                .target(target.x, target.y)
//                .start(Main.game.tween);
        }
    }

    @Override
    public void render(SpriteBatch batch) {
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

    }

    @Override
    public boolean shouldCollideWith(Collidable object) {
        return false;
    }
}
