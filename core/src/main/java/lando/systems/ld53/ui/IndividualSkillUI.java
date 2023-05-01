package lando.systems.ld53.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.kotcrab.vis.ui.widget.VisImage;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisWindow;
import lando.systems.ld53.Assets;
import lando.systems.ld53.Config;
import lando.systems.ld53.entities.Player;
import lando.systems.ld53.entities.PlayerAbility;
import lando.systems.ld53.screens.GameScreen;

public class IndividualSkillUI extends VisWindow {
    private final Vector2 WINDOW_SIZE = new Vector2(70f, 70f);
    private final Vector2 WINDOW_POSITION = new Vector2(Config.Screen.window_width / 2 - WINDOW_SIZE.x, Config.Screen.window_height - 100f);
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
//        VisLabel label80px = new VisLabel(ability.title, "outfit-medium-80px");
        VisLabel label20px = new VisLabel(ability.title, "outfit-medium-20px");
        label20px.setScale(.1f);

        stack = new Stack();
        VisImage image = new VisImage(ability.textureRegion);

//        if(ability.type == InputPrompts.Type.key_light_bang) {
//            image = new VisImage(assets.bomb.getKeyFrame(0));
//        }
        lock = new VisImage(assets.lock);
        image.setOrigin(25f, 25f);
        image.setAlign(Align.center);
        image.addAction(Actions.repeat(RepeatAction.FOREVER, Actions.sequence(Actions.scaleTo(1.1f, 1.1f, .4f), Actions.scaleTo(1f, 1f, .4f))));
        top();
//        add(label80px).top().row();
        stack.add(image);
        stack.add(lock);
        label20px.setAlignment(Align.bottom);
        stack.add(label20px);
        add(stack).align(Align.center).width(50f).height(50f).row();
//
//        TextButton.TextButtonStyle outfitMediumStyle = skin.get("text", TextButton.TextButtonStyle.class);
//        TextButton.TextButtonStyle titleScreenButtonStyle = new TextButton.TextButtonStyle(outfitMediumStyle);
//        titleScreenButtonStyle.font = assets.smallFont;
//        titleScreenButtonStyle.fontColor = Color.WHITE;
//        titleScreenButtonStyle.up = Assets.Patch.glass.drawable;
//        titleScreenButtonStyle.down = Assets.Patch.glass_dim.drawable;
//        titleScreenButtonStyle.over = Assets.Patch.glass_dim.drawable;
//        equipButton = new TextButton("Equip (E)", titleScreenButtonStyle);
//        equipButton.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);
//        equipButton.pad(10f);
//        add(equipButton).height(BUTTON_HEIGHT).row();

//        equipButton.addListener(new ClickListener(){
//            @Override
//            public void clicked(InputEvent event, float x, float y) {
//                if (ability.isUnlocked) {
//                    player.currentAbility = ability;
//                    screen.swapMusic();
//                    screen.hideSkillUI();
//
//                } else {
//                    lock.addAction(shakeLocker());
//                }
//            }
//        });
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
//            equipButton.setText("Locked");
            lock.setVisible(true);
        } else {
//            equipButton.setText("Equip (E)");
            lock.setVisible(false);
        }
    }
}
