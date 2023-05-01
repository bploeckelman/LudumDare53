package lando.systems.ld53.particles;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.*;
import lando.systems.ld53.Assets;

public class Particles implements Disposable {

    public enum Layer { background, middle, foreground }

    private static final int MAX_PARTICLES = 4000;

    private final Assets assets;
    private final ObjectMap<Layer, Array<Particle>> activeParticles;
    private final Pool<Particle> particlePool = Pools.get(Particle.class, MAX_PARTICLES);

    public Particles(Assets assets) {
        this.assets = assets;
        this.activeParticles = new ObjectMap<>();
        int particlesPerLayer = MAX_PARTICLES / Layer.values().length;
        this.activeParticles.put(Layer.background, new Array<>(false, particlesPerLayer));
        this.activeParticles.put(Layer.middle,     new Array<>(false, particlesPerLayer));
        this.activeParticles.put(Layer.foreground, new Array<>(false, particlesPerLayer));
    }

    public void clear() {
        for (Layer layer : Layer.values()) {
            particlePool.freeAll(activeParticles.get(layer));
            activeParticles.get(layer).clear();
        }
    }

    public void update(float dt) {
        for (Layer layer : Layer.values()) {
            for (int i = activeParticles.get(layer).size - 1; i >= 0; --i) {
                Particle particle = activeParticles.get(layer).get(i);
                particle.update(dt);
                if (particle.isDead()) {
                    activeParticles.get(layer).removeIndex(i);
                    particlePool.free(particle);
                }
            }
        }
    }

    public void draw(SpriteBatch batch, Layer layer) {
        activeParticles.get(layer).forEach(particle -> particle.render(batch));
    }

    @Override
    public void dispose() {
        clear();
    }

    // ------------------------------------------------------------------------
    // Helper fields for particle spawner methods
    // ------------------------------------------------------------------------
    private final Color tempColor = new Color();
    private final Vector2 tempVec2 = new Vector2();

    // ------------------------------------------------------------------------
    // Spawners for different particle effects
    // ------------------------------------------------------------------------

    public void bleed(float x, float y) {
        TextureRegion keyframe = assets.particles.circle;
        tempColor.set(Color.RED);
        int numParticles = 50;
        for (int i = 0; i < numParticles; ++i) {
            activeParticles.get(Layer.foreground).add(Particle.initializer(particlePool.obtain())
                    .keyframe(keyframe)
                    .startPos(x, y)
                    .velocityDirection(MathUtils.random(0, 360), MathUtils.random(-50, -25))
                    .startSize(MathUtils.random(10, 16))
                    .endSize(MathUtils.random(2, 8))
                    .startAlpha(1f)
                    .endAlpha(0f)
                    .timeToLive(1f)
                    .startColor(tempColor)
                    .init());
        }
    }

    public void gobblerGobble(float x, float y) {
        for (int i = 0; i < 5; i++) {

            activeParticles.get(Layer.foreground).add(Particle.initializer(particlePool.obtain())
                .keyframe(assets.particles.dollar)
                .startPos(x, y)
                .velocity(MathUtils.random(-100f, 100), MathUtils.random(-100f, 100f))
                    .startColor(.8f, 0f, 0f,1f)
                    .endColor(0,0,0,0)
                    .startSize(30f)
                    .endSize(0f)
                    .timeToLive(1f)
                .init()
            );
        }
    }

}
