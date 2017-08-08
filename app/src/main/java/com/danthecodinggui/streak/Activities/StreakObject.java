package com.danthecodinggui.streak.Activities;

/**
 * Streak Object for holding information such as streak text, time kept etc.
 */
public class StreakObject {

    private long id;

    private String streakText;
    private int streakDuration;
    private boolean isPriority;
    private int viewIndex;
    private int previousViewIndex;

    /**
     * Data model represented in RecyclerView
     * @param streakText The streak description
     */
    public StreakObject(String streakText, int streakDuration, int viewIndex) {
        this.streakText = streakText;
        this.streakDuration = streakDuration;
        this.isPriority = false;
        this.viewIndex = previousViewIndex = viewIndex;
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
                && isPriority == otherStreak.getStreakIsPriority()
                && viewIndex == otherStreak.getStreakViewIndex();

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

    public int getStreakViewIndex() {
        return viewIndex;
    }
    public void setStreakViewIndex(int newViewIndex) {
        viewIndex = newViewIndex;
    }

    public int getPreviousStreakViewIndex() {
        return previousViewIndex;
    }
    public void setPreviousStreakViewIndex(int newViewIndex) {
        previousViewIndex = newViewIndex;
    }

    public boolean getStreakIsPriority() {
        return isPriority;
    }
    public void setStreakIsPriority(boolean newPriority) {
        isPriority = newPriority;
    }
}
