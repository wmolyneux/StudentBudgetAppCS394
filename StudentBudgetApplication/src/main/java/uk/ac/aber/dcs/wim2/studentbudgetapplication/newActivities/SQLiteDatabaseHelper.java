package uk.ac.aber.dcs.wim2.studentbudgetapplication.newActivities;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.LinkedList;
import java.util.List;

import uk.ac.aber.dcs.wim2.studentbudgetapplication.database.Category;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.database.Transaction;

/**
 * Created by wim2 on 26/02/2014.
 */
public class SQLiteDatabaseHelper extends SQLiteOpenHelper {

    //TABLE NAMES
    private static final String TABLE_CONSTANTS = "constants";
    private static final String TABLE_DETAILS = "details";
    private static final String TABLE_TRANSACTIONS = "transactions";
    private static final String TABLE_CATEGORIES = "categories";

    //CONSTANT COLUMN NAMES
    private static final String KEY_CONSTANT_ID = "id";
    private static final String KEY_CONSTANT_TYPE = "type";
    private static final String KEY_CONSTANT_AMOUNT = "amount";
    private static final String KEY_CONSTANT_RECURR = "recurr";

    //Detail column names
    private static final String KEY_DETAIL_ID = "id";
    private static final String KEY_DETAIL_STARTDATE = "startDate";
    private static final String KEY_DETAIL_ENDDATE = "endDate";
    private static final String KEY_DETAIL_WEEKSREMAINING = "weeksRemaining";
    private static final String KEY_DETAIL_WEEKLYINCOME = "weeklyIncome";
    private static final String KEY_DETAIL_WEEKLYEXPENSE = "weeklyExpense";
    private static final String KEY_DETAIL_WEEKLYBALANCE = "weeklyBalance";
    private static final String KEY_DETAIL_BALANCE = "balance";

    //Transaction table column names
    private static final String KEY_TRANSACTION_ID = "id";
    private static final String KEY_TRANSACTION_AMOUNT = "amount";
    private static final String KEY_TRANSACTION_SHORTDESC = "shortDesc";
    private static final String KEY_TRANSACTION_TYPE = "type";
    private static final String KEY_TRANSACTION_CATEGORY = "category";
    private static final String KEY_TRANSACTION_DATE = "date";

    //Category table column names
    private static final String KEY_CATEGORY_ID = "id";
    private static final String KEY_CATEGORY_NAME = "name";

    private static final String[] DETAIL_COLUMNS = {KEY_DETAIL_ID, KEY_DETAIL_STARTDATE, KEY_DETAIL_ENDDATE, KEY_DETAIL_WEEKSREMAINING,
                KEY_DETAIL_WEEKLYINCOME, KEY_DETAIL_WEEKLYEXPENSE, KEY_DETAIL_WEEKLYBALANCE, KEY_DETAIL_BALANCE};

    private static final String[] CONSTANT_COLUMNS = {KEY_CONSTANT_ID, KEY_CONSTANT_TYPE, KEY_CONSTANT_AMOUNT, KEY_CONSTANT_RECURR};

    private static final String[] TRANSACTION_COLUMNS = {KEY_TRANSACTION_ID,
            KEY_TRANSACTION_AMOUNT, KEY_TRANSACTION_SHORTDESC, KEY_TRANSACTION_TYPE,
            KEY_TRANSACTION_CATEGORY, KEY_TRANSACTION_DATE};

    private static final String[] CATEGORY_COLUMNS = {KEY_CATEGORY_ID, KEY_CATEGORY_NAME};

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "APPLICATION_DB";

    public SQLiteDatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //SQL statement for creating detail table
        String CREATE_DETAIL_TABLE = "CREATE TABLE details ( "+
                "id INTEGER PRIMARY KEY AUTOINCREMENT, "+
                "startDate TEXT, "+
                "endDate TEXT, "+
                "weeksRemaining REAL, "+
                "weeklyIncome REAL, "+
                "weeklyExpense REAL, "+
                "weeklyBalance REAL, "+
                "balance REAL)";

        db.execSQL(CREATE_DETAIL_TABLE);

        //SQL statement for creating constant table
        String CREATE_CONSTANT_TABLE = "CREATE TABLE constants ( "+
                "id INTEGER PRIMARY KEY AUTOINCREMENT, "+
                "type TEXT, "+
                "amount REAL, "+
                "recurr TEXT)";

        db.execSQL(CREATE_CONSTANT_TABLE);

        //SQL statement to create categories table
        String CREATE_CATEGORY_TABLE = "CREATE TABLE categories ( "+
                "id INTEGER PRIMARY KEY AUTOINCREMENT, "+
                "name TEXT)";

        //create categories table
        db.execSQL(CREATE_CATEGORY_TABLE);

        // SQL statement to create transactions table
        String CREATE_TRANSACTION_TABLE = "CREATE TABLE transactions ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "amount REAL, "+
                "shortDesc TEXT, "+
                "type TEXT, "+
                "category TEXT, "+
                "date TEXT)";

        // create transactions table
        db.execSQL(CREATE_TRANSACTION_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS details");
        db.execSQL("DROP TABLE IF EXISTS constants");
        db.execSQL("DROP TABLE IF EXISTS transactions");
        db.execSQL("DROP TABLE IF EXISTS categories");
        this.onCreate(db);
    }

    /**
     *
     *
     *
     *
     * CODE FOR DETAIL DATABASE OPERATIONS IS HERE
     *
     *
     *
     *
     */

    /**
     * Function takes an detail object and adds it into the database.
     * @param detail -  detail object to be added to the database.
     */
    public void addDetail(Detail detail){
        //get reference to writable database
        SQLiteDatabase db = this.getWritableDatabase();

        //create content values to add key to column/value
        ContentValues values = new ContentValues();
        values.put(KEY_DETAIL_STARTDATE, detail.getStartDate());
        values.put(KEY_DETAIL_ENDDATE, detail.getEndDate());
        values.put(KEY_DETAIL_WEEKSREMAINING, detail.getWeeksRemaining());
        values.put(KEY_DETAIL_WEEKLYINCOME, detail.getWeeklyIncome());
        values.put(KEY_DETAIL_WEEKLYEXPENSE, detail.getWeeklyExpense());
        values.put(KEY_DETAIL_WEEKLYBALANCE, detail.getWeeklyBalance());
        values.put(KEY_DETAIL_BALANCE, detail.getBalance());


        //insert into database .insert(tablename, columnhack,
        // key/value -> keys = columns names/ values = column values)
        db.insert(TABLE_DETAILS, null, values);

        //close db connection
        db.close();

    }

    /**
     * query database for an account using the given id parameter. creates a detail object using the values
     * from the first returned item from the query.
     * @param id - id of the detail to be found
     * @return - returns first detail object returned from database query
     */
    public Detail getDetail(int id){
        //get the database reference
        SQLiteDatabase db = this.getReadableDatabase();

        //construct query using Cursor class which allows read/write access to returned query set
        Cursor cursor = //query format .query(table name, column names, selections, selection args, group by, having, order by, limit)
                db.query(TABLE_DETAILS, DETAIL_COLUMNS, " id = ?", new String[] {String.valueOf(id)}, null, null, null, null);

        //if we got results, get the first one
        if(cursor != null){
            cursor.moveToFirst();
        }

        //construct detail using values returned from query
        Detail detail = new Detail();
        detail.setId(Integer.parseInt(cursor.getString(0)));
        detail.setStartDate(cursor.getString(1));
        detail.setEndDate(cursor.getString(2));
        detail.setWeeksRemaining(Integer.parseInt(cursor.getString(3)));
        detail.setWeeklyIncome(Float.valueOf(cursor.getString(4)));
        detail.setWeeklyExpense(Float.valueOf(cursor.getString(5)));
        detail.setWeeklyBalance(Float.valueOf(cursor.getString(6)));
        detail.setBalance(Float.valueOf(cursor.getString(7)));


        //return account
        return detail;


    }

    /**
     * Queries the accounts table for all details and returns them all in a linkedlist
     *
     * @return - Linked list of all details in the table
     */
    public List<Detail> getAllDetails(){
        List<Detail> details = new LinkedList<Detail>();

        //build query
        String query = "SELECT * FROM "+TABLE_DETAILS;

        //get reference to database
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        //go over each row, build an account item and add it to the list.
        Detail detail = null;
        if(cursor.moveToFirst()){
            do{
                detail = new Detail();
                detail.setId(Integer.parseInt(cursor.getString(0)));
                detail.setStartDate(cursor.getString(1));
                detail.setEndDate(cursor.getString(2));
                detail.setWeeksRemaining(Integer.parseInt(cursor.getString(3)));
                detail.setWeeklyIncome(Float.valueOf(cursor.getString(4)));
                detail.setWeeklyExpense(Float.valueOf(cursor.getString(5)));
                detail.setWeeklyBalance(Float.valueOf(cursor.getString(6)));
                detail.setBalance(Float.valueOf(cursor.getString(7)));
                details.add(detail);



            }while(cursor.moveToNext());

        }

        db.close();

        return details;
    }

    /**
     * takes an detail and updates them via its id
     *
     * @param detail - detail to update
     * @return - number of rows affected
     */
    public int updateDetail(Detail detail){

        //get reference to database
        SQLiteDatabase db = this.getWritableDatabase();

        //create content values to add key to column/value
        ContentValues values = new ContentValues();
        values.put(KEY_DETAIL_STARTDATE, detail.getStartDate());
        values.put(KEY_DETAIL_ENDDATE, detail.getEndDate());
        values.put(KEY_DETAIL_WEEKSREMAINING, detail.getWeeksRemaining());
        values.put(KEY_DETAIL_WEEKLYINCOME, detail.getWeeklyIncome());
        values.put(KEY_DETAIL_WEEKLYEXPENSE, detail.getWeeklyExpense());
        values.put(KEY_DETAIL_WEEKLYBALANCE, detail.getWeeklyBalance());
        values.put(KEY_DETAIL_BALANCE, detail.getBalance());

        //update the row in the table
        //in the format .update(tablename, column/value, selections, selection args)
        int rowsAffected = db.update(TABLE_DETAILS, values, KEY_DETAIL_ID +" = ?", new String[] {String.valueOf(detail.getId())});

        //close connection to the database
        db.close();

        return rowsAffected;
    }

    public void deleteDetail(Detail detail){
        //get reference to the database
        SQLiteDatabase db = this.getWritableDatabase();

        //delete constants
        db.delete(TABLE_DETAILS, KEY_DETAIL_ID +" = ?", new String[]{String.valueOf(detail.getId())});

        //close connection to database
        db.close();

    }

    /**
     *
     *
     *
     *
     * CODE FOR DETAIL DATABASE OPERATIONS IS HERE
     *
     *
     *
     *
     */

    /**
     * Function takes an constant object and adds it into the database.
     * @param constant -  constant object to be added to the database.
     */
    public void addConstant(Constant constant){
        //get reference to writable database
        SQLiteDatabase db = this.getWritableDatabase();

        //create content values to add key to column/value
        ContentValues values = new ContentValues();
        values.put(KEY_CONSTANT_TYPE, constant.getType());
        values.put(KEY_CONSTANT_AMOUNT, constant.getAmount());
        values.put(KEY_CONSTANT_RECURR, constant.getRecurr());


        //insert into database .insert(tablename, columnhack,
        // key/value -> keys = columns names/ values = column values)
        db.insert(TABLE_CONSTANTS, null, values);

        //close db connection
        db.close();

    }

    /**
     * query database for an constant using the given id parameter. creates a constant object using the values
     * from the first returned item from the query.
     * @param id - id of the constant to be found
     * @return - returns first detail object returned from database query
     */
    public Constant getConstant(int id){
        //get the database reference
        SQLiteDatabase db = this.getReadableDatabase();

        //construct query using Cursor class which allows read/write access to returned query set
        Cursor cursor = //query format .query(table name, column names, selections, selection args, group by, having, order by, limit)
                db.query(TABLE_CONSTANTS, CONSTANT_COLUMNS, " id = ?", new String[] {String.valueOf(id)}, null, null, null, null);

        //if we got results, get the first one
        if(cursor != null){
            cursor.moveToFirst();
        }

        //construct constant using values returned from query
        Constant constant = new Constant();
        constant.setId(Integer.parseInt(cursor.getString(0)));
        constant.setType(cursor.getString(1));
        constant.setAmount(Float.valueOf(cursor.getString(2)));
        constant.setRecurr(cursor.getString(3));

        //return account
        return constant;


    }

    /**
     * Queries the constant table for all constants and returns them all in a linkedlist
     *
     * @return - Linked list of all constants in the table
     */
    public List<Constant> getAllConstants(){
        List<Constant> constants = new LinkedList<Constant>();

        //build query
        String query = "SELECT * FROM "+TABLE_CONSTANTS;

        //get reference to database
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        //go over each row, build an account item and add it to the list.
        Constant constant = null;
        if(cursor.moveToFirst()){
            do{
                //construct constant using values returned from query
                constant = new Constant();
                constant.setId(Integer.parseInt(cursor.getString(0)));
                constant.setType(cursor.getString(1));
                constant.setAmount(Float.valueOf(cursor.getString(2)));
                constant.setRecurr(cursor.getString(3));
                constants.add(constant);



            }while(cursor.moveToNext());

        }

        db.close();

        return constants;
    }

    /**
     * takes an constant and updates them via its id
     *
     * @param constant - constant to update
     * @return - number of rows affected
     */
    public int updateConstant(Constant constant){

        //get reference to database
        SQLiteDatabase db = this.getWritableDatabase();

        //create content values to add key to column/value
        ContentValues values = new ContentValues();
        values.put(KEY_CONSTANT_TYPE, constant.getType());
        values.put(KEY_CONSTANT_AMOUNT, constant.getAmount());
        values.put(KEY_CONSTANT_RECURR, constant.getRecurr());

        //update the row in the table
        //in the format .update(tablename, column/value, selections, selection args)
        int rowsAffected = db.update(TABLE_CONSTANTS, values, KEY_CONSTANT_ID +" = ?", new String[] {String.valueOf(constant.getId())});

        //close connection to the database
        db.close();

        return rowsAffected;
    }

    public void deleteConstant(Constant constant){
        //get reference to the database
        SQLiteDatabase db = this.getWritableDatabase();

        //delete constants
        db.delete(TABLE_CONSTANTS, KEY_CONSTANT_ID +" = ?", new String[]{String.valueOf(constant.getId())});

        //close connection to database
        db.close();

    }

    /**
     *
     * ################################################
     * CODE FOR TRANSACTION DATABASE OPERATIONS IS HERE
     * ################################################
     *
     *
     *
     */



    /**
     * Function takes an transaction object and adds it into the database.
     * @param transaction -  transaction object to be added to the database.
     */
    public void addTransaction(Transaction transaction){
        //get reference to writable database
        SQLiteDatabase db = this.getWritableDatabase();

        //create content values to add key to column/value
        ContentValues values = new ContentValues();
        values.put(KEY_TRANSACTION_AMOUNT, transaction.getAmount());
        values.put(KEY_TRANSACTION_SHORTDESC, transaction.getShortDesc());
        values.put(KEY_TRANSACTION_TYPE, transaction.getType());
        values.put(KEY_TRANSACTION_CATEGORY, transaction.getCategory());
        values.put(KEY_TRANSACTION_DATE, transaction.getDate());

        //insert into database .insert(tablename, columnhack,
        // key/value -> keys = columns names/ values = column values)
        db.insert(TABLE_TRANSACTIONS, null, values);

        //close db connection
        db.close();

    }

    /**
     * query database for an transaction using the given id parameter. creates a transaction object using the values
     * from the first returned item from the query.
     * @param id - id of the transaction to be found
     * @return - returns first transaction object returned from database query
     */
    public Transaction getTransaction(int id){
        //get the database reference
        SQLiteDatabase db = this.getReadableDatabase();

        //construct query using Cursor class which allows read/write access to returned query set
        Cursor cursor = //query format .query(table name, column names, selections, selection args, group by, having, order by, limit)
                db.query(TABLE_TRANSACTIONS, TRANSACTION_COLUMNS, " id = ?", new String[] {String.valueOf(id)}, null, null, null, null);

        //if we got results, get the first one
        if(cursor != null){
            cursor.moveToFirst();
        }

        //construct account using values returned from query
        Transaction trans = new Transaction();
        trans.setId(Integer.parseInt(cursor.getString(0)));
        trans.setAmount(Float.parseFloat(cursor.getString(1)));
        trans.setShortDesc(cursor.getString(2));
        trans.setType(cursor.getString(3));
        trans.setCategory(cursor.getString(4));
        trans.setDate(cursor.getString(5));

        //return account
        return trans;


    }

    /**
     * Queries the transaction table for all transactions and returns them all in a linkedlist
     *
     * @return - Linked list of all transactions in the table
     */
    public List<Transaction> getAllTransactions(){
        List<Transaction> transactions = new LinkedList<Transaction>();

        //build query
        String query = "SELECT * FROM "+TABLE_TRANSACTIONS;

        //get reference to database
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        //go over each row, build an account item and add it to the list.
        Transaction trans = null;
        if(cursor.moveToFirst()){
            do{
                trans = new Transaction();
                trans.setId(Integer.parseInt(cursor.getString(0)));
                trans.setAmount(Float.parseFloat(cursor.getString(1)));
                trans.setShortDesc(cursor.getString(2));
                trans.setType(cursor.getString(3));
                trans.setCategory(cursor.getString(4));
                trans.setDate(cursor.getString(5));


                transactions.add(trans);


            }while(cursor.moveToNext());

        }

        db.close();

        return transactions;
    }

    /**
     * takes a transaction and updates them via its id
     *
     * @param transaction - transaction to update
     * @return - number of rows affected
     */
    public int updateTransaction(Transaction transaction){

        //get reference to database
        SQLiteDatabase db = this.getWritableDatabase();

        //create contentValues to add key to column/value
        ContentValues values = new ContentValues();
        values.put(KEY_TRANSACTION_AMOUNT, transaction.getAmount());
        values.put(KEY_TRANSACTION_SHORTDESC, transaction.getShortDesc());
        values.put(KEY_TRANSACTION_TYPE, transaction.getType());
        values.put(KEY_TRANSACTION_CATEGORY, transaction.getCategory());
        values.put(KEY_TRANSACTION_DATE, transaction.getDate());


        //update the row in the table
        //in the format .update(tablename, column/value, selections, selection args)
        int rowsAffected = db.update(TABLE_TRANSACTIONS, values, KEY_TRANSACTION_ID +" = ?", new String[] {String.valueOf(transaction.getId())});

        //close connection to the database
        db.close();

        return rowsAffected;
    }


    public void deleteTransaction(Transaction transaction){
        //get reference to the database
        SQLiteDatabase db = this.getWritableDatabase();

        //delete account
        db.delete(TABLE_TRANSACTIONS, KEY_TRANSACTION_ID +" = ?", new String[]{String.valueOf(transaction.getId())});

        //close connection to database
        db.close();

    }

    /**
     *
     * ################################################
     * CODE FOR CATEGORY DATABASE OPERATIONS IS HERE
     * ################################################
     *
     *
     *
     */



    /**
     * Function takes an transaction object and adds it into the database.
     * @param category -  transaction object to be added to the database.
     */
    public void addCategory(Category category){
        //get reference to writable database
        SQLiteDatabase db = this.getWritableDatabase();

        //create content values to add key to column/value
        ContentValues values = new ContentValues();
        values.put(KEY_CATEGORY_NAME, category.getName());

        //insert into database .insert(tablename, columnhack,
        // key/value -> keys = columns names/ values = column values)
        db.insert(TABLE_CATEGORIES, null, values);

        //close db connection
        db.close();

    }

    /**
     * query database for an transaction using the given id parameter. creates a transaction object using the values
     * from the first returned item from the query.
     * @param id - id of the transaction to be found
     * @return - returns first transaction object returned from database query
     */
    public Category getCategory(int id){
        //get the database reference
        SQLiteDatabase db = this.getReadableDatabase();

        //construct query using Cursor class which allows read/write access to returned query set
        Cursor cursor = //query format .query(table name, column names, selections, selection args, group by, having, order by, limit)
                db.query(TABLE_CATEGORIES, CATEGORY_COLUMNS, " id = ?", new String[] {String.valueOf(id)}, null, null, null, null);

        //if we got results, get the first one
        if(cursor != null){
            cursor.moveToFirst();
        }

        //construct account using values returned from query
        Category cat = new Category();
        cat.setId(Integer.parseInt(cursor.getString(0)));
        cat.setName(cursor.getString(1));

        //return account
        return cat;


    }

    /**
     * Queries the transaction table for all transactions and returns them all in a linkedlist
     *
     * @return - Linked list of all transactions in the table
     */
    public List<Category> getAllCategories(){
        List<Category> categories = new LinkedList<Category>();

        //build query
        String query = "SELECT * FROM "+TABLE_CATEGORIES;

        //get reference to database
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        //go over each row, build an account item and add it to the list.
        Category cats = null;
        if(cursor.moveToFirst()){
            do{
                cats = new Category();
                cats.setId(Integer.parseInt(cursor.getString(0)));
                cats.setName(cursor.getString(1));

                categories.add(cats);


            }while(cursor.moveToNext());

        }

        db.close();

        return categories;
    }

    /**
     * takes a category and updates them via its id
     *
     * @param category - category to update
     * @return - number of rows affected
     */
    public int updateCategory(Category category){

        //get reference to database
        SQLiteDatabase db = this.getWritableDatabase();

        //create contentValues to add key to column/value
        ContentValues values = new ContentValues();
        values.put(KEY_CATEGORY_NAME, category.getName());

        //update the row in the table
        //in the format .update(tablename, column/value, selections, selection args)
        int rowsAffected = db.update(TABLE_CATEGORIES, values, KEY_CATEGORY_ID +" = ?", new String[] {String.valueOf(category.getId())});

        //close connection to the database
        db.close();

        return rowsAffected;
    }


    public void deleteCategory(Category category){
        //get reference to the database
        SQLiteDatabase db = this.getWritableDatabase();

        //delete account
        db.delete(TABLE_CATEGORIES, KEY_CATEGORY_ID +" = ?", new String[]{String.valueOf(category.getId())});

        //close connection to database
        db.close();

    }
}
