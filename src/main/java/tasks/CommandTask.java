package tasks;

import com.almasb.fxgl.dsl.FXGL;
import java.util.Collection;
import java.util.Collections;
import java.util.function.Consumer;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import main.ATask;
import main.EDifficulty;

public class CommandTask extends ATask {

    private static final String ERROR_STYLE = "-fx-text-fill: red";
    private static final double WIDTH = 155.0;
    private static final Font INSTRUCTION_FONT = Font.font(Font.getDefault().getFamily(), FontWeight.EXTRA_BOLD, 14.0);

    private static final String[] COMMANDS = new String[] {
            "overdrive.sh", "sudo rm -rf /", "stop_slacking", "lubricate_gears.sh",
                "encourage_drill.sh", "push_it -f"
    };

    private String mCommand;

    public CommandTask(EDifficulty pDifficulty, Consumer<ATask> pRemovalTask) {
        super(pDifficulty, pRemovalTask);
    }

    @Override
    protected Collection<Node> generateViews() {
        mCommand = COMMANDS[(int)(Math.random() * COMMANDS.length)];

        Text text1 = new Text("Execute command");
        text1.setFont(INSTRUCTION_FONT);
        text1.setPickOnBounds(false);
        text1.setMouseTransparent(true);
        Text text2 = new Text("\"" + mCommand + "\"");
        text2.setFont(INSTRUCTION_FONT);
        text2.setPickOnBounds(false);
        text2.setMouseTransparent(true);
        TextField tf = new TextField();
        tf.setFocusTraversable(false);
        tf.getStyleClass().add("command-box");
        tf.setPromptText("Hit Enter to submit");
        tf.addEventFilter(KeyEvent.KEY_PRESSED, pEvt -> {
            if (pEvt.getCode() == KeyCode.ENTER) {
                submit(tf);
            }
        });
        tf.textProperty().addListener(pObs -> tf.setStyle(""));
        EventHandler<MouseEvent> mouseHandler = pEvt -> {
            if (pEvt.getSource() != tf) {
                tf.getParent().requestFocus();
            }
        };
        tf.sceneProperty().addListener((pObs, pOld, pNew) -> {
            if (pOld != null) {
                pOld.removeEventHandler(MouseEvent.MOUSE_CLICKED, mouseHandler);
            }
            if (pNew != null) {
                pNew.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseHandler);
            }
        });

        VBox box = new VBox(5.0, text1, text2, tf);
        box.setAlignment(Pos.CENTER);

        box.setMinWidth(WIDTH);
        box.setMaxWidth(WIDTH);
        box.setPrefWidth(WIDTH);

        return Collections.singleton(box);
    }

    @Override
    protected double getBaseTimeout() {
        return 10.0;
    }

    private void submit(TextField pTf) {
        if (mCommand.equalsIgnoreCase(pTf.getText().trim())) {
            onCompleted();
        } else {
            pTf.setStyle(ERROR_STYLE);
        }
    }

    @Override
    protected void onCompleted() {
        super.onCompleted();
        FXGL.play("beepboop.wav");
    }

    @Override
    protected Point2D generateViewLocation() {
        return LocationUtils.genRandomLocation(WIDTH);
    }

}
