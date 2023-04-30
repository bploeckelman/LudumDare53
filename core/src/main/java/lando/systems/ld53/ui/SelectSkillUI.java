package lando.systems.ld53.ui;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import lando.systems.ld53.Assets;
import lando.systems.ld53.entities.Player;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


public class SelectSkillUI extends Group {
    private HashMap<Player.SpecialAbility, IndividualSkillUI> abilityUIMap = new HashMap<>();
    private List<Player.SpecialAbility> abilityList;
    private IndividualSkillUI selectedSkillUI;
    private Player player;
    private Assets assets;
    private Skin skin;
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
        int previousIndex = abilityList.indexOf(player.currentAbility);
        int currentIndex = (previousIndex + 1) % abilityList.size();

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
        abilityUIMap.get(abilityList.get(leftIndex2)).setSizePerOffset(-2);
        abilityUIMap.get(abilityList.get(leftIndex1)).setSizePerOffset(-1);
        abilityUIMap.get(abilityList.get(currentIndex)).setSizePerOffset(0);
        abilityUIMap.get(abilityList.get(rightIndex1)).setSizePerOffset(1);
        abilityUIMap.get(abilityList.get(rightIndex2)).setSizePerOffset(2);
        abilityUIMap.get(abilityList.get(leftIndex1)).toFront();
        abilityUIMap.get(abilityList.get(rightIndex1)).toFront();
        abilityUIMap.get(abilityList.get(currentIndex)).toFront();

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
