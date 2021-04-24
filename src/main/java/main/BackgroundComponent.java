package main;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import com.almasb.fxgl.texture.Texture;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.geometry.Point2D;
import javafx.util.Duration;

public class BackgroundComponent extends Component {

    private static final double DRILL_ANIMATION_SPEED = 0.4;
    private static final double BACKGROUND_SCROLL_SPEED = 100.0;

    private final ReadOnlyBooleanProperty mBackgroundMoving =
            FXGL.getWorldProperties().booleanProperty(Names.BACKGROUND_MOVING);

    private final String mTexturePath;

    public BackgroundComponent(int pLevelIndex) {
        if (pLevelIndex == 0) {
            mTexturePath = "background-crust.png";
        } else if (pLevelIndex == 1) {
            mTexturePath = "background-mantle.png";
        } else if (pLevelIndex == 2) {
            mTexturePath = "background-outer-core.png";
        } else {
            mTexturePath = "background-inner-core.png";
        }
    }

    @Override
    public void onAdded() {
        // Scrolling background image
        spawnBackgroundEntity(0.0);

        // Drill animation
        AnimationChannel animChannel = new AnimationChannel(FXGL.image("drill.png"), 4, 10, 40,
                Duration.seconds(DRILL_ANIMATION_SPEED), 0, 3);
        AnimatedTexture texture = new AnimatedTexture(animChannel).loop();
        texture.setFitWidth(Constants.DRILL_WIDTH);
        texture.setFitHeight(Constants.DRILL_HEIGHT);
        texture.setSmooth(false);
        FXGL.entityBuilder()
                .at(new Point2D(300.0 - Constants.DRILL_WIDTH / 2.0, 0.0))
                .view(texture)
                .with(new Component() {
                    @Override
                    public void onUpdate(double tpf) {
                        // Ensure the current frame's texture is fitted properly
                        texture.setFitWidth(Constants.DRILL_WIDTH);
                        texture.setFitHeight(Constants.DRILL_HEIGHT);
                        texture.setSmooth(false);
                    }
                }).buildAndAttach();

        // Stop/start the drill animation with the background
        mBackgroundMoving.addListener((pObs, pOld, pNew) -> {
            if (pNew) {
                texture.loop();
            } else {
                texture.stop();
            }
        });
    }

    private void spawnBackgroundEntity(double pY) {
        Texture backgroundTexture = FXGL.texture(mTexturePath);
        backgroundTexture.setFitWidth(600.0);
        backgroundTexture.setFitHeight(4 * 600.0);
        backgroundTexture.setSmooth(false);
        FXGL.entityBuilder()
                .at(new Point2D(0.0, pY))
                .view(backgroundTexture)
                .with(new Component() {
                    private boolean mSpawnedNew = false;
                    @Override
                    public void onUpdate(double tpf) {
                        if (mBackgroundMoving.get()) {
                            getEntity().translateY(-BACKGROUND_SCROLL_SPEED * tpf);

                            if (!mSpawnedNew && getEntity().getY() < -2.5 * 600.0) {
                                // Apply slight y offset to account for slip ups
                                spawnBackgroundEntity(getEntity().getY() + 600.0 * 4.0 - 25.0);
                                mSpawnedNew = true;
                            }
                            if (getEntity().getY() < -4.0 * 600.0) {
                                getEntity().removeFromWorld();
                            }
                        }
                    }
                })
                .zIndex(-1) // Always furthest back
                .buildAndAttach();
    }

}
