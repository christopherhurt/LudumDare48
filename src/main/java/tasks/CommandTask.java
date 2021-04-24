package tasks;

import java.util.Collection;
import java.util.Collections;
import java.util.function.Consumer;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import main.ATask;
import main.EDifficulty;

public class CommandTask extends ATask {

    public CommandTask(EDifficulty pDifficulty, Consumer<ATask> pRemovalTask) {
        super(pDifficulty, pRemovalTask);
    }

    @Override
    protected Collection<Node> generateViews() {
        // TODO: better appearance, random commands
        Text text = new Text("Enter command \"Temp\"");
        TextField tf = new TextField();
        tf.setPromptText("Hit \"Enter\" to submit");
        tf.addEventFilter(KeyEvent.KEY_PRESSED, pEvt -> {
            if (pEvt.getCode() == KeyCode.ENTER) {
                submit(tf.getText());
            }
        });
        VBox box = new VBox(text, tf);
        return Collections.singleton(box);
    }

    private void submit(String pText) {
        // TODO: better input validation
        if ("Temp".equals(pText)) {
            onCompleted();
        }
    }

    @Override
    protected Point2D generateViewLocation() {
        // TODO
        return new Point2D(Math.random() * 600, Math.random() * 600);
    }

}
