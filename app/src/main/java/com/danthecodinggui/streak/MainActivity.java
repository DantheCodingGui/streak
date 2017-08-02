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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView streakContainer = (RecyclerView)findViewById(R.id.home_container);

        StaggeredGridLayoutManager streakLayoutManager = new StaggeredGridLayoutManager(2, 1);
        streakContainer.setLayoutManager(streakLayoutManager);

        List<StreakObject> streaks = getListItemData();

        StreakRecyclerViewAdapter rcAdapter = new StreakRecyclerViewAdapter(MainActivity.this, streaks);
        streakContainer.setAdapter(rcAdapter);
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.home_action_bar_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private List<StreakObject> getListItemData(){
        List<StreakObject> listViewItems = new ArrayList<StreakObject>();
        listViewItems.add(new StreakObject("Alkane"));
        listViewItems.add(new StreakObject("Ethane"));
        listViewItems.add(new StreakObject("Alkyne is the best thing in the world to me, its really really really great"));
        listViewItems.add(new StreakObject("Benzene"));
        listViewItems.add(new StreakObject("Amide"));
        listViewItems.add(new StreakObject("Amino Acid"));
        listViewItems.add(new StreakObject("Phenol"));
        listViewItems.add(new StreakObject("Carbonxylic sounds really toxic, are you really sure we can display that"));
        listViewItems.add(new StreakObject("Nitril"));
        listViewItems.add(new StreakObject("Ether"));
        listViewItems.add(new StreakObject("Ester"));;

        return listViewItems;
    }
}