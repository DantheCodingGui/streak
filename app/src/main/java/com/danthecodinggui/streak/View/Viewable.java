package com.danthecodinggui.streak.View;

import android.content.Context;

/**
 * Allows presenter to access view through an interface
 */
public interface Viewable {
    Context getActivityContext();
    String getStringResource(int resource);
}
