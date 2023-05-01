package lando.systems.ld53.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Queue;
import de.damios.guacamole.tuple.Pair;
import lando.systems.ld53.Assets;
import lando.systems.ld53.Main;
import lando.systems.ld53.audio.AudioManager;
import lando.systems.ld53.physics.Collidable;
import lando.systems.ld53.physics.CollisionShape;
import lando.systems.ld53.physics.CollisionShapeCircle;
import lando.systems.ld53.screens.GameScreen;
import lando.systems.ld53.utils.Calc;
import lando.systems.ld53.utils.VectorPool;
import space.earlygrey.shapedrawer.ShapeDrawer;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Player implements Entity, Collidable {

    private static final Vector2 COLLISION_OFFSET = new Vector2(0, -22f);
    private static final float COLLISION_RADIUS = 25;
    private static final float RENDER_SIZE = 120f;
    private static final float SPEED_NORMAL = 2000f;
    private static final float SPEED_FAST = 4000f;
    private static final float MAX_STAMINA = 10f; // seconds to charge fully
    private static final float STUN_TIMER = .3f;

    private final HashMap<State, Animation<TextureRegion>> animations = new HashMap<>();
    private final HashMap<State, Animation<TextureRegion>> swipeAnimations = new HashMap<>();
    private final Vector2 vec2 = new Vector2();
    private final Vector2 position;
    private final Vector2 movement;
    private final Vector2 velocity;

    final Rectangle renderBounds;
    final Rectangle collisionBounds;
    final CollisionShapeCircle collisionShape;

    private Animation<TextureRegion> animation;
    private Animation<TextureRegion> swipeAnimation;
    private TextureRegion keyframe;
    private TextureRegion swipeKeyframe;
    private State currentState;
    private Direction currentDirection;

    private final Queue<Pair<Rectangle, TextureRegion>> speedGhosts = new Queue<>();
    private final float SPEED_DURATION = 3f;
    private float speedTimer = 0f;
    private float speedGhostAddTime = 0f;
    private boolean speedActive = false;

    public final PlayerPersonalRepulsor personalRepulsor;
    public final PlayerShield shield;

    private boolean isAttacking = false;
    private boolean isStunned = false;
    private float animTimer = 0;
    private float attackTimer = 0;
    private float stunTimer = 0;
    private float stamina = MAX_STAMINA;
    private float speed = SPEED_NORMAL;

    public float mass = 20f;
    public float friction = 0.001f;
    public PlayerAbility currentAbility = PlayerAbility.bomb_throw;
    public List<PlayerAbility> abilityList = Arrays.asList(PlayerAbility.values());

    public enum State {
        idle_down,
        idle_up,
        idle_left,
        idle_right,
        walk_left,
        walk_right,
        walk_up,
        walk_down,
        slash_left,
        slash_right,
        slash_up,
        slash_down,
        slash_360,
        stun,
    }
    public enum Direction { up, down, left, right, up_left, up_right, down_left, down_right }

    public Player(Assets assets, float x, float y) {
        this.position = new Vector2(x, y);
        this.movement = new Vector2();
        this.velocity = new Vector2();

        this.collisionShape = new CollisionShapeCircle(
            COLLISION_RADIUS,
            position.x + COLLISION_OFFSET.x,
            position.y + COLLISION_OFFSET.y
        );
        this.collisionBounds = new Rectangle(
            position.x - COLLISION_RADIUS + COLLISION_OFFSET.x,
            position.y - COLLISION_RADIUS + COLLISION_OFFSET.y,
            COLLISION_RADIUS * 2f,
            COLLISION_RADIUS * 2f
        );
        this.renderBounds = new Rectangle(
            position.x - (RENDER_SIZE / 2f),
            position.y - (RENDER_SIZE / 2f),
            RENDER_SIZE, RENDER_SIZE
        );

        animations.put(State.idle_down, assets.playerIdleDown);
        animations.put(State.idle_up, assets.playerIdleUp);
        animations.put(State.idle_left, assets.playerIdleLeft);
        animations.put(State.idle_right, assets.playerIdleRight);
        animations.put(State.walk_left, assets.playerWalkLeft);
        animations.put(State.walk_right, assets.playerWalkRight);
        animations.put(State.walk_up, assets.playerWalkUp);
        animations.put(State.walk_down, assets.playerWalkDown);
        animations.put(State.slash_left, assets.playerSlashLeft);
        animations.put(State.slash_right, assets.playerSlashRight);
        animations.put(State.slash_up, assets.playerSlashUp);
        animations.put(State.slash_down, assets.playerSlashDown);
        animations.put(State.slash_360, assets.playerSlash360);
        animations.put(State.stun, assets.playerStun);

        swipeAnimations.put(State.slash_left, assets.playerSlashOverlayLeft);
        swipeAnimations.put(State.slash_right, assets.playerSlashOverlayRight);
        swipeAnimations.put(State.slash_up, assets.playerSlashOverlayUp);
        swipeAnimations.put(State.slash_down, assets.playerSlashOverlayDown);
        swipeAnimations.put(State.slash_360, assets.playerSlashOverlay360);

        animation = animations.get(State.idle_down);
        swipeAnimation = assets.playerSlashOverlayUp;
        currentDirection = Direction.down;

        personalRepulsor = new PlayerPersonalRepulsor(this);
        shield = new PlayerShield(this);
    }

    @Override
    public void update(float delta) {
        stamina += delta;
        stamina = MathUtils.clamp(stamina, 0f, MAX_STAMINA);

        animTimer += delta;
        movement.setZero();
        if (Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP))    movement.y = 1;
        if (Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN))  movement.y -= 1;
        if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) movement.x = 1;
        if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT))  movement.x -= 1;
        movement.nor();
        //position.add(movementVector.x * tempSpeed * delta, movementVector.y * tempSpeed * delta);
        // Player is attacking, so keep going without changing state if anim not finished
        if (isAttacking) {
            if (animation.isAnimationFinished(attackTimer)) {
                currentState = State.idle_down;
                attackTimer = 0;
                isAttacking = false;
            }
        }
        //player is stunned, so keep going without changing state if anim not finished
        else if (isStunned) {
            if (stunTimer > STUN_TIMER) {
                currentState = State.idle_down;
                stunTimer = 0;
                isStunned = false;
            }
        }
        // Player is not moving
        else if (movement.equals(Vector2.Zero)) {
            switch (currentDirection) {
                case up:
                    currentState = State.idle_up;
                    break;
                case down:
                    currentState = State.idle_down;
                    break;
                case left:
                case up_left:
                case down_left:
                    currentState = State.idle_left;
                    break;
                case right:
                case up_right:
                case down_right:
                    currentState = State.idle_right;
                    break;
            }
        }
        // Player is moving
        else {
            if (movement.x > 0) {
                currentState = State.walk_right;
                currentDirection = Direction.right;
                if (movement.y > 0) {
                    currentDirection = Direction.up_right;
                }
                else if (movement.y < 0) {
                    currentDirection = Direction.down_right;
                }
            }
            else if (movement.x < 0) {
                currentState = State.walk_left;
                currentDirection = Direction.left;
                if (movement.y > 0) {
                    currentDirection = Direction.up_left;
                }
                else if (movement.y < 0) {
                    currentDirection = Direction.down_left;
                }
            }
            else {
                if (movement.y > 0) {
                    currentState = State.walk_up;
                    currentDirection = Direction.up;
                }
                else if (movement.y < 0) {
                    currentState = State.walk_down;
                    currentDirection = Direction.down;
                }
            }
        }

        // If player does specialAttack

            if(Main.game.currentScreen instanceof GameScreen) {
                GameScreen gameScreen = (GameScreen) Main.game.currentScreen;
                if(!gameScreen.isSelectSkillUIShown) {
                    if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) || Gdx.input.justTouched()) {
                        float cost = currentAbility.cost;
                        if (stamina < cost) {
                            Main.game.audioManager.playSound(AudioManager.Sounds.error, .35f);
                        } else {
                            isAttacking = true;
                            stamina -= cost;
                            triggerAbility();
                        }
                    }

                }
            }


        //set player image based on currentState
        boolean didAddSpeed = false; // NOTE(brian) - this is a dumb workaround
        animation = animations.get(currentState);
        swipeAnimation = swipeAnimations.get(State.slash_up);
        if (isAttacking) {
            attackTimer += delta;
            keyframe = animation.getKeyFrame(attackTimer);
            if(currentState == State.slash_up
                || currentState == State.slash_down
                || currentState == State.slash_left
                || currentState == State.slash_right
                || currentState == State.slash_360
            ) {
                swipeAnimation = swipeAnimations.get(currentState);
                swipeKeyframe = swipeAnimation.getKeyFrame(attackTimer);
            }

        }
        else if (isStunned) {
            stunTimer += delta;
            keyframe = animation.getKeyFrame(stunTimer);
        }
        else {
            keyframe = animation.getKeyFrame(animTimer);
            velocity.add(movement.x * speed * delta, movement.y * speed * delta);
            didAddSpeed = true;
        }

        // handle speed updates
        if (speedActive) {
            // update speed effect
            speedTimer -= delta;
            if (speedTimer <= 0) {
                speedTimer = 0;
                speedGhostAddTime = 0;
                speedActive = false;
                speedGhosts.clear();
                speed = SPEED_NORMAL;
            } else {
                // NOTE(brian) - hacky workaround for some of the state flags interfering with this effect
                if (!didAddSpeed) {
                    didAddSpeed = true;
                    velocity.add(movement.x * speed * delta, movement.y * speed * delta);
                }

                // update ghosts
                float addInterval = 0.08f;
                speedGhostAddTime += delta;
                if (speedGhostAddTime > addInterval) {
                    speedGhostAddTime -= addInterval;

                    Pair<Rectangle, TextureRegion> ghost = new Pair<>(new Rectangle(renderBounds), keyframe);
                    speedGhosts.addLast(ghost);

                    // limit the number of ghosts there can be
                    if (speedGhosts.size > 5) {
                        speedGhosts.removeFirst();
                    }
                }
            }
        }
//        swipeKeyframe = swipeAnimations.get(State.slash_up).getKeyFrame(attackTimer);

        personalRepulsor.update(delta);
        // NOTE(brian) - moar hacks
        if (personalRepulsor.isActive() && !didAddSpeed) {
            velocity.add(movement.x * speed * delta, movement.y * speed * delta);
        }

        shield.update(delta);
    }

    public float getStaminaPercentage() {
        return stamina / MAX_STAMINA * 100f;
    }

    @Override
    public void render(SpriteBatch batch) {
        if (!speedGhosts.isEmpty()) {
            for (int i = 0; i < speedGhosts.size; i++) {
                Pair<Rectangle, TextureRegion> ghost = speedGhosts.get(i);
                Rectangle bounds = ghost.x;
                TextureRegion region = ghost.y;

                float minAlpha = 0.2f;
                float maxAlpha = 0.8f;
                float t = i / (float) (speedGhosts.size - 1);
                float alpha = minAlpha + t * (maxAlpha - minAlpha);
                batch.setColor(1, 1, 1, alpha);
                batch.draw(keyframe, bounds.x, bounds.y, bounds.width, bounds.height);
                batch.setColor(Color.WHITE);
            }
        }

        batch.draw(keyframe, renderBounds.x, renderBounds.y, renderBounds.width, renderBounds.height);
            if(currentState == State.slash_up
                || currentState == State.slash_down
                || currentState == State.slash_left
                || currentState == State.slash_right
                || currentState == State.slash_360
            ) {
                batch.draw(swipeKeyframe, renderBounds.x, renderBounds.y, renderBounds.width, renderBounds.height);
            }
//            batch.draw(swipeKeyframe, renderBounds.x, renderBounds.y, renderBounds.width, renderBounds.height);

        shield.render(batch);
    }

    private static final Color debugColor = new Color(50f / 255f, 205f / 255f, 50f / 255f, 0.5f); // Color.LIME half alpha
    @Override
    public void renderDebug(ShapeDrawer shapes) {
        shapes.filledCircle(collisionShape.center, collisionShape.radius, debugColor);
    }

    private void handleSlash() {
        isAttacking = true;

//        Main.game.audioManager.playSound(AudioManager.Sounds.swoosh);

        switch (currentDirection) {
            case up:
                currentState = State.slash_up;
                break;
            case down:
                currentState = State.slash_down;
                break;
            case left:
            case up_left:
            case down_left:
                currentState = State.slash_left;
                break;
            case right:
            case up_right:
            case down_right:
                currentState = State.slash_right;
                break;
        }
    }

    private void triggerAbility() {
        switch(currentAbility) {
            case shield_360: {
                shield.activate();
                currentState = State.slash_360;
                Main.game.audioManager.playSound(AudioManager.Sounds.bigswoosh, .26f);
            } break;
            case bomb_throw: {
                throwBomb();
                Main.game.audioManager.playSound(AudioManager.Sounds.impact, .26f);
            } break;
            case speed_up: {
                speedUp();
                Main.game.audioManager.playSound(AudioManager.Sounds.bigswoosh, .26f);
            } break;
            case repulse: {
                personalRepulsor.activate();
                Main.game.audioManager.playSound(AudioManager.Sounds.swoosh, .26f);
            } break;
            case grapple: {

            } break;
        }
    }

    private void throwBomb() {
        Vector2 vel = VectorPool.getVec2();
        Vector2 pos = VectorPool.getVec2()
            .set(getPosition()).add(0, -10f);

        switch (currentDirection) {
            case up:   vel.set(0,  1); break;
            case down: vel.set(0, -1); break;
            case left:
            case up_left:
            case down_left: vel.set(-1, 0); break;
            case right:
            case up_right:
            case down_right: vel.set(1, 0); break;
        }

        float speed = 1000f;
        vel.scl(speed);

        GameScreen screen = (GameScreen) Main.game.currentScreen;
        Bomb bomb = new Bomb(screen.assets, pos.x, pos.y, vel.x, vel.y);
        screen.bombs.add(bomb);

        VectorPool.freeAll(pos, vel);
    }

    private void speedUp() {
        speed = SPEED_FAST;
        speedTimer = SPEED_DURATION;
        speedActive = true;
        speedGhostAddTime = 0;
        speedGhosts.clear();
    }

    @Override
    public float getMass() {
        // NOTE(brian) - this isn't ideal,
        //  want to only impact bullets and still have
        //  them be destroyed but not push the player... hard to do right now
        return shield.isActive() ? 5000f : mass;
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
        if (object instanceof Peg) {
            isStunned = true;
            currentState = State.stun;
//            Main.game.audioManager.playSound(AudioManager.Sounds.grunt);
            Main.game.audioManager.playSound(AudioManager.Sounds.grunt, 11f);
//            Main.game.assets.grunt1.play();
        }
        else if (object instanceof Cargo) {
            // swipe in the direction of the ball when colliding with it
            Cargo cargo = (Cargo) object;
            vec2.set(cargo.getPosition())
                .sub(this.getPosition())
                .nor();
            float angle = vec2.angleDeg();

            if (Calc.between(angle, 0, 45) || Calc.between(angle, 315, 360)) {
                // slash right
                currentDirection = Direction.right;
                movement.set(1, 0);
                handleSlash();
            }
            else if (Calc.between(angle, 45, 135)) {
                // slash up
                currentDirection = Direction.up;
                movement.set(0, 1);
                handleSlash();
            }
            else if (Calc.between(angle, 135, 225)) {
                // slash left
                currentDirection = Direction.left;
                movement.set(-1, 0);
                handleSlash();
            }
            else if (Calc.between(angle, 225, 315)) {
                // slash down
                currentDirection = Direction.down;
                movement.set(0, -1);
                handleSlash();
            }
        }
    }

    @Override
    public boolean shouldCollideWith(Collidable object) {
        return true;
    }
}
