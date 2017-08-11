package com.danthecodinggui.streak.View;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.danthecodinggui.streak.Data.StreakObject;
import com.danthecodinggui.streak.Presenter.EditPresenter;
import com.danthecodinggui.streak.R;

import static com.danthecodinggui.streak.R.id.editStreak;

/**
 * Screen shown whenever activity added or existing streak clicked by user
 */
public class EditStreakActivity extends AppCompatActivity implements Viewable {

    private EditPresenter presenter;

    private String streakText;
    private int streakDuration;

    private int streakViewId;
    private long streakUniqueId;

    private boolean streakIsPriority;

    private int function;

    public static final int ADD_STREAK = 0;
    public static final int EDIT_STREAK = 1;

    //streak object as it exists entering this activity, used later on to check for changes upon
    //activity exit
    private StreakObject initialStreak;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_streak);

        presenter = new EditPresenter(this);

        Intent intent = getIntent();
        function = intent.getIntExtra("function", ADD_STREAK);

        switch(function) {
            case (ADD_STREAK):

                streakDuration = 1;
                streakViewId = getIntent().getIntExtra("listSize", -1);

                break;
            case (EDIT_STREAK):

                streakViewId = getIntent().getIntExtra("viewId", -1);
                streakUniqueId = getIntent().getLongExtra("streakId", -1);
                initialStreak = presenter.GetStreak(streakUniqueId);

                streakText = initialStreak.getStreakText();

                EditText text = (EditText)findViewById(editStreak);
                text.append(streakText);
                break;
            default:
                Log.d("Error", "Invalid intent to EditStreakActivity");
                return;
        }
    }

    /**
     * Saves new/edited streak to database
     * @param view Button view Pressed
     */
    public void SubmitStreak(View view) {

        EditText text = (EditText)findViewById(editStreak);
        streakText = text.getText().toString();

        StreakObject streakToSubmit = new StreakObject(streakText, streakDuration);

        Intent output = new Intent();

        switch(function) {
            case (ADD_STREAK):
                long primaryKey = presenter.AddStreak(streakToSubmit, streakViewId);

                output.putExtra("newStreakId", primaryKey);
                output.putExtra("newStreak", streakText);
                output.putExtra("newStreakDuration", streakDuration);

                setResult(RESULT_OK, output);
                break;
            case (EDIT_STREAK):
                if (initialStreak.equals(streakToSubmit)) {
                    output.putExtra("hasStreakChanged", false);
                    setResult(HomeActivity.EDIT_STREAK, output);
                    break;
                }

                streakToSubmit.setStreakId(streakUniqueId);
                presenter.UpdateStreak(streakToSubmit, HomeActivity.UPDATE_TEXT);

                output.putExtra("editedStreak", streakText);
                output.putExtra("editedStreakPosition", streakViewId);
                output.putExtra("hasStreakChanged", true);

                setResult(RESULT_OK, output);
                break;
            default:
                Log.d("Error", "Invalid call to SubmitStreak");
                break;
        }

        //output.putExtra("newStreakIsPriority", streakIsPriority);

        finish();
    }

    @Override
    public Context getActivityContext() {
        return this;
    }

    @Override
    public String getStringResource(int resource) {
        return getString(resource);
    }
}
