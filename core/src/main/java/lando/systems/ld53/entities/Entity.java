package lando.systems.ld53.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public interface Entity {
    public void update(float delta);
    public void render(SpriteBatch batch);
}
