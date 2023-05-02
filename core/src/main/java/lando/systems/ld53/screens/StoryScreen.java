package lando.systems.ld53.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import lando.systems.ld53.Config;
import lando.systems.ld53.ui.StoryScreenUI;

public class StoryScreen extends BaseScreen {
    //    private final Texture background;
    private float storyAccum;
    private float phaseAccum;
    private int clickPhase;
    private int maxClick;
    private String subtitles;
    private String startSubtitles;
    public float motionCounter1;
    public float accumulator;
    public boolean isStoryOver;
//    public Map<Float, String> subtitlesMap;

    public StoryScreen() {
        super();

        worldCamera.setToOrtho(false, Config.Screen.window_width, Config.Screen.window_height);
        worldCamera.update();
        Gdx.input.setInputProcessor(uiStage);
        motionCounter1 = worldCamera.viewportWidth * -1;
        storyAccum = 0;
        phaseAccum = 0;
        clickPhase = 0;
        maxClick = 6;
        isStoryOver = false;
        startSubtitles = "Have you ever had an idea for a game come to you in a dream?\n\n" +
            "One so fully formed that it felt as if it was put there by someone - or someTHING - else?";
        subtitles = startSubtitles;





//        background = assets.titleScreen;

    }

    @Override
    public void update(float delta) {
        super.update(delta);

        storyAccum += delta;
//        if(storyAccum < 3f) {
//            subtitles = "Have you ever had an idea come to you in a dream?\n\n " +
//                "One so fully formed, you felt as if it came from somewhere else?";
//        }
//        else if (storyAccum < 5f) {
//            subtitles = "\n\nNo?";
//        }
//        else if (storyAccum < 8f) {
//            subtitles = "\n\nWell buckle the fuck up, honey, because that's what this game is all ABOUT!";
//        }


        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }

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
                        subtitles = "Perhaps a genie?\n\n";
                        break;
                    case 1:
                        subtitles = "Perhaps a genie? Possibly some sort of GAME genie?\n\n";

                        break;

                    case 2:
                        subtitles = "Perhaps a genie? Possibly some sort of GAME genie?\n\n" +
                            "Or perhaps a team of game genies, working together, each of whom require you to deliver\n" +
                            "colorful, uniquely shaped objects to their respective goal areas on the level map?\n\n";


                        break;
                    case 3:
                        subtitles = "And in the process, they would be helping you deliver a finished game (complete with\n" +
                            "different abilities and enemies and spawners thereof) while also navigating\n" +
                            "forcefields that change the behavior of the various in-game objects in unusual ways?\n\n" +
                            "Maybe while using...\n\n" +
                            "WASD and Arrows to move\n" +
                            "Spacebar to activate special abilities\n" +
                            "and number keys to select between the different abilities?";
                        break;

                        case 4:
                        subtitles = "Me either.\n\n" +
                            "That would be a weird, weird premise for a game.\n\n";
                        break;
                        case 5:
                        subtitles = "And really quite a stretch for a game jam whose theme was \"delivery\".";
                        break;

                    case 6:
                        game.setScreen(new TutorialScreen());
                        break;
//                    case 3:
//
//
//                        subtitles = "Reaping, crossing over, ferrying spirits... Doesn't really matter WHAT you call it.\n\n" +
//                            "Bottom line, lot of souls need to be harvested these days. \n\n" +
//                            "Too many, if you ask me."
//                        ;
//
//                        break;
//                    case 4:
//                        cutsceneTexture = game.assets.cutscene2;
//
//
//                        subtitles = "How many people we got on Earth these days, anyway? \n\n" +
//                            "Seven, eight thousand?\n\n";
//
//                        break;
//                    case 5:
//                        subtitles = "BILLION? \n\n" +
//                            "With a B?? \n\n" +
//                            "Jesus. No wonder they're \"encouraging\" us to work overtime.";
//                        break;
//                    case 6:
//                        subtitles = "Honestly, you ask me, this whole gig's wearing a bit thin.\n\n"+
//                            "I've barely got it in me to meet the upstairs quota... \n\n" +
//                            "and don't even get me STARTED on all the people going downstairs";
//                        break;
//                    case 7:
//                        subtitles = "Ya know, I probably don't have to get ALL the souls on my list.\n\n" +
//                            "Enough to hit a quota, maybe get the boss to notice... Easy peasy.\n\n";
//
//                        break;
//                    case 8:
//                        subtitles = "Anyway, we better get this show on the road - can't risk another write-up.\n\n" +
//                            "You're late with the scythe on ONE royal matriarch and Management won't let you hear the end of it. \n\n" +
//                            "Let's do this.";

//                        break;
                    default:
                        isStoryOver = true;
//                        game.getScreenManager().pushScreen("game", TransitionManager.TransitionType.PAGE_CURL.name());
                        break;
                }
                if(clickPhase < maxClick) {
                    clickPhase++;
                }

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
            assets.largeFont.draw(batch, assets.layout, 100, worldCamera.viewportHeight * .85f );

        }

        batch.end();

        uiStage.draw();
    }

    @Override
    public void initializeUI() {
        super.initializeUI();
        StoryScreenUI storyScreenUI = new StoryScreenUI(this);
        uiStage.addActor(storyScreenUI);
    }


}
