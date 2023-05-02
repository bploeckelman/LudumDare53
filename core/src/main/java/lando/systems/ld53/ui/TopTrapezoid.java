package lando.systems.ld53.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import lando.systems.ld53.Assets;
import lando.systems.ld53.Config;
import lando.systems.ld53.entities.Player;
import lando.systems.ld53.utils.Utils;

public class TopTrapezoid {

    private float TOP_WIDTH = 400f;
    private float BOTTOM_WIDTH = 300f;
    private float MARGIN = 10f;
    private float HEIGHT = 75f;
    private Vector2 ABILITY_ICON_SIZE = new Vector2(75f, 75f);
    private float staminaPercentage = 100f;
    private Player player;
    private Assets assets;
    private ShaderProgram cooldownShader;
    private Color barColor;

    public TopTrapezoid(Player player, Assets assets) {

        this.player = player;
        this.assets = assets;
        this.cooldownShader = assets.cooldownShader;
        this.barColor = new Color(Color.GREEN);
    }

    public void render(SpriteBatch batch) {

        batch.begin();
        batch.draw(assets.trapezoidBorder, (Config.Screen.window_width - TOP_WIDTH) / 2 - MARGIN,Config.Screen.window_height - 5f - HEIGHT, TOP_WIDTH + 2 *  MARGIN, HEIGHT + 5f);
        batch.setShader(cooldownShader);
        cooldownShader.setUniformf("u_percent", staminaPercentage);
        batch.setColor(barColor);
        batch.draw(assets.trapezoidTexture, (Config.Screen.window_width - TOP_WIDTH) / 2,Config.Screen.window_height - HEIGHT, TOP_WIDTH, HEIGHT - 5f);
        batch.setShader(null);
        TextureRegion ipIcon = player.currentAbility.textureRegion;

        if (Config.Debug.general) {
            assets.font.draw(batch, (int)staminaPercentage + "%", (Config.Screen.window_width - TOP_WIDTH) / 2,Config.Screen.window_height - HEIGHT);
        }
        //batch.draw(ipIcon, Config.Screen.window_width / 2 - ABILITY_ICON_SIZE.x / 2, Config.Screen.window_height - HEIGHT, ABILITY_ICON_SIZE.x, ABILITY_ICON_SIZE.y);
        batch.end();
    }

    public void update() {
        staminaPercentage = player.getStaminaPercentage();
        float hue = MathUtils.map(0, 100f, 0, .3f, staminaPercentage);
        Utils.hsvToRgb(hue, 1f, .6f, barColor);
    }
}
