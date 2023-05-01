package lando.systems.ld53.entities;

import lando.systems.ld53.assets.InputPrompts;

public enum PlayerAbility {

    shield_360   (InputPrompts.Type.key_light_at,          2f, "@ shield @",    "Use the shield@",       true),
    bomb_throw   (InputPrompts.Type.key_light_bang,        3f, "! bomb !",      "Throw a bomb!",         true),
    speed_up     (InputPrompts.Type.key_light_tilde,       5f, "~ speed ~",     "Gotta go fast~",        false),
    repulse      (InputPrompts.Type.key_light_minus,       6f, "- repulse -",   "Keep the bad away-",    false),
    grapple      (InputPrompts.Type.key_light_arrow_right, 7f, "-> grapple ->", "Keep the good near->",  false)
    ;

    public final InputPrompts.Type type; // TODO(brian) - eventually want an Animation for the icon instead of a ref to the input prompts sheet (scales up poorly)
    public final String title;
    public final String description;

    public boolean isUnlocked;
    public float cost;

    PlayerAbility(InputPrompts.Type type, float cost, String title, String description, boolean isUnlocked) {
        this.type = type;
        this.cost = cost;
        this.title = title;
        this.description = description;
        this.isUnlocked = isUnlocked;
    }

}
