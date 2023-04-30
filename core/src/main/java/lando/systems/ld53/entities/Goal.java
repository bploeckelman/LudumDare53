package lando.systems.ld53.entities;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;

public class Goal implements Entity {

    public enum Type {
        cyan
        ;
        public Animation<TextureRegion> anim;
    }

    private final Rectangle bounds;
    private final Type type;

    private TextureRegion keyframe;
    private float animTime;

    public Goal(RectangleMapObject rectMapObject) {
        Rectangle rect = rectMapObject.getRectangle();
        String colorProp = rectMapObject.getProperties().get("color", "cyan", String.class);
        this.bounds = new Rectangle(rect);
        this.type = Type.valueOf(colorProp);
        this.keyframe = type.anim.getKeyFrame(0f);
        this.animTime = 0f;
    }

    @Override
    public void update(float delta) {
        animTime += delta;
        keyframe = type.anim.getKeyFrame(animTime);
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.draw(keyframe, bounds.x, bounds.y, bounds.width, bounds.height);
    }

}
