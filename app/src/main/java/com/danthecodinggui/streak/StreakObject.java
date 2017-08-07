package com.danthecodinggui.streak;

/**
 * Streak Object for holding information such as streak text, time kept etc.
 */
public class StreakObject {

    private int viewIndex;

    private boolean isPriority;

    private String streakText;
    private int streakDuration;

    /**
     * @param streakText The streak description
     */
    public StreakObject(String streakText, int streakDuration, int viewIndex) {
        this.streakText = streakText;
        this.streakDuration = streakDuration;
        this.viewIndex = viewIndex;
        this.isPriority = false;
    }

    @Override
    public boolean equals(Object otherObject) {
        if (!(otherObject instanceof StreakObject)){
            return false;
        }

        StreakObject otherStreak = (StreakObject)otherObject;

        return streakText.equals(otherStreak.getStreakText())
                && streakDuration == otherStreak.getStreakDuration()
                && isPriority == otherStreak.getStreakIsPriority()
                && viewIndex == otherStreak.getStreakViewIndex();

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

    public int getStreakViewIndex() {
        return viewIndex;
    }
    public void setStreakViewIndex(int newViewIndex) {
        viewIndex = newViewIndex;
    }

    public boolean getStreakIsPriority() {
        return isPriority;
    }
    public void setStreakIsPriority(boolean newPriority) {
        isPriority = newPriority;
    }
}
