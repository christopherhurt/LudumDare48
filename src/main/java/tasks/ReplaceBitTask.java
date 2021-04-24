package tasks;

import com.almasb.fxgl.animation.Animation;
import com.almasb.fxgl.animation.Interpolators;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.texture.Texture;
import com.almasb.fxgl.time.TimerAction;
import java.util.Collection;
import java.util.Collections;
import java.util.function.Consumer;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;
import main.ATask;
import main.Constants;
import main.EDifficulty;

public class ReplaceBitTask extends ATask {

    private static final String YELLOW_GRADIENT = "linear-gradient(to top right, derive(yellow, -15%) 0%, yellow 100%)";
    private static final String RED_GRADIENT = "linear-gradient(to top right, derive(rgb(255, 80, 80), -15%) 0%, rgb(255, 80, 80) 100%)";
    private static final String YELLOW = "-fx-text-fill: " + YELLOW_GRADIENT + "; -fx-border-color: " + YELLOW_GRADIENT + ";";
    private static final String RED = "-fx-text-fill: " + RED_GRADIENT + "; -fx-border-color: " + RED_GRADIENT + ";";
    private static final double FLICKER_INTERVAL = 0.5;
    private static final double WIDTH = 125;

    private boolean mIsYellow;
    private TimerAction mFlickerTimer;

    public ReplaceBitTask(EDifficulty pDifficulty, Consumer<ATask> pRemovalTask) {
        super(pDifficulty, pRemovalTask);
    }

    @Override
    protected Collection<Node> generateViews() {
        Button button = new Button("Replace Drill Bit\n(Click me!)");
        button.getStyleClass().add("replace-bit-btn");
        button.setTextAlignment(TextAlignment.CENTER);
        button.setOnAction(pEvt -> onCompleted());
        mFlickerTimer = FXGL.run(() -> flickerColor(button), Duration.seconds(FLICKER_INTERVAL));

        button.setMinWidth(WIDTH);
        button.setMaxWidth(WIDTH);
        button.setPrefWidth(WIDTH);

        flickerColor(button);
        return Collections.singleton(button);
    }

    private void flickerColor(Button pBtn) {
        pBtn.setStyle(mIsYellow ? RED : YELLOW);
        mIsYellow = !mIsYellow;
    }

    @Override
    protected Point2D generateViewLocation() {
        return LocationUtils.genRandomLocation(WIDTH);
    }

    @Override
    protected double getBaseTimeout() {
        // TODO: adjust
        return 3.0;
    }

    @Override
    protected void onCompleted() {
        super.onCompleted();

        // Drill bit attachment animation
        Texture texture = FXGL.getAssetLoader().loadTexture("drill-bit.png");
        texture.setFitWidth(Constants.DRILL_WIDTH);
        texture.setFitHeight(Constants.DRILL_WIDTH);
        texture.setSmooth(false);

        Animation<?> animation = FXGL.animationBuilder()
                .interpolator(Interpolators.QUADRATIC.EASE_IN_OUT())
                .duration(Duration.seconds(0.6))
                .translate(texture)
                .from(new Point2D(600.0, -Constants.DRILL_WIDTH))
                .to(new Point2D(300.0 - Constants.DRILL_WIDTH / 2.0, Constants.DRILL_HEIGHT - Constants.DRILL_WIDTH))
                .build();
        FXGL.entityBuilder()
                .view(texture)
                .with(new Component() {
                    @Override
                    public void onAdded() {
                        animation.start();
                        animation.setOnFinished(getEntity()::removeFromWorld);
                    }
                    @Override
                    public void onUpdate(double pTpf) {
                        animation.onUpdate(pTpf);
                    }
                })
                .buildAndAttach();

        // TODO: play drill sound
    }

    @Override
    public void destroyAndRemove() {
        super.destroyAndRemove();
        if (mFlickerTimer != null) {
            mFlickerTimer.expire();
        }
    }

}
