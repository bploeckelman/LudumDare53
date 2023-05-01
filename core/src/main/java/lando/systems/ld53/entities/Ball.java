package lando.systems.ld53.entities;

import com.badlogic.gdx.graphics.Color;
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
import space.earlygrey.shapedrawer.ShapeDrawer;

public class Ball implements Entity, Collidable {

    private static final Vector2 COLLISION_OFFSET = new Vector2(0, 5f);
    private static final float COLLISION_RADIUS = 20;
    private static final float RENDER_SIZE = 40f;

    private final Rectangle renderBounds;
    private final Rectangle collisionBounds;
    private final CollisionShapeCircle collisionShape;
    private final Animation<TextureRegion> animation;
    private final Vector2 velocity = new Vector2();
    private final Vector2 position = new Vector2();

    private TextureRegion keyframe;
    private float animTime;

    public float mass = 8f;
    public float friction = 0.9f;

    public boolean collected;

    public Ball(Assets assets, float x, float y) {
        this.collected = false;
        this.animation = assets.ball;
        this.animTime = 0f;
        this.keyframe = animation.getKeyFrame(0f);
        this.position.set(x, y);
        this.collisionShape = new CollisionShapeCircle(
            COLLISION_RADIUS,
            x + COLLISION_OFFSET.x,
            y + COLLISION_OFFSET.y
        );
        this.collisionBounds = new Rectangle(
            x - COLLISION_RADIUS + COLLISION_OFFSET.x,
            y - COLLISION_RADIUS + COLLISION_OFFSET.y,
            COLLISION_RADIUS * 2f,
            COLLISION_RADIUS * 2f
        );
        this.renderBounds = new Rectangle(
            x - (RENDER_SIZE / 2f),
            y - (RENDER_SIZE / 2f),
            RENDER_SIZE, RENDER_SIZE
        );
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
        batch.draw(keyframe,
            collisionShape.center.x - collisionShape.radius,
            collisionShape.center.y - collisionShape.radius,
            collisionShape.radius * 2,
            collisionShape.radius * 2);
    }

    private static final Color debugColor = new Color(1, 0, 1, 0.5f); // Color.MAGENTA half alpha
    @Override
    public void renderDebug(ShapeDrawer shapes) {
        shapes.filledCircle(collisionShape.center, collisionShape.radius, debugColor);
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
        return collisionShape.center;
    }

    @Override
    public void setPosition(float x, float y) {
        collisionShape.center.set(x, y);
        position.set(
            collisionShape.center.x - COLLISION_OFFSET.x,
            collisionShape.center.y - COLLISION_OFFSET.y);
        collisionBounds.set(
            position.x - COLLISION_RADIUS + COLLISION_OFFSET.x,
            position.y - COLLISION_RADIUS + COLLISION_OFFSET.y,
            COLLISION_RADIUS * 2f,
            COLLISION_RADIUS * 2f);
        renderBounds.setPosition(
            position.x - (renderBounds.width / 2f),
            position.y - (renderBounds.height / 2f));
    }

    @Override
    public void setPosition(Vector2 newPos) {
        setPosition(newPos.x, newPos.y);
    }

    @Override
    public Rectangle getCollisionBounds() {
        return collisionBounds;
    }

    @Override
    public CollisionShape getCollisionShape() {
        return collisionShape;
    }

    @Override
    public void collidedWith(Collidable object) {
        if (object instanceof Player) {
            Main.game.audioManager.playSound(AudioManager.Sounds.zap, .25f);
        } else {
            Main.game.audioManager.playSound(AudioManager.Sounds.pop);
        }
    }

    @Override
    public boolean shouldCollideWith(Collidable object) {
        return true;
    }
}
