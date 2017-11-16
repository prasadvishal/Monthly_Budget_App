package com.mindfiresolutions.monthlybudget.DatabaseFiles;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import com.mindfiresolutions.monthlybudget.Utilities.Constants;
import com.mindfiresolutions.monthlybudget.Utilities.LoggerUtilities.LoggerUtility;
import com.mindfiresolutions.monthlybudget.modelClasses.MonthEntry;
import com.mindfiresolutions.monthlybudget.modelClasses.MonthYear;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import static com.mindfiresolutions.monthlybudget.Utilities.Constants.AMOUNT_KEY;
import static com.mindfiresolutions.monthlybudget.Utilities.Constants.DATE_KEY;
import static com.mindfiresolutions.monthlybudget.Utilities.Constants.MONTH_KEY;
import static com.mindfiresolutions.monthlybudget.Utilities.Constants.TIME_KEY;
import static com.mindfiresolutions.monthlybudget.Utilities.Constants.TITLE_KEY;
import static com.mindfiresolutions.monthlybudget.Utilities.Constants.TYPE_KEY;
import static com.mindfiresolutions.monthlybudget.Utilities.Constants.YEAR_KEY;

/**
 * Created by Vishal Prasad on 5/30/2017.
 * last modified on 6/1/2017
 * This File is for Handling Database Operations
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final String TAG = "SQLite log: ";
    private static DatabaseHandler sInstance;

    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "MonthManager";

    // table name
    private static final String MONTHS_TABLE = "Months";
    private static final String MONTH_ENTRY_TABLE = "MonthEntry";

    private SQLiteDatabase db;

    public static synchronized DatabaseHandler getInstance(Context context) {

        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx

        if (sInstance == null) {
            sInstance = new DatabaseHandler(context.getApplicationContext());
        }

        return sInstance;
    }

    /**
     * Constructor should be private to prevent direct instantiation.
     * make call to static method "getInstance()" instead.
     */
    private DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_MONTHS_TABLE = "CREATE TABLE " + MONTHS_TABLE + "(" + MONTH_KEY + " TEXT, "
                + YEAR_KEY + " TEXT, " + "PRIMARY KEY (" + MONTH_KEY + "," + YEAR_KEY + "))";
        LoggerUtility.makeLog("SQLite Log: ", CREATE_MONTHS_TABLE);
        db.execSQL(CREATE_MONTHS_TABLE);

        String CREATE_MONTH_ENTRY_TABLE = "CREATE TABLE " + MONTH_ENTRY_TABLE + "(" + DATE_KEY + " TEXT, " + MONTH_KEY + " TEXT, "
                + YEAR_KEY + " TEXT, " + TIME_KEY + " TEXT, " + TYPE_KEY + " TEXT, " + TITLE_KEY + " TEXT, " + AMOUNT_KEY + " TEXT " + ")";
        LoggerUtility.makeLog("SQLite Log: ", CREATE_MONTH_ENTRY_TABLE);
        db.execSQL(CREATE_MONTH_ENTRY_TABLE);

    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + MONTHS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + MONTH_ENTRY_TABLE);
        // Create tables again
        onCreate(db);
    }


    //********************************* MONTHS_TABLE Operations **********************************//

    /**
     * This Method inserts Month entry into the MONTHS_TABLE
     *
     * @param entry
     */
    public void addMonth(MonthYear entry) {
        db = this.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(MONTH_KEY, entry.getMonth()); // MonthYear Month
            values.put(YEAR_KEY, entry.getYear()); // MonthYear Year

            // Inserting Row
            db.insert(MONTHS_TABLE, null, values);
            // Closing database connection
        } finally {
            db.close();
        }
    }

    /**
     * this method checks the database whether an entry is present in databse or not.
     *
     * @param month : month of the year to be checked
     * @param year  : Year of which is being checked
     * @return
     */
    public boolean hasEntry(String month, String year) {

        db = this.getWritableDatabase();
        boolean isEntryExists = false;
        Cursor cursor = db.query(MONTHS_TABLE, null, null, null, null, null, null);
        try {
            if ((cursor != null)) {

                if (cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    do {
                        if ((cursor.getString(cursor.getColumnIndex(MONTH_KEY)).equals(month))
                                && (cursor.getString(cursor.getColumnIndex(YEAR_KEY)).equals(year))) {
                            isEntryExists = true;
                        }
                    }
                    while (cursor.moveToNext());

                }
                cursor.close();
            }
            return isEntryExists;
        } catch (SQLException e) {
            LoggerUtility.makeLog(TAG, e.getMessage());
            return false;
        } finally {
            db.close();
        }
    }

    /**
     * This method checks whether MONTHS_TABLE table is empty or not.
     *
     * @return
     */
    public boolean isDatabaseEmpty() {
        db = this.getWritableDatabase();
        Cursor cursor = db.query(MONTHS_TABLE, null, null, null, null, null, null);
        try {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                cursor.close();
                return false;

            } else
                return true;
        } finally {
            db.close();
        }

    }

    /**
     * this method returns the month list with year, which are added into the database
     *
     * @return
     */

    public ArrayList<HashMap<String, String>> getMonthsList() {
        ArrayList<HashMap<String, String>> monthsList = new ArrayList<>();

        db = this.getReadableDatabase();
        Cursor cursor = db.query(MONTHS_TABLE, null, null, null, null, null, null);
        try {
            if (cursor != null) {
                if (cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    do {

                        HashMap<String, String> monthEntry = new HashMap<>();
                        monthEntry.put(MONTH_KEY, cursor.getString(cursor.getColumnIndex(MONTH_KEY)));
                        monthEntry.put(YEAR_KEY, cursor.getString(cursor.getColumnIndex(YEAR_KEY)));

                        monthsList.add(monthEntry);

                    }
                    while (cursor.moveToNext());

                    return monthsList;
                }
                cursor.close();
            }
            return null;

        } finally {
            db.close();
        }
    }


    //******************************* MONTH_ENTRY_TABLE Operations ***************************//


    /**
     * This method adds credit/debit operation for that month into the database
     *
     * @param entry : is the object of MonthEntry Class which holds the data to be entered.
     */
    public void addMonthEntry(MonthEntry entry) {
        db = this.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(DATE_KEY, entry.getDate());
            values.put(MONTH_KEY, entry.getMonth());
            values.put(YEAR_KEY, entry.getYear());
            values.put(TIME_KEY, entry.getTime());
            values.put(TYPE_KEY, entry.getType());
            values.put(TITLE_KEY, entry.getTitle());
            values.put(AMOUNT_KEY, entry.getAmount());

            // Inserting Row
            db.insert(MONTH_ENTRY_TABLE, null, values);
        } finally {
            db.close(); // Closing database connection
        }
    }

    /**
     * this method returns the current date and time from the system
     *
     * @return Current date time string.
     */
    public String getCurrentDateTime() {
        return (java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime()));
    }


    /**
     * this method returns a arraylist including all the credit/debit operations for a month.
     *
     * @return
     */
    public ArrayList<HashMap<String, String>> getMonthEntryList() {
        ArrayList<HashMap<String, String>> monthsList = new ArrayList<>();

        db = this.getReadableDatabase();
        Cursor cursor = db.query(MONTH_ENTRY_TABLE, null, null, null, null, null, null);
        try {
            if (cursor != null) {
                if (cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    do {

                        HashMap<String, String> monthEntry = new HashMap<>();
                        monthEntry.put(DATE_KEY, cursor.getString(cursor.getColumnIndex(DATE_KEY)));
                        monthEntry.put(MONTH_KEY, cursor.getString(cursor.getColumnIndex(MONTH_KEY)));
                        monthEntry.put(YEAR_KEY, cursor.getString(cursor.getColumnIndex(YEAR_KEY)));
                        monthEntry.put(TIME_KEY, cursor.getString(cursor.getColumnIndex(TIME_KEY)));
                        monthEntry.put(TYPE_KEY, cursor.getString(cursor.getColumnIndex(TYPE_KEY)));
                        monthEntry.put(TITLE_KEY, cursor.getString(cursor.getColumnIndex(TITLE_KEY)));
                        monthEntry.put(AMOUNT_KEY, cursor.getString(cursor.getColumnIndex(AMOUNT_KEY)));

                        monthsList.add(monthEntry);
                    }
                    while (cursor.moveToNext());

                    return monthsList;
                }
                cursor.close();
            }
            return null;
        } finally {
            db.close();
        }
    }

    /**
     * method to return the list of Monthly Expense Opeartions, entered in the Database
     *
     * @param month :String Month Name
     * @param year  : String Year Name
     * @return
     */

    public ArrayList<HashMap<String, String>> getMonthExpenseEntryList(String month, String year) {
        ArrayList<HashMap<String, String>> monthsList = new ArrayList<>();

        db = this.getReadableDatabase();
        Cursor cursor = db.query(MONTH_ENTRY_TABLE, null, MONTH_KEY + "=? and " + YEAR_KEY + "=? and " + TYPE_KEY + " =?", new String[]{month, year, Constants.EXPENSE}, null, null, null);
        try {
            if (cursor != null) {
                if (cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    do {

                        HashMap<String, String> monthEntry = new HashMap<>();
                        monthEntry.put(DATE_KEY, cursor.getString(cursor.getColumnIndex(DATE_KEY)));
                        monthEntry.put(MONTH_KEY, cursor.getString(cursor.getColumnIndex(MONTH_KEY)));
                        monthEntry.put(YEAR_KEY, cursor.getString(cursor.getColumnIndex(YEAR_KEY)));
                        monthEntry.put(TIME_KEY, cursor.getString(cursor.getColumnIndex(TIME_KEY)));
                        monthEntry.put(TYPE_KEY, cursor.getString(cursor.getColumnIndex(TYPE_KEY)));
                        monthEntry.put(TITLE_KEY, cursor.getString(cursor.getColumnIndex(TITLE_KEY)));
                        monthEntry.put(AMOUNT_KEY, cursor.getString(cursor.getColumnIndex(AMOUNT_KEY)));

                        monthsList.add(monthEntry);
                    }
                    while (cursor.moveToNext());

                    return monthsList;
                }
                cursor.close();
            }
            return null;
        } finally {
            db.close();
        }
    }


    /**
     * method to return the list of Monthly Expense Opeartions, entered in the Database
     *
     * @param month : String Month Name
     * @param year  : String Year Name
     * @return
     */
    public ArrayList<HashMap<String, String>> getMonthIncomeEntryList(String month, String year) {
        ArrayList<HashMap<String, String>> monthsList = new ArrayList<>();

        db = this.getReadableDatabase();
        Cursor cursor = db.query(MONTH_ENTRY_TABLE, null, MONTH_KEY + "=? and " + YEAR_KEY + "=? and " + TYPE_KEY + " =?", new String[]{month, year, Constants.INCOME}, null, null, null);
        try {
            if (cursor != null) {
                if (cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    do {

                        HashMap<String, String> monthEntry = new HashMap<>();
                        monthEntry.put(DATE_KEY, cursor.getString(cursor.getColumnIndex(DATE_KEY)));
                        monthEntry.put(MONTH_KEY, cursor.getString(cursor.getColumnIndex(MONTH_KEY)));
                        monthEntry.put(YEAR_KEY, cursor.getString(cursor.getColumnIndex(YEAR_KEY)));
                        monthEntry.put(TIME_KEY, cursor.getString(cursor.getColumnIndex(TIME_KEY)));
                        monthEntry.put(TYPE_KEY, cursor.getString(cursor.getColumnIndex(TYPE_KEY)));
                        monthEntry.put(TITLE_KEY, cursor.getString(cursor.getColumnIndex(TITLE_KEY)));
                        monthEntry.put(AMOUNT_KEY, cursor.getString(cursor.getColumnIndex(AMOUNT_KEY)));

                        monthsList.add(monthEntry);
                    }
                    while (cursor.moveToNext());


                    db.close();
                    return monthsList;
                }
                cursor.close();
            }
            return null;
        } finally {
            db.close();
        }
    }


    /**
     * This Method Returns Total Expenses of a month.
     *
     * @param month : String Month Name
     * @param year  : Year Name
     * @return : Double Total Month Expenses
     */
    public Double getTotalMonthExpense(String month, String year) {
        ArrayList<HashMap<String, String>> monthsList = new ArrayList<>();

        db = this.getReadableDatabase();
        Cursor cursor = db.query(MONTH_ENTRY_TABLE, null, MONTH_KEY + "=? and " + YEAR_KEY + "=? and " + TYPE_KEY + " =?", new String[]{month, year, Constants.EXPENSE}, null, null, null);
        Double sum = 0.0;
        try {
            if ((cursor != null)) {
                if (cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    do {
                        sum = sum + Double.parseDouble(cursor.getString(cursor.getColumnIndex(AMOUNT_KEY)));
                    }
                    while (cursor.moveToNext());

                }
                cursor.close();
            }
            return sum;
        } finally {
            db.close();
        }
    }

    /**
     * method to return total Income of a month
     *
     * @param month :String Month Name
     * @param year  :String Year Name
     * @return Double Total Income
     */
    public Double getTotalMonthIncome(String month, String year) {
        ArrayList<HashMap<String, String>> monthsList = new ArrayList<>();

        db = this.getReadableDatabase();
        Cursor cursor = db.query(MONTH_ENTRY_TABLE, null, MONTH_KEY + "=? and " + YEAR_KEY + "=? and " + TYPE_KEY + " =?", new String[]{month, year, Constants.INCOME}, null, null, null);
        Double sum = 0.0;
        try {
            if (cursor != null) {
                if (cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    do {
                        sum = sum + Double.parseDouble(cursor.getString(cursor.getColumnIndex(AMOUNT_KEY)));
                    }
                    while (cursor.moveToNext());
                }
                cursor.close();
            }
            return (sum);
        } finally {
            db.close();
        }
    }
}
