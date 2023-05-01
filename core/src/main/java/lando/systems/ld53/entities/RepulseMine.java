package lando.systems.ld53.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import lando.systems.ld53.physics.Collidable;
import lando.systems.ld53.physics.Influencer;
import lando.systems.ld53.screens.GameScreen;
import lando.systems.ld53.utils.InfluenceRenderer;
import lando.systems.ld53.utils.interpolationobjects.InterpolatingFloat;

public class RepulseMine implements Influencer {
    public static float TIME_TO_LIVE = 6f;

    Vector2 position = new Vector2();
    InterpolatingFloat strength;
    InterpolatingFloat range;
    InfluenceRenderer influenceRenderer;
    GameScreen screen;

    public RepulseMine(GameScreen screen, Vector2 position) {
        this.screen = screen;
        this.position.set(position);
        this.strength = new InterpolatingFloat(-600);
        strength.setNewValue(-100, TIME_TO_LIVE);
        this.range = new InterpolatingFloat(150f);
        range.setNewValue(10, TIME_TO_LIVE);
        this.influenceRenderer = new InfluenceRenderer(this, Color.SALMON);
    }

    @Override
    public float getStrength() {
        return strength.getValue();
    }

    @Override
    public Vector2 getPosition() {
        return position;
    }

    @Override
    public float getRange() {
        return range.getValue();
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
        range.update(dt);
        strength.update(dt);
        if (strength.getValue() >= -110) {
            screen.influencers.removeValue(this, true);
        }
        influenceRenderer.update(dt);
    }

    @Override
    public void renderInfluence(SpriteBatch batch) {
        influenceRenderer.render(batch);
    }
}
