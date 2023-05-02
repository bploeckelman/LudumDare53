package lando.systems.ld53.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import lando.systems.ld53.Config;
import lando.systems.ld53.ui.Cutscene1ScreenUI;

public class Cutscene1Screen extends BaseScreen {
    //    private final Texture background;
    private float storyAccum;
    private float phaseAccum;
    private int clickPhase;
    private int maxClick;
    private String subtitles;
    private String genieSubtitles;
    private String startSubtitles;
    private String startGenieSubtitles;
    public float motionCounter1;
    public float accumulator;
    public boolean isStoryOver;
//    public Map<Float, String> subtitlesMap;

    public Cutscene1Screen() {
        super();

        worldCamera.setToOrtho(false, Config.Screen.window_width, Config.Screen.window_height);
        worldCamera.update();
        Gdx.input.setInputProcessor(uiStage);
        motionCounter1 = worldCamera.viewportWidth * -1;
        storyAccum = 0;
        phaseAccum = 0;
        clickPhase = 0;
        maxClick = 4;
        isStoryOver = false;
        startSubtitles = "Wow, yeah, exactly like that. Jeez.\n\n" ;
        startGenieSubtitles = " " ;
        subtitles = startSubtitles;
        genieSubtitles = startGenieSubtitles;





//        background = assets.titleScreen;

    }

    @Override
    public void update(float delta) {
        super.update(delta);

        storyAccum += delta;

        phaseAccum += delta;

        if (((Gdx.input.justTouched() && phaseAccum > .2f) || phaseAccum > .25F)&& !isStoryOver) {

            storyAccum += delta * 3;

            if (((Gdx.input.justTouched() && phaseAccum > .2f)) && !isStoryOver) {

                // todo cancel playing sounds
                game.audioManager.stopAllSounds();

                phaseAccum = 0;

                if(clickPhase < maxClick) {
                    subtitles = startSubtitles;

                }

                switch (clickPhase) {
                    case 0:
                        subtitles = "Okay, so that was just a dream?\n\n";
                        break;
                    case 1:
                        subtitles = "I still have to BUILD and SHIP this whole thing?\n\n";

                        break;

                    case 2:
                        subtitles = "I still have to BUILD and SHIP this whole thing?\n\n" +
                            "On a deadline for Ludum Dare?\n\n" +
                            "At least it's still just the first day...\n\n";
                        break;
                    case 3:
                        subtitles = "I still have to BUILD and SHIP this whole thing?\n\n" +
                            "On a deadline for Ludum Dare?\n\n" +
                            "At least it's still just the first day...\n\n"+
                            "And I DO have those game genies to help me deliver the finished game...";
                        break;
                    case 4:

                        subtitles = "I still have to BUILD and SHIP this whole thing?\n\n" +
                            "On a deadline for Ludum Dare?\n\n" +
                            "At least it's still just the first day...\n\n"+
                            "And I DO have those game genies to help me deliver the finished game...";
                        genieSubtitles = "\n\n" +
                            "\n\n" +
                            "\n\n" +
                            "A little presumptuous of you, but... I guess we can see how this goes";
                        break;

                    case 5:
                        game.setScreen(new GameScreen(GameScreen.Levels.level2));
//                        subtitles = "Us either.\n\n" +
//                            "That would be a weird, weird premise for a game.\n\n";
                        break;

                    default:
                        isStoryOver = true;
                        break;
                }
                    clickPhase++;

            }
        }







    }

    @Override
    public void render(SpriteBatch batch) {
        ScreenUtils.clear(0.01f, 0.01f, 0.16f, 1f);

        batch.setProjectionMatrix(worldCamera.combined);
        batch.begin();
        {
            float width = worldCamera.viewportWidth;
            float height = worldCamera.viewportHeight;

            assets.largeFont.getData().setScale(.3f);
            assets.largeFont.setColor(Color.WHITE);

            assets.layout.setText(assets.largeFont, subtitles, Color.WHITE, worldCamera.viewportWidth, Align.left, true);
            assets.genieLayout.setText(assets.largeFont, genieSubtitles, Color.GOLDENROD, worldCamera.viewportWidth * .8f, Align.right, true);
            assets.largeFont.draw(batch, assets.layout, 100, worldCamera.viewportHeight * .85f );
            assets.largeFont.draw(batch, assets.genieLayout, 100, worldCamera.viewportHeight * .75f );

        }

        batch.end();

        uiStage.draw();
    }

    @Override
    public void initializeUI() {
        super.initializeUI();
        Cutscene1ScreenUI cutscene1ScreenUI = new Cutscene1ScreenUI(this);
        uiStage.addActor(cutscene1ScreenUI);
    }


}
