package levels;

import com.almasb.fxgl.dsl.FXGL;
import main.BackgroundComponent;
import main.EDifficulty;
import main.LevelTransitionComponent;
import main.LifeComponent;
import main.Names;
import main.TaskManagerComponent;

public class GameplayLevel extends ALevel {

    private final EDifficulty mDifficulty;
    private final boolean mStartMusic;

    public GameplayLevel(EDifficulty pDifficulty, boolean pStartMusic) {
        mDifficulty = pDifficulty;
        mStartMusic = pStartMusic;
    }

    @Override
    protected void setup() {
        if (mStartMusic) {
            // TODO: start music playing
        }

        // Task spawner, life manager
        int levelIndex = FXGL.getWorldProperties().getInt(Names.LEVEL_INDEX);
        FXGL.entityBuilder().with(new TaskManagerComponent(mDifficulty, levelIndex),
                new LifeComponent(), new BackgroundComponent(levelIndex), new LevelTransitionComponent(mDifficulty))
                .buildAndAttach();
    }

}
