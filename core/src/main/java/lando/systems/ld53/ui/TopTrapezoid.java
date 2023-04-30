package lando.systems.ld53.ui;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import lando.systems.ld53.Assets;
import lando.systems.ld53.Config;
import lando.systems.ld53.assets.InputPrompts;
import lando.systems.ld53.entities.Player;

public class TopTrapezoid {
    private PolygonSpriteBatch polygonSpriteBatch;
    private PolygonRegion trapezoidRegion;
    private PolygonSprite trapezoidSprite;
    private float TOP_WIDTH = 400f;
    private float BOTTOM_WIDTH = 300f;
    private float HEIGHT = 75f;
    private Vector2 ABILITY_ICON_SIZE = new Vector2(50f, 50f);
    private final Rectangle trapezoidBound;
    private float staminaPercentage = 100f;
    private Player player;
    private Assets assets;
    private ShaderProgram cooldownShader;
    public TopTrapezoid(Player player, Assets assets) {
        float x1 = 0f;   // left top vertex
        float y1 = HEIGHT;
        float x2 = TOP_WIDTH;;  // right top vertex
        float y2 = HEIGHT;
        float x3 = (TOP_WIDTH + BOTTOM_WIDTH) / 2;;  // right bottom vertex
        float y3 = 0;
        float x4 = (TOP_WIDTH - BOTTOM_WIDTH) / 2;   // left bottom vertex
        float y4 = 0;

        float[] vertices = {x1, y1, x2, y2, x3, y3, x4, y4};
        short[] triangles = {0, 1, 2, 2, 3, 0};
        trapezoidRegion = new PolygonRegion(assets.pixelRegion, vertices, triangles);
        polygonSpriteBatch = new PolygonSpriteBatch();
        trapezoidSprite = new PolygonSprite(trapezoidRegion);
        trapezoidSprite.setColor(Color.WHITE);
        trapezoidSprite.setPosition((Config.Screen.window_width - TOP_WIDTH) / 2,Config.Screen.window_height - HEIGHT);
        trapezoidBound = new Rectangle((Config.Screen.window_width - TOP_WIDTH) / 2,Config.Screen.window_height - HEIGHT, TOP_WIDTH, HEIGHT);
        this.player = player;
        this.assets = assets;
        this.cooldownShader = assets.cooldownShader;
    }

    public void dispose() {
        polygonSpriteBatch.dispose();
    }
    public void render(SpriteBatch batch) {
//        polygonSpriteBatch.begin();
//        polygonSpriteBatch.setShader(cooldownShader);
//        cooldownShader.setUniformf("u_percent", staminaPercentage);
//        trapezoidSprite.draw(polygonSpriteBatch);
//        polygonSpriteBatch.setShader(null);
//        assets.font.draw(polygonSpriteBatch, staminaPercentage + "%", (Config.Screen.window_width - TOP_WIDTH) / 2,Config.Screen.window_height - HEIGHT);
//        polygonSpriteBatch.end();
        batch.begin();
        batch.draw(assets.trapezoidBorder, (Config.Screen.window_width - TOP_WIDTH) / 2 - 20f,Config.Screen.window_height - 12f - HEIGHT, TOP_WIDTH + 40f, HEIGHT + 12f);
        batch.setShader(cooldownShader);
        cooldownShader.setUniformf("u_percent", staminaPercentage);
        batch.draw(assets.trapezoidTexture, (Config.Screen.window_width - TOP_WIDTH) / 2,Config.Screen.window_height - HEIGHT, TOP_WIDTH, HEIGHT);
        batch.setShader(null);
        InputPrompts ip = new InputPrompts(assets);
        TextureRegion ipIcon = ip.get(player.currentAbility.type);
        if (Config.Debug.general) {
            assets.font.draw(batch, (int)staminaPercentage + "%", (Config.Screen.window_width - TOP_WIDTH) / 2,Config.Screen.window_height - HEIGHT);
        }
        batch.draw(ipIcon, Config.Screen.window_width / 2 - ABILITY_ICON_SIZE.x / 2, Config.Screen.window_height - HEIGHT + ABILITY_ICON_SIZE.y / 4, ABILITY_ICON_SIZE.x, ABILITY_ICON_SIZE.y);
        batch.end();
    }

    public void update() {
        staminaPercentage = player.getStaminaPercentage();
    }
}
