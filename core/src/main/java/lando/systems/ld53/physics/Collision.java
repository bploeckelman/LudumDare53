package lando.systems.ld53.physics;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;

public class Collision implements Comparable, Pool.Poolable{
    public double t;
    public Vector2 position;
    public Vector2 normal;
    public Collidable col1;
    public Collidable col2;

    public Collision() {
        this.t = 0;
        this.position = new Vector2();
        this.normal = new Vector2();
    }

    public void init(double t, Vector2 pos, Vector2 normal, Collidable col1, Collidable col2) {
        this.t = t;
        this.position.set(pos);
        this.normal.set(normal);
        this.col1 = col1;
        this.col2 = col2;
    }


    @Override
    public void reset() {
        this.t = 0;
        this.position.setZero();
        this.normal.setZero();
        this.col1 = null;
        this.col2 = null;
    }

    public void handleCollision() {
        col1.collidedWith(col2);
        col2.collidedWith(col1);

        if (col1.getMass() == Collidable.IMMOVABLE || col2.getMass() == Collidable.IMMOVABLE) {
            Collidable bouncer = col1;
            Collidable solid = col2;
            if (col1.getMass() == Collidable.IMMOVABLE) {
                bouncer = col2;
                solid = col1;
            }
            bouncer.setVelocity(PhysicsSystem.reflectVector(bouncer.getVelocity(), normal));

            // Scoot it away a little bit
            Vector2 oldCenter = bouncer.getPosition();
            bouncer.setPosition(oldCenter.x + .001f * -normal.x, oldCenter.y + .001f * -normal.y);
        } else {
            float p = 2 * (col1.getVelocity().x * normal.x + col1.getVelocity().y * normal.y - col2.getVelocity().x * normal.x - col2.getVelocity().y * normal.y)/(col1.getMass() + col2.getMass());
            Vector2 firstVel = col1.getVelocity();
            Vector2 secondVel = col2.getVelocity();
            col1.setVelocity(firstVel.x - p * col2.getMass() * normal.x, firstVel.y - p * col2.getMass() * normal.y);
            col2.setVelocity(secondVel.x + p * col1.getMass() * normal.x, secondVel.y + p * col1.getMass() * normal.y);
        }
    }

    @Override
    public int compareTo(Object o) {
        if (o instanceof Collision) {
            Collision other = (Collision) o;
            return Double.compare(this.t, other.t);
        }
        return 0;
    }

}
