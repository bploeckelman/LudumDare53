package lando.systems.ld53.entities;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public enum PlayerAbility {

    shield_360   (4f,  "Shield",  "Block It!",         true),
    bomb_throw   (3f,  "Bomb",    "    Blast!",        true),
    speed_up     (5f,  "Speed",   "Personal space...", true),
    repulse      (6f,  "Repulse", "Get away, please.", true),
    fetch        (10f, "Fetch",   "Good pupper->",     true)
    ;

    // textureRegion is being set in assets class.
    public TextureRegion textureRegion;
    public final String title;
    public final String description;
    public final float cost;

    public boolean isUnlocked;

    PlayerAbility(float cost, String title, String description, boolean isUnlocked) {
        this.cost = cost;
        this.title = title;
        this.description = description;
        this.isUnlocked = isUnlocked;
    }

}
