package com.danthecodinggui.streak;

/**
 * Streak Object for holding information such as streak text, time kept etc.
 */
class StreakObject {
    private String streakText;

    /**
     * @param streakText The streak description
     */
    StreakObject(String streakText) {
        this.streakText = streakText;
    }

    String getStreakText() {
        return streakText;
    }

    public void setStreakText(String text) {
        streakText = text;
    }
}
