package com.danthecodinggui.streak.View;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.text.format.Time;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.codetroopers.betterpickers.recurrencepicker.EventRecurrence;
import com.codetroopers.betterpickers.recurrencepicker.EventRecurrenceFormatter;
import com.codetroopers.betterpickers.recurrencepicker.RecurrencePickerDialogFragment;
import com.danthecodinggui.streak.Data.StreakObject;
import com.danthecodinggui.streak.Presenter.EditPresenter;
import com.danthecodinggui.streak.R;

/**
 * Screen shown whenever activity added or existing streak clicked by user
 */
public class EditStreakActivity extends AppCompatActivity implements Viewable
        , TimePickerDialog.OnTimeSetListener, RecurrencePickerDialogFragment.OnRecurrenceSetListener {

    private EditPresenter presenter;

    private EditText streakTextBox;
    private TextView scheduleTime;
    private TextView checkInTime;

    private int checkInHour;
    private int checkInMinute;

    private String streakText;
    private int streakDuration;

    private int streakViewId;
    private long streakUniqueId;

    private boolean streakIsPriority;

    private int function;

    public static final int ADD_STREAK = 0;
    public static final int EDIT_STREAK = 1;

    private static final String FRAG_TAG_RECUR_PICKER = "recurrencePickerDialogFragment";
    private String mRrule;
    private EventRecurrence mEventRecurrence = new EventRecurrence();

    //streak object as it exists entering this activity, used later on to check for changes upon
    //activity exit
    private StreakObject initialStreak;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_streak);

        presenter = new EditPresenter(this);

        Toolbar editBar = (Toolbar)findViewById(R.id.tbar_edit_bar);
        setSupportActionBar(editBar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        Intent intent = getIntent();
        function = intent.getIntExtra("function", ADD_STREAK);

        streakTextBox = (EditText)findViewById(R.id.etxt_streak_description);
        scheduleTime = (TextView)findViewById(R.id.txt_streak_schedule);
        checkInTime = (TextView)findViewById(R.id.txt_select_checkin);
        scheduleTime.setText(R.string.default_schedule);
        checkInHour = 18;
        checkInMinute = 0;
        checkInTime.setText(checkInHour + ":" + ((checkInMinute < 10) ? ("0" + checkInMinute) : (checkInMinute)));

        checkInTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog d = new TimePickerDialog(getActivityContext(), EditStreakActivity.this, checkInHour, checkInMinute, true);
                d.show();
            }
        });
        scheduleTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                Bundle bundle = new Bundle();
                Time time = new Time();
                time.setToNow();
                bundle.putLong(RecurrencePickerDialogFragment.BUNDLE_START_TIME_MILLIS, time.toMillis(false));
                bundle.putString(RecurrencePickerDialogFragment.BUNDLE_TIME_ZONE, time.timezone);

                // may be more efficient to serialize and pass in EventRecurrence
                bundle.putString(RecurrencePickerDialogFragment.BUNDLE_RRULE, mRrule);

                RecurrencePickerDialogFragment rpd = (RecurrencePickerDialogFragment) fm.findFragmentByTag(
                        FRAG_TAG_RECUR_PICKER);
                if (rpd != null) {
                    rpd.dismiss();
                }
                rpd = new RecurrencePickerDialogFragment();
                rpd.setArguments(bundle);
                rpd.setOnRecurrenceSetListener(EditStreakActivity.this);
                rpd.show(fm, FRAG_TAG_RECUR_PICKER);
            }
        });

        switch(function) {
            case (ADD_STREAK):

                streakDuration = 0;
                streakViewId = getIntent().getIntExtra("listSize", -1);

                break;
            case (EDIT_STREAK):

                streakViewId = getIntent().getIntExtra("viewId", -1);
                streakUniqueId = getIntent().getLongExtra("streakId", -1);
                initialStreak = presenter.GetStreak(streakUniqueId);
                streakDuration = initialStreak.getStreakDuration();

                streakText = initialStreak.getStreakText();

                streakTextBox.append(streakText);
                break;
            default:
                Log.d("Error", "Invalid intent to EditStreakActivity");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_action_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch(id) {
            case R.id.btn_confirm_streak:
                SubmitStreak();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Saves new/edited streak to database
     */
    public void SubmitStreak() {

        streakText = streakTextBox.getText().toString();

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

    //Interface methods so presenter can access activity based functionality
    @Override
    public Context getActivityContext() {
        return this;
    }
    @Override
    public String getStringResource(int resource) {
        return getString(resource);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        checkInTime.setText(hourOfDay + ":" + ((minute < 10) ? ("0" + minute) : (minute)));
        checkInHour = hourOfDay;
        checkInMinute = minute;
    }

    @Override
    public void onRecurrenceSet(String rrule) {
        mRrule = rrule;
        if (mRrule != null) {
            mEventRecurrence.parse(mRrule);
        }
        populateRepeats();
    }

    private void populateRepeats() {
        Resources r = getResources();
        String repeatString = "";
        boolean enabled;
        if (!TextUtils.isEmpty(mRrule)) {
            repeatString = EventRecurrenceFormatter.getRepeatString(this, r, mEventRecurrence, true);
        }

        scheduleTime.setText(mRrule + "\n" + repeatString);
    }
}
