package com.danthecodinggui.streak.Data;

/**
 * Streak Object for holding information such as streak text, time kept etc.
 */
public class StreakObject {

    private long id;

    private String streakText;
    private int streakDuration;
    private boolean isPriority;

    public StreakObject(String streakText, int streakDuration) {
        this.streakText = streakText;
        this.streakDuration = streakDuration;
        this.isPriority = false;
        id = 0;
    }

    @Override
    public boolean equals(Object otherObject) {
        if (!(otherObject instanceof StreakObject)){
            return false;
        }

        StreakObject otherStreak = (StreakObject)otherObject;

        return streakText.equals(otherStreak.getStreakText())
                && streakDuration == otherStreak.getStreakDuration()
                && isPriority == otherStreak.getStreakIsPriority();

    }

    public long getStreakId() {
        return id;
    }
    public void setStreakId(long newId) {
        id = newId;
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


    public boolean getStreakIsPriority() {
        return isPriority;
    }
    public void setStreakIsPriority(boolean newPriority) {
        isPriority = newPriority;
    }
}
