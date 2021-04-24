package main;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.scene.text.Text;

// TODO: update view "redness", camera shake, game over procedure
public class LifeComponent extends Component {

    private final ReadOnlyIntegerProperty mCounter = FXGL.getWorldProperties().intProperty(Names.LIFE_COUNT);

    @Override
    public void onAdded() {
        // TODO: update
        Text counter = new Text();
        counter.textProperty().bind(Bindings.createStringBinding(() -> "Lives: " + mCounter.get(), mCounter));
        FXGL.addUINode(counter, 30.0, 30.0);
    }

}
