package lando.systems.ld53.physics;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public interface Collidable {
    float IMMOVABLE = Float.MAX_VALUE;

    float getMass();
    Vector2 getVelocity();
    void setVelocity(Vector2 newVel);
    void setVelocity(float x, float y);
    Vector2 getPosition();
    void setPosition(float x, float y);
    void setPosition(Vector2 newPos);
    Rectangle getCollisionBounds();
//    void setCollisionBounds(float dt);
    CollisionShape getCollisionShape();
    void collidedWith(Collidable object);
}
