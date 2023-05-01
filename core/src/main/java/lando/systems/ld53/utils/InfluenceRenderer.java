package lando.systems.ld53.utils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import lando.systems.ld53.Main;
import lando.systems.ld53.physics.Influencer;

public class InfluenceRenderer {
    Influencer influencer;
    ShaderProgram shader;
    Texture texture;
    float accum;
    Rectangle bounds;
    Color color;

    public InfluenceRenderer(Influencer influencer, Color color) {
        this.color = color;
        this.influencer = influencer;
        shader = Main.game.assets.influencerShader;
        this.accum = 0;
        this.texture = Main.game.assets.pixel;
        Vector2 center = influencer.getPosition();
        float range = influencer.getRange();
        bounds = new Rectangle(center.x - range, center.y - range, range*2f, range*2f);
    }

    public void update(float dt) {
        accum += dt * (-influencer.getStrength()/5000f);
        Vector2 center = influencer.getPosition();
        float range = influencer.getRange();
        bounds.set(center.x - range, center.y - range, range*2f, range*2f);
    }

    public void render(SpriteBatch batch) {
        if (influencer.getStrength() != 0) {
            batch.setShader(shader);
            batch.setColor(color);
            shader.setUniformf("u_time", accum);
            batch.draw(texture, bounds.x, bounds.y, bounds.width, bounds.height);
            batch.setColor(Color.WHITE);
            batch.setShader(null);
        }
    }
}
