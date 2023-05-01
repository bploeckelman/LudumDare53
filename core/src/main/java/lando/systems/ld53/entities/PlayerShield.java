package lando.systems.ld53.entities;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import lando.systems.ld53.Main;
import lando.systems.ld53.screens.GameScreen;

public class PlayerShield implements Entity {

    private final Player player;
    private final Animation<TextureRegion> animation;
    private final float DURATION = 3f;

    private float animTimer = 0f;
    private float timer = 0f;
    private boolean active = false;

    public PlayerShield(Player player) {
        this.player = player;
        this.animation = Main.game.assets.shield;
    }

    public void activate() {
        active = true;
        timer = DURATION;
        animTimer = 0f;
        GameScreen currentScreen = (GameScreen) Main.game.getScreen();
        currentScreen.swapMusic();
    }

    public boolean isActive() {
        return active;
    }

    @Override
    public void update(float delta) {
        if (active) {
            timer -= delta;
            if (timer <= 0) {
                timer = 0;
                active = false;
                GameScreen currentScreen = (GameScreen) Main.game.getScreen();
                currentScreen.swapMusic();
            } else {
                animTimer += delta;
            }
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        if (!active) return;
        TextureRegion keyframe = animation.getKeyFrame(animTimer);
        Rectangle bounds = player.collisionBounds;
        float margin = 35f;
        batch.draw(keyframe,
            bounds.x - margin / 2f,
            bounds.y - margin / 2f,
            bounds.width + margin,
            bounds.height + margin
        );
    }

}
