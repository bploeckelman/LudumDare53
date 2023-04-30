package lando.systems.ld53.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.kotcrab.vis.ui.widget.VisImage;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisWindow;
import lando.systems.ld53.Assets;
import lando.systems.ld53.Config;
import lando.systems.ld53.entities.Player;
import lando.systems.ld53.screens.GameScreen;

public class IndividualSkillUI extends VisWindow {
    private final float BUTTON_WIDTH = 180f;
    private final float BUTTON_HEIGHT = 50f;
    public IndividualSkillUI(GameScreen screen, Player.SpecialAbility ability) {
        super("");
        setBackground(Assets.Patch.metal.drawable);
        setSize(Config.Screen.window_width / 3, Config.Screen.window_height * 2 / 3);
        setPosition(Config.Screen.window_width / 3, Config.Screen.window_height / 6);
        VisLabel label80px = new VisLabel(ability.title, "outfit-medium-80px");
        VisLabel label20px = new VisLabel(ability.description, "outfit-medium-20px");
        label20px.setWrap(true);
        VisImage image = new VisImage(screen.assets.inputPrompts.get(ability.type));
        image.setSize(200f, 200f);
        top();
        add(label80px).top().row();
        add(image).align(Align.center).width(200f).height(200f).row();
        add(label20px).growX().growY().pad(20f).align(Align.center).row();

        TextButton.TextButtonStyle outfitMediumStyle = screen.skin.get("text", TextButton.TextButtonStyle.class);
        TextButton.TextButtonStyle titleScreenButtonStyle = new TextButton.TextButtonStyle(outfitMediumStyle);
        titleScreenButtonStyle.font = screen.assets.smallFont;
        titleScreenButtonStyle.fontColor = Color.WHITE;
        titleScreenButtonStyle.up = Assets.Patch.glass.drawable;
        titleScreenButtonStyle.down = Assets.Patch.glass_dim.drawable;
        titleScreenButtonStyle.over = Assets.Patch.glass_dim.drawable;
        TextButton textButton = new TextButton("Equip", titleScreenButtonStyle);
        textButton.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        add(textButton).height(BUTTON_HEIGHT).row();
        textButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // TODO: clicked listener
            }
        });
    }
}
