package com.danthecodinggui.streak.Database;

import android.provider.BaseColumns;

/**
 * Defines how data is laid out in database
 */

public final class StreakContract {

    private StreakContract() {}

    public static final class StreakTable implements BaseColumns {

        public static final String TABLE_NAME = "streaks";

        public static final String STREAK_DESCRIPTION = "description";
        public static final String STREAK_DURATION = "duration";

    }
}
