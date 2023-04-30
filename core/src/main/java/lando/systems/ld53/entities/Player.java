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
    private float _animTime = 0;
    private Direction currentDirection;
    public Vector2 position;
    public Vector2 movementVector;
    private float tempSpeed = 200f; //TODO: Replace speed usage with physics system
    private HashMap<State, Animation<TextureRegion>> animations = new HashMap<>();

    public enum State {
        idle,
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

    public enum Direction { top, bottom, left, right }

    public Player(Assets assets) {
        playerIdle = assets.playerIdle;
        currentPlayerAnimation = playerIdle;
        currentDirection = Direction.bottom;
        position = new Vector2(Config.Screen.window_width / 2, Config.Screen.window_height / 2);
        movementVector = new Vector2();
        animations.put(State.idle, assets.playerIdle);
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
        _animTime += delta;
        movementVector.setZero();
        if (Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP))    movementVector.y = 1;
        if (Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN))  movementVector.y -= 1;
        if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) movementVector.x = 1;
        if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT))  movementVector.x -= 1;
        if (movementVector.equals(Vector2.Zero)) {
            currentState = State.idle;
        }
        else {
            if (movementVector.y > 0) {
                currentState = State.walk_up;
            }
            else if (movementVector.y < 0) {
                currentState = State.walk_down;
            }
            if (movementVector.x > 0) {
                currentState = State.walk_right;
            }
            else if (movementVector.x < 0) {
                currentState = State.walk_left;
            }
        }
        currentPlayerAnimation = animations.get(currentState);
        playerImage = currentPlayerAnimation.getKeyFrame(_animTime);
        movementVector.nor();
        position.add(movementVector.x * tempSpeed * delta, movementVector.y * tempSpeed * delta);
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.draw(playerImage, position.x, position.y);
    }
}
