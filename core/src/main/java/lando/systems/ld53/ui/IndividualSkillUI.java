package lando.systems.ld53.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.*;
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
import lando.systems.ld53.assets.InputPrompts;
import lando.systems.ld53.entities.Player;
import lando.systems.ld53.entities.PlayerAbility;
import lando.systems.ld53.screens.GameScreen;

public class IndividualSkillUI extends VisWindow {
    private final Vector2 WINDOW_SIZE = new Vector2(Config.Screen.window_width / 3, Config.Screen.window_height * 2 / 3);
    private final Vector2 WINDOW_POSITION = new Vector2(Config.Screen.window_width / 3, Config.Screen.window_height / 6);
    private final float BUTTON_WIDTH = 200f;
    private final float BUTTON_HEIGHT = 50f;
    private final float OFFSET = 50f;
    private TextButton equipButton;
    private Stack stack;
    private Assets assets;
    public VisImage lock;
    public PlayerAbility ability;
    public SequenceAction sequenceAction;
    public RepeatAction shakeLockAction;
    public MoveToAction returnToOriginalRotationAction;
    private boolean isCenter = false;

    public IndividualSkillUI(Assets assets, Skin skin, Player player, PlayerAbility ability, GameScreen screen) {
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
        switch(ability.type) {
            case key_light_bang: // Bomb
                image = new VisImage(assets.bomb.getKeyFrame(0));
                break;
                case key_light_tilde: // Speed
                image = new VisImage(assets.abilityIcons.getKeyFrame(0));
                break;
            case key_light_at: // Shield
                image = new VisImage(assets.abilityIcons.getKeyFrame(1));
                break;
        }
//        if(ability.type == InputPrompts.Type.key_light_bang) {
//            image = new VisImage(assets.bomb.getKeyFrame(0));
//        }
        lock = new VisImage(assets.lock);
        image.setSize(200f, 200f);
        image.setOrigin(100f, 100f);
        image.setAlign(Align.center);
        image.addAction(Actions.repeat(RepeatAction.FOREVER, Actions.sequence(Actions.scaleTo(1.1f, 1.1f, .4f), Actions.scaleTo(1f, 1f, .4f))));
        top();
        add(label80px).top().row();
        stack.add(image);
        stack.add(lock);
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
        this.isCenter = isCenter;
        if (isCenter) {
            setTouchable(Touchable.enabled);
        } else {
            setTouchable(Touchable.disabled);
        }
    }

    public SequenceAction shakeLocker() {
        shakeLockAction = Actions.repeat(3,
            Actions.sequence(
                Actions.moveBy(0f, 10f, .05f),
                Actions.moveBy(0f, -10f, .05f)
            )
        );
        returnToOriginalRotationAction = Actions.moveTo(0f, 0f, .1f);
        sequenceAction = Actions.sequence(shakeLockAction, returnToOriginalRotationAction);
        return sequenceAction;
    }

    public void update() {
        if (!ability.isUnlocked) {
            setColor(Color.GRAY);
            equipButton.setDisabled(true);
            equipButton.setText("Locked");
            lock.setVisible(true);
        } else {
            equipButton.setDisabled(false);
            equipButton.setText("Equip");
            lock.setVisible(false);
        }
    }
}
