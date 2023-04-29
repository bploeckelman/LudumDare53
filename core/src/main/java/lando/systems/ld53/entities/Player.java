package lando.systems.ld53.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import lando.systems.ld53.Assets;
import lando.systems.ld53.Config;

public class Player implements Entity{

    private final Animation<TextureRegion> playerIdle;
    private final Animation<TextureRegion> playerMove;
    private Animation<TextureRegion> currentPlayerAnimation;
    private float _animTime = 0;
    private Direction currentDirection;
    public Vector2 position;
    public Vector2 movementVector;
    private float tempSpeed = 200f; //TODO: Replace speed usage with physics system

    public enum State {
        idle,
        move,
    }

    public enum Direction { top, bottom, left, right }

    public Player(Assets assets) {
        playerIdle = assets.playerIdle;
        playerMove = assets.playerMove;
        currentPlayerAnimation = playerIdle;
        currentDirection = Direction.left;
        position = new Vector2(Config.Screen.window_width / 2, Config.Screen.window_height / 2);
        movementVector = new Vector2();
    }

    @Override
    public void update(float delta) {
        _animTime += delta;
        movementVector.setZero();
        if (Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP))    movementVector.y = 1;
        if (Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN))  movementVector.y -= 1;
        if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) movementVector.x = 1;
        if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT))  movementVector.x -= 1;
        movementVector.nor();
        if (movementVector.equals(Vector2.Zero)) {
            currentPlayerAnimation = playerIdle;
        }
        else {
            currentPlayerAnimation = playerMove;
        }

        position.add(movementVector.x * tempSpeed * delta, movementVector.y * tempSpeed * delta);
    }

    @Override
    public void render(SpriteBatch batch) {
        TextureRegion playerImage = currentPlayerAnimation.getKeyFrame(_animTime);
        batch.draw(playerImage, position.x, position.y);
    }
}
