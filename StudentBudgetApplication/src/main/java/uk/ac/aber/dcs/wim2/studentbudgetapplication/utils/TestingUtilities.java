package uk.ac.aber.dcs.wim2.studentbudgetapplication.utils;

import com.robotium.solo.Solo;

import uk.ac.aber.dcs.wim2.studentbudgetapplication.database.Budget;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.database.Constant;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.database.Detail;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.database.SQLiteDatabaseHelper;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.database.Transaction;

/**
 * Created by wim2 on 28/03/2014.
 */
public class TestingUtilities {

    public static void checkDatabase(Solo solo) {
        SQLiteDatabaseHelper db = new SQLiteDatabaseHelper(solo.getCurrentActivity());
        if(db.getAllDetails().size()!=0){
            for (Detail detail : db.getAllDetails()){
                db.deleteDetail(detail);
            }
            for (Transaction trans : db.getAllTransactions()){
                db.deleteTransaction(trans);
            }
            for (Budget budget : db.getAllBudgets()){
                db.deleteBudget(budget);
            }
            for (Constant con : db.getAllConstants()){
                db.deleteConstant(con);
            }
        }

    }
}
