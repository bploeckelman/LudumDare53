package lando.systems.ld53.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import lando.systems.ld53.Main;
import lando.systems.ld53.physics.Collidable;
import lando.systems.ld53.physics.Influencer;
import lando.systems.ld53.screens.GameScreen;
import lando.systems.ld53.utils.InfluenceRenderer;

public class PlayerPersonalRepulsor implements Influencer {

    private final Player player;
    private final float DURATION = 5f;
    private final float STRENGTH = -2500f;
    private final float RANGE = 150f;
    private final InfluenceRenderer renderer;

    private float timer = 0f;
    private boolean active = false;

    public PlayerPersonalRepulsor(Player player) {
        this.player = player;
        // TODO - make it rainbow if we have time
        this.renderer = new InfluenceRenderer(this, Color.PURPLE);
    }

    public void activate() {
        active = true;
        timer = DURATION;
        GameScreen currentScreen = (GameScreen) Main.game.getScreen();
        currentScreen.swapMusic();
    }

    public void update(float delta) {
        if (active) {
            timer -= delta;
            if (timer <= 0) {
                timer = 0;
                active = false;
                GameScreen currentScreen = (GameScreen) Main.game.getScreen();
                currentScreen.swapMusic();
            }
        }
    }

    public boolean isActive() {
        return active;
    }

    @Override
    public float getStrength() {
        return active ? STRENGTH : 0;
    }

    @Override
    public Vector2 getPosition() {
        return player.getPosition();
    }

    @Override
    public float getRange() {
        return active ? RANGE : 0;
    }

    @Override
    public void debugRender(SpriteBatch batch) {}

    @Override
    public boolean shouldEffect(Collidable c) {
        if (!active) return false;
        if (c instanceof Player
         || c instanceof Cargo) {
            return false;
        }
        return true;
    }

    @Override
    public void updateInfluence(float dt) {
        if (!active) return;
        renderer.update(dt);
    }

    @Override
    public void renderInfluence(SpriteBatch batch) {
        if (!active) return;
        renderer.render(batch);
    }

}
