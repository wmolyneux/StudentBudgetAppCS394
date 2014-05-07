package uk.ac.aber.dcs.wim2.studentbudgetapplication.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

import uk.ac.aber.dcs.wim2.studentbudgetapplication.database.Category;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.R;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.database.SQLiteDatabaseHelper;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.utils.FragmentUtilities;

/**
 * This class contains the functionality the initial screen of the application
 *
 * @author wim2
 * @version 1.0
 */
public class EnterActivity extends Activity implements View.OnClickListener{

    private Button entryButton;
    private SQLiteDatabaseHelper db;
    private Context context;

    /**
     * Called when the activity is created to instantiate objects
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new SQLiteDatabaseHelper(this);
        //refresh databases
        FragmentUtilities.refreshPreferences(this);
        context = this;

        //check if database is empty, if it is NOT then redirect to main page
        if(db.getAllDetails().size()!=0){
            startDetail();
        }
        setContentView(R.layout.activity_enter);
        entryButton = (Button)findViewById(R.id.enterButton);
        entryButton.setOnClickListener(this);
    }

    /**
     * Called when a activity is finished with a result code sent to this activity.
     *
     * @param requestCode - request code
     * @param resultCode - result code
     * @param data - data sent from finishing activity
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        FragmentUtilities.refreshPreferences(this);
        Intent i = getIntent();
        finish();
        startActivity(i);
    }

    /**
     * Called on creationg of the activity to create options menu.
     *
     * @param menu - Menu on the screen
     *
     * @return true if passed
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return FragmentUtilities.menuItemSetup(menu, this);
    }

    /**
     * Called when view with an attached OnClickListener is clicked.
     *
     * @param view - view that has been clicked
     */
    @Override
    public void onClick(View view) {
        switch(view.getId()){
            //if the enter button is clicked
            case R.id.enterButton:
                //populate category table
                populateCategoryTable();
                //if database is not empty then redirect to home screen
                if(db.getAllDetails().size()!=0){
                    startDetail();
                }
                //else start the setup process starting with budget period setup
                else {
                    Intent intent = new Intent(this, BudgetPeriodActivity.class);
                    startActivity(intent);
                }
                break;
        }
    }

    /**
     * Starts the main detail activity
     */
    private void startDetail() {
        Intent intent = new Intent(this, DetailActivity.class);
        startActivity(intent);
    }

    /**
     * Populates the category table with all the categories as an integer referring to their position
     * in the String array in the strings.xml file and their colours.
     */
    private void populateCategoryTable() {
        if(db.getAllCategories().size()==0){
            db.addCategory(new Category(0, "cyan"));
            db.addCategory(new Category(1, "darkGreen"));
            db.addCategory(new Category(2, "green"));
            db.addCategory(new Category(3, "magenta"));
            db.addCategory(new Category(4, "yellow"));
            db.addCategory(new Category(5, "red"));
            db.addCategory(new Category(6, "blue"));
            db.addCategory(new Category(7, "purple"));
        }
    }




}
