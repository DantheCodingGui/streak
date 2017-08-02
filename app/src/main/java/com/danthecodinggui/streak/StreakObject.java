package com.danthecodinggui.streak;

/**
 * Streak Object for holding streak information such as streak text and time kept
 */

public class StreakObject {
    private String streakText;

    public StreakObject(String streakText) {
        this.streakText = streakText;
    }

    public String getStreakText() {
        return streakText;
    }

    public void setStreakText(String text) {
        streakText = text;
    }
}
