package lando.systems.ld53.ui;

import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.kotcrab.vis.ui.widget.VisImage;
import com.kotcrab.vis.ui.widget.VisLabel;
import lando.systems.ld53.Assets;
import lando.systems.ld53.entities.PlayerAbility;
import lando.systems.ld53.screens.GameScreen;

import java.util.List;

public class BottomGameUIButtons extends Table {
    public VisImage lock;
    public SequenceAction sequenceAction;
    public RepeatAction shakeLockAction;
    public MoveToAction returnToOriginalRotationAction;

    public BottomGameUIButtons(GameScreen screen, int index, List<PlayerAbility> abilityList) {
        setBackground(Assets.Patch.glass.drawable);
        Stack stack = new Stack();
        VisImage icon = new VisImage(abilityList.get(index).textureRegion);
        stack.add(icon);
        VisLabel label = new VisLabel(String.valueOf(index + 1));
        label.setScale(2f);
        label.setAlignment(Align.topRight);
        stack.add(label);
        lock = new VisImage(screen.assets.lock);
        lock.setColor(1f, 1f, 1f, 0.7f);
        stack.add(lock);
        add(stack).growX().growY();
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
}
