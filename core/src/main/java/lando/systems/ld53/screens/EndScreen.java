package lando.systems.ld53.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import lando.systems.ld53.Config;
import lando.systems.ld53.ui.Cutscene3ScreenUI;
import lando.systems.ld53.ui.EndScreenUI;

public class EndScreen extends BaseScreen {
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

    public EndScreen() {
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
        startSubtitles = "Wow. That was... unbelievable.\n\n I can't believe I made that entire game in 3 days!" ;
        startGenieSubtitles = "\n\n" ;
        subtitles = startSubtitles;
        genieSubtitles = startGenieSubtitles;





//        background = assets.titleScreen;

    }

    @Override
    public void update(float delta) {
        super.update(delta);

        storyAccum += delta;


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

//                if(clickPhase < maxClick) {
//                    subtitles = startSubtitles;
//
//                }

                switch (clickPhase) {
                    case 0:
                        subtitles = "Wow. That was... unbelievable.\n\n I can't believe I made that entire game in 3 days!" ;
                        genieSubtitles = "\n\n" +
                            "That's because you didn't.";
                        break;
                    case 1:
                        subtitles = "An entire game in 3 days, and all it took was an omnipotent phantasm of unspeakable power!" ;
                        genieSubtitles = "\n\n" +
                            "Flattery will get you everywhere, kid, but it's too late.\n\n"+
                            "I've got to say though, that was actually pretty neat.\n\n";
                        break;

                    case 2:
                        subtitles = "That we got to work together?\n\n";

                        genieSubtitles = "\n\n" +
                            "No, bec-";
                        break;
                    case 3:
                        subtitles = "That we got to work together?\n\n"+
                            "And become friends??";

                        genieSubtitles = "\n\n" +
                            "No, bec-\n\n";
                        break;
                    case 4:
                        subtitles = "That we got to work together?\n\n"+
                            "And become friends??";
                        genieSubtitles = "\n\n" +
                            "No, because I realized that all it took to get a developer to give me their eternal soul\n" +
                            "was some nominal help building a silly video game over a weekend.\n\n";
                        break;
                    case 5:
                        subtitles = "I... What?\n\n";
                        genieSubtitles = "Did you seriously think outsourcing your work to a cosmic demiurge\nwould be a consequence-free affair?";
                        break;
                  case 6:
                      subtitles = "But... I thought we were going to be friends\n\n" +
                          "Don't you want to make more games with me?\n\n";
                        genieSubtitles = "\n\n" +
                            "";
                        break;
                  case 7:
                      subtitles = "But... I thought we were going to be friends\n\n" +
                          "Don't you want to make more games with me?\n\n" +
                          "Forever?\n\n";
                      genieSubtitles = "\n\n" +
                          "";
                        break;
                  case 8:
                      subtitles = "But... I thought we were going to be friends\n\n" +
                          "Don't you want to make more games with me?\n\n" +
                          "Forever? \n\n" +
                          "...please?";
                      genieSubtitles = "\n\n" +
                          "";
                        break;
                  case 9:
                      subtitles = "But... I thought we were going to be friends\n\n" +
                          "Don't you want to make more games with me?\n\n" +
                          "Forever?\n\n" +
                          "...please?";
                      genieSubtitles = "\n\n\n\n\n" +
                          "Oof, wow. We genies don't even have hearts and you're breaking mine, kid.\n\n" +
                          "Tell you what. Let's see what my schedule looks like for the next game jam and we can see\n" +
                          "how things work out. \n\n" +
                          "No promises, but I can probably pencil you in.";
                        break;

                  case 10:
                      subtitles = "I'd like that.\n\n" +
                          "... can I call you dad for the next one?";
                      genieSubtitles = "\n\n\n\n" +
                          "Wow.\n\n"+
                          "Okay, we're done here.";
                        break;


                    case 11:
                        game.setScreen(new CreditScreen(game));
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
        EndScreenUI EndScreenUI = new EndScreenUI(this);
        uiStage.addActor(EndScreenUI);
    }


}
