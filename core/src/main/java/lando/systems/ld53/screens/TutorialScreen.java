package lando.systems.ld53.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import lando.systems.ld53.Assets;
import lando.systems.ld53.Config;
import lando.systems.ld53.Main;
import lando.systems.ld53.entities.Goal;
import lando.systems.ld53.entities.PlayerAbility;

public class TutorialScreen extends BaseScreen {

    private int page;
    private Rectangle bounds;
    float alpha;
    Color textColor;
    Color titleColor;
    float targetAlpha;
    float accum;


    public TutorialScreen() {
        page = 0;
        accum = 0;
        bounds = new Rectangle(60, 60, Config.Screen.window_width - 120, Config.Screen.window_height-120);
        alpha = 0;
        targetAlpha = 1;
        textColor = new Color(Color.WHITE);
        titleColor = new Color(.8f, .8f, 1f, 1f);
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        accum += delta;

        if (alpha < targetAlpha) alpha = Math.min(targetAlpha, alpha + delta * 2f);
        if (alpha > targetAlpha) alpha = Math.max(targetAlpha, alpha - delta * 2f);
        textColor.set(.8f, .8f, 1f, alpha);

        if (alpha == targetAlpha){
            if (targetAlpha == 0) {
                page++;
                targetAlpha = 1f;
                if (page >= 3) {
                    Main.game.setScreen(new GameScreen(GameScreen.Levels.preview));
                }
            } else {
                if (Gdx.input.justTouched() || Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY)){
                    targetAlpha = 0;
                }
            }
        }
    }

        @Override
    public void render(SpriteBatch batch) {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);

        batch.setProjectionMatrix(worldCamera.combined);
        batch.begin();

        Assets.NinePatches.glass.draw(batch, bounds.x, bounds.y, bounds.width, bounds.height);
        assets.largeFont.getData().setScale(.8f);
        assets.layout.setText(assets.largeFont, "Tutorial", titleColor, bounds.width, Align.center, true);
        assets.largeFont.draw(batch, assets.layout, bounds.x, bounds.y + bounds.height - 20);
        assets.largeFont.getData().setScale(1f);
        float height = assets.layout.height;
        assets.font.getData().setScale(.5f);
        assets.layout.setText(assets.font, "(We somehow had time for this..)", titleColor, bounds.width, Align.center, true);
        assets.font.draw(batch, assets.layout, bounds.x, bounds.y + bounds.height - 40 - height);

        batch.setColor(1, 1, 1, alpha);
        switch (page) {
            case 0: render1(batch);
                break;
            case 1: render2(batch);
                break;
            case 2: render3(batch);
                break;
        }
        batch.setColor(Color.WHITE);

        assets.font.getData().setScale(.5f);
        String continueString = "Click or press a key to continue";
        if (page == 2) continueString = "Let's start";
        assets.layout.setText(assets.font, continueString, textColor, bounds.width, Align.center, true);
        assets.font.draw(batch, assets.layout, bounds.x, bounds.y + assets.layout.height + 15);
        assets.font.getData().setScale(1f);
        batch.end();
    }

    private void render1(SpriteBatch batch) {
        // Packages

        float delta = 60;
        for (int i = 0; i < 4; i++){
            TextureRegion tex = assets.cargoCyan.getKeyFrame(accum);
            switch(i){
                case 0:
                    tex = assets.cargoCyan.getKeyFrame(accum);
                    break;
                case 1:
                    tex = assets.cargoRed.getKeyFrame(accum);
                    break;
                case 2:
                    tex = assets.cargoGreen.getKeyFrame(accum);
                    break;
                case 3:
                    tex = assets.cargoYellow.getKeyFrame(accum);
                    break;
                default:
            }
            batch.draw(tex, bounds.x + 445 + (i * (delta+10)), bounds.y + 400, delta, delta);
        }
        assets.layout.setText(assets.font, "Packages", textColor, bounds.width, Align.center, true);
        assets.font.draw(batch, assets.layout, bounds.x, bounds.y + 400 - 10);

        TextureRegion tex = Goal.Type.cyan.baseAnim.getKeyFrame(accum);
        batch.draw(tex, Config.Screen.window_width/2f - 30, bounds.y + 290, delta, delta);

        assets.layout.setText(assets.font, "Goals", textColor, bounds.width, Align.center, true);
        assets.font.draw(batch, assets.layout, bounds.x, bounds.y + 290 - 10);

        assets.layout.setText(assets.font, "You need to \"deliver\" the packages to their corresponding goals.\nEach level has a quota of packages that need to be delivered to move to the next level.", textColor, bounds.width, Align.center, true);
        assets.font.draw(batch, assets.layout, bounds.x, bounds.y + 200 - 10);
    }

    private void render2(SpriteBatch batch) {
        // Controls
        float delta = 60;
        for (int i = 0; i < 4; i++){
            TextureRegion tex = assets.gobbler.getKeyFrame(accum);
            switch(i){
                case 0:
                    tex = assets.thief.getKeyFrame(accum);
                    break;
                case 1:
                    tex = assets.reapo.getKeyFrame(accum);
                    break;
                case 2:
                    tex = assets.turtle.getKeyFrame(accum);
                    break;
                case 3:
                    tex = assets.gobbler.getKeyFrame(accum);
                    break;
                default:
            }
            batch.draw(tex, bounds.x + 445 + (i * (delta+10)), bounds.y + 400, delta, delta);
        }
        assets.layout.setText(assets.font, "Enemies", textColor, bounds.width, Align.center, true);
        assets.font.draw(batch, assets.layout, bounds.x, bounds.y + 400 - 10);

        batch.draw(assets.playerIdleRight.getKeyFrame(accum), Config.Screen.window_width/2f - 40, bounds.y + 290, 80, 80);
        assets.layout.setText(assets.font, "The Player (You)", textColor, bounds.width, Align.center, true);
        assets.font.draw(batch, assets.layout, bounds.x, bounds.y + 290 - 10);

        assets.layout.setText(assets.font, "Move the player with WASD and press SPACE to use your power\n\nThings bounce around like billiard balls.  Knock the packages into their goals by moving into them, but watch out enemies will show up to harass you.", textColor, bounds.width - 20, Align.center, true);
        assets.font.draw(batch, assets.layout, bounds.x+10, bounds.y + 200 - 10);
    }

    private void render3(SpriteBatch batch) {
        // Powerups
        float delta = 60;
        for (int i = 0; i < 5; i++){
            PlayerAbility ability = PlayerAbility.bomb_throw;
            switch(i){
                case 0:
                    ability = PlayerAbility.bomb_throw;
                    break;
                case 1:
                    ability = PlayerAbility.speed_up;
                    break;
                case 2:
                    ability = PlayerAbility.repulse;
                    break;
                case 3:
                    ability = PlayerAbility.shield_360;
                    break;
                case 4:
                    ability = PlayerAbility.fetch;
                    break;
                default:
            }
            batch.draw(ability.textureRegion, bounds.x + 100, bounds.y + 420 - (i * (delta+5)), delta, delta);
            assets.layout.setText(assets.font, ability.title + " - " + ability.description, textColor, bounds.width - 200, Align.left, true);
            assets.font.draw(batch, assets.layout, bounds.x + 190, bounds.y + 420 - (i * (delta+5)) + 30 + assets.layout.height/2f);
        }
        assets.layout.setText(assets.font, "Powerups: You can select which powerup you are using with (1-5) and press SPACE to activate, but watch how much stamina you have (The bar at the top)", textColor, bounds.width, Align.center, true);
        assets.font.draw(batch, assets.layout, bounds.x, bounds.y + 100 - 10);

    }
}
