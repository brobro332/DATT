package xyz.datt.domain.gamification.policy;

public final class LevelPolicy {
    private static final int EXP_PER_LEVEL = 100;

    private LevelPolicy() {
    }

    public static int calculateLevel(int exp) {
        if (exp < 0) {
            return 1;
        }

        return (exp / EXP_PER_LEVEL) + 1;
    }

    public static int getRequiredExpForNextLevel(int level) {
        if (level <= 0) {
            return EXP_PER_LEVEL;
        }

        return level * EXP_PER_LEVEL;
    }
}