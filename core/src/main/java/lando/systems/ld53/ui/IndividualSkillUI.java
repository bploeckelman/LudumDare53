package lando.systems.ld53.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
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

public class IndividualSkillUI extends VisWindow {
    private final Vector2 WINDOW_SIZE = new Vector2(Config.Screen.window_width / 3, Config.Screen.window_height * 2 / 3);
    private final Vector2 WINDOW_POSITION = new Vector2(Config.Screen.window_width / 3, Config.Screen.window_height / 6);
    private final float BUTTON_WIDTH = 180f;
    private final float BUTTON_HEIGHT = 50f;
    public IndividualSkillUI(Assets assets, Skin skin, Player player) {
        super("");
        setBackground(Assets.Patch.metal.drawable);
        setSize(WINDOW_SIZE.x, WINDOW_SIZE.y);
        setPosition(WINDOW_POSITION.x, WINDOW_POSITION.y);
        VisLabel label80px = new VisLabel(player.currentAbility.title, "outfit-medium-80px");
        VisLabel label20px = new VisLabel(player.currentAbility.description, "outfit-medium-20px");
        label20px.setWrap(true);
        VisImage image = new VisImage(assets.inputPrompts.get(player.currentAbility.type));
        image.setSize(200f, 200f);
        top();
        add(label80px).top().row();
        add(image).align(Align.center).width(200f).height(200f).row();
        add(label20px).growX().growY().pad(20f).align(Align.center).row();

        TextButton.TextButtonStyle outfitMediumStyle = skin.get("text", TextButton.TextButtonStyle.class);
        TextButton.TextButtonStyle titleScreenButtonStyle = new TextButton.TextButtonStyle(outfitMediumStyle);
        titleScreenButtonStyle.font = assets.smallFont;
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
