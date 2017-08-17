package com.danthecodinggui.streak.Data.Database;

import android.provider.BaseColumns;

/**
 * Defines how data is laid out in database
 */

final class StreakContract {

    private StreakContract() {}

    /**
     * Defines how data is laid out for the streak table
     */
    static final class StreakTable implements BaseColumns {

        static final String TABLE_NAME = "streaks";

        static final String STREAK_DESCRIPTION = "description";
        static final String STREAK_DURATION = "duration";
        static final String STREAK_IS_PRIORITY = "priority";
        static final String STREAK_VIEW_INDEX = "view_index";
    }
}
