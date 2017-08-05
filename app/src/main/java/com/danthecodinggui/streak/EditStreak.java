package com.danthecodinggui.streak;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.danthecodinggui.streak.Database.StreakDbHelper;

public class EditStreak extends AppCompatActivity {

    private String streakText;
    private int streakDuration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_streak);
        streakDuration = 1;
    }

    /**
     * Saves streak to file system
     * @param view Button view Pressed
     */
    public void AddStreak(View view) {

        EditText text = (EditText)findViewById(R.id.editStreak);
        streakText = text.getText().toString();

        SaveToDatabase(new StreakObject(streakText));

        Intent output = new Intent();
        output.putExtra("newStreak", streakText);
        setResult(HomeActivity.ADD_STREAK, output);
        finish();
    }

    private void SaveToDatabase(StreakObject newStreak) {

        StreakDbHelper sDbHelper = new StreakDbHelper(this);
        sDbHelper.AddStreak(newStreak);
    }
}
