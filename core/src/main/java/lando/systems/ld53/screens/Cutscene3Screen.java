package lando.systems.ld53.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import lando.systems.ld53.Config;
import lando.systems.ld53.ui.Cutscene3ScreenUI;

public class Cutscene3Screen extends BaseScreen {
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

    public Cutscene3Screen() {
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
        startSubtitles = "I think this might just work!\n\n" ;
        startGenieSubtitles = "...\n\n" ;
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
                        subtitles = "Two days down, and we mostly have a game!\n\n";
                        genieSubtitles = "... That's...";
                        break;
                    case 1:
                        subtitles = "Two days down, and we mostly have a game!\n\n";;
                        genieSubtitles = "That is an awfully generous interpretation of \"we\"\n\n" +
                            "Who exactly do you think is doing all the work here?";

                        break;

                    case 2:
                        subtitles = "What do you mean?\n\n" +
                            "Other than the programming and graphics and music and gameplay and level design,\n" +
                            "I'm doing EVERYTHING!\n";
                        genieSubtitles = "\n\n" +
                            "So you're the \"ideas guy\". Wow.\n\n" +
                            "What's crunch time like when you're trying to ship ideas?";
                        break;
                    case 3:
                        subtitles = "What do you mean?\n\n" +
                            "Other than the programming and graphics and music and gameplay and level design,\n" +
                            "I'm doing EVERYTHING!\n\n";
                        genieSubtitles = "\n\n" +
                            "So you're the \"ideas guy\". Wow.\n\n" +
                            "What's crunch time like when you're trying to ship ideas?\n\n" +
                            "Pretty stressful? Lots of naps, I imagine?";
                        break;
                    case 4:
                        subtitles = "Only when I get the sleepies!\n\n";
                        genieSubtitles = "\n\n...";
                        break;
                    case 5:
                        subtitles = "Only when I get the sleepies!\n\n";
                        genieSubtitles = "\n\n... \n\nChrist. Let's wrap this up - I've got a lamp that needs rubbing, if you catch my meaning.";
                        break;
                  case 6:
                        subtitles = "Dude! Eew.\n\n";
                        genieSubtitles = "\n\n" +
                            "Whatever, square. Sounds like someone needs THEIR lamp rubbed.";
                        break;
                  case 7:
                        subtitles = "Also, what's the deal with all those sprites? I feel like I've seen them all before...\n\n";
                        genieSubtitles = "" +
                            "Yeah, and?";
                        break;
                  case 8:
                        subtitles = "Also, what's the deal with all those sprites? I feel like I've seen them all before...\n\n";
                        genieSubtitles = "" +
                            "Yeah, and?\n\n" +
                            "Don't exactly see you burning the midnight oil over here. \n\n" +
                            "In fact, haven't seen you do much of ANYTHING at this point.\n\n" +
                            "Let's just bring this thing home.";
                        break;


                    case 9:
                        game.setScreen(new GameScreen(GameScreen.Levels.level4));
//                        subtitles = "Us either.\n\n" +
//                            "That would be a weird, weird premise for a game.\n\n";
                        break;
//                    case 5:
//                        subtitles = "And really quite a stretch for a game jam whose theme was \"delivery\".";
//                        break;
//
//                    case 6:
//                        game.setScreen(new GameScreen());
//                        break;
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
        Cutscene3ScreenUI cutscene3ScreenUI = new Cutscene3ScreenUI(this);
        uiStage.addActor(cutscene3ScreenUI);
    }


}
