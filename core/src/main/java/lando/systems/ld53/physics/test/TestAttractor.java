package lando.systems.ld53.physics.test;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import lando.systems.ld53.Main;
import lando.systems.ld53.physics.Collidable;
import lando.systems.ld53.physics.Influencer;
import lando.systems.ld53.utils.InfluenceRenderer;

public class TestAttractor implements Influencer {
    Vector2 position;
    float strength;
    float range;
    InfluenceRenderer influenceRenderer;

    public TestAttractor(Vector2 pos) {
        this.position = pos;
        this.strength = 500;
        this.range = 150;
        influenceRenderer = new InfluenceRenderer(this, Color.GREEN);
    }


    @Override
    public float getStrength() {
        return strength;
    }

    @Override
    public Vector2 getPosition() {
        return position;
    }

    @Override
    public float getRange() {
        return range;
    }

    public void debugRender(SpriteBatch batch) {
        batch.setColor(0f, 1f, 1f, .5f);
        batch.draw(Main.game.assets.ring, position.x - range, position.y - range, range*2f, range*2f);
        batch.setColor(Color.WHITE);
    }

    @Override
    public boolean shouldEffect(Collidable c) {
        return true;
    }

    @Override
    public void updateInfluence(float dt) {
        influenceRenderer.update(dt);
    }

    @Override
    public void renderInfluence(SpriteBatch batch) {
        influenceRenderer.render(batch);
    }
}
