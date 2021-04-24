package tasks;

import javafx.geometry.Point2D;
import main.Constants;

public final class LocationUtils {

    private static final double X_PAD = 25.0;
    private static final double Y_PAD = 125.0;

    public static Point2D genRandomLocation(double pWidth) {
        // Generate a random location that is padded along the edges that doesn't overlap
        // the drill or go offscreen
        double y = Math.random() * (600 - 2 * Y_PAD) + Y_PAD;

        double minScreenX = X_PAD;
        double maxScreenX = 600.0 - X_PAD - pWidth;
        double x;
        if (y > Constants.DRILL_HEIGHT) {
            x = Math.random() * (maxScreenX - minScreenX) + minScreenX;
        } else {
            // Pick x location that doesn't overlap the drill
            if (Math.random() < 0.5) {
                // Left of drill
                double maxDrillX = 300.0 - Constants.DRILL_WIDTH / 2.0 - X_PAD - pWidth;
                x = Math.random() * (maxDrillX - minScreenX) + minScreenX;
            } else {
                // Right of drill
                double minDrillX = 300.0 + Constants.DRILL_WIDTH / 2.0 + X_PAD;
                x = Math.random() * (maxScreenX - minDrillX) + minDrillX;
            }
        }
        return new Point2D(x, y);
    }

    private LocationUtils() {
    }

}
