package tasks;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.texture.Texture;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import main.ATask;
import main.EDifficulty;

public class RefuelTask extends ATask {

    private static final double BUCKET_WIDTH = 40;
    private static final int NUM_FILLS = 10;

    private int mFilled = 0;

    public RefuelTask(EDifficulty pDifficulty, Consumer<ATask> pRemovalTask) {
        super(pDifficulty, pRemovalTask);
    }

    @Override
    protected Collection<Node> generateViews() {
        Texture bucketSheet = FXGL.texture("fuel-bucket.png");
        int bucketSprites = (int)(bucketSheet.getWidth() / bucketSheet.getHeight());
        List<Texture> bucketTextures = IntStream.range(0, bucketSprites).mapToObj(pIndex ->
                bucketSheet.subTexture(new Rectangle2D(pIndex * bucketSheet.getHeight(), 0,
                        bucketSheet.getHeight(), bucketSheet.getHeight()))).peek(pTex -> {
                            pTex.setFitWidth(BUCKET_WIDTH);
                            pTex.setFitHeight(BUCKET_WIDTH);
                            pTex.setSmooth(false);
                        }).collect(Collectors.toList());

        VBox box = new VBox(5.0);

        Button button = new Button("Refuel\n(Click me!)");
        button.setTextAlignment(TextAlignment.CENTER);
        button.setStyle("-fx-background-color: linear-gradient(to top right, rgb(92, 92, 92) 0%, rgb(128, 128, 128) 100%); -fx-text-fill: white; -fx-font-weight: bold;");
        button.setOnAction(pEvt -> {
            // TODO: splash sound

            mFilled++;
            if (mFilled >= NUM_FILLS) {
                onCompleted();
            }

            // Set matching bucket texture
            box.getChildren().removeAll(bucketTextures);
            List<Texture> availableTex = bucketTextures.stream().filter(pTex -> (double)mFilled / NUM_FILLS >= (double)bucketTextures.indexOf(pTex)
                    / bucketTextures.size()).collect(Collectors.toList());
            if (!availableTex.isEmpty()) {
                box.getChildren().add(availableTex.get(availableTex.size() - 1));
            }
        });

        box.getChildren().addAll(button, bucketTextures.get(0));
        box.setAlignment(Pos.CENTER);
        return Collections.singleton(box);
    }

    @Override
    protected Point2D generateViewLocation() {
        return LocationUtils.genRandomLocation(BUCKET_WIDTH);
    }

    @Override
    protected double getBaseTimeout() {
        // TODO: adjust
        return 5.0;
    }

}
