package tasks;

import java.util.Collection;
import java.util.Collections;
import java.util.function.Consumer;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.Button;
import main.ATask;
import main.EDifficulty;

public class ReplaceBitTask extends ATask {

    public ReplaceBitTask(EDifficulty pDifficulty, Consumer<ATask> pRemovalTask) {
        super(pDifficulty, pRemovalTask);
    }

    @Override
    protected Collection<Node> generateViews() {
        // TODO: fancy up the appearance
        Button button = new Button("Replace Drill Bit");
        button.setOnAction(pEvt -> onCompleted());
        return Collections.singleton(button);
    }

    @Override
    protected Point2D generateViewLocation() {
        // TODO
        return new Point2D(Math.random() * 600, Math.random() * 600);
    }

}
