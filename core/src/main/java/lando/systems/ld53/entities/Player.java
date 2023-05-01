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
import lando.systems.ld53.Assets;
import lando.systems.ld53.Main;
import lando.systems.ld53.assets.InputPrompts;
import lando.systems.ld53.audio.AudioManager;
import lando.systems.ld53.physics.Collidable;
import lando.systems.ld53.physics.CollisionShape;
import lando.systems.ld53.physics.CollisionShapeCircle;
import lando.systems.ld53.utils.Calc;
import space.earlygrey.shapedrawer.ShapeDrawer;

import java.util.HashMap;

public class Player implements Entity, Collidable {

    private static final Vector2 COLLISION_OFFSET = new Vector2(0, -22f);
    private static final float COLLISION_RADIUS = 25;
    private static final float RENDER_SIZE = 120f;
    private static final float SPEED = 3000f;
    private static final float MAX_STAMINA = 10f; // seconds to charge fully
    private static final float SPECIAL_COST = 2f; //TODO: ability specific cost set in enum of abilities

    private final HashMap<State, Animation<TextureRegion>> animations = new HashMap<>();
    private final Vector2 vec2 = new Vector2();
    private final Vector2 position;
    private final Vector2 movement;
    private final Vector2 velocity;
    private final Rectangle renderBounds;
    private final Rectangle collisionBounds;
    private final CollisionShapeCircle collisionShape;

    private Animation<TextureRegion> animation;
    private TextureRegion keyframe;
    private State currentState;
    private Direction currentDirection;

    private boolean isAttacking = false;
    private boolean isStunned = false;
    private float animTimer = 0;
    private float attackTimer = 0;
    private float stunTimer = 0;
    private float stamina = MAX_STAMINA;

    public float mass = 20f;
    public float friction = 0.001f;
    public SpecialAbility currentAbility = SpecialAbility.slash_360;

    public enum SpecialAbility {
        //TODO: some descriptions are non-sense, make it sensical. Must have 5 :(
        slash_360(InputPrompts.Type.key_light_at, "@ attack!", "Slash your 1 360 degrees", true),
        hash_attack(InputPrompts.Type.key_light_hash, "# attack!", "Hash your 1 out", false),
        bomb_attack(InputPrompts.Type.key_light_bang, "! attack?","Bang your bomb", false),
        plus_attack(InputPrompts.Type.key_light_plus, "+ attack!", "Add packet", false),
        minus_attack(InputPrompts.Type.key_light_minus, "- attack!", "Remove bug", false);


        public final InputPrompts.Type type; // TODO(brian) - eventually want an Animation for the icon instead of a ref to the input prompts sheet (scales up poorly)
        public final String title;
        public final String description;
        public boolean isUnlocked;
        SpecialAbility(InputPrompts.Type type, String title, String description, boolean isUnlocked) {
            this.title = title;
            this.type = type;
            this.description = description;
            this.isUnlocked = isUnlocked;
        }
    }
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

        animation = animations.get(State.idle_down);
        currentDirection = Direction.down;
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
            if (animation.isAnimationFinished(stunTimer)) {
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
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) || Gdx.input.justTouched()) {
            if (stamina < SPECIAL_COST) {
                Main.game.audioManager.playSound(AudioManager.Sounds.error, .35f);
            } else {
                isAttacking = true;
                stamina -= SPECIAL_COST;
                switch(currentAbility) {
                    case slash_360:
                        currentState = State.slash_360;
                        Main.game.audioManager.playSound(AudioManager.Sounds.bigswoosh, .26f);
                }
            }
        }


        //set player image based on currentState
        animation = animations.get(currentState);
        if (isAttacking) {
            attackTimer += delta;
            keyframe = animation.getKeyFrame(attackTimer);
        }
        else if (isStunned) {
            stunTimer += delta;
            keyframe = animation.getKeyFrame(stunTimer);
        }
        else {
            keyframe = animation.getKeyFrame(animTimer);
            velocity.add(movement.x * SPEED * delta, movement.y * SPEED * delta);
        }
    }

    public float getStaminaPercentage() {
        return stamina / MAX_STAMINA * 100f;
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.draw(keyframe, renderBounds.x, renderBounds.y, renderBounds.width, renderBounds.height);
    }

    private static final Color debugColor = new Color(50f / 255f, 205f / 255f, 50f / 255f, 0.5f); // Color.LIME half alpha
    @Override
    public void renderDebug(ShapeDrawer shapes) {
        shapes.filledCircle(collisionShape.center, collisionShape.radius, debugColor);
    }

    private void handleSlash() {
        isAttacking = true;

        Main.game.audioManager.playSound(AudioManager.Sounds.swoosh);

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
