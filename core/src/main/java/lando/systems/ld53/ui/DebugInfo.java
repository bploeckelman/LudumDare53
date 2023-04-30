package lando.systems.ld53.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import lando.systems.ld53.Assets;
import lando.systems.ld53.Config;

public class DebugInfo {
    public static Array<Long> physicsMS = new Array<>();

    public static void addPhysicsStep(long time) {
        physicsMS.add(time);
        while (physicsMS.size > 60) physicsMS.removeIndex(0);
    }

    public static void render(Assets assets) {
        float spacing = 20;
        float y = Config.Screen.window_height;
        assets.batch.begin();
        assets.font.getData().setScale(.5f);
        assets.font.draw(assets.batch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 0, y);
        y -= spacing;
        long physicsSmooth = 0;
        long longestPhysics = 0;
        for (long p : physicsMS){
            physicsSmooth += p;
            longestPhysics = Math.max(longestPhysics, p);
        }
        physicsSmooth /= Math.max(1, physicsMS.size);
        assets.font.draw(assets.batch, "Physics: " + physicsSmooth+"ms", 0, y);
        y -= spacing;
        assets.font.draw(assets.batch, "Longest Step: " + longestPhysics+"ms", 0, y);

        assets.font.getData().setScale(1);
        assets.batch.end();

    }
}
