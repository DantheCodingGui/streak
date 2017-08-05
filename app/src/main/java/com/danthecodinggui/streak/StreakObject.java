package com.danthecodinggui.streak;

/**
 * Streak Object for holding information such as streak text, time kept etc.
 */
public class StreakObject {
    private String streakText;
    private int streakDuration;

    /**
     * @param streakText The streak description
     */
    public StreakObject(String streakText) {
        this.streakText = streakText;
        this.streakDuration = 1;
    }

    public String getStreakText() {
        return streakText;
    }

    public void setStreakText(String text) {
        streakText = text;
    }

    public int getStreakDuration() {
        return streakDuration;
    }

    public void incrementStreakDuration() {
        ++streakDuration;
    }
}
