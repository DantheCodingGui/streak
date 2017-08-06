package com.danthecodinggui.streak;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.danthecodinggui.streak.Database.StreakDbHelper;

import static com.danthecodinggui.streak.R.id.editStreak;


public class EditStreak extends AppCompatActivity {

    private String streakText;
    private int streakDuration;

    private int streakViewId;

    private boolean streakIsPriority;

    private int function;

    static final int ADD_STREAK = 0;
    static final int EDIT_STREAK = 1;

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
                break;
        }
    }

    /**
     * Saves streak to file system
     * @param view Button view Pressed
     */
    public void AddStreak(View view) {

        if (function == EDIT_STREAK) {
            finish();
            Log.d("boogie", "has finished");
            return;
        }

        EditText text = (EditText)findViewById(editStreak);
        streakText = text.getText().toString();
        Log.d("boogie", "before database save");

        SaveToDatabase(new StreakObject(streakText, streakDuration, streakViewId));

        Intent output = new Intent();
        output.putExtra("newStreak", streakText);
        output.putExtra("newStreakDuration", streakDuration);
        //output.putExtra("newStreakIsPriority", streakIsPriority);
        setResult(HomeActivity.ADD_STREAK, output);

        finish();
    }

    private void SaveToDatabase(StreakObject newStreak) {

        StreakDbHelper sDbHelper = new StreakDbHelper(this);
        sDbHelper.AddStreak(newStreak);
    }
}
