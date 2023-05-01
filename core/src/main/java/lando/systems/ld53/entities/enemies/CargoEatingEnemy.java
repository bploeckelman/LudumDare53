package lando.systems.ld53.entities.enemies;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import lando.systems.ld53.Assets;
import lando.systems.ld53.Config;
import lando.systems.ld53.entities.Cargo;
import lando.systems.ld53.physics.CollisionShapeCircle;
import lando.systems.ld53.screens.GameScreen;

public class CargoEatingEnemy extends Enemy {
    public static float chaseDist = 300;
    public static float chaseDist2 = chaseDist * chaseDist;

    float timeInPhase;
    Vector2 targetPosition;



    public CargoEatingEnemy(GameScreen screen, float x, float y) {
        super(screen, x, y);
        this.targetPosition = new Vector2(Config.Screen.window_width/2f, Config.Screen.window_height/2f);
        timeInPhase = 0;
    }

    @Override
    public void update(float dt) {
        super.update(dt);
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
            float collectionRadius = circle.radius + ((CollisionShapeCircle)chasedCargo.getCollisionShape()).radius + 2;
            if (collectionRadius * collectionRadius > nearestCargoDist2) {
                // TODO: particles, chomp sound
                chasedCargo.lifetime -= 2f * dt;
                screen.particles.gobblerGobble(targetPosition.x, targetPosition.y);
            }
        }

        float speed = 200f;
        v.set(targetPosition).sub(getPosition()).nor().scl(speed * dt);
        velocity.add(v);
    }
}
