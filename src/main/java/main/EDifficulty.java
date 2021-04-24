package main;

public enum EDifficulty {

    // TODO: adjust these vals
    EASY(1.0, 1.0, 3, 3),
    MEDIUM(1.0, 1.0, 3, 3),
    HARD(1.0, 1.0, 3, 3);

    private final double mTaskRateMultiplier;
    private final double mTaskTimeoutMultiplier;
    private final int mInitialTasks;
    private final int mLives;

    EDifficulty(double pTaskRateMultiplier, double pTaskTimeoutMultiplier, int pInitialTasks, int pLives) {
        mTaskRateMultiplier = pTaskRateMultiplier;
        mTaskTimeoutMultiplier = pTaskTimeoutMultiplier;
        mInitialTasks = pInitialTasks;
        mLives = pLives;
    }

    public double getTaskRateMultiplier() {
        return mTaskRateMultiplier;
    }

    public double getTaskTimeoutMultiplier() {
        return mTaskTimeoutMultiplier;
    }

    public int getInitialTasks() {
        return mInitialTasks;
    }

    public int getLives() {
        return mLives;
    }

}
