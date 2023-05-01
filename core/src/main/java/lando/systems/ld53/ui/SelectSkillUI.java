package lando.systems.ld53.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.kotcrab.vis.ui.widget.VisWindow;
import lando.systems.ld53.Assets;
import lando.systems.ld53.Config;
import lando.systems.ld53.assets.InputPrompts;
import lando.systems.ld53.entities.Player;
import lando.systems.ld53.entities.PlayerAbility;
import lando.systems.ld53.screens.GameScreen;
import lando.systems.ld53.utils.Utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


public class SelectSkillUI extends Group {
    private final GameScreen screen;
    public HashMap<PlayerAbility, IndividualSkillUI> abilityUIMap = new HashMap<>();
    private List<PlayerAbility> abilityList;
    public IndividualSkillUI skillUIBeingShown;
    private Player player;
    private Assets assets;
    private Skin skin;
    private final float WINDOW_SIZE = 70f;
    private final Vector2 WINDOW_POSITION = new Vector2(Config.Screen.window_width / 2 - WINDOW_SIZE / 2, Config.Screen.window_height - 75f);
    private final float OFFSET = 30f;
    private ImageButton previousButton;
    private ImageButton nextButton;
    private VisWindow greyOutWindow;
    public float transitionDuration;
    private float autoScrollTimer = 0f;
    public boolean isAutoScrolling = false;
    private int scrollToSkillIndex = 0;
    public boolean isFound = false;

    public SelectSkillUI(GameScreen screen) {
        this.screen = screen;
        this.player = screen.player;
        this.assets = screen.assets;
        this.skin = screen.skin;
        this.transitionDuration = .0325f;
        greyOutWindow = new VisWindow("");
        greyOutWindow.setPosition(0f, 0f);
        greyOutWindow.setSize(Config.Screen.window_width, Config.Screen.window_height);
        greyOutWindow.setTouchable(Touchable.disabled);
        greyOutWindow.setBackground(new TextureRegionDrawable(Utils.getColoredTextureRegion(new Color(.3f, .3f, .3f, .5f))));
        greyOutWindow.setMovable(false);
        //addActor(greyOutWindow);
        abilityList = Arrays.asList(PlayerAbility.values());
        for (PlayerAbility ability : abilityList) {
            IndividualSkillUI individualSkillUI = new IndividualSkillUI(assets, skin, player, ability, screen);
            abilityUIMap.put(ability, individualSkillUI);
            addActor(individualSkillUI);
        }
//        TextureRegionDrawable prevButtonDrawable = new TextureRegionDrawable(assets.inputPrompts.get(InputPrompts.Type.hand_point_left));
        TextureRegionDrawable prevButtonDrawable = new TextureRegionDrawable(assets.inputPrompts.get(InputPrompts.Type.key_light_a));
        prevButtonDrawable.setMinSize(75f, 75f);
        TextureRegionDrawable nextButtonDrawable = new TextureRegionDrawable(assets.inputPrompts.get(InputPrompts.Type.key_light_key_d));
        nextButtonDrawable.setMinSize(75f, 75f);
        Table buttonTable = new Table();
        buttonTable.setPosition(225f, 200f);
        buttonTable.setSize(Config.Screen.window_width - 475f, 100f);
        previousButton = new ImageButton(prevButtonDrawable);
        nextButton = new ImageButton(nextButtonDrawable);
        previousButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showPreviousSkill();
            }
        });
        nextButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showNextSkill();
            }
        });
        //buttonTable.add(previousButton).left().expandX();
        //buttonTable.add(nextButton).right().expandX();
        previousButton.addAction(Actions.repeat(RepeatAction.FOREVER, Actions.sequence(Actions.moveBy(0f, 20f, .5f), Actions.moveBy(-0f, -20f, .5f))));
        nextButton.addAction(Actions.repeat(RepeatAction.FOREVER, Actions.sequence(Actions.moveBy(0f, 20f, .5f), Actions.moveBy(-0f, -20f, .5f))));
        addActor(buttonTable);

        setVisible(true);
        setUpInitialOrder();
        //Vector2 newPosition = new Vector2(Config.Screen.window_width / 2f, Config.Screen.window_height - 50f);
        //setPosition(newPosition.x, newPosition.y);
        //setScale(0f, 0f);
    }
//    public IndividualSkillUI setMainWindowWithCurrentSkill(Player player) {
////        IndividualSkillUI ui = new IndividualSkillUI(assets, skin, player, player.currentAbility);
//        return ui;
//    }
    public int showNextSkill() {
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
        return centerIndex;
    }
    public int showPreviousSkill() {
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
        return centerIndex;
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

        abilityUIMap.get(abilityList.get(leftIndex1)).toFront();
        abilityUIMap.get(abilityList.get(rightIndex1)).toFront();
        abilityUIMap.get(abilityList.get(currentIndex)).toFront();
        skillUIBeingShown = abilityUIMap.get(abilityList.get(currentIndex));
    }

    public void performParallelMoveToScaleTo(IndividualSkillUI ui, int indexOffset) {
        Vector2 newPosition = getNewPositionForIndex(indexOffset);
        Vector2 newScale = getNewScaleForIndex(indexOffset);
        changeColorPerIndexOffset(ui, indexOffset);
        ui.addAction(Actions.parallel(Actions.moveTo(newPosition.x, newPosition.y, transitionDuration), Actions.scaleTo(newScale.x, newScale.y, transitionDuration)));
        if (indexOffset == 0) {
            ui.setCenterConfiguration(true);
        } else {
            ui.setCenterConfiguration(false);
        }
        previousButton.toFront();
        nextButton.toFront();
    }

    public void changeColorPerIndexOffset(IndividualSkillUI ui, int indexOffset) {
        Color currentColor = ui.ability.isUnlocked ? Color.WHITE : Color.GRAY;
        float colorOffset = 1 - Math.abs(indexOffset) * .15f;
        ui.setColor(currentColor.r * colorOffset, currentColor.g * colorOffset, currentColor.b * colorOffset, currentColor.a);
    }

    public void hide(float duration) {
        Vector2 newPosition = new Vector2(Config.Screen.window_width / 2, Config.Screen.window_height - 50f);
        Vector2 newScale = Vector2.Zero;
        addAction(Actions.parallel(Actions.moveTo(newPosition.x, newPosition.y, transitionDuration), Actions.scaleTo(newScale.x, newScale.y, transitionDuration)));
    }

    public Vector2 getNewScaleForIndex(int indexOffset) {
//        return new Vector2(WINDOW_SIZE.x - OFFSET * Math.abs(indexOffset)  , WINDOW_SIZE.y - OFFSET * Math.abs(indexOffset));
        return new Vector2(1 - Math.abs(indexOffset) * .05f, 1 - Math.abs(indexOffset) * .05f);
    }
    public Vector2 getNewPositionForIndex(int indexOffset) {
        // 1.3 is a magic number
        float x = indexOffset > 0 ? WINDOW_POSITION.x + indexOffset * 1.23f * OFFSET : WINDOW_POSITION.x + indexOffset * OFFSET;
        //y value works, not sure why, but it works
        return new Vector2(x, WINDOW_POSITION.y + Math.abs(indexOffset) * OFFSET / 4);
    }


    public void show(boolean show) {
        if (show) {
            addAction(Actions.parallel(Actions.moveTo(0f, 0f, transitionDuration), Actions.scaleTo(1f, 1f, transitionDuration)));
            setUpInitialOrder();
        }
        else {
            hide(.5f);
        }
    }

    // Call this if ability is unlocked
    public void updateAbilityUnlocks() {
        for (IndividualSkillUI ui : abilityUIMap.values()) {
            ui.update();
        }
    }

    public void autoScrollToSkillInit(int skillIndex) {
        autoScrollTimer = 0f;
        isAutoScrolling = true;
        scrollToSkillIndex = skillIndex;
        isFound = false;
        show(true);
    }

    public void autoScrollUpdate(float delta) {
        if (autoScrollTimer > .1f && !isFound) {
            if (showNextSkill() != scrollToSkillIndex) {
                autoScrollTimer = 0f;
            } else {
                isFound = true;
            }
        }
//        else if (isFound) {
//            if (autoScrollTimer > 1f) {
//                show(false);
//                isAutoScrolling = false;
//                isFound = false;
//            }
//        }
        autoScrollTimer += delta;
    }
}
