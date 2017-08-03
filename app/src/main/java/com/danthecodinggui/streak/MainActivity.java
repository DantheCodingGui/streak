package com.danthecodinggui.streak;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<StreakObject> listViewItems;

    private StreakRecyclerViewAdapter rcAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView streakRecycler = (RecyclerView)findViewById(R.id.home_container);

        StaggeredGridLayoutManager streakLayoutManager = new StaggeredGridLayoutManager(2, 1);
        streakRecycler.setLayoutManager(streakLayoutManager);

        List<StreakObject> streaks = getListItemData();

        rcAdapter = new StreakRecyclerViewAdapter(streaks);
        streakRecycler.setAdapter(rcAdapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_action_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch(id) {
            case R.id.home_action_bar_add:
                listViewItems.add(new StreakObject("New Streak"));
                rcAdapter.notifyItemInserted(listViewItems.size() - 1);
                break;
            case R.id.home_action_bar_remove:
                listViewItems.remove(listViewItems.size() - 1);
                rcAdapter.notifyItemRemoved(listViewItems.size());
                break;
            case R.id.home_action_bar_overflow:
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**Should check for saved streaks and add, if not available then return empty list*/
    private List<StreakObject> getListItemData(){
        listViewItems = new ArrayList<StreakObject>();

        return listViewItems;
    }
}