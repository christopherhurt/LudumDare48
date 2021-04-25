package levels;

import com.almasb.fxgl.dsl.FXGL;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import main.EDifficulty;

public class MainMenu extends ALevel {

    private static final String DESCRIPTION = "The deepest hole every bored was the Soviet Union's Kola Superdeep Borehole. "
            + "That hole, however, did not even pierce the mantle past the Earth's crust. The team of researchers at your "
            + "company, Dig Inc., has developed a series of revolutionary technologies that, in practice, may make it "
            + "possible to dig to the very center of the earth. With the drill's construction completed and all other "
            + "preparations in place, the only remaining endeavor is to begin the journey to go deeper, and deeper, and deeper...";
    private static final double OFFSET_Y = 75.0;

    @Override
    protected void setup() {
        LevelUtils.createAndAttachMenuBackground();

        // Game description
        final double fixedTextWidth = 500.0;
        Label description = LevelUtils.createLabel(DESCRIPTION, fixedTextWidth, false, 20.0);

        // Difficulty selectors
        VBox buttonBox = new VBox(
                createButton("Easy", "64, 255, 64, 0.7", EDifficulty.EASY),
                createButton("Medium", "255, 255, 64, 0.7", EDifficulty.MEDIUM),
                createButton("Hard", "255, 64, 64, 0.7", EDifficulty.HARD));

        // Menu items container
        VBox mainMenuItems = new VBox(40.0, description, buttonBox);
        buttonBox.setAlignment(Pos.CENTER);
        FXGL.entityBuilder().at(300.0 - fixedTextWidth / 2.0, OFFSET_Y).view(mainMenuItems).buildAndAttach();
    }

    private static Button createButton(String pText, String pRgba, EDifficulty pDifficulty) {
        Button btn = new Button(pText);
        btn.setFont(Font.font(Font.getDefault().getFamily(), FontWeight.BOLD, LevelUtils.BUTTON_FONT_SIZE));
        btn.setStyle("-fx-base: rgba(" + pRgba + ");");
        btn.setOnAction(pEvt -> startGame(pDifficulty));

        final double fixedWidth = 100.0;
        btn.setMinWidth(fixedWidth);
        btn.setMaxWidth(fixedWidth);
        btn.setPrefWidth(fixedWidth);

        return btn;
    }

    private static void startGame(EDifficulty pDifficulty) {
        new GameplayLevel(pDifficulty, true).load(true, 0);
    }

}
