package lando.systems.ld53.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import lando.systems.ld53.Assets;
import lando.systems.ld53.Config;
import lando.systems.ld53.entities.Player;
import lando.systems.ld53.screens.GameScreen;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


public class SelectSkillUI extends Group {
    private HashMap<Player.SpecialAbility, IndividualSkillUI> abilityUIMap = new HashMap<>();
    private List<Player.SpecialAbility> abilityList;
    private IndividualSkillUI skillUIBeingShown;
    private Player player;
    private Assets assets;
    private Skin skin;
    private final Vector2 WINDOW_SIZE = new Vector2(Config.Screen.window_width / 3, Config.Screen.window_height * 2 / 3);
    private final Vector2 WINDOW_POSITION = new Vector2(Config.Screen.window_width / 3, Config.Screen.window_height / 6);
    private final float OFFSET = 50f;

    public SelectSkillUI(GameScreen screen) {
        this.player = screen.player;
        this.assets = screen.assets;
        this.skin = screen.skin;
        abilityList = Arrays.asList(Player.SpecialAbility.values());
        for (Player.SpecialAbility ability : abilityList) {
            IndividualSkillUI individualSkillUI = new IndividualSkillUI(assets, skin, player, ability, screen);
            abilityUIMap.put(ability, individualSkillUI);
            addActor(individualSkillUI);
        }

        setVisible(true);
        Vector2 newPosition = new Vector2(Config.Screen.window_width / 2, Config.Screen.window_height - 50f);
        setPosition(newPosition.x, newPosition.y);
        setScale(0f, 0f);
    }
//    public IndividualSkillUI setMainWindowWithCurrentSkill(Player player) {
////        IndividualSkillUI ui = new IndividualSkillUI(assets, skin, player, player.currentAbility);
//        return ui;
//    }
    public void showNextSkill() {
        int centerIndex = (abilityList.indexOf(skillUIBeingShown.ability) + 1) % abilityList.size();
        int leftIndex1;
        int leftIndex2;
        int rightIndex1 = (centerIndex + 1) % abilityList.size();
        int rightIndex2 = (centerIndex + 2) % abilityList.size();
        if (centerIndex - 1 < 0) {
            leftIndex1 = abilityList.size() - 1;
        } else {
            leftIndex1 = centerIndex - 1;
        }
        if (leftIndex1 - 1 < 0) {
            leftIndex2 = abilityList.size() - 1;
        } else {
            leftIndex2 = leftIndex1 - 1;
        }
        IndividualSkillUI leftIndex2UI = abilityUIMap.get(abilityList.get(leftIndex2));
        IndividualSkillUI leftIndex1UI = abilityUIMap.get(abilityList.get(leftIndex1));
        IndividualSkillUI centerIndexUI = abilityUIMap.get(abilityList.get(centerIndex));
        IndividualSkillUI rightIndex1UI = abilityUIMap.get(abilityList.get(rightIndex1));
        IndividualSkillUI rightIndex2UI = abilityUIMap.get(abilityList.get(rightIndex2));

        performParallelMoveToScaleTo(leftIndex2UI, -2);
        performParallelMoveToScaleTo(leftIndex1UI, -1);
        performParallelMoveToScaleTo(centerIndexUI, 0);
        performParallelMoveToScaleTo(rightIndex1UI, 1);
        performParallelMoveToScaleTo(rightIndex2UI, 2);

        abilityUIMap.get(abilityList.get(leftIndex1)).toFront();
        abilityUIMap.get(abilityList.get(rightIndex1)).toFront();
        abilityUIMap.get(abilityList.get(centerIndex)).toFront();
        skillUIBeingShown = abilityUIMap.get(abilityList.get(centerIndex));
    }
    public void showPreviousSkill() {
        int previousCenterIndex = abilityList.indexOf(skillUIBeingShown.ability);
        if (previousCenterIndex == 0) {
            previousCenterIndex = abilityList.size();
        }
        int centerIndex = (previousCenterIndex - 1) % abilityList.size();
        int leftIndex1;
        int leftIndex2;
        int rightIndex1 = (centerIndex + 1) % abilityList.size();
        int rightIndex2 = (centerIndex + 2) % abilityList.size();
        if (centerIndex - 1 < 0) {
            leftIndex1 = abilityList.size() - 1;
        } else {
            leftIndex1 = centerIndex - 1;
        }
        if (leftIndex1 - 1 < 0) {
            leftIndex2 = abilityList.size() - 1;
        } else {
            leftIndex2 = leftIndex1 - 1;
        }
        IndividualSkillUI leftIndex2UI = abilityUIMap.get(abilityList.get(leftIndex2));
        IndividualSkillUI leftIndex1UI = abilityUIMap.get(abilityList.get(leftIndex1));
        IndividualSkillUI centerIndexUI = abilityUIMap.get(abilityList.get(centerIndex));
        IndividualSkillUI rightIndex1UI = abilityUIMap.get(abilityList.get(rightIndex1));
        IndividualSkillUI rightIndex2UI = abilityUIMap.get(abilityList.get(rightIndex2));

        performParallelMoveToScaleTo(leftIndex2UI, -2);
        performParallelMoveToScaleTo(leftIndex1UI, -1);
        performParallelMoveToScaleTo(centerIndexUI, 0);
        performParallelMoveToScaleTo(rightIndex1UI, 1);
        performParallelMoveToScaleTo(rightIndex2UI, 2);

        abilityUIMap.get(abilityList.get(rightIndex1)).toFront();
        abilityUIMap.get(abilityList.get(leftIndex1)).toFront();
        abilityUIMap.get(abilityList.get(centerIndex)).toFront();
        skillUIBeingShown = abilityUIMap.get(abilityList.get(centerIndex));
    }

    public void setUpInitialOrder() {
        int currentIndex = abilityList.indexOf(player.currentAbility);
        int leftIndex1;
        int leftIndex2;
        int rightIndex1 = (currentIndex + 1) % abilityList.size();
        int rightIndex2 = (currentIndex + 2) % abilityList.size();
        if (currentIndex - 1 < 0) {
            leftIndex1 = abilityList.size() - 1;
        } else {
            leftIndex1 = currentIndex - 1;
        }
        if (leftIndex1 - 1 < 0) {
            leftIndex2 = abilityList.size() - 1;
        } else {
            leftIndex2 = leftIndex1 - 1;
        }
        performParallelMoveToScaleTo(abilityUIMap.get(abilityList.get(leftIndex2)), -2);
        performParallelMoveToScaleTo(abilityUIMap.get(abilityList.get(leftIndex1)), -1);
        performParallelMoveToScaleTo(abilityUIMap.get(abilityList.get(currentIndex)), 0);
        performParallelMoveToScaleTo(abilityUIMap.get(abilityList.get(rightIndex1)), 1);
        performParallelMoveToScaleTo(abilityUIMap.get(abilityList.get(rightIndex2)), 2);
//        abilityUIMap.get(abilityList.get(leftIndex2)).setSizePerOffset(-2);
//        abilityUIMap.get(abilityList.get(leftIndex1)).setSizePerOffset(-1);
//        abilityUIMap.get(abilityList.get(currentIndex)).setSizePerOffset(0);
//        abilityUIMap.get(abilityList.get(rightIndex1)).setSizePerOffset(1);
//        abilityUIMap.get(abilityList.get(rightIndex2)).setSizePerOffset(2);
        abilityUIMap.get(abilityList.get(leftIndex1)).toFront();
        abilityUIMap.get(abilityList.get(rightIndex1)).toFront();
        abilityUIMap.get(abilityList.get(currentIndex)).toFront();
        skillUIBeingShown = abilityUIMap.get(abilityList.get(currentIndex));
    }

    public void performParallelMoveToScaleTo(IndividualSkillUI ui, int indexOffset) {
        Vector2 newPosition = getNewPositionForIndex(indexOffset);
        Vector2 newScale = getNewScaleForIndex(indexOffset);
        changeColorPerIndexOffset(ui, indexOffset);
        ui.addAction(Actions.parallel(Actions.moveTo(newPosition.x, newPosition.y, .25f), Actions.scaleTo(newScale.x, newScale.y, .25f)));
    }

    public void changeColorPerIndexOffset(IndividualSkillUI ui, int indexOffset) {
        Color currentColor = ui.ability.isUnlocked ? Color.WHITE : Color.GRAY;
        float colorOffset = 1 - Math.abs(indexOffset) * .15f;
        ui.setColor(currentColor.r * colorOffset, currentColor.g * colorOffset, currentColor.b * colorOffset, currentColor.a);
    }

    public void hide(float duration) {
        Vector2 newPosition = new Vector2(Config.Screen.window_width / 2, Config.Screen.window_height - 50f);
        Vector2 newScale = Vector2.Zero;
        addAction(Actions.parallel(Actions.moveTo(newPosition.x, newPosition.y, .25f), Actions.scaleTo(newScale.x, newScale.y, .25f)));
    }

    public Vector2 getNewScaleForIndex(int indexOffset) {
//        return new Vector2(WINDOW_SIZE.x - OFFSET * Math.abs(indexOffset)  , WINDOW_SIZE.y - OFFSET * Math.abs(indexOffset));
        return new Vector2(1 - Math.abs(indexOffset) * .05f, 1 - Math.abs(indexOffset) * .05f);
    }
    public Vector2 getNewPositionForIndex(int indexOffset) {
        // 1.3 is a magic number
        float x = indexOffset > 0 ? WINDOW_POSITION.x + indexOffset * 1.3f * OFFSET : WINDOW_POSITION.x + indexOffset * OFFSET;
        //y value works, not sure why, but it works
        return new Vector2(x, WINDOW_POSITION.y + Math.abs(indexOffset) * OFFSET / 4);
    }


    public void show(boolean show) {
        if (show) {
            addAction(Actions.parallel(Actions.moveTo(0f, 0f, .25f), Actions.scaleTo(1f, 1f, .25f)));
            setUpInitialOrder();
            setVisible(true);
        }
        else {
            hide(.5f);
//            for (IndividualSkillUI ui : abilityUIMap.values()) {
//                performParallelMoveToScaleToHide(ui);
//            }
        }
    }

    // Call this if ability is unlocked
    public void updateAbilityUnlocks() {
        for (IndividualSkillUI ui : abilityUIMap.values()) {
            ui.update();
        }
    }
}
