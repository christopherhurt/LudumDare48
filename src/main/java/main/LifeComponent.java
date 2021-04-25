package main;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.texture.Texture;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.geometry.Point2D;
import levels.GameOverMenu;

public class LifeComponent extends Component {

    private static final double MIN_DAMAGE_OPACITY = 0.4;

    private final ReadOnlyIntegerProperty mCounter = FXGL.getWorldProperties().intProperty(Names.LIFE_COUNT);

    @Override
    public void onAdded() {
        // Damage overlay
        Texture texture = FXGL.getAssetLoader().loadTexture("damage-overlay.png");
        texture.setFitWidth(600.0);
        texture.setFitHeight(600.0);
        texture.setSmooth(false);
        texture.setOpacity(0.0);
        FXGL.entityBuilder()
                .at(Point2D.ZERO)
                .view(texture)
                .zIndex(1) // Always render above other entities
                .buildAndAttach();

        // Update damage overlay opacity with life counter
        mCounter.addListener((pObs, pOld, pNew) -> {
            int clampedLife = Math.max(Math.min(pNew.intValue(), Constants.MAX_LIVES), 0);
            int clampedOld = Math.max(Math.min(pOld.intValue(), Constants.MAX_LIVES), 0);
            if (clampedLife < clampedOld && clampedLife > 0) {
                FXGL.play("damage.wav");
            }

            double opacity;
            if (clampedLife >= Constants.MAX_LIVES) {
                opacity = 0.0;
            } else {
                opacity = (1.0 - (double)clampedLife / Constants.MAX_LIVES)
                        * (1.0 - MIN_DAMAGE_OPACITY) + MIN_DAMAGE_OPACITY;
            }
            texture.setOpacity(opacity);
            if (clampedLife <= 0) {
                gameOver();
            }
        });
    }

    private static void gameOver() {
        new GameOverMenu().load(true, 0);
    }

}
