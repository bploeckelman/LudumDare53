package lando.systems.ld53.entities;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public enum PlayerAbility {

    shield_360   (4f,  "Shield",  "Block it!",            true),
    bomb_throw   (3f,  "Bomb",    "Big bada-boom",        true),
    speed_up     (5f,  "Speed",   "Gotta go fast!",       true),
    repulse      (6f,  "Repulse", "Enforce your personal space",      true),
    fetch        (10f, "Fetch",   "Good puppers help fetch packages", true)
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
