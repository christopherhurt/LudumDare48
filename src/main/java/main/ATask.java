package main;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.time.TimerAction;
import java.util.Collection;
import java.util.function.Consumer;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.util.Duration;

public abstract class ATask {

    private static final double MIN_BASE_TIMEOUT = 3.0;
    private static final double MAX_BASE_TIMEOUT = 6.0;

    private final Consumer<ATask> mRemovalTask;
    private final Collection<Node> mViews;
    private final TimerAction mTimeoutAction;

    public ATask(EDifficulty pDifficulty, Consumer<ATask> pRemovalTask) {
        mRemovalTask = pRemovalTask;

        // Start task now
        mViews = generateViews();
        mViews.forEach(pNode -> {
            Point2D location = generateViewLocation();
            FXGL.addUINode(pNode, location.getX(), location.getY());
        });

        // Timeout timer
        double timeout = pDifficulty.getTaskTimeoutMultiplier()
                * (Math.random() * (MAX_BASE_TIMEOUT - MIN_BASE_TIMEOUT) + MIN_BASE_TIMEOUT);
        mTimeoutAction = FXGL.runOnce(this::onTimeout, Duration.seconds(timeout));
    }

    protected abstract Collection<Node> generateViews();
    protected abstract Point2D generateViewLocation();

    protected void onCompleted() {
        mTimeoutAction.expire();
        mRemovalTask.accept(this);
        // Override for task success behavior
    }

    protected void onTimeout() {
        mRemovalTask.accept(this);
        FXGL.getWorldProperties().increment(Names.LIFE_COUNT, -1);
        // Override for task failure behavior
    }

    public void destroyAndRemove() {
        mTimeoutAction.expire();
        mViews.forEach(FXGL::removeUINode);
    }

}
