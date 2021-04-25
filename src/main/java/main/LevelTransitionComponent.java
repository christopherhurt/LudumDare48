package main;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;
import levels.GameplayLevel;
import levels.VictoryMenu;

public class LevelTransitionComponent extends Component {

    private static final double SURVIVAL_TIME = 70.0;
    private static final double TEXT_VISIBLE_TIME = 3.5;
    private static final double TEXT_TRANSITION_TIME = 1.0;
    private static final double TEXT_Y = 150.0;
    private static final double LEVEL_TRANSITION_TIME = 2.5;

    private final EDifficulty mDifficulty;
    private final Rectangle mTransitionOverlay = new Rectangle(0, 0, 600, 600);

    private Label mIntroLabel = null;
    private double mSurvivedTime = 0.0;
    private boolean mTransitioning = false;
    private double mTransitionCounter = 0.0;

    public LevelTransitionComponent(EDifficulty pDifficulty) {
        mDifficulty = pDifficulty;

        mTransitionOverlay.setFill(new Color(1.0, 1.0, 1.0, 1.0));
        mTransitionOverlay.setOpacity(0.0);
    }

    @Override
    public void onAdded() {
        // Level indicator label
        String textStr;
        int level = FXGL.getWorldProperties().getInt(Names.LEVEL_INDEX);
        if (level == 0) {
            textStr = "CRUST";
        } else if (level == 1) {
            textStr = "MANTLE";
        } else if (level == 2) {
            textStr = "OUTER CORE";
        } else {
            textStr = "INNER CORE";
        }

        mIntroLabel = new Label(textStr);
        mIntroLabel.setTextAlignment(TextAlignment.CENTER);
        mIntroLabel.setAlignment(Pos.CENTER);
        double fixedWidth = 600.0;
        mIntroLabel.setMinWidth(fixedWidth);
        mIntroLabel.setMaxWidth(fixedWidth);
        mIntroLabel.setPrefWidth(fixedWidth);
        mIntroLabel.setPickOnBounds(false);
        mIntroLabel.setMouseTransparent(true);
        mIntroLabel.setFont(Font.font("Helvetica", FontWeight.EXTRA_BOLD, FontPosture.ITALIC, 70.0));
        mIntroLabel.setStyle("-fx-text-fill: linear-gradient(to top right, rgb(128, 0, 0) 0%, rgb(255, 48, 48) 100%);");
        FXGL.addUINode(mIntroLabel, 300.0 - fixedWidth / 2.0, TEXT_Y);

        FXGL.runOnce(() -> {
            if (mIntroLabel != null) {
                FXGL.removeUINode(mIntroLabel);
                mIntroLabel = null;
            }
        }, Duration.seconds(TEXT_VISIBLE_TIME));
    }

    @Override
    public void onUpdate(double pTpf) {
        if (!mTransitioning) {
            mSurvivedTime += pTpf;

            // Update the intro label (if it's still around)
            if (mIntroLabel != null) {
                double opacity;
                if (mSurvivedTime < TEXT_TRANSITION_TIME) {
                    opacity = mSurvivedTime / TEXT_TRANSITION_TIME;
                } else if (mSurvivedTime < TEXT_VISIBLE_TIME - TEXT_TRANSITION_TIME) {
                    opacity = 1.0;
                } else {
                    opacity = 1.0 - (mSurvivedTime - (TEXT_VISIBLE_TIME - TEXT_TRANSITION_TIME)) / TEXT_TRANSITION_TIME;
                    opacity = Math.min(opacity, 1.0);
                }
                mIntroLabel.setOpacity(opacity);
            }

            // Check if we've survived the level
            if (mSurvivedTime >= SURVIVAL_TIME) {
                int nextLevelIndex = FXGL.getWorldProperties().getInt(Names.LEVEL_INDEX) + 1;
                if (nextLevelIndex >= Constants.NUM_LEVELS) {
                    victory();
                } else {
                    nextLevel(nextLevelIndex);
                }
            }
        } else {
            mTransitionCounter = Math.min(mTransitionCounter + pTpf, LEVEL_TRANSITION_TIME);
            double opacity = mTransitionCounter / LEVEL_TRANSITION_TIME;
            mTransitionOverlay.setOpacity(opacity);
        }
    }

    private static void victory() {
        new VictoryMenu().load(true, 0);
    }

    private void nextLevel(int pNextLevelIndex) {
        // Clear screen of UI nodes and begin transition period
        mTransitioning = true;
        FXGL.getGameScene().clearUINodes();
        FXGL.addUINode(mTransitionOverlay);

        // Cleanup task manager to prevent additional timeouts
        FXGL.getGameScene().getGameWorld().getEntitiesByComponent(TaskManagerComponent.class)
                .forEach(pEntity -> pEntity.getComponent(TaskManagerComponent.class).cleanup());

        // Start next level after transition period
        FXGL.runOnce(() -> new GameplayLevel(mDifficulty, false).load(false, pNextLevelIndex),
                Duration.seconds(LEVEL_TRANSITION_TIME));
    }

}
