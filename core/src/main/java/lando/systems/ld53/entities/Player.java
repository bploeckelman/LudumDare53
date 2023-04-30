package lando.systems.ld53.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import lando.systems.ld53.Assets;
import lando.systems.ld53.Config;

import java.util.HashMap;

public class Player implements Entity{

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
    private float tempSpeed = 400f; //TODO: Replace speed usage with physics system
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
        position.add(movementVector.x * tempSpeed * delta, movementVector.y * tempSpeed * delta);
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
        batch.draw(playerImage, position.x, position.y);
    }

    private void handleSlash() {
        isAttacking = true;
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
}
