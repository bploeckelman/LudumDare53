package lando.systems.ld53.ui;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.kotcrab.vis.ui.widget.VisLabel;
import lando.systems.ld53.Assets;
import lando.systems.ld53.Config;
import lando.systems.ld53.entities.Goal;
import lando.systems.ld53.screens.GameScreen;

import java.util.ArrayList;
import java.util.List;

public class TopGameUI extends Table {
    private final float UI_HEIGHT = 80f;
    private final float CELL_WIDTH = Config.Screen.window_width / 6f - 5f;
    private Image redCargo;
    private Image yellowCargo;
    private Image greenCargo;
    private Image blueCargo;
    private float animTimer = 0f;
    private List<TextureRegionDrawable> redTextureRegionDrawableList = new ArrayList<>();
    private List<TextureRegionDrawable> yellowTextureRegionDrawableList = new ArrayList<>();
    private List<TextureRegionDrawable> blueTextureRegionDrawableList = new ArrayList<>();
    private List<TextureRegionDrawable> greenTextureRegionDrawableList = new ArrayList<>();
    private VisLabel redCountLabel;
    private VisLabel yellowCountLabel;
    private VisLabel blueCountLabel;
    private VisLabel greenCountLabel;
    private VisLabel redCapLabel;
    private VisLabel yellowCapLabel;
    private VisLabel blueCapLabel;
    private VisLabel greenCapLabel;
    private Label label;
    private GameScreen screen;


    public TopGameUI(GameScreen screen) {
        this.screen = screen;
        setWidth(Config.Screen.window_width);
        setHeight(UI_HEIGHT);
        setPosition(0f, Config.Screen.window_height - UI_HEIGHT);
        Table redTable = new Table();
        Table yellowTable = new Table();
        Table greenTable = new Table();
        Table blueTable = new Table();
        redTable.setBackground(Assets.Patch.glass_red.drawable);
        yellowTable.setBackground(Assets.Patch.glass_yellow.drawable);
        greenTable.setBackground(Assets.Patch.glass_green.drawable);
        blueTable.setBackground(Assets.Patch.glass_blue.drawable);

        Animation<TextureRegion> redCargoAnim = screen.assets.cargoRed;
        Animation<TextureRegion> yellowCargoAnim = screen.assets.cargoYellow;
        Animation<TextureRegion> greenCargoAnim = screen.assets.cargoGreen;
        Animation<TextureRegion> blueCargoAnim = screen.assets.cargoCyan;
        for (TextureRegion region : redCargoAnim.getKeyFrames()) {
            redTextureRegionDrawableList.add(new TextureRegionDrawable(region));
        }
        for (TextureRegion region : yellowCargoAnim.getKeyFrames()) {
            yellowTextureRegionDrawableList.add(new TextureRegionDrawable(region));
        }
        for (TextureRegion region : greenCargoAnim.getKeyFrames()) {
            greenTextureRegionDrawableList.add(new TextureRegionDrawable(region));
        }
        for (TextureRegion region : blueCargoAnim.getKeyFrames()) {
            blueTextureRegionDrawableList.add(new TextureRegionDrawable(region));
        }
        redCargo = new Image(redCargoAnim.getKeyFrame(0f));
        yellowCargo = new Image(yellowCargoAnim.getKeyFrame(0f));
        greenCargo = new Image(greenCargoAnim.getKeyFrame(0f));
        blueCargo = new Image(blueCargoAnim.getKeyFrame(0f));

        redTable.add(redCargo);
        yellowTable.add(yellowCargo);
        greenTable.add(greenCargo);
        blueTable.add(blueCargo);

        Table redTextTable = new Table();
        Table yellowTextTable = new Table();
        Table greenTextTable = new Table();
        Table blueTextTable = new Table();


        label = new VisLabel("Red", "outfit-medium-20px");
        label.setAlignment(Align.center);
        redTextTable.add(label).colspan(3).height(30f).width(100f).row();
        label = new VisLabel("Yellow", "outfit-medium-20px");
        label.setAlignment(Align.center);
        yellowTextTable.add(label).colspan(3).height(30f).width(100f).row();
        label = new VisLabel("Green", "outfit-medium-20px");
        label.setAlignment(Align.center);
        greenTextTable.add(label).colspan(3).height(30f).width(100f).row();
        label = new VisLabel("Blue", "outfit-medium-20px");
        label.setAlignment(Align.center);
        blueTextTable.add(label).colspan(3).height(30f).width(100f).row();

        redCountLabel = new VisLabel("", "outfit-medium-20px");
        redCountLabel.setText("0");
        yellowCountLabel = new VisLabel("", "outfit-medium-20px");
        yellowCountLabel.setText("0");
        greenCountLabel = new VisLabel("", "outfit-medium-20px");
        greenCountLabel.setText("0");
        blueCountLabel = new VisLabel("", "outfit-medium-20px");
        blueCountLabel.setText("0");

        redTextTable.add(redCountLabel);
        yellowTextTable.add(yellowCountLabel);
        greenTextTable.add(greenCountLabel);
        blueTextTable.add(blueCountLabel);

        VisLabel slashLabel = new VisLabel("/", "outfit-medium-20px");
        redTextTable.add(slashLabel);
        slashLabel = new VisLabel("/", "outfit-medium-20px");
        yellowTextTable.add(slashLabel);
        slashLabel = new VisLabel("/", "outfit-medium-20px");
        greenTextTable.add(slashLabel);
        slashLabel = new VisLabel("/", "outfit-medium-20px");
        blueTextTable.add(slashLabel);

        redCapLabel = new VisLabel("", "outfit-medium-20px");
        redCapLabel.setText("0");
        yellowCapLabel = new VisLabel("", "outfit-medium-20px");
        yellowCapLabel.setText("0");
        greenCapLabel = new VisLabel("", "outfit-medium-20px");
        greenCapLabel.setText("0");
        blueCapLabel = new VisLabel("", "outfit-medium-20px");
        blueCapLabel.setText("0");

        redTextTable.add(redCapLabel);
        yellowTextTable.add(yellowCapLabel);
        greenTextTable.add(greenCapLabel);
        blueTextTable.add(blueCapLabel);

        redTable.add(redTextTable);
        yellowTable.add(yellowTextTable);
        greenTable.add(greenTextTable);
        blueTable.add(blueTextTable);

        add(redTable).width(CELL_WIDTH);
        add(yellowTable).width(CELL_WIDTH);
        add().width(CELL_WIDTH);
        add().width(CELL_WIDTH);
        add(greenTable).width(CELL_WIDTH);
        add(blueTable).width(CELL_WIDTH);
    }

    public void update(float delta) {
        animTimer += delta;
        redCargo.setDrawable(redTextureRegionDrawableList.get((int)(animTimer * 12) % redTextureRegionDrawableList.size()));
        yellowCargo.setDrawable(yellowTextureRegionDrawableList.get((int)(animTimer * 12) % yellowTextureRegionDrawableList.size()));
        blueCargo.setDrawable(blueTextureRegionDrawableList.get((int)(animTimer * 12) % blueTextureRegionDrawableList.size()));
        greenCargo.setDrawable(greenTextureRegionDrawableList.get((int)(animTimer * 12) % greenTextureRegionDrawableList.size()));

        redCountLabel.setText(screen.collectedMap.get(Goal.Type.red));
        yellowCountLabel.setText(screen.collectedMap.get(Goal.Type.yellow));
        greenCountLabel.setText(screen.collectedMap.get(Goal.Type.green));
        blueCountLabel.setText(screen.collectedMap.get(Goal.Type.cyan));

        redCapLabel.setText(screen.needToCollectMap.get(Goal.Type.red));
        yellowCapLabel.setText(screen.needToCollectMap.get(Goal.Type.yellow));
        greenCapLabel.setText(screen.needToCollectMap.get(Goal.Type.green));
        blueCapLabel.setText(screen.needToCollectMap.get(Goal.Type.cyan));

    }



}
