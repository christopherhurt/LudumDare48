package main;

public enum EDifficulty {

    // TODO: adjust these vals
    EASY(1.0, 1.0, 6),
    MEDIUM(1.0, 1.0, 6),
    HARD(1.0, 1.0, 6);

    private final double mTaskRateMultiplier;
    private final double mTaskTimeoutMultiplier;
    private final int mInitialTasks;

    EDifficulty(double pTaskRateMultiplier, double pTaskTimeoutMultiplier, int pInitialTasks) {
        mTaskRateMultiplier = pTaskRateMultiplier;
        mTaskTimeoutMultiplier = pTaskTimeoutMultiplier;
        mInitialTasks = pInitialTasks;
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

}
