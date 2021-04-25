package tasks;

import com.almasb.fxgl.audio.Sound;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.time.TimerAction;
import java.util.Collection;
import java.util.Collections;
import java.util.function.Consumer;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;
import main.ATask;
import main.EDifficulty;
import main.Names;

public class BlockageTask extends ATask {

    private static final Font FONT = Font.font("SansSerif", FontWeight.EXTRA_BOLD, FontPosture.ITALIC, 20.0);
    private static final String COLOR_GRADIENT =
            "-fx-fill: linear-gradient(to top right, rgb(190, 0, 0) 0%, red 100%);";

    private static final int NUM_MASHES = 15;

    private final EventHandler<KeyEvent> mHandler;
    private final TimerAction mSoundTimer;
    private Sound mSound = null;
    private MashKey mKey;

    private TimerAction mFlash;
    private int mMashCount = 0;
    private boolean mResumed = false;

    public BlockageTask(EDifficulty pDifficulty, Consumer<ATask> pRemovalTask) {
        super(pDifficulty, pRemovalTask);

        mHandler = pEvt -> {
            if (!isExpired() && mKey.getCode() == pEvt.getCode()) {
                mMashCount++;
                if (mMashCount >= NUM_MASHES) {
                    onCompleted();
                }
            }
        };
        FXGL.getInput().addEventHandler(KeyEvent.KEY_PRESSED, mHandler);

        FXGL.getWorldProperties().setValue(Names.BACKGROUND_MOVING, false);
        // Play sound once first
        mSound = FXGL.getAssetLoader().loadSound("blockage.wav");
        FXGL.getAudioPlayer().playSound(mSound);
        // Play sound later on timer
        mSoundTimer = FXGL.run(() -> {
            mSound = FXGL.getAssetLoader().loadSound("blockage.wav");
            FXGL.getAudioPlayer().playSound(mSound);
        }, Duration.seconds(1));
    }

    @Override
    protected Collection<Node> generateViews() {
        mKey = MashKey.getRandom();

        Text instructionsText = new Text("Blockage reached!");
        instructionsText.setFont(FONT);
        instructionsText.setStyle(COLOR_GRADIENT);
        Text unblockText = new Text("Mash " + mKey.getName() + " to unblock");
        unblockText.setFont(FONT);
        unblockText.setStyle(COLOR_GRADIENT);
        VBox box = new VBox(10.0, instructionsText, unblockText);
        box.setAlignment(Pos.CENTER);

        box.setMinWidth(100.0);
        box.setMaxWidth(100.0);
        box.setPrefWidth(100.0);

        mFlash = FXGL.run(() -> box.setVisible(!box.isVisible()), Duration.seconds(0.5));

        return Collections.singleton(box);
    }

    @Override
    protected double getBaseTimeout() {
        return 6.0;
    }

    @Override
    protected void onCompleted() {
        super.onCompleted();
        resumeDrillRotation();
    }

    @Override
    protected void onTimeout() {
        super.onTimeout();
        resumeDrillRotation();
    }

    private void resumeDrillRotation() {
        if (!mResumed) {
            mResumed = true;
            FXGL.getWorldProperties().setValue(Names.BACKGROUND_MOVING, true);
            mSoundTimer.expire();
            if (mSound != null) {
                FXGL.getAudioPlayer().stopSound(mSound);
                mSound = null;
            }
        }
    }

    @Override
    protected Point2D generateViewLocation() {
        return new Point2D(250.0, 400.0);
    }

    @Override
    public void destroyAndRemove() {
        super.destroyAndRemove();
        resumeDrillRotation();
        if (mFlash != null) {
            mFlash.expire();
        }
        FXGL.getInput().removeEventHandler(KeyEvent.KEY_PRESSED, mHandler);
    }

    private enum MashKey {

        Q("Q", KeyCode.Q),
        T("T", KeyCode.T),
        P("M", KeyCode.M),
        F("F", KeyCode.F),
        K("K", KeyCode.K),
        B("B", KeyCode.B);

        private final String mName;
        private final KeyCode mCode;

        MashKey(String pName, KeyCode pCode) {
            mName = pName;
            mCode = pCode;
        }

        public String getName() {
            return mName;
        }

        public KeyCode getCode() {
            return mCode;
        }

        static MashKey getRandom() {
            return values()[(int)(Math.random() * values().length)];
        }

    }

}
