package lando.systems.ld53.physics.test;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import lando.systems.ld53.physics.Collidable;
import lando.systems.ld53.physics.CollisionShape;
import lando.systems.ld53.physics.CollisionShapeCircle;

public class TestBall implements Collidable {
    float RADIUS = 5f;
    float COLLISION_MARGIN = 10f;
    Vector2 position;
    Vector2 velocity;
    float mass;
    CollisionShapeCircle collisionShape;
    Rectangle collisionBounds;


    public TestBall(Vector2 pos, Vector2 velocity) {
        this.position = new Vector2(pos);
        this.velocity = new Vector2(velocity);
        this.mass = 1;
        this.collisionShape = new CollisionShapeCircle(RADIUS, position.x, position.y);
        this.collisionBounds = new Rectangle(position.x - RADIUS - COLLISION_MARGIN, position.y - RADIUS - COLLISION_MARGIN, (RADIUS+COLLISION_MARGIN)*2f , (RADIUS+COLLISION_MARGIN)*2f);
    }

    public void debugRender(SpriteBatch batch) {
        collisionShape.debugRender(batch);
    }

    @Override
    public float getMass() {
        return mass;
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
        return position;
    }

    @Override
    public void setPosition(float x, float y) {
        position.set(x, y);
        collisionShape.center.set(position);
        this.collisionBounds = new Rectangle(position.x - RADIUS, position.y - RADIUS, RADIUS*2f, RADIUS*2f);
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
        // TODO pick up, make sound, that stuff lives here
        // Need to check what type it is
    }
}
