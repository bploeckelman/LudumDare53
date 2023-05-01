package lando.systems.ld53.entities.enemies;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import lando.systems.ld53.Assets;
import lando.systems.ld53.Config;
import lando.systems.ld53.Main;
import lando.systems.ld53.audio.AudioManager;
import lando.systems.ld53.entities.Entity;
import lando.systems.ld53.entities.Player;
import lando.systems.ld53.physics.Collidable;
import lando.systems.ld53.physics.CollisionShape;
import lando.systems.ld53.physics.CollisionShapeCircle;
import lando.systems.ld53.screens.GameScreen;
import space.earlygrey.shapedrawer.ShapeDrawer;

public class Enemy implements Entity, Collidable {

    private static final float MAX_SPEED = 300;
    protected Rectangle targetBounds = new Rectangle(80, 80, Config.Screen.window_width - 160, Config.Screen.window_height- 160);

    protected final GameScreen screen;
    protected final Rectangle bounds;
    protected final CollisionShapeCircle circle;
    protected Animation<TextureRegion> animation;
    protected final Vector2 velocity;
    protected final Vector2 v = new Vector2();

    // TODO(brian) - use target to set velocity? need to add behavioral code
    protected Vector2 target;
    protected float moveTime;

    protected TextureRegion keyframe;
    protected float animTime;

    protected float mass = 10f;
    protected float friction = 0.3f;

    public Enemy(GameScreen screen, float x, float y) {
        this.screen = screen;
        this.animation = screen.assets.gobbler;
        this.animTime = 0f;
        this.keyframe = animation.getKeyFrame(0f);
        float size = 60;
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
//        moveTime -= delta;
//        if (moveTime <= 0f) {
//            moveTime = MathUtils.random(1f, 3f);
//            float x = MathUtils.random(80, 1200);
//            float y = MathUtils.random(80, 640);
//            target.set(x, y);
//
//            float speed = 300f;
//            v.set(target).sub(getPosition()).nor().scl(speed);
//            velocity.add(v);
//        }
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.draw(keyframe,
            circle.center.x - circle.radius,
            circle.center.y - circle.radius,
            circle.radius * 2,
            circle.radius * 2);
    }

    public boolean isAlive() {
        return true; // TODO make this real
    }

    private static final Color debugColor = new Color(0, 1, 1, 0.5f); // Color.CYAN half alpha
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
        setVelocity(newVel.x, newVel.y);
    }

    @Override
    public void setVelocity(float x, float y) {
        velocity.set(x, y);
        if (velocity.len() > MAX_SPEED) {
            velocity.nor().scl(MAX_SPEED);
        }
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

        if(object instanceof Player) {
            Main.game.audioManager.playSound(AudioManager.Sounds.gobble, .3f);
//            Main.game.audioManager.playSound(AudioManager.Sounds.ticktock, .3f);
        }


    }

    @Override
    public boolean shouldCollideWith(Collidable object) {
        return true;
    }
}
