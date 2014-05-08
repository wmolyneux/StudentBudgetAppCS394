package uk.ac.aber.dcs.wim2.studentbudgetapplication.utils;

import com.robotium.solo.Solo;

import uk.ac.aber.dcs.wim2.studentbudgetapplication.database.Budget;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.database.Constant;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.database.Detail;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.database.SQLiteDatabaseHelper;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.database.Transaction;

/**
 * This class contains the functionality to assist with testing the application
 *
 * @author wim2
 * @version 1.0
 */
public class TestingUtilities {

    /**
     * Checks if the database exists and removes all values from it
     *
     * @param solo - testing instrumentation object
     */
    public static void checkDatabase(Solo solo) {
        SQLiteDatabaseHelper db = new SQLiteDatabaseHelper(solo.getCurrentActivity());
        if(db.getAllDetails().size()!=0){
            //removes all detail objects
            for (Detail detail : db.getAllDetails()){
                db.deleteDetail(detail);
            }

            //removes all transactions
            for (Transaction trans : db.getAllTransactions()){
                db.deleteTransaction(trans);
            }

            //removes all budgets
            for (Budget budget : db.getAllBudgets()){
                db.deleteBudget(budget);
            }

            //removes all constants
            for (Constant con : db.getAllConstants()){
                db.deleteConstant(con);
            }
        }

    }
}
