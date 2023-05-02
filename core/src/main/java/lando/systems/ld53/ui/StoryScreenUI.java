package lando.systems.ld53.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import lando.systems.ld53.Assets;
import lando.systems.ld53.Main;
import lando.systems.ld53.audio.AudioManager;
import lando.systems.ld53.screens.BaseScreen;
import lando.systems.ld53.screens.GameScreen;
import lando.systems.ld53.screens.TutorialScreen;

public class StoryScreenUI extends Group {



    private TextButton startGameButton;
    private TextButton creditButton;
    private TextButton nextButton;
    private final float BUTTON_WIDTH = 180f;
    private final float BUTTON_HEIGHT = 50f;
    private final float BUTTON_PADDING = 10f;

    public StoryScreenUI(BaseScreen screen) {
        SettingsUI settingsUI = new SettingsUI(screen.assets, screen.skin, screen.audioManager, screen.windowCamera);

        TextButton.TextButtonStyle outfitMediumStyle = screen.skin.get("text", TextButton.TextButtonStyle.class);
        TextButton.TextButtonStyle storyScreenButtonStyle = new TextButton.TextButtonStyle(outfitMediumStyle);
        storyScreenButtonStyle.font = screen.assets.smallFont;
        storyScreenButtonStyle.fontColor = Color.WHITE;
        storyScreenButtonStyle.up = Assets.Patch.glass.drawable;
        storyScreenButtonStyle.down = Assets.Patch.glass_dim.drawable;
        storyScreenButtonStyle.over = Assets.Patch.glass_dim.drawable;

        Main.game.audioManager.playMusic(AudioManager.Musics.level1Thin);

        BaseScreen storyScreen = screen;
        float left = screen.windowCamera.viewportWidth * .8f;
        float top = screen.windowCamera.viewportHeight * .15f;

        startGameButton = new TextButton("Start Game", storyScreenButtonStyle);
        startGameButton.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        startGameButton.setPosition(left, top);
        startGameButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                screen.audioManager.stopAllSounds();
                screen.audioManager.stopMusic();
                screen.exitingScreen = true;
                Gdx.input.setInputProcessor(null);
                screen.game.setScreen(new TutorialScreen());
            }
        });

        nextButton = new TextButton("Next...", storyScreenButtonStyle);
        nextButton.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        nextButton.setPosition(left, startGameButton.getY() + startGameButton.getHeight() + BUTTON_PADDING + 25);
        nextButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {



            }
        });


//        creditButton = new TextButton("Credits", storyScreenButtonStyle);
//        creditButton.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);
//        creditButton.setPosition(left, nextButton.getY() - nextButton.getHeight() - BUTTON_PADDING);
//        creditButton.addListener(new ClickListener() {
//            @Override
//            public void clicked(InputEvent event, float x, float y) {
//                Gdx.input.setInputProcessor(null);
//                screen.exitingScreen = true;
//                screen.game.setScreen(new CreditScreen(screen.game));
//            }
//        });


        addActor(startGameButton);
        addActor(nextButton);
//        addActor(creditButton);
//        addActor(settingsUI);
    }
}
