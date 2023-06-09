package lando.systems.ld53.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import lando.systems.ld53.Assets;
import lando.systems.ld53.physics.Collidable;
import lando.systems.ld53.physics.CollisionShape;
import lando.systems.ld53.physics.CollisionShapeCircle;
import space.earlygrey.shapedrawer.ShapeDrawer;

public class Peg implements Entity, Collidable {

    private final Rectangle bounds;
    private final CollisionShapeCircle circle;
    private final Animation<TextureRegion> animation;

    private TextureRegion cap;
    private TextureRegion keyframe;
    private float animTime;

    public float mass = IMMOVABLE;
    public float friction = 0.0000f;
    private final Vector2 velocity = new Vector2();

    public Peg(Assets assets, float x, float y) {
        this.cap = assets.atlas.findRegion("objects/peg/spinner-cap");
        this.animation = assets.peg;
        this.animTime = 0f;
        this.keyframe = animation.getKeyFrame(0f);
        float scale = 0.45f;
        float size = scale * Math.max(keyframe.getRegionWidth(), keyframe.getRegionHeight());
        float radius = size / 2f;
        this.bounds = new Rectangle(x - radius, y - radius, size, size);
        this.circle = new CollisionShapeCircle(radius, x, y);
    }

    @Override
    public void update(float delta) {
        animTime += delta;
        keyframe = animation.getKeyFrame(animTime);
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.draw(keyframe,
            circle.center.x - circle.radius,
            circle.center.y - circle.radius,
            circle.radius * 2,
            circle.radius * 2);
        batch.draw(cap,
            circle.center.x - circle.radius,
            circle.center.y - circle.radius,
            circle.radius * 2,
            circle.radius * 2);
    }

    private static final Color debugColor = new Color(1, 105f / 255f, 180f / 255f, 0.5f); // Color.PINK half alpha
    @Override
    public void renderDebug(ShapeDrawer shapes) {
        shapes.filledCircle(circle.center, circle.radius, debugColor);
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
        bounds.set(x - circle.radius, y - circle.radius, circle.radius * 2f, circle.radius * 2f );
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
        return true;
    }


}
