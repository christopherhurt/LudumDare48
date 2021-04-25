package levels;

import com.almasb.fxgl.dsl.FXGL;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

public final class LevelUtils {

    public static final double BUTTON_FONT_SIZE = 15.0;

    public static void createAndAttachMenuBackground() {
        // Background
        Rectangle background = new Rectangle(0, 0, 600, 600);
        background.setFill(Color.BLACK);
        FXGL.entityBuilder().view(background).buildAndAttach();
    }

    public static void createAndAttachMainMenuButton(double pY) {
        Button btn = new Button("Main Menu");
        btn.setFont(Font.font(Font.getDefault().getFamily(), FontWeight.BOLD, 14.0));
        btn.setOnAction(pEvt -> new MainMenu().load(true, 0));

        final double fixedWidth = 100.0;
        btn.setMinWidth(fixedWidth);
        btn.setMaxWidth(fixedWidth);
        btn.setPrefWidth(fixedWidth);

        FXGL.entityBuilder().at(300.0 - fixedWidth / 2.0, pY).view(btn).buildAndAttach();
    }

    public static Label createLabel(String pText, double pWidth, boolean pCenter, double pFontSize) {
        Label label = new Label(pText);
        label.setAlignment(Pos.CENTER);
        label.setMinWidth(pWidth);
        label.setMaxWidth(pWidth);
        label.setPrefWidth(pWidth);
        label.setFont(Font.font(Font.getDefault().getFamily(), FontWeight.BOLD, pFontSize));
        label.setTextAlignment(pCenter ? TextAlignment.CENTER : TextAlignment.JUSTIFY);
        label.setWrapText(true);
        label.setStyle("-fx-text-fill: linear-gradient(to top right, rgb(170, 170, 0) 0%, rgb(255, 255, 0) 100%);");
        return label;
    }

    private LevelUtils() {
    }

}
