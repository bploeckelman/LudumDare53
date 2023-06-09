package lando.systems.ld53.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import lando.systems.ld53.Main;
import lando.systems.ld53.physics.Collidable;
import lando.systems.ld53.physics.CollisionShape;
import lando.systems.ld53.physics.CollisionShapeSegment;
import space.earlygrey.shapedrawer.ShapeDrawer;

public class WallSegment implements Collidable {

    CollisionShapeSegment collisionShape;
    Rectangle collisionBounds;

    public WallSegment(float x1, float y1, float x2, float y2) {
        collisionShape = new CollisionShapeSegment(x1, y1, x2, y2);
        collisionBounds = new Rectangle(Math.min(x1, x2)  - 5, Math.min(y1, y2) -5, Math.abs(x2 - x1) + 10, Math.abs(y2-y1) + 10);
    }

    @Override
    public void renderDebug(ShapeDrawer shapes) {
        shapes.line(
            collisionShape.start.x, collisionShape.start.y,
            collisionShape.end.x, collisionShape.end.y,
            Color.YELLOW, 4f);
        shapes.filledCircle(collisionShape.start, 8f, Color.GOLDENROD);
        shapes.filledCircle(collisionShape.end, 8f, Color.GOLDENROD);
    }

    public void render(SpriteBatch batch) {
        float width = 2f;
        batch.setColor(.5f, .3f, .1f, 1f);
        batch.draw(Main.game.assets.pixelRegion, collisionShape.start.x, collisionShape.start.y - width/2f, 0, width/2f, collisionShape.delta.len(), width, 1, 1, collisionShape.getRotation());
        batch.setColor(Color.WHITE);
    }

    @Override
    public float getMass() {
        return Collidable.IMMOVABLE;
    }

    @Override
    public float getFriction() {
        return 0;
    }

    @Override
    public Vector2 getVelocity() {
        return Vector2.Zero;
    }

    @Override
    public void setVelocity(Vector2 newVel) {
        Gdx.app.error("WallSegment", "You shouldn't be setting the Velocity of a wall object");
    }

    @Override
    public void setVelocity(float x, float y) {

    }

    @Override
    public Vector2 getPosition() {
        return null;
    }

    @Override
    public void setPosition(float x, float y) {
        Gdx.app.error("WallSegment", "You shouldn't be setting the position of a wall object");
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

    }

    @Override
    public boolean shouldCollideWith(Collidable object) {
        return true;
    }

}
