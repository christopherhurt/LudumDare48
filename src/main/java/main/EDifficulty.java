package main;

public enum EDifficulty {

    EASY(1.2, 1.1, 3),
    MEDIUM(1.0, 0.95, 4),
    HARD(0.8, 0.85, 5);

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
