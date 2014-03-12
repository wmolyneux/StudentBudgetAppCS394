package uk.ac.aber.dcs.wim2.studentbudgetapplication.activities;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import uk.ac.aber.dcs.wim2.studentbudgetapplication.R;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.fragments.BudgetsFragment;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.fragments.HistoryFragment;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.fragments.OverviewFragment;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.fragments.ReportFragment;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.fragments.TransactionsFragment;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.fragments.ExpenseFragment;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.fragments.IncomeFragment;

public class DetailActivity extends FragmentActivity {

    private String[] drawerListViewItems;
    private ListView drawerListView;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;


    @Override
    public void onBackPressed() {}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        manageFragments(new OverviewFragment(), R.id.content_frame);

        // get list items from strings.xml
        drawerListViewItems = getResources().getStringArray(R.array.items);

        // get ListView defined in activity_main.xml
        drawerListView = (ListView) findViewById(R.id.left_drawer);

        // Set the adapter for the list view
        drawerListView.setAdapter(new ArrayAdapter<String>(this,
                R.layout.listview_navigator, drawerListViewItems));

        // App Icon
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        actionBarDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                drawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer icon to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description */
                R.string.drawer_close  /* "close drawer" description */
        );

        // Set actionBarDrawerToggle as the DrawerListener
        drawerLayout.setDrawerListener(actionBarDrawerToggle);

        getActionBar().setDisplayHomeAsUpEnabled(true);

        drawerListView.setOnItemClickListener(new DrawerItemClickListener());


    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        actionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // call ActionBarDrawerToggle.onOptionsItemSelected(), if it returns true
        // then it has handled the app icon touch event
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        actionBarDrawerToggle.syncState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.detail, menu);
        return true;
    }

    public void manageFragments(Fragment newFrag, int oldFragId){
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(oldFragId, newFrag);
        transaction.commit();
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            Fragment frag = null;
            switch(position){
                case 0:
                    frag = new OverviewFragment();
                    break;
                case 1:
                    frag = new TransactionsFragment();
                    break;
                case 2:
                    frag = new HistoryFragment();
                    break;
                case 3:
                    frag = new ReportFragment();
                    break;
                case 4:
                    frag = new BudgetsFragment();
                    break;
                case 5:
                    frag = new IncomeFragment();
                    break;
                case 6:
                    frag = new ExpenseFragment();
                    break;
            }
            manageFragments(frag, R.id.content_frame);


            drawerLayout.closeDrawer(drawerListView);
        }
    }


    
}
