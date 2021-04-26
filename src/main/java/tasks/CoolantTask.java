package tasks;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.texture.Texture;
import java.util.Collection;
import java.util.Collections;
import java.util.function.Consumer;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import main.ATask;
import main.EDifficulty;

public class CoolantTask extends ATask {

    private static final Font FONT = Font.font(Font.getDefault().getFamily(), FontWeight.SEMI_BOLD, 13.0);
    private static final double WIDTH = 50.0;
    private static final double TOTAL_ROTATION = 4 * 2 * Math.PI;

    private Point2D mLastLoc = Point2D.ZERO;
    private double mRotation = 0.0;

    public CoolantTask(EDifficulty pDifficulty, Consumer<ATask> pRemovalTask) {
        super(pDifficulty, pRemovalTask);
    }

    @Override
    protected Collection<Node> generateViews() {
        Text coolantText = new Text("Apply Coolant");
        coolantText.setPickOnBounds(false);
        coolantText.setMouseTransparent(true);
        coolantText.setFont(FONT);
        Text instructionsText = new Text("(Click and rotate CW)");
        instructionsText.setFont(FONT);
        Texture wheel = FXGL.getAssetLoader().loadTexture("coolant-wheel.png");
        wheel.setFitWidth(WIDTH);
        wheel.setFitHeight(WIDTH);
        wheel.setPickOnBounds(true);
        wheel.setSmooth(false);
        wheel.setOnMousePressed(this::updateMouseLoc);
        wheel.setOnMouseDragged(pEvt -> {
            // Wheel center to old mouse loc vector
            double oldX = mLastLoc.getX() - WIDTH / 2.0;
            double oldY = mLastLoc.getY() - WIDTH / 2.0;

            // Wheel center to new mouse loc vector
            double newX = pEvt.getX() - WIDTH / 2.0;
            double newY = pEvt.getY() - WIDTH / 2.0;

            // Check the rotation was clockwise
            double determinant = oldX * newY - oldY * newX;
            if (determinant > 0.0) {
                // Angular distance between new mouse vector and old mouse vector
                mRotation += Math.acos((oldX * newX + oldY * newY)
                        / (Math.sqrt(oldX * oldX + oldY * oldY) * Math.sqrt(newX * newX + newY * newY)));

                wheel.setRotate(Math.toDegrees(mRotation));
                if (Math.abs(mRotation) >= TOTAL_ROTATION) {
                    onCompleted();
                }
            }
            updateMouseLoc(pEvt);
        });

        VBox box = new VBox(5.0, coolantText, instructionsText, wheel);
        box.setAlignment(Pos.CENTER);
        return Collections.singleton(box);
    }

    private void updateMouseLoc(MouseEvent pEvt) {
        mLastLoc = new Point2D(pEvt.getX(), pEvt.getY());
    }

    @Override
    protected Point2D generateViewLocation() {
        return LocationUtils.genRandomLocation(WIDTH * 2.0); // Adjust for text components
    }

    @Override
    protected double getBaseTimeout() {
        return 5.0;
    }

    @Override
    protected void onCompleted() {
        super.onCompleted();
        FXGL.play("sizzle.wav");
    }

}
