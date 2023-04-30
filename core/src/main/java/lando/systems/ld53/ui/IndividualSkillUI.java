package lando.systems.ld53.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.kotcrab.vis.ui.widget.VisImage;
import com.kotcrab.vis.ui.widget.VisWindow;
import lando.systems.ld53.Assets;
import lando.systems.ld53.Config;
import lando.systems.ld53.entities.Player;

public class IndividualSkillUI extends VisWindow {

    public IndividualSkillUI(Assets assets, Player.SpecialAbility ability) {
        super("");
        setSize(Config.Screen.window_width / 2, Config.Screen.window_height / 2);
        setPosition(Config.Screen.window_width / 4, Config.Screen.window_height / 4);
        VisImage image = new VisImage(assets.inputPrompts.get(ability.type));
    }
}
