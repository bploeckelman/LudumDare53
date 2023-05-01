package lando.systems.ld53.entities;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import lando.systems.ld53.Main;
import lando.systems.ld53.physics.Collidable;
import lando.systems.ld53.physics.Influencer;

public class Goal implements Entity, Influencer {

    public enum Type {
        cyan
        , red
        ;
        public Animation<TextureRegion> anim;
        public Animation<TextureRegion> baseAnim;
        public Animation<TextureRegion> shimmerAnim;
    }

    private final Rectangle bounds;
    private final Vector2 attractorPosition;
    private final float range;
    private final float strength;
    private final Type type;

    private TextureRegion keyframe;
    private TextureRegion baseKeyframe;
    private TextureRegion shimmerKeyframe;
    private float animTime;

    public Goal(RectangleMapObject rectMapObject) {
        Rectangle rect = rectMapObject.getRectangle();
        String colorProp = rectMapObject.getProperties().get("color", "red", String.class);
        this.bounds = new Rectangle(rect);
        this.type = Type.valueOf(colorProp);
        this.keyframe = type.anim.getKeyFrame(0f);
        this.shimmerKeyframe = type.shimmerAnim.getKeyFrame(0f);
        this.baseKeyframe = type.baseAnim.getKeyFrame(0f);
        this.animTime = 0f;
        this.attractorPosition = new Vector2();
        bounds.getCenter(attractorPosition);
        this.range = Math.max(bounds.width, bounds.height);
        this.strength = 900;
    }

    @Override
    public void update(float delta) {
        animTime += delta;
        keyframe = type.anim.getKeyFrame(animTime);
        shimmerKeyframe = type.shimmerAnim.getKeyFrame(animTime);
        baseKeyframe = type.baseAnim.getKeyFrame(animTime);
    }

    public void tryToCollectPackage(Cargo b) {
        float dist = b.getPosition().dst(attractorPosition);
        if (dist < range /3f) {
            // TODO: Collected it do things like score
            b.collected = true;
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.draw(baseKeyframe, bounds.x, bounds.y, bounds.width, bounds.height);
        batch.draw(keyframe, bounds.x, bounds.y, bounds.width, bounds.height);
        batch.draw(shimmerKeyframe, bounds.x, bounds.y, bounds.width, bounds.height);
    }


    @Override
    public float getStrength() {
        return strength;
    }

    @Override
    public Vector2 getPosition() {
        return attractorPosition;
    }

    @Override
    public float getRange() {
        return range;
    }

    @Override
    public boolean shouldEffect(Collidable c) {
        if (c instanceof Cargo) return true;
        return false;
    }

    @Override
    public void debugRender(SpriteBatch batch) {

    }
}
