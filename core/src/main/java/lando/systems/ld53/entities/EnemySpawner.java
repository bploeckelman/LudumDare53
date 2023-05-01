package lando.systems.ld53.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import lando.systems.ld53.entities.enemies.*;
import lando.systems.ld53.screens.GameScreen;

public class EnemySpawner implements Entity {
    public static int MAX_SPAWNS = 5;
    public static float SPAWN_DELAY = 10;
    public enum EnemyType {cargo_eater, attract, repulse, invader, random}

    private EnemyType spawnType;
    Vector2 position;
    GameScreen screen;
    float spawnTimer;

    Array<Enemy> spawnedEnemies;


    public EnemySpawner(GameScreen screen, Vector2 position, EnemyType spawnType) {
        this.screen = screen;
        this.position = position;
        this.spawnType = spawnType;
        spawnedEnemies = new Array<>();
        spawnTimer = 0;
    }


    @Override
    public void update(float dt) {
        for (int i = spawnedEnemies.size -1; i >= 0; i--){
            if (!spawnedEnemies.get(i).isAlive()) spawnedEnemies.removeIndex(i);
        }
        spawnTimer -= dt;
        if (spawnTimer < 0 && spawnedEnemies.size < MAX_SPAWNS) {
            spawnTimer = SPAWN_DELAY;
            Enemy e = getEnemy();
            spawnedEnemies.add(e);
            screen.enemies.add(e);
            // TODO: do we need a sound here?
        }
    }

    private Enemy getEnemy() {
        EnemyType type = spawnType;
        if (type == EnemyType.random) {
            switch(MathUtils.random(3)){
                case 0: type = EnemyType.cargo_eater; break;
                case 1: type = EnemyType.attract; break;
                case 2: type = EnemyType.repulse; break;
                case 3: type = EnemyType.invader; break;
                default:
                    Gdx.app.log("Enemy Spawner", "This is a bug somehow?");
                    type = EnemyType.cargo_eater;
            }
        }
        switch (type) {
            case cargo_eater: return new CargoEatingEnemy(screen, position.x, position.y);
            case attract: return new AttractMineEnemy(screen, position.x, position.y);
            case repulse: return new RepulseMineEnemy(screen, position.x, position.y);
            case invader: return new InvaderEnemy(screen, position.x, position.y);
        }
        return new CargoEatingEnemy(screen, position.x, position.y);
    }

    @Override
    public void render(SpriteBatch batch) {

    }
}
