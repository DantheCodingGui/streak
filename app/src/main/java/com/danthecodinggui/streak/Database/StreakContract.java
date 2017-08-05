package com.danthecodinggui.streak.Database;

import android.provider.BaseColumns;

/**
 * Defines how data is laid out in database
 */

final class StreakContract {

    private StreakContract() {}

    static final class StreakTable implements BaseColumns {

        static final String TABLE_NAME = "streaks";

        static final String STREAK_DESCRIPTION = "description";
        static final String STREAK_DURATION = "duration";

    }
}
