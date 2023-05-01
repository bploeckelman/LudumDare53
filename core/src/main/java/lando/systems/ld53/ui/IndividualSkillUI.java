package lando.systems.ld53.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
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
    private final Vector2 WINDOW_SIZE = new Vector2(Config.Screen.window_width / 3, Config.Screen.window_height * 2 / 3);
    private final Vector2 WINDOW_POSITION = new Vector2(Config.Screen.window_width / 3, Config.Screen.window_height / 6);
    private final float BUTTON_WIDTH = 200f;
    private final float BUTTON_HEIGHT = 50f;
    public Player.SpecialAbility ability;
    private final float OFFSET = 50f;
    private TextButton equipButton;
    private Stack stack;
    private Assets assets;
    private VisImage lock;

    public IndividualSkillUI(Assets assets, Skin skin, Player player, Player.SpecialAbility ability, GameScreen screen) {
        super("");
        this.ability = ability;
        this.assets = assets;
        setBackground(Assets.Patch.metal.drawable);
        setSize(WINDOW_SIZE.x, WINDOW_SIZE.y);
        setPosition(WINDOW_POSITION.x, WINDOW_POSITION.y);
        setTransform(true);
        setMovable(false);
        setResizable(false);
        setTouchable(Touchable.disabled);
        VisLabel label80px = new VisLabel(ability.title, "outfit-medium-80px");
        VisLabel label20px = new VisLabel(ability.description, "outfit-medium-20px");
        label20px.setWrap(true);

        stack = new Stack();
        VisImage image = new VisImage(assets.inputPrompts.get(ability.type));
        lock = new VisImage(assets.lock);
        image.setSize(200f, 200f);
        image.setOrigin(100f, 100f);
        image.addAction(Actions.repeat(RepeatAction.FOREVER, Actions.sequence(Actions.scaleTo(1.1f, 1.1f, .4f), Actions.scaleTo(1f, 1f, .4f))));
        top();
        add(label80px).top().row();
        stack.add(image);
        add(stack).align(Align.center).width(200f).height(200f).row();
        add(label20px).growX().growY().pad(20f).align(Align.center).row();

        TextButton.TextButtonStyle outfitMediumStyle = skin.get("text", TextButton.TextButtonStyle.class);
        TextButton.TextButtonStyle titleScreenButtonStyle = new TextButton.TextButtonStyle(outfitMediumStyle);
        titleScreenButtonStyle.font = assets.smallFont;
        titleScreenButtonStyle.fontColor = Color.WHITE;
        titleScreenButtonStyle.up = Assets.Patch.glass.drawable;
        titleScreenButtonStyle.down = Assets.Patch.glass_dim.drawable;
        titleScreenButtonStyle.over = Assets.Patch.glass_dim.drawable;
        equipButton = new TextButton("Equip", titleScreenButtonStyle);
        equipButton.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        equipButton.pad(10f);
        add(equipButton).height(BUTTON_HEIGHT).row();

        equipButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!equipButton.isDisabled()) {
                    player.currentAbility = ability;
                    screen.hideSkillUI();
                }
            }
        });
        update();
    }
    public void setCenterConfiguration(boolean isCenter) {
        if (isCenter) {
            setTouchable(Touchable.enabled);
        } else {
            setTouchable(Touchable.disabled);
        }
    }

    public void update() {
        if (!ability.isUnlocked) {
            setColor(Color.GRAY);
            equipButton.setDisabled(true);
            equipButton.setText("Locked");
            stack.addActor(lock);
        } else {
            equipButton.setDisabled(false);
            equipButton.setText("Equip");
            stack.removeActor(lock);
        }
    }
}
