package main;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import java.util.Map;
import javafx.scene.Cursor;
import levels.MainMenu;

public class GameApp extends GameApplication {

    @Override
    protected void initGameVars(Map<String, Object> pVars) {
        pVars.put(Names.LIFE_COUNT, Constants.MAX_LIVES);
        pVars.put(Names.LEVEL_INDEX, 0);
        pVars.put(Names.BACKGROUND_MOVING, true);
    }

    @Override
    protected void initSettings(GameSettings pSettings) {
        pSettings.setWidth(600);
        pSettings.setHeight(600);
        pSettings.setTitle("Dig Inc.");
        pSettings.setVersion("");
        pSettings.getCSSList().add("styles.css");
    }

    @Override
    protected void initGame() {
        FXGL.getGameScene().getRoot().setCursor(Cursor.DEFAULT);

        // Launch main menu
        new MainMenu().load(true, 0);
    }

    public static void main(String[] pArgs) {
        launch(pArgs);
    }

}
