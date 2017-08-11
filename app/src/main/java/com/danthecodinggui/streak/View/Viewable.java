package com.danthecodinggui.streak.View;

import android.content.Context;

/**
 * Created by Dan on 10/08/2017.
 */

public interface Viewable {
    Context getActivityContext();
    String getStringResource(int resource);
}
