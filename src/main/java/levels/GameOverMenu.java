package levels;

import com.almasb.fxgl.dsl.FXGL;
import javafx.scene.control.Label;

public class GameOverMenu extends ALevel {

    private static final double LABEL_Y = 200.0;
    private static final double BUTTON_Y = 350.0;

    @Override
    protected void setup() {
        LevelUtils.createAndAttachMenuBackground();

        double fixedLabelWidth = 400.0;
        Label label = LevelUtils.createLabel("GAME OVER!", fixedLabelWidth, true, 60.0);
        FXGL.entityBuilder().at(300.0 - fixedLabelWidth / 2.0, LABEL_Y).view(label).buildAndAttach();

        LevelUtils.createAndAttachMainMenuButton(BUTTON_Y);
    }

}
