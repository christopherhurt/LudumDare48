package tasks;

import java.util.Collection;
import java.util.Collections;
import java.util.function.Consumer;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.text.Text;
import main.ATask;
import main.EDifficulty;

public class RefuelTask extends ATask {

    public RefuelTask(EDifficulty pDifficulty, Consumer<ATask> pRemovalTask) {
        super(pDifficulty, pRemovalTask);
    }

    @Override
    protected Collection<Node> generateViews() {
        // TODO
        return Collections.singleton(new Text("Refuel!!"));
    }

    @Override
    protected Point2D generateViewLocation() {
        // TODO
        return new Point2D(Math.random() * 600, Math.random() * 600);
    }

}
