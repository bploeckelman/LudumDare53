package lando.systems.ld53.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Align;
import lando.systems.ld53.Config;
import lando.systems.ld53.Main;
import lando.systems.ld53.utils.typinglabel.TypingLabel;

public class CreditScreen extends BaseScreen {

    private final TypingLabel titleLabel;
    private final TypingLabel themeLabel;
    private final TypingLabel leftCreditLabel;
    private final TypingLabel rightCreditLabel;
    private final TypingLabel thanksLabel;
    private final TypingLabel disclaimerLabel;
    private final TypingLabel rossmanLabel;

    private final Animation<TextureRegion> catAnimation;
    private final Animation<TextureRegion> dogAnimation;
    private final Animation<TextureRegion> kittenAnimation;
    private final TextureRegion background;

    private final String title = "{GRADIENT=purple;cyan}Game, Genie: Crunch Time{ENDGRADIENT}";
    private final String theme = "Made for Ludum Dare 53: Delivery";

    private final String thanks = "{GRADIENT=purple;cyan}Thanks for playing our game!{ENDGRADIENT}";
    private final String developers = "{COLOR=gray}Developed by:{COLOR=white}\n {GRADIENT=white;gray}Brian Ploeckelman{ENDGRADIENT} \n {GRADIENT=white;gray}Doug Graham{ENDGRADIENT} \n {GRADIENT=white;gray}Jeffrey Hwang{ENDGRADIENT}";
    private final String rossman = "{GRADIENT=gray;white}...also Brian Rossman /s{ENDGRADIENT}";
    private final String artists = "{COLOR=gray}Art by:{COLOR=white}\n {GRADIENT=white;gray}Matt Neumann{ENDGRADIENT}";
    private final String emotionalSupport = "{COLOR=cyan}Emotional Support:{COLOR=white}\n  Asuka,     Osha\n  and Cherry";
    private final String music = "{COLOR=gray}Music, Sounds, and Miscellaneous:{COLOR=white}\n {GRADIENT=white;gray}Pete Valeo{ENDGRADIENT}";
    private final String libgdx = "Made with {COLOR=red}<3{COLOR=white}\nand LibGDX";
    private final String disclaimer = "{GRADIENT=black;gray}Disclaimer:{ENDGRADIENT}  {GRADIENT=gold;yellow}{JUMP=.2}{WAVE=0.8;1.1;1.1}No binaries were harmed in the making of this game{ENDWAVE}{ENDJUMP}{ENDGRADIENT}";

    private float accum = 0f;

    public CreditScreen(Main game) {
        super();

        titleLabel = new TypingLabel(game.assets.font, title.toLowerCase(), 0f, Config.Screen.window_height - 15f);
        titleLabel.setWidth(Config.Screen.window_width);
        titleLabel.setFontScale(1f);

        themeLabel = new TypingLabel(game.assets.smallFont, theme.toLowerCase(), 0f, Config.Screen.window_height - 70f);
        themeLabel.setWidth(Config.Screen.window_width);
        themeLabel.setFontScale(1f);

        leftCreditLabel = new TypingLabel(game.assets.smallFont, developers.toLowerCase() + "\n\n" + emotionalSupport.toLowerCase() + "\n\n", 75f, Config.Screen.window_height / 2f + 135f);
        leftCreditLabel.setWidth(Config.Screen.window_width / 2f - 150f);
        leftCreditLabel.setLineAlign(Align.left);
        leftCreditLabel.setFontScale(1f);

        rossmanLabel = new TypingLabel(game.assets.smallFont, rossman.toLowerCase(), 200f, 165f);
        rossmanLabel.setWidth(Config.Screen.window_width / 2f - 150f);
        rossmanLabel.setLineAlign(Align.left);
        rossmanLabel.setFontScale(0.4f);

        background = game.assets.pixelRegion;

        rightCreditLabel = new TypingLabel(game.assets.smallFont, artists.toLowerCase() + "\n\n" + music.toLowerCase() + "\n\n" + libgdx.toLowerCase(), Config.Screen.window_width / 2 + 75f, Config.Screen.window_height / 2f + 135f);
        rightCreditLabel.setWidth(Config.Screen.window_width / 2f - 150f);
        rightCreditLabel.setLineAlign(Align.left);
        rightCreditLabel.setFontScale(1f);

        thanksLabel = new TypingLabel(game.assets.smallFont, thanks.toLowerCase(), 0f, 115f);
        thanksLabel.setWidth(Config.Screen.window_width);
        thanksLabel.setLineAlign(Align.center);
        thanksLabel.setFontScale(1f);

        disclaimerLabel = new TypingLabel(game.assets.smallFont, disclaimer, 0f, 50f);
        disclaimerLabel.setWidth(Config.Screen.window_width);
        thanksLabel.setLineAlign(Align.center);
        disclaimerLabel.setFontScale(.6f);

        catAnimation = game.assets.cherry;
        dogAnimation = game.assets.asuka;
        kittenAnimation = game.assets.osha;

    }

    @Override
    public void update(float dt) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) || Gdx.input.isTouched()) {
            game.setScreen(new TitleScreen());
            return;
        }
        accum += dt;
        titleLabel.update(dt);
        themeLabel.update(dt);
        leftCreditLabel.update(dt);
        rightCreditLabel.update(dt);
        if (leftCreditLabel.hasEnded()) {
            rossmanLabel.update(dt);
        }
        thanksLabel.update(dt);
        disclaimerLabel.update(dt);
    }

    @Override
    public void render(SpriteBatch batch) {

        batch.setProjectionMatrix(windowCamera.combined);
        batch.begin();
        {
            //batch.draw(background, 0, 0, Config.Screen.window_width, Config.Screen.window_height);

            batch.setColor(0f, 0f, 0f, 0.6f);
            batch.draw(game.assets.pixelRegion, 25f, 130f, Config.Screen.window_width / 2f - 50f, 400f);
            batch.draw(game.assets.pixelRegion, Config.Screen.window_width / 2f + 25f, 130f, Config.Screen.window_width / 2f - 50f, 400f);

            batch.setColor(Color.WHITE);
            titleLabel.render(batch);
            themeLabel.render(batch);
            leftCreditLabel.render(batch);
            if (leftCreditLabel.hasEnded()) {
                rossmanLabel.render(batch);
            }
            rightCreditLabel.render(batch);
            thanksLabel.render(batch);
            disclaimerLabel.render(batch);
            if (accum > 7.5) {
                TextureRegion catTexture = catAnimation.getKeyFrame(accum);
                TextureRegion dogTexture = dogAnimation.getKeyFrame(accum);
                TextureRegion kittenTexture = kittenAnimation.getKeyFrame(accum);
                batch.draw(kittenTexture, 200f, 275f);
                batch.draw(dogTexture, 60f, 275f);
                batch.draw(catTexture, 270f, 250f);
            }
            batch.setColor(Color.WHITE);
        }
        batch.end();
    }

}
