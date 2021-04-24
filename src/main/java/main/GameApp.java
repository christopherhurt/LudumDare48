package main;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import java.util.Map;

public class GameApp extends GameApplication {

    @Override
    protected void initGameVars(Map<String, Object> pVars) {
        // TODO: do on level start
        // Initialized on level start based on difficulty
        pVars.put(Names.LIFE_COUNT, 999);
        // Initialized on level start
        pVars.put(Names.LEVEL_INDEX, 999);
    }

    @Override
    protected void initSettings(GameSettings pSettings) {
        pSettings.setWidth(600);
        pSettings.setHeight(600);
        pSettings.setTitle("Dig Inc.");
        pSettings.setVersion("");
    }

    @Override
    protected void initGame() {
        // TODO: do on level start
        // Task spawner, life manager
        FXGL.entityBuilder().with(new TaskManagerComponent(EDifficulty.HARD),
                new LifeComponent()).buildAndAttach();
    }

    public static void main(String[] pArgs) {
        launch(pArgs);
    }

}
