package lando.systems.ld53.ui;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import lando.systems.ld53.Assets;
import lando.systems.ld53.Config;
import lando.systems.ld53.entities.Player;

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

    public SelectSkillUI(Assets assets, Skin skin, Player player) {
        this.player = player;
        this.assets = assets;
        this.skin = skin;
        abilityList = Arrays.asList(Player.SpecialAbility.values());
        for (Player.SpecialAbility ability : abilityList) {
            IndividualSkillUI individualSkillUI = new IndividualSkillUI(assets, skin, player, ability);
            abilityUIMap.put(ability, individualSkillUI);
            addActor(individualSkillUI);
        }
        setUpInitialOrder();

        setVisible(false);
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
        int previousIndex = abilityList.indexOf(player.currentAbility);
        if (previousIndex == 0) {
            previousIndex = abilityList.size();
        }
        int currentIndex = (previousIndex - 1) % abilityList.size();

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
        Vector2 newSize = getNewSizeForIndex(indexOffset);
        ui.addAction(Actions.parallel(Actions.moveTo(newPosition.x, newPosition.y, .5f), Actions.scaleTo(newSize.x, getNewSizeForIndex(indexOffset).y, .5f)));
    }

    public Vector2 getNewSizeForIndex(int indexOffset) {
//        return new Vector2(WINDOW_SIZE.x - OFFSET * Math.abs(indexOffset)  , WINDOW_SIZE.y - OFFSET * Math.abs(indexOffset));
        return new Vector2(1 - Math.abs(indexOffset) * .05f, 1 - Math.abs(indexOffset) * .05f);
    }
    public Vector2 getNewPositionForIndex(int indexOffset) {
        float x = indexOffset > 0 ? WINDOW_POSITION.x + indexOffset * 1.3f * OFFSET : WINDOW_POSITION.x + indexOffset * OFFSET;
        return new Vector2(x, WINDOW_POSITION.y + Math.abs(indexOffset) * OFFSET / 4);
    }


    public void show(boolean show) {
        if (show) {
            setVisible(true);
        }
        else {
            setVisible(false);
        }
    }
}
