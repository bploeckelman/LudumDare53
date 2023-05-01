package lando.systems.ld53.entities.enemies;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import lando.systems.ld53.Config;
import lando.systems.ld53.Main;
import lando.systems.ld53.entities.Cargo;
import lando.systems.ld53.physics.CollisionShapeCircle;
import lando.systems.ld53.screens.GameScreen;
import lando.systems.ld53.utils.Utils;

public class InvaderEnemy extends Enemy {
    public static float chaseDist = 300;
    public static float chaseDist2 = chaseDist * chaseDist;

    float timeInPhase;
    Vector2 targetPosition;
    float waitTimer;
    Cargo heldCargo;
    Vector2 beamSegment = new Vector2();
    Color beamColor;
    float beamAccum;

    public InvaderEnemy(GameScreen screen, float x, float y) {
        super(screen, x, y);
        this.animation = screen.assets.gobbler;
        this.keyframe = animation.getKeyFrame(0f);

        this.targetPosition = new Vector2();
        timeInPhase = 0;
        waitTimer = 1f;
        beamColor = new Color(Color.RED);
        beamAccum = 0;
        friction = .2f;
    }

    public void update(float dt) {
        super.update(dt);
        beamAccum += dt;
        Utils.hsvToRgb(beamAccum, 1f, 1f, beamColor);
        timeInPhase += dt;
        if (timeInPhase > 2) {
            // It is stuck trying to get somewhere, try somewhere else
            timeInPhase = 0;
            targetPosition.set(MathUtils.random(Config.Screen.window_width), MathUtils.random(Config.Screen.window_height));
        }
        Cargo chasedCargo = null;
        float nearestCargoDist2 = Float.MAX_VALUE;
        for (Cargo c : screen.cargos) {
            float dist = getPosition().dst2(c.getPosition());
            if ( dist < nearestCargoDist2) {
                nearestCargoDist2 = dist;
                chasedCargo = c;
            }
        }
        if (nearestCargoDist2 < chaseDist2 && chasedCargo != null){
            targetPosition.set(chasedCargo.getPosition());
            timeInPhase = 0;
            float attractionRadius = circle.radius + ((CollisionShapeCircle)chasedCargo.getCollisionShape()).radius + 80;
            if (attractionRadius * attractionRadius > nearestCargoDist2) {
                // TODO: particles, attract sound
                heldCargo = chasedCargo;
                heldCargo.getVelocity().add(targetPosition.sub(getPosition()).scl(-1).nor().scl(50));
                targetPosition.set(screen.player.getPosition()).sub(getPosition()).scl(-1).add(getPosition());

            } else {
                heldCargo = null;
            }
        } else {
            heldCargo = null;
        }

        float speed = 100f;
        if (heldCargo != null){
            speed = 400f;
        }
        v.set(targetPosition).sub(getPosition()).nor().scl(speed * dt);
        velocity.add(v);
    }

    @Override
    public void render(SpriteBatch batch) {
        // draw beam
        if (heldCargo != null) {
            beamSegment.set(heldCargo.getPosition()).sub(getPosition());
            float width = 2f;
            batch.setColor(beamColor);
            batch.draw(Main.game.assets.pixelRegion, getPosition().x, getPosition().y - width/2f, 0, width/2f, beamSegment.len(), width, 1, 1, beamSegment.angleDeg());
            batch.setColor(Color.WHITE);
        }
        super.render(batch);
    }

}
