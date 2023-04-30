package lando.systems.ld53.ui;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import lando.systems.ld53.Assets;
import lando.systems.ld53.entities.Player;

import java.util.Arrays;
import java.util.List;


public class SelectSkillUI extends Group {
    private List<Player.SpecialAbility> abilityList;
    private IndividualSkillUI selectedSkillUI;
    private Player player;
    private Assets assets;
    private Skin skin;
    public SelectSkillUI(Assets assets, Skin skin, Player player) {
        abilityList = Arrays.asList(Player.SpecialAbility.values());
        this.player = player;
        this.assets = assets;
        this.skin = skin;
//        selectedSkillUI = setMainWindowWithCurrentSkill(player);
        EmptyWindow emptyWindowLeft2 = new EmptyWindow(-2);
        EmptyWindow emptyWindowLeft1 = new EmptyWindow(-1);
        EmptyWindow emptyWindowRight1 = new EmptyWindow(1);
        EmptyWindow emptyWindowRight2 = new EmptyWindow(2);
        //addActor(selectedSkillUI);
        addActor(emptyWindowLeft2);
        addActor(emptyWindowLeft1);
        addActor(emptyWindowRight2);
        addActor(emptyWindowRight1);
        //selectedSkillUI.toFront();
        setVisible(false);
    }
//    public IndividualSkillUI setMainWindowWithCurrentSkill(Player player) {
////        IndividualSkillUI ui = new IndividualSkillUI(assets, skin, player, player.currentAbility);
//        return ui;
//    }
    public void showNextSkill() {
        int previousIndex = abilityList.indexOf(player.currentAbility);
        int currentIndex = (previousIndex + 1) % abilityList.size();
        //player.currentAbility = abilityList.get();
//        selectedSkillUI = setMainWindowWithCurrentSkill(player);
    }
    public void showPreviousSkill() {
        int previousIndex = abilityList.indexOf(player.currentAbility);
        if (previousIndex == 0) {
            previousIndex = abilityList.size();
        }
        int currentIndex = (previousIndex + 1) % abilityList.size();

        //player.currentAbility = abilityList.get((currentIndex - 1) % abilityList.size());
//        selectedSkillUI = setMainWindowWithCurrentSkill(player);
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
