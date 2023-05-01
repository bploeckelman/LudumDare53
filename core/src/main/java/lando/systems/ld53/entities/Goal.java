package lando.systems.ld53.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import lando.systems.ld53.Main;
import lando.systems.ld53.audio.AudioManager;
import lando.systems.ld53.physics.Collidable;
import lando.systems.ld53.physics.Influencer;
import lando.systems.ld53.screens.GameScreen;
import lando.systems.ld53.utils.InfluenceRenderer;

public class Goal implements Entity, Influencer {

    public enum Type {
        cyan (Color.CYAN)
        , red (Color.RED)
        , green (Color.GREEN)
        , yellow (Color.YELLOW)
        ;
        Type(Color color)
        {
            this.color = color;
        }
        public Animation<TextureRegion> anim;
        public Animation<TextureRegion> baseAnim;
        public Animation<TextureRegion> shimmerAnim;
        public TextureRegion icon;

        public Color color;

        public static Type getRandom(Type lastSpawned) {
            int getIndex = MathUtils.random(0, 3);
            Type randomType = null;
            switch (getIndex) {
                case 0:
                    randomType = Type.red == lastSpawned ?  Type.green : Type.red ;

                    break;
                case 1:
                    randomType = Type.green == lastSpawned ?  Type.yellow : Type.green ;
                    break;
                case 2:
                    randomType = Type.yellow == lastSpawned ?  Type.cyan : Type.yellow ;
                    break;
                case 3:
                    randomType = Type.cyan == lastSpawned ?  Type.red : Type.cyan ;
                    break;
            }
            return randomType;
        }
    }

    private final Rectangle bounds;
    private final Vector2 attractorPosition;
    private final InfluenceRenderer influenceRenderer;
    private final float range;
    private final float strength;
    private final Type type;

    private TextureRegion keyframe;
    private TextureRegion baseKeyframe;
    private TextureRegion shimmerKeyframe;
    private TextureRegion icon;
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
        this.range = Math.max(bounds.width, bounds.height) * 1.5f;
        this.strength = 1500;
        influenceRenderer = new InfluenceRenderer(this, type.color);
        this.icon = type.icon;

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
        if (dist < range / 3f) {
            if(b.goalType == type) {
                Main.game.audioManager.playSound(AudioManager.Sounds.collect, .5f);
                // TODO: Collected it do things like score
                b.collected = true;
            }

        }
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.draw(baseKeyframe, bounds.x, bounds.y, bounds.width, bounds.height);
        batch.draw(keyframe, bounds.x, bounds.y, bounds.width, bounds.height);
        batch.draw(shimmerKeyframe, bounds.x, bounds.y, bounds.width, bounds.height);
        batch.draw(icon, bounds.x + bounds.width / 4, bounds.y + bounds
            .height / 4, bounds.width / 2, bounds.height / 2);
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
        if (c instanceof Cargo) {
            Cargo ca = (Cargo) c;
            if (ca.goalType == this.type) return true;
            return false;
        }
        return false;
    }

    @Override
    public void debugRender(SpriteBatch batch) {

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
