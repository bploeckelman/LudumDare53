package lando.systems.ld53.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import lando.systems.ld53.audio.AudioManager;
import lando.systems.ld53.screens.GameScreen;

public class CargoSpawner implements Entity {

    GameScreen screen;
    Vector2 position;
    float timer;
    Goal.Type lastSpawnedType;

    public CargoSpawner(GameScreen screen, float x, float y) {
        this.screen = screen;
        this.position = new Vector2(x, y);
        this.lastSpawnedType = Goal.Type.yellow;
    }

    @Override
    public void update(float delta) {
        timer -= delta;
        if (timer < 0) {
            lastSpawnedType = Goal.Type.getRandom(lastSpawnedType);
            if (doWeNeed(lastSpawnedType)){
                screen.cargos.add(new Cargo(screen.assets, lastSpawnedType, position.x, position.y));
                timer = 3f;
                screen.audioManager.playSound(AudioManager.Sounds.spawn, .0626f);
            }
        }
    }

    private boolean doWeNeed(Goal.Type type) {
        int currently = screen.collectedMap.get(type);
        for (Cargo c : screen.cargos){
            if (c.goalType == type) currently++;
        }
        return currently < screen.needToCollectMap.get(type);
    }

    @Override
    public void render(SpriteBatch batch) {

    }
}
