package lando.systems.ld53.entities;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import lando.systems.ld53.Assets;
import lando.systems.ld53.Main;
import lando.systems.ld53.audio.AudioManager;
import lando.systems.ld53.physics.Collidable;
import lando.systems.ld53.physics.CollisionShape;
import lando.systems.ld53.physics.CollisionShapeCircle;
import lando.systems.ld53.utils.Calc;

public class Ball implements Entity, Collidable {

    private final Rectangle bounds;
    private final CollisionShapeCircle circle;
    private final Animation<TextureRegion> animation;

    private TextureRegion keyframe;
    private float animTime;

    public float mass = 8f;
    public float friction = 0.9f;
    private final Vector2 velocity = new Vector2();

    public Ball(Assets assets, float x, float y) {
        this.animation = assets.ball;
        this.animTime = 0f;
        this.keyframe = animation.getKeyFrame(0f);
        float scale = 3f;
        float size = scale * Math.max(keyframe.getRegionWidth(), keyframe.getRegionHeight());
        float radius = size / 2f;
        this.bounds = new Rectangle(x - radius, y - radius, size, size);
        this.circle = new CollisionShapeCircle(radius, x, y);
    }

    @Override
    public void update(float delta) {
        Vector2 vel = getVelocity();
        float animSpeed = Calc.clampf(vel.len2() / 1000f, 0f, 1f);
        animTime += delta * animSpeed;
        keyframe = animation.getKeyFrame(animTime);
    }

    @Override
    public void render(SpriteBatch batch) {
        // TODO - should circle or bounds be used here? not sure which takes priority
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
        velocity.set(newVel);
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
    }

    @Override
    public void setPosition(Vector2 newPos) {
        circle.set(newPos.x, newPos.y);
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
        Main.game.audioManager.playSound(AudioManager.Sounds.pop);

    }

    @Override
    public boolean shouldCollideWith(Collidable object) {
        return true;
    }
}
