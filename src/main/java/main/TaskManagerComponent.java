package main;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.time.TimerAction;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.text.Text;
import javafx.util.Duration;
import tasks.BlockageTask;
import tasks.CommandTask;
import tasks.CoolantTask;
import tasks.FlamesTask;
import tasks.RefuelTask;
import tasks.ReplaceBitTask;

public final class TaskManagerComponent extends Component {

    private static final double INITIAL_DELAY = 5.0;
    private static final double MIN_SPAWN_DELAY = 3.0;
    private static final double MAX_SPAWN_DELAY = 5.0;
    private static final int MAX_CONCURRENT_TASKS = 3;

    private final EDifficulty mDifficulty;
    private final Map<TaskName, ATask> mCurrentTasks = new ConcurrentHashMap<>();
    private final Consumer<ATask> mTaskRemovalTask = pTask -> {
        mCurrentTasks.values().remove(pTask);
        pTask.destroyAndRemove();
    };

    private TimerAction mSpawnAction = null;
    private boolean mSpawnable = false;

    public TaskManagerComponent(EDifficulty pDifficulty) { // TODO: change spawns with level index!!
        mDifficulty = pDifficulty;
    }

    @Override
    public void onAdded() {
        mSpawnAction = FXGL.runOnce(this::startSpawnTimer, Duration.seconds(INITIAL_DELAY));
    }

    @Override
    public void onUpdate(double pTpf) {
        if (mSpawnable && mCurrentTasks.size() < MAX_CONCURRENT_TASKS) {
            int taskIndex = (int)(Math.random() * Math.min(mDifficulty.getInitialTasks()
                    + FXGL.getWorldProperties().getInt(Names.LEVEL_INDEX), TaskName.values().length));
            TaskName taskName = TaskName.values()[taskIndex];

            // Don't add more than one of the same task at once
            // If this task is already in play, skip a frame
            if (!mCurrentTasks.containsKey(taskName)) {
                mCurrentTasks.put(taskName, createTask(taskName));
                startSpawnTimer();
            }
        }
    }

    private void startSpawnTimer() {
        mSpawnable = false;
        double time = mDifficulty.getTaskRateMultiplier()
                * (Math.random() * (MAX_SPAWN_DELAY - MIN_SPAWN_DELAY) + MIN_SPAWN_DELAY);
        mSpawnAction = FXGL.runOnce(() -> mSpawnable = true, Duration.seconds(time));
    }

    private ATask createTask(TaskName pName) {
        switch (pName) {
            case BIT:
                return new ReplaceBitTask(mDifficulty, mTaskRemovalTask);
            case REFUEL:
                return new RefuelTask(mDifficulty, mTaskRemovalTask);
            case COOLANT:
                return new CoolantTask(mDifficulty, mTaskRemovalTask);
            case COMMAND:
                return new CommandTask(mDifficulty, mTaskRemovalTask);
            case BLOCKAGE:
                return new BlockageTask(mDifficulty, mTaskRemovalTask);
            case FLAMES:
                return new FlamesTask(mDifficulty, mTaskRemovalTask);
            default:
                return new ATask(mDifficulty, mTaskRemovalTask) {
                    @Override
                    protected Collection<Node> generateViews() {
                        return Collections.singleton(new Text("You goofed!"));
                    }
                    @Override
                    protected Point2D generateViewLocation() {
                        return new Point2D(100, 100);
                    }
                    @Override
                    protected double getBaseTimeout() {
                        return 3.0;
                    }
                };
        }
    }

    @Override
    public void onRemoved() {
        mCurrentTasks.forEach((pKey, pVal) -> pVal.destroyAndRemove());
        if (mSpawnAction != null) {
            mSpawnAction.expire();
        }
    }

    private enum TaskName {
        // Ordered by difficulty (easy to hard)
        BIT,
        REFUEL,
        COOLANT,
        BLOCKAGE,
        COMMAND,
        FLAMES
    }

}
