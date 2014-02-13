package uk.ac.aber.dcs.wim2.studentbudgetapplication.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by wim2 on 05/02/2014.
 */
public class SQLiteHelper extends SQLiteOpenHelper {

    // Table nameS
    private static final String TABLE_ACCOUNTS = "accounts";
    private static final String TABLE_TRANSACTIONS = "transactions";

    // Account Table Columns names
    private static final String KEY_ACCOUNT_ID = "id";
    private static final String KEY_ACCOUNT_NAME = "name";
    private static final String KEY_ACCOUNT_BALANCE = "balance";
    private static final String KEY_ACCOUNT_OVERDRAFT = "overdraft";

    //Transaction table column names
    private static final String KEY_TRANSACTION_ID = "id";
    private static final String KEY_TRANSACTION_ACCOUNTID = "accountId";
    private static final String KEY_TRANSACTION_AMOUNT = "amount";

    private static final String[] ACCOUNT_COLUMNS = {KEY_ACCOUNT_ID, KEY_ACCOUNT_NAME, KEY_ACCOUNT_BALANCE, KEY_ACCOUNT_OVERDRAFT};
    private static final String[] TRANSACTION_COLUMNS = {KEY_TRANSACTION_ID, KEY_TRANSACTION_ACCOUNTID, KEY_TRANSACTION_AMOUNT};


    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "APPLICATION_DB";

    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        // SQL statement to create account table
        String CREATE_ACCOUNT_TABLE = "CREATE TABLE accounts ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT, "+
                "balance REAL, "+
                "overdraft REAL)";

        // create account table
        db.execSQL(CREATE_ACCOUNT_TABLE);

        // SQL statement to create transactions table
        String CREATE_TRANSACTION_TABLE = "CREATE TABLE transactions ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "accountId INTEGER, "+
                "amount REAL)";

        // create transactions table
        db.execSQL(CREATE_TRANSACTION_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older accounts table if existed
        db.execSQL("DROP TABLE IF EXISTS accounts");

        // Drop older transactions table if existed
        db.execSQL("DROP TABLE IF EXISTS transaction");

        // create fresh accounts table
        this.onCreate(db);
    }


    /**
     *
     *
     * ############################################
     * CODE FOR ACCOUNT DATABASE OPERATIONS IS HERE
     * ############################################
     *
     *
     */



    /**
     * Function takes an account object and adds it into the database.
     * @param account -  account object to be added to the database.
     */
    public void addAccount(Account account){
        //get reference to writable database
        SQLiteDatabase db = this.getWritableDatabase();

        //create content values to add key to column/value
        ContentValues values = new ContentValues();
        values.put(KEY_ACCOUNT_NAME, account.getAccountName());
        values.put(KEY_ACCOUNT_BALANCE, account.getBalance());
        values.put(KEY_ACCOUNT_OVERDRAFT, account.getOverdraft());

        //insert into database .insert(tablename, columnhack,
        // key/value -> keys = columns names/ values = column values)
        db.insert(TABLE_ACCOUNTS, null, values);

        //close db connection
        db.close();

    }

    /**
     * query database for an account using the given id parameter. creates a account object using the values
     * from the first returned item from the query.
     * @param id - id of the account to be found
     * @return - returns first account object returned from database query
     */
    public Account getAccount(int id){
        //get the database reference
        SQLiteDatabase db = this.getReadableDatabase();

        //construct query using Cursor class which allows read/write access to returned query set
        Cursor cursor = //query format .query(table name, column names, selections, selection args, group by, having, order by, limit)
                db.query(TABLE_ACCOUNTS, ACCOUNT_COLUMNS, " id = ?", new String[] {String.valueOf(id)}, null, null, null, null);

        //if we got results, get the first one
        if(cursor != null){
            cursor.moveToFirst();
        }

        //construct account using values returned from query
        Account acc = new Account();
        acc.setId(Integer.parseInt(cursor.getString(0)));
        acc.setAccountName(cursor.getString(1));
        acc.setBalance(Float.parseFloat(cursor.getString(2)));
        acc.setOverdraft(Float.parseFloat(cursor.getString(3)));

        //return account
        return acc;


    }

    /**
     * Queries the accounts table for all accounts and returns them all in a linkedlist
     *
     * @return - Linked list of all accounts in the table
     */
    public List<Account> getAllAccounts(){
        List<Account> accounts = new LinkedList<Account>();

        //build query
        String query = "SELECT * FROM "+TABLE_ACCOUNTS;

        //get reference to database
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        //go over each row, build an account item and add it to the list.
        Account acc = null;
        if(cursor.moveToFirst()){
            do{
                acc = new Account();
                acc.setId(Integer.parseInt(cursor.getString(0)));
                acc.setAccountName(cursor.getString(1));
                acc.setBalance(Float.parseFloat(cursor.getString(2)));
                acc.setOverdraft(Float.parseFloat(cursor.getString(3)));

                accounts.add(acc);

            }while(cursor.moveToNext());

        }

        db.close();

        return accounts;
    }

    /**
     * takes an account and updates them via its id
     *
     * @param account - account to update
     * @return - number of rows affected
     */
    public int updateAccount(Account account){

        //get reference to database
        SQLiteDatabase db = this.getWritableDatabase();

        //create contentValues to add key to column/value
        ContentValues values = new ContentValues();
        values.put(KEY_ACCOUNT_NAME, account.getAccountName());
        values.put(KEY_ACCOUNT_BALANCE, account.getBalance());
        values.put(KEY_ACCOUNT_OVERDRAFT, account.getOverdraft());

        //update the row in the table
        //in the format .update(tablename, column/value, selections, selection args)
        int rowsAffected = db.update(TABLE_ACCOUNTS, values, KEY_ACCOUNT_ID +" = ?", new String[] {String.valueOf(account.getId())});

        //close connection to the database
        db.close();

        return rowsAffected;
    }


    public void deleteAccount(Account account){
        //get reference to the database
        SQLiteDatabase db = this.getWritableDatabase();

        //delete account
        db.delete(TABLE_ACCOUNTS, KEY_ACCOUNT_ID +" = ?", new String[]{String.valueOf(account.getId())});

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
        values.put(KEY_TRANSACTION_ACCOUNTID, transaction.getAccountId());
        values.put(KEY_TRANSACTION_AMOUNT, transaction.getAmount());

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
        trans.setAccountId(Integer.parseInt(cursor.getString(1)));
        trans.setAmount(Float.parseFloat(cursor.getString(2)));

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
                trans.setAccountId(Integer.parseInt(cursor.getString(1)));
                trans.setAmount(Float.parseFloat(cursor.getString(2)));


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
        values.put(KEY_TRANSACTION_ACCOUNTID, transaction.getAccountId());
        values.put(KEY_TRANSACTION_AMOUNT, transaction.getAmount());

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


}
