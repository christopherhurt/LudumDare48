package tasks;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import java.util.Collection;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;
import main.ATask;
import main.Constants;
import main.EDifficulty;

public class FlamesTask extends ATask {

    private static final double Y_PAD = 100.0;
    private static final double SIZE = 20.0;
    private static final int COUNT = 5;
    private static final double ANIM_TIME = 0.3;

    private int mCount = COUNT;
    private Entity mEntity;

    public FlamesTask(EDifficulty pDifficulty, Consumer<ATask> pRemovalTask) {
        super(pDifficulty, pRemovalTask);
    }

    @Override
    protected Collection<Node> generateViews() {
        return IntStream.range(0, COUNT).mapToObj(pI -> generateView()).collect(Collectors.toList());
    }

    // Get a single flame
    private Node generateView() {
        Text text = new Text("Click to\nextinguish!");
        text.setFont(Font.font(Font.getDefault().getFamily(), FontWeight.BOLD, Font.getDefault().getSize()));
        text.setTextAlignment(TextAlignment.CENTER);
        text.setPickOnBounds(false);
        text.setMouseTransparent(true);

        VBox box = new VBox(5.0);

        AnimationChannel animChannel = new AnimationChannel(FXGL.image("flames.png"), 4, 16, 16, // TODO: change these with tex size increase
                Duration.seconds(ANIM_TIME), 0, 3);
        AnimatedTexture texture = new AnimatedTexture(animChannel).loop();
        texture.setFitWidth(SIZE);
        texture.setFitHeight(SIZE);
        texture.setSmooth(false);
        texture.setPickOnBounds(true);
        texture.setOnMousePressed(pEvt -> {
            // TODO: sizzle sound effect
            FXGL.removeUINode(box);
            mCount--;
            if (mCount <= 0) {
                onCompleted();
            }
            pEvt.consume();
        });

        // Wrap in entity so animation plays
        mEntity = FXGL.entityBuilder().view(texture).buildAndAttach();

        box.getChildren().addAll(text, texture);
        box.setAlignment(Pos.CENTER);
        box.setPickOnBounds(false);

        // Fix width for accurate location generation
        box.setMinWidth(SIZE * 2.0);
        box.setMaxWidth(SIZE * 2.0);
        box.setPrefWidth(SIZE * 2.0);

        return box;
    }

    @Override
    protected Point2D generateViewLocation() {
        // Generate a location somewhere on the drill
        double minX = 300.0 - Constants.DRILL_WIDTH / 2.0 - SIZE * 0.5; // Account for VBox width
        double maxX = 300.0 + Constants.DRILL_WIDTH / 2.0 - SIZE * 1.5; // Account for VBox width
        double maxY = Constants.DRILL_HEIGHT - Y_PAD;
        return new Point2D(FXGL.random(minX, maxX), FXGL.random(Y_PAD / 2.0, maxY));
    }

    @Override
    protected double getBaseTimeout() {
        // TODO: adjust
        return 5.0;
    }

    @Override
    public void destroyAndRemove() {
        super.destroyAndRemove();
        if (mEntity != null) {
            mEntity.removeFromWorld();
        }
    }

}
