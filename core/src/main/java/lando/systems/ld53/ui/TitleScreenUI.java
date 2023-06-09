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
import lando.systems.ld53.screens.CreditScreen;
import lando.systems.ld53.screens.StoryScreen;
import lando.systems.ld53.screens.TutorialScreen;

public class TitleScreenUI extends Group {

    private TextButton startGameButton;
    private TextButton creditButton;
    private TextButton settingsButton;
    private final float BUTTON_WIDTH = 180f;
    private final float BUTTON_HEIGHT = 50f;
    private final float BUTTON_PADDING = 10f;

    public TitleScreenUI(BaseScreen screen) {
        SettingsUI settingsUI = new SettingsUI(screen.assets, screen.skin, screen.audioManager, screen.windowCamera);

        TextButton.TextButtonStyle outfitMediumStyle = screen.skin.get("text", TextButton.TextButtonStyle.class);
        TextButton.TextButtonStyle titleScreenButtonStyle = new TextButton.TextButtonStyle(outfitMediumStyle);
        titleScreenButtonStyle.font = screen.assets.smallFont;
        titleScreenButtonStyle.fontColor = Color.WHITE;
        titleScreenButtonStyle.up = Assets.Patch.glass.drawable;
        titleScreenButtonStyle.down = Assets.Patch.glass_dim.drawable;
        titleScreenButtonStyle.over = Assets.Patch.glass_dim.drawable;

        Main.game.audioManager.playMusic(AudioManager.Musics.introMusic);


        float left = screen.windowCamera.viewportWidth * .8f;
        float top = screen.windowCamera.viewportHeight * (1f / 4f);

        startGameButton = new TextButton("Start Game", titleScreenButtonStyle);
        startGameButton.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        startGameButton.setPosition(left, top);
        startGameButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                screen.audioManager.stopAllSounds();
                screen.audioManager.stopMusic();
                screen.exitingScreen = true;
                Gdx.input.setInputProcessor(null);
//                screen.game.setScreen(new GameScreen());
                screen.game.setScreen(new TutorialScreen());
            }
        });

        settingsButton = new TextButton("Settings", titleScreenButtonStyle);
        settingsButton.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        settingsButton.setPosition(left, startGameButton.getY() - startGameButton.getHeight() - BUTTON_PADDING);
        settingsButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                settingsUI.showSettings();
            }
        });


        creditButton = new TextButton("Credits", titleScreenButtonStyle);
        creditButton.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        creditButton.setPosition(left, settingsButton.getY() - settingsButton.getHeight() - BUTTON_PADDING);
        creditButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.input.setInputProcessor(null);
                screen.exitingScreen = true;
                screen.game.setScreen(new CreditScreen(screen.game));
            }
        });


        addActor(startGameButton);
        addActor(settingsButton);
        addActor(creditButton);
        addActor(settingsUI);
    }
}
