package lando.systems.ld53.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import lando.systems.ld53.Main;
import lando.systems.ld53.audio.AudioManager;
import lando.systems.ld53.physics.Collidable;
import lando.systems.ld53.physics.CollisionShape;
import lando.systems.ld53.physics.CollisionShapeCircle;
import lando.systems.ld53.screens.GameScreen;
import lando.systems.ld53.utils.Utils;
import space.earlygrey.shapedrawer.ShapeDrawer;

public class PlayerFetcher implements Entity, Collidable {

    private final Player player;
    private final Animation<TextureRegion> animation;
    private final Vector2 velocity = new Vector2();
    private final Vector2 vec2 = new Vector2();
    private final Rectangle bounds;
    private final CollisionShapeCircle circle;
    private final float RADIUS = 30f;
    private final float DURATION = 10f;
    private final float MASS = 50f;
    private final float FRICTION = 0.2f;

    private float animTimer = 0f;
    private float timer = 0f;
    private boolean active = false;

    public static float chaseDist = 100;
    public static float chaseDist2 = chaseDist * chaseDist;

    GameScreen screen;
    Vector2 targetPosition = new Vector2();
    Vector2 beamSegment = new Vector2();
    float timeInPhase;
    float waitTimer;
    float beamAccum;
    Color beamColor;
    Cargo heldCargo;

    public PlayerFetcher(Player player) {
        this.player = player;
        this.animation = Main.game.assets.asuka;
        this.screen = player.screen;

        Vector2 playerPos = player.getPosition();
        float offset = (player.renderBounds.width + RADIUS) / 2f;
        this.circle = new CollisionShapeCircle(RADIUS, playerPos.x + offset, playerPos.y);
        this.bounds = new Rectangle(
            circle.center.x - circle.radius,
            circle.center.y - circle.radius,
            circle.radius * 2,
            circle.radius * 2);

        timeInPhase = 0;
        waitTimer = 1;
        beamAccum = 0;
        beamColor = new Color(Color.RED);
        heldCargo = null;
    }

    public void activate() {
        active = true;
        timer = DURATION;
        animTimer = 0f;

        // spawn asuka right next to the player (hope this works if player is next to a wall)
        Vector2 playerPos = player.getPosition();
        float offset = (player.renderBounds.width + RADIUS) / 2f;
        setPosition(playerPos.x + offset, playerPos.y);
    }

    public boolean isActive() {
        return active;
    }

    @Override
    public void update(float delta) {
        if (!active) return;

        timer -= delta;
        if (timer <= 0) {
            timer = 0;
            animTimer = 0;
            active = false;
            return;
        }

        animTimer += delta;


        beamAccum += delta;
        Utils.hsvToRgb(beamAccum, 1f, 1f, beamColor);

        timeInPhase += delta;
        if (timeInPhase > 0) {
            // It is stuck trying to get somewhere, try somewhere else
            timeInPhase = 0;
            targetPosition.set(player.getPosition());
        }

        Cargo chasedCargo = null;
        float nearestCargoDist2 = Float.MAX_VALUE;
        for (Cargo cargo : screen.cargos) {
            float dist = getPosition().dst2(cargo.getPosition());
            if (dist < nearestCargoDist2) {
                nearestCargoDist2 = dist;
                chasedCargo = cargo;
            }
        }

        if (nearestCargoDist2 < chaseDist2 && chasedCargo != null) {
            targetPosition.set(chasedCargo.getPosition());
            timeInPhase = 0;

            float attractionRadius = circle.radius + ((CollisionShapeCircle) chasedCargo.getCollisionShape()).radius + 80;
            if (attractionRadius * attractionRadius > nearestCargoDist2) {
                if (heldCargo == null) {
                    // TODO: particles
                    Main.game.audioManager.playSound(AudioManager.Sounds.giggle, .5f);
                }

                heldCargo = chasedCargo;
                heldCargo.getVelocity().add(targetPosition.sub(getPosition()).scl(-1).nor().scl(50));
                targetPosition.set(screen.player.getPosition()).sub(getPosition()).scl(1).add(getPosition());
            } else {
                heldCargo = null;
            }
        } else {
            heldCargo = null;
        }

        float speed = 300f;
        if (heldCargo != null) {
            speed = 400f;
        }

        vec2.set(targetPosition)
            .sub(getPosition())
            .nor()
            .scl(speed * delta);
        velocity.add(vec2);
    }

    @Override
    public void render(SpriteBatch batch) {
        if (!active) return;

        // draw beam
        if (heldCargo != null) {
            beamSegment.set(heldCargo.getPosition()).sub(getPosition());
            float width = 2f;
            batch.setColor(beamColor);
            batch.draw(Main.game.assets.pixelRegion, getPosition().x, getPosition().y - width/2f, 0, width/2f, beamSegment.len(), width, 1, 1, beamSegment.angleDeg());
            batch.setColor(Color.WHITE);
        }

        TextureRegion keyframe = animation.getKeyFrame(animTimer);
        boolean flipX = (velocity.x > 0);
        if ((keyframe.isFlipX() && !flipX)
         || (!keyframe.isFlipX() && flipX)) {
            keyframe.flip(true, false);
        }

        batch.draw(keyframe,
            circle.center.x - circle.radius,
            circle.center.y - circle.radius,
            circle.radius * 2,
            circle.radius * 2);
    }

    @Override
    public void renderDebug(ShapeDrawer shapes) {

    }

    @Override
    public float getFriction() {
        return FRICTION;
    }

    @Override
    public float getMass() {
        return MASS;
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
        circle.center.set(x, y);
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
        if (object instanceof Player
         || object instanceof Cargo) {
            return false;
        }
        return true;
    }

}
