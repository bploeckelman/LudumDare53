package lando.systems.ld53.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import lando.systems.ld53.Assets;
import lando.systems.ld53.Main;
import lando.systems.ld53.audio.AudioManager;
import lando.systems.ld53.entities.enemies.Enemy;
import lando.systems.ld53.physics.Collidable;
import lando.systems.ld53.physics.CollisionShape;
import lando.systems.ld53.physics.CollisionShapeCircle;
import lando.systems.ld53.physics.Influencer;
import lando.systems.ld53.screens.GameScreen;
import lando.systems.ld53.utils.InfluenceRenderer;
import space.earlygrey.shapedrawer.ShapeDrawer;

public class Bomb implements Entity, Collidable {

    private static final Vector2 COLLISION_OFFSET = new Vector2(0, 0f);
    private static final float COLLISION_RADIUS = 20;
    private static final float RENDER_SIZE = 66f;

    private final GameScreen screen;
    private final Rectangle renderBounds;
    private final Rectangle collisionBounds;
    private final CollisionShapeCircle collisionShape;
    private final Animation<TextureRegion> animation;
    private final Vector2 velocity = new Vector2();
    private final Vector2 position = new Vector2();
    private final Vector2 vec2 = new Vector2();

    private TextureRegion keyframe;
    private float animTime;

    public float mass = 1000f;
    public float friction = 0.001f;
    public boolean alive = false;

    public Influencer repulsor;
    public boolean repulsorActive;

    private float collisionDelay = 0.2f;

    private final float fuseDuration = .75f;
    public float fuseTime = 0f;

    public Bomb(GameScreen screen, float x, float y, float velX, float velY) {
        this.screen = screen;
        this.animation = screen.assets.bomb;
        this.keyframe = animation.getKeyFrame(0f);
        this.animTime = 0f;
        this.alive = true;
        setVelocity(velX, velY);

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

        this.repulsorActive = false;
        this.repulsor = new Influencer() {
            final InfluenceRenderer renderer = new InfluenceRenderer(this, Color.GOLD);

            @Override
            public float getStrength() {
                return repulsorActive ? -4000 : 0;
            }

            @Override
            public Vector2 getPosition() {
                return Bomb.this.getPosition();
            }

            @Override
            public float getRange() {
                return repulsorActive ? 160 : 0;
            }

            @Override
            public void debugRender(SpriteBatch batch) {
            }

            @Override
            public boolean shouldEffect(Collidable c) {
                return true;
            }

            @Override
            public void updateInfluence(float dt) {
                if (!repulsorActive) return;
                renderer.update(dt);
            }

            @Override
            public void renderInfluence(SpriteBatch batch) {
                if (!repulsorActive) return;
                renderer.render(batch);
            }
        };
    }

    @Override
    public void update(float delta) {
        if (!alive) return;

        collisionDelay -= delta;
        if (collisionDelay < 0) {
            collisionDelay = 0;
        }

        fuseTime += delta;
        if (fuseTime >= fuseDuration) {
            fuseTime = fuseDuration;

            if (animation.isAnimationFinished(animTime)) {
                alive = false;
            } else {
                if (repulsorActive == false) {
                    repulsorActive = true;
                    Main.game.currentScreen.screenShaker.addDamage(100);
                    Main.game.audioManager.playSound(AudioManager.Sounds.cannon, 0.3f);

                    // hurt nearby enemies
                    Array<Enemy> enemies = screen.enemies;
                    for (Enemy enemy : enemies) {
                        float dist2 = vec2.set(enemy.getPosition()).sub(getPosition()).len2();
                        if (dist2 < repulsor.getRange() * repulsor.getRange()) {
                            // TODO - hurt, not kill?
                            enemy.kill();
                        }
                    }
                }
            }
            animTime += delta;
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        keyframe = animation.getKeyFrame(animTime);
        batch.draw(keyframe, renderBounds.x, renderBounds.y, renderBounds.width, renderBounds.height);
    }

    private static final Color debugColor = new Color(0.8f, 1, 0.8f, 0.5f);
    @Override
    public void renderDebug(ShapeDrawer shapes) {
        shapes.filledCircle(collisionShape.center, collisionShape.radius, debugColor);
    }

    @Override
    public float getFriction() {
        return friction;
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
        setVelocity(newVel.x, newVel.y);
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

    }

    @Override
    public boolean shouldCollideWith(Collidable object) {
        if (collisionDelay != 0 && object instanceof Player) {
            return false;
        }
        return true;
    }

}
