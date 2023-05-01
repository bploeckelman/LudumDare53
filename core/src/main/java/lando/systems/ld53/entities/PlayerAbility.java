package lando.systems.ld53.entities;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public enum PlayerAbility {

    shield_360   (2f, "Shield",    "Block It!",       true),
    bomb_throw   (3f, "Bomb",      "    Blast!",         true),
    speed_up     (5f, "Speed",     "Quick like a bunny!",        true),
    repulse      (6f, "Repulse",   "Get away, please.",    false),
    grapple      (7f, "Grapple", "    Get over here! ->",  false)
    ;

    //textureRegion is being set in assets class.
    public TextureRegion textureRegion; // TODO(brian) - eventually want an Animation for the icon instead of a ref to the input prompts sheet (scales up poorly)
    public final String title;
    public final String description;

    public boolean isUnlocked;
    public float cost;

    PlayerAbility(float cost, String title, String description, boolean isUnlocked) {
        this.cost = cost;
        this.title = title;
        this.description = description;
        this.isUnlocked = isUnlocked;
    }

}
