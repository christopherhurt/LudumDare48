package levels;

import com.almasb.fxgl.dsl.FXGL;
import main.Constants;
import main.Names;

public abstract class ALevel {

    public void load(boolean pStopMusic, int pNewLevelIndex) {
        clearAll(pStopMusic);
        resetGameVars(pNewLevelIndex);
        setup();
    }

    private void clearAll(boolean pStopMusic) {
        FXGL.getAudioPlayer().stopAllSounds();
        if (pStopMusic) {
            FXGL.getAudioPlayer().stopAllMusic();
        }
        FXGL.getGameTimer().clear();
        FXGL.getGameWorld().removeEntities(FXGL.getGameWorld().getEntitiesCopy());
        FXGL.getGameScene().clearGameViews();
        FXGL.getGameScene().clearUINodes();
    }

    private void resetGameVars(int pNewLevelIndex) {
        FXGL.getWorldProperties().setValue(Names.LIFE_COUNT, Constants.MAX_LIVES);
        FXGL.getWorldProperties().setValue(Names.LEVEL_INDEX, pNewLevelIndex);
        FXGL.getWorldProperties().setValue(Names.BACKGROUND_MOVING, true);
    }

    protected abstract void setup();

}
