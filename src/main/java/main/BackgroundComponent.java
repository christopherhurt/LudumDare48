package main;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import com.almasb.fxgl.texture.Texture;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Point2D;
import javafx.util.Duration;

public class BackgroundComponent extends Component {

    private static final double DRILL_ANIMATION_SPEED = 0.75;
    private static final double BACKGROUND_SCROLL_SPEED = 100.0;
    private static final double MAX_SHAKE_RADIUS = 10.0;
    private static final double SHAKE_SPEED = 30.0;
    private static final double BACKGROUND_HEIGHT_TO_WIDTH = 2.0;

    private final ReadOnlyBooleanProperty mBackgroundMoving =
            FXGL.getWorldProperties().booleanProperty(Names.BACKGROUND_MOVING);

    private final String mTexturePath;
    private final ReadOnlyIntegerProperty mLifeCounter =
            FXGL.getWorldProperties().intProperty(Names.LIFE_COUNT);
    // Amount of time executed on this component, updated each frame
    private final DoubleProperty mLiveTime = new SimpleDoubleProperty(0.0);

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
        // Frame counter entity, created first for accuracy
        FXGL.entityBuilder().with(new Component() {
            @Override
            public void onUpdate(double pTpf) {
                mLiveTime.set(mLiveTime.get() + pTpf);
            }
        }).buildAndAttach();

        // Scrolling background image
        spawnBackgroundEntity(0.0);

        // Drill animation
        AnimationChannel animChannel = new AnimationChannel(FXGL.image("drill.png"), 4, 94, 376,
                Duration.seconds(DRILL_ANIMATION_SPEED), 0, 3);
        AnimatedTexture texture = new AnimatedTexture(animChannel).loop();
        texture.setFitWidth(Constants.DRILL_WIDTH);
        texture.setFitHeight(Constants.DRILL_HEIGHT);
        texture.setSmooth(false);
        FXGL.entityBuilder()
                .at(new Point2D(300.0 - Constants.DRILL_WIDTH / 2.0, 0.0))
                .view(texture)
                .with(createCameraShakeComponent(300.0 - Constants.DRILL_WIDTH / 2.0))
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
        final double adjustedWidth = 600.0 + MAX_SHAKE_RADIUS * 2.0;
        backgroundTexture.setFitWidth(adjustedWidth);
        backgroundTexture.setFitHeight(BACKGROUND_HEIGHT_TO_WIDTH * adjustedWidth);
        backgroundTexture.setSmooth(false);
        FXGL.entityBuilder()
                .at(new Point2D(-MAX_SHAKE_RADIUS, pY))
                .view(backgroundTexture)
                .with(createCameraShakeComponent(-MAX_SHAKE_RADIUS))
                .with(new Component() {
                    private boolean mSpawnedNew = false;
                    @Override
                    public void onUpdate(double tpf) {
                        if (mBackgroundMoving.get()) {
                            getEntity().translateY(-BACKGROUND_SCROLL_SPEED * tpf);

                            if (!mSpawnedNew && getEntity().getY() < -0.5 * adjustedWidth) {
                                // Apply slight y offset to account for slip ups
                                spawnBackgroundEntity(getEntity().getY() + adjustedWidth * BACKGROUND_HEIGHT_TO_WIDTH - 25.0);
                                mSpawnedNew = true;
                            }
                            if (getEntity().getY() < -BACKGROUND_HEIGHT_TO_WIDTH * adjustedWidth) {
                                getEntity().removeFromWorld();
                            }
                        }
                    }
                })
                .zIndex(-1) // Always furthest back
                .buildAndAttach();
    }

    // Used to produce camera shake effect for background and drill
    private Component createCameraShakeComponent(double pBaseX) {
        return new Component() {
            @Override
            public void onUpdate(double pTpf) {
                int clampedLives = Math.min(Math.max(mLifeCounter.get(), 0), Constants.LIVES_SHAKE_BEGIN);
                if (clampedLives < Constants.LIVES_SHAKE_BEGIN) {
                    double amplitude = MAX_SHAKE_RADIUS * (1.0 - (double)clampedLives / Constants.LIVES_SHAKE_BEGIN);
                    double shakeOffset = Math.sin(mLiveTime.get() * SHAKE_SPEED
                            * (Constants.LIVES_SHAKE_BEGIN - clampedLives)) * amplitude;
                    getEntity().setX(pBaseX + shakeOffset);
                } else {
                    getEntity().setX(pBaseX);
                }
            }
        };
    }

}
