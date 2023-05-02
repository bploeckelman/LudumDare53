package lando.systems.ld53.entities.enemies;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import lando.systems.ld53.entities.RepulseMine;
import lando.systems.ld53.screens.GameScreen;

public class RepulseMineEnemy extends Enemy{

    float timeInPhase;
    Vector2 targetPosition;
    float waitTimer;

    public RepulseMineEnemy(GameScreen screen, float x, float y) {
        super(screen, x, y);
        this.animation = screen.assets.turtle;
        this.keyframe = animation.getKeyFrame(0f);

        this.targetPosition = new Vector2();
        timeInPhase = 0;
        waitTimer = 1f;
        updateSize(30);
    }

    @Override
    public void update(float dt) {
        super.update(dt);
        timeInPhase += dt;
        if (waitTimer > 0) {
            waitTimer -= dt;
            if (waitTimer <= 0) timeInPhase += 20;
        }

        if (timeInPhase > 10) {
            // It is stuck trying to get somewhere, try somewhere else
            timeInPhase = 0;
            targetPosition.set(MathUtils.random(targetBounds.x , targetBounds.x + targetBounds.width),
                MathUtils.random(targetBounds.y, targetBounds.y + targetBounds.height));
        }


        if (waitTimer > 0) return;

        float dist2 = targetPosition.dst2(getPosition());
        if (dist2 < (circle.radius + 20) * (circle.radius + 20)) {
            // TODO: sound when it gets created?
            RepulseMine mine = new RepulseMine(screen, targetPosition);
            screen.influencers.add(mine);
            waitTimer = 1f;
        }

        float speed = 150f;
        v.set(targetPosition).sub(getPosition()).nor().scl(speed * dt);
        velocity.add(v);
    }
}
