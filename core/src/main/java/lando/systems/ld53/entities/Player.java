package lando.systems.ld53.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import lando.systems.ld53.Assets;
import lando.systems.ld53.Config;
import lando.systems.ld53.Main;
import lando.systems.ld53.audio.AudioManager;
import lando.systems.ld53.screens.BaseScreen;
import lando.systems.ld53.screens.GameScreen;
import lando.systems.ld53.physics.Collidable;
import lando.systems.ld53.physics.CollisionShape;
import lando.systems.ld53.physics.CollisionShapeCircle;

import java.util.HashMap;

public class Player implements Entity, Collidable {
    private static float RADIUS = 30;
    float COLLISION_MARGIN = 10f;


    private final Animation<TextureRegion> playerIdle;
    private Animation<TextureRegion> currentPlayerAnimation;
    private TextureRegion playerImage;
    private Assets assets;
    private State currentState;
    private float animTime = 0;
    private float attackTime = 0;
    private Direction currentDirection;
    public Vector2 position;
    public Vector2 movementVector;
    public Vector2 velocity;
    float friction;
    float mass;
    CollisionShapeCircle collisionShape;
    Rectangle collisionBounds;

    private float tempSpeed = 20f; //TODO: Replace speed usage with physics system
    private HashMap<State, Animation<TextureRegion>> animations = new HashMap<>();
    private boolean isAttacking = false;

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
    }

    public enum Direction { up, down, left, right, up_left, up_right, down_left, down_right }

    public Player(Assets assets) {
        playerIdle = assets.playerIdleDown;
        currentPlayerAnimation = playerIdle;
        currentDirection = Direction.down;

        position = new Vector2(Config.Screen.window_width / 2f, Config.Screen.window_height / 2f);
        this.velocity = new Vector2();
        this.mass = 20;
        this.friction = .01f;
        this.collisionShape = new CollisionShapeCircle(RADIUS, position.x, position.y);
        this.collisionBounds = new Rectangle(new Rectangle(position.x - RADIUS - COLLISION_MARGIN, position.y - RADIUS - COLLISION_MARGIN, (RADIUS+COLLISION_MARGIN)*2f , (RADIUS+COLLISION_MARGIN)*2f));

        movementVector = new Vector2();
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
    }

    @Override
    public void update(float delta) {
        animTime += delta;
        movementVector.setZero();
        if (Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP))    movementVector.y = 1;
        if (Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN))  movementVector.y -= 1;
        if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) movementVector.x = 1;
        if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT))  movementVector.x -= 1;
        movementVector.nor();
        //position.add(movementVector.x * tempSpeed * delta, movementVector.y * tempSpeed * delta);
        velocity.add(movementVector.x * tempSpeed, movementVector.y * tempSpeed);
        // Player is attacking
        if (isAttacking) {
            if (currentPlayerAnimation.isAnimationFinished(attackTime)) {
                currentState = State.idle_down;
                attackTime = 0;
                isAttacking = false;
            }
        }
        // Player is not moving
        else if (movementVector.equals(Vector2.Zero)) {
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
            if (movementVector.x > 0) {
                currentState = State.walk_right;
                currentDirection = Direction.right;
                if (movementVector.y > 0) {
                    currentDirection = Direction.up_right;
                }
                else if (movementVector.y < 0) {
                    currentDirection = Direction.down_right;
                }
            }
            else if (movementVector.x < 0) {
                currentState = State.walk_left;
                currentDirection = Direction.left;
                if (movementVector.y > 0) {
                    currentDirection = Direction.up_left;
                }
                else if (movementVector.y < 0) {
                    currentDirection = Direction.down_left;
                }
            }
            else {
                if (movementVector.y > 0) {
                    currentState = State.walk_up;
                    currentDirection = Direction.up;
                }
                else if (movementVector.y < 0) {
                    currentState = State.walk_down;
                    currentDirection = Direction.down;
                }
            }
        }
        // If player slashes
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            handleSlash();
        }
        // If player does 360
        if (Gdx.input.isKeyJustPressed(Input.Keys.Q)) {
            isAttacking = true;
            currentState = State.slash_360;
            Main.game.audioManager.playSound(AudioManager.Sounds.bigswoosh);
        }
        //set player image based on currentState
        currentPlayerAnimation = animations.get(currentState);
        if (isAttacking) {
            attackTime += delta;
            playerImage = currentPlayerAnimation.getKeyFrame(attackTime);
        } else {
            playerImage = currentPlayerAnimation.getKeyFrame(animTime);
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.draw(playerImage, position.x - RADIUS, position.y - RADIUS, RADIUS*2f, RADIUS*2f);
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
        return position;
    }

    @Override
    public void setPosition(float x, float y) {
        this.position.set(x, y);
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

    }

    @Override
    public boolean shouldCollideWith(Collidable object) {
        return true;
    }
}
