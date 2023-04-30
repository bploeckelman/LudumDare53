package lando.systems.ld53.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.kotcrab.vis.ui.widget.VisImage;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisWindow;
import lando.systems.ld53.Assets;
import lando.systems.ld53.Config;
import lando.systems.ld53.entities.Player;

public class IndividualSkillUI extends VisWindow {

    public IndividualSkillUI(Assets assets, Player.SpecialAbility ability) {
        super("");
        setBackground(Assets.Patch.glass_dim.drawable);
        setSize(Config.Screen.window_width / 3, Config.Screen.window_height * 2 / 3);
        setPosition(Config.Screen.window_width / 3, Config.Screen.window_height / 6);
        VisLabel label40px = new VisLabel(ability.title, "outfit-medium-40px");
        addActor(label40px);
        VisLabel label20px = new VisLabel(ability.description, "outfit-medium-20px");
        add(label40px).align(Align.center).row();
        VisImage image = new VisImage(assets.inputPrompts.get(ability.type));
        image.setSize(300f, 300f);
        add(image).align(Align.center).width(300f).height(300f).row();
        add(label20px).align(Align.center).row();
    }
}
