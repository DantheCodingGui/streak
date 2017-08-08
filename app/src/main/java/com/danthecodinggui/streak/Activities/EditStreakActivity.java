package com.danthecodinggui.streak.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.danthecodinggui.streak.Database.StreakDbHelper;
import com.danthecodinggui.streak.R;

import static com.danthecodinggui.streak.R.id.editStreak;


public class EditStreakActivity extends AppCompatActivity {

    private String streakText;
    private int streakDuration;

    private int streakViewId;

    private boolean streakIsPriority;

    private int function;

    public static final int ADD_STREAK = 0;
    public static final int EDIT_STREAK = 1;

    private StreakObject oldStreak;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_streak);

        Intent intent = getIntent();
        function = intent.getIntExtra("function", ADD_STREAK);

        switch(function) {
            case (ADD_STREAK):
                streakDuration = 1;
                streakViewId = getIntent().getIntExtra("listSize", 0);
                break;
            case (EDIT_STREAK):
                streakText = getIntent().getStringExtra("streakText");
                EditText text = (EditText)findViewById(editStreak);
                text.setText(streakText);

                streakViewId = getIntent().getIntExtra("viewId", -1);

                oldStreak = new StreakObject(streakText, streakDuration, streakViewId);
                break;
            default:
                Log.d("Error", "Invalid intent to EditStreakActivity");
                break;
        }
    }

    /**
     * Saves streak to file system
     * @param view Button view Pressed
     */
    public void SubmitStreak(View view) {

        EditText text = (EditText)findViewById(editStreak);
        streakText = text.getText().toString();

        StreakObject ob = new StreakObject(streakText, streakDuration, streakViewId);

        Intent output = new Intent();

        switch(function) {
            case (ADD_STREAK):
                long primaryKey = SaveToDatabase(ob);

                output.putExtra("newStreakId", primaryKey);
                output.putExtra("newStreak", streakText);
                output.putExtra("newStreakDuration", streakDuration);

                setResult(HomeActivity.ADD_STREAK, output);
                break;
            case (EDIT_STREAK):
                if (oldStreak.equals(ob)) {
                    output.putExtra("hasStreakChanged", false);
                    setResult(HomeActivity.EDIT_STREAK, output);
                    Log.d("boogie", "value sent was false");
                    break;
                }

                StreakDbHelper sDbHelper = new StreakDbHelper(this);
                sDbHelper.UpdateStreakValues(new StreakObject(streakText, streakDuration, streakViewId), HomeActivity.UPDATE_TEXT);

                output.putExtra("editedStreak", streakText);
                output.putExtra("editedStreakPosition", streakViewId);
                output.putExtra("hasStreakChanged", true);

                setResult(HomeActivity.EDIT_STREAK, output);
                break;
            default:
                Log.d("Error", "Invalid call to SubmitStreak");
                break;
        }

        //output.putExtra("newStreakIsPriority", streakIsPriority);

        finish();
    }

    private long SaveToDatabase(StreakObject newStreak) {

        StreakDbHelper sDbHelper = new StreakDbHelper(this);
        return sDbHelper.AddStreak(newStreak);
    }
}
