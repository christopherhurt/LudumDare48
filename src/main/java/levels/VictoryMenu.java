package levels;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.texture.Texture;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class VictoryMenu extends ALevel {

    private static final double LABELS_Y = 10.0;
    private static final double BUTTON_Y = 535.0;

    @Override
    protected void setup() {
        // Background image
        Texture background = FXGL.texture("victory-background.png");
        background.setFitWidth(600.0);
        background.setFitHeight(600.0);
        background.setSmooth(false);
        FXGL.entityBuilder().at(0, 0).view(background).buildAndAttach();

        // Labels
        Label youWin = LevelUtils.createLabel("You Win!", 300.0, true, 35.0);
        double fixedWidth = 550.0;
        Label message = LevelUtils.createLabel(
                "You have reached the center of the earth! You and Dig Inc. will go down in history!", fixedWidth, true, 13.0);
        VBox container = new VBox(youWin, message);
        container.setAlignment(Pos.CENTER);
        FXGL.entityBuilder().at(300.0 - fixedWidth / 2.0, LABELS_Y).view(container).buildAndAttach();

        LevelUtils.createAndAttachMainMenuButton(BUTTON_Y);
    }

}
