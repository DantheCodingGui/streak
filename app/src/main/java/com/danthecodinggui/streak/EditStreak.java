package com.danthecodinggui.streak;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class EditStreak extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_streak);
    }

    /**
     * Saves streak to file system
     * @param view Button view Pressed
     */
    public void AddStreak(View view) {

        EditText text = (EditText)findViewById(R.id.editStreak);
        String streakText = text.getText().toString();

        Intent output = new Intent();
        output.putExtra("newStreak", streakText);
        setResult(HomeActivity.ADD_STREAK, output);
        finish();
    }
}
