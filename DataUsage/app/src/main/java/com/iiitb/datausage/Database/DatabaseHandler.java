package com.iiitb.datausage.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.util.Log;

import com.iiitb.datausage.Model.DatabaseModel;

/**
 * Created by Bala Manoj on 21-10-2016.
 */
public class DatabaseHandler extends SQLiteOpenHelper
{
    String TAG = "DatabaseHandler";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "data_usage";
    private static final String TABLE_NAME = "apps";

    // Apps Table Column names
    private static final String KEY_UID = "uid";
    private static final String KEY_TX = "tx";
    private static final String KEY_RX = "rx";
    private static final String KEY_NAME = "name";

    SQLiteDatabase db = null;

    public DatabaseHandler(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        db = getWritableDatabase();
    }

    //Creating Table
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String CREATE_APPS_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + KEY_UID + " INTEGER PRIMARY KEY," + KEY_TX + " INTEGER," + KEY_RX + " INTEGER,"
                + KEY_NAME + " TEXT"
                + ")";
        try
        {
            db.execSQL(CREATE_APPS_TABLE);
            //db.close();
            Log.d(TAG, "onCreate(): Table Creation Success!");
        }
        catch(SQLiteException e)
        {
            Log.d(TAG, "onCreate(): Table Creation Failed!");
        }

    }

    //Upgrading Database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME); //Drop older table if exists
        onCreate(db); //create the table again
        //db.close();
    }

    //Add your CRUD operations from here

    //Insert record into table
    public boolean insertAppData(DatabaseModel app)
    {
        boolean status = false;
        long rowID = 0;

        if(db == null)
        {
            Log.d(TAG, "insertAppData(): SQLiteDatabase instance is null");
            return false;
        }

        try
        {
            ContentValues contentValues = new ContentValues();
            contentValues.put("uid", app.getUid());
            contentValues.put("tx", app.getTx());
            contentValues.put("rx", app.getRx());
            contentValues.put("name", app.getName());

            rowID = db.insert(TABLE_NAME, null, contentValues);
            if(rowID == -1)
            {
                status = false;
                Log.d(TAG, "insertAppData(): Record Insertion Failed DB returned -1");
            }
            else
            {
                status = true;
                //Log.d(TAG, "insertAppData(): Record Inserted");
            }

            //db.close();
        }
        catch (SQLiteException e)
        {
            Log.e(TAG, "storeAppData(): " + e.toString());
        }

        return status;
    }

    //Update record in table
    public boolean updateAppData(DatabaseModel app)
    {
        boolean status = false;
        long rowsAffected = 0;

        if(db == null)
        {
            Log.d(TAG, "updateAppData(): SQLiteDatabase instance is null");
            return false;
        }

        try
        {
            ContentValues contentValues = new ContentValues();
            contentValues.put("uid", app.getUid());
            contentValues.put("tx", app.getTx());
            contentValues.put("rx", app.getRx());
            contentValues.put("name", app.getName());

            rowsAffected = db.update(TABLE_NAME, contentValues, KEY_UID + " =?", new String[] { String.valueOf(app.getUid()) } );
            if(rowsAffected > 0)
            {
                //Log.d(TAG, "insertAppData(): Rows update count: " + rowsAffected);
                status = true;
            }
            else if(rowsAffected == 0)
            {
                //Log.d(TAG, "insertAppData(): 0 rows updated");
                status = true;
            }
            else
            {
                Log.d(TAG, "insertAppData(): Update failed");
            }
            //db.close();
        }
        catch(SQLiteException e)
        {
            Log.e(TAG, "updateAppData(): update failed!");
        }

        return status;
    }

    //display the table
    public void retrieveAppsData()
    {
        String SELECT_QUERY = "SELECT * FROM " + TABLE_NAME;

        if(db == null)
        {
            Log.d(TAG, "retrieveAppsData(): SQLiteDatabase instance is null");
            return ;
        }

        try
        {
            Cursor cursor = db.rawQuery(SELECT_QUERY, null);

            if(cursor.moveToFirst())
            {
                //System.out.println("UID \t\t\t TX \t\t\t RX \t\t\t Name");
                do
                {
                    /*System.out.println(cursor.getInt(0) + " \t\t\t " +
                            cursor.getLong(1) + " \t\t\t " +
                            cursor.getLong(2) + " \t\t\t " +
                            cursor.getString(3));*/
                }while(cursor.moveToNext());
            }

            cursor.close();
            //db.close();
        }
        catch(SQLiteException e)
        {
            Log.d(TAG, "retrieveAppData(): Failed in retrieving records!");
        }
    }

    //Retrieve app Data
    public DatabaseModel retrieveAppData(int uid)
    {
        DatabaseModel app = null;

        if(db == null)
        {
            Log.d(TAG, "retrieveAppData(): SQLiteDatabase instance is null");
            return null;
        }

        try
        {
            Cursor cursor = db.query(TABLE_NAME, new String[] { KEY_UID, KEY_TX, KEY_RX, KEY_NAME }, KEY_UID + " =?", new String[] { String.valueOf(uid) }, null, null, null);

            if(cursor != null)
            {
                if(cursor.moveToFirst())
                {
                    app = new DatabaseModel();
                    app.setUid(cursor.getInt(0));
                    app.setTx(cursor.getLong(1));
                    app.setRx(cursor.getLong(2));
                    app.setName(cursor.getString(3));

                }
            }
            else
            {

                Log.d(TAG, "retrieveAppData(): Cursor is null");
            }

            cursor.close();

            //db.close();
        }
        catch(SQLiteException e)
        {
            Log.d(TAG, "retrieveAppData(): Failed to retrieve the app data with uid: " + uid);
        }

        return app;
    }

    //delete a row from the table
    public boolean deleteAppData(DatabaseModel app)
    {
        boolean status = false;

        try
        {
            SQLiteDatabase db = getWritableDatabase();
            int rowsAffected = db.delete(TABLE_NAME, KEY_UID + " =?", new String[]{ String.valueOf(app.getUid() )} );
            //db.close();

            if(rowsAffected == 0)
            {
                Log.d(TAG, "deleteAppData(): 0 rows affected!");
            }
            else
            {
                Log.d(TAG, "deleteAppData(): rows affected count: " + rowsAffected);
            }

            status = true;
        }
        catch(SQLiteException e)
        {
            Log.d(TAG, "deleteAppData() Failed to delete an App data");
        }

        return status;
    }

    //delete table
    public boolean deleteAppsData()
    {
        boolean status = false;

        try
        {
            SQLiteDatabase db = getWritableDatabase();
            int rowsAffected  = db.delete(TABLE_NAME, "1", null);

            //db.close();
            Log.d(TAG, "deleteAppsData(): Rows Affected after table delete: " + rowsAffected);
        }
        catch(SQLiteException e)
        {
            Log.d(TAG, "deleteAppsData(): " + e.toString());
        }

        return status;
    }

    //closing the database instance
    public void close()
    {
        if(db != null)
        {
            db.close();
            Log.d("Service", "close(): closed the SQLiteDatabase");
        }
        else
        {
            Log.d("Service", "close(): Couldn't close the SQLiteDatabase since it was null");
        }
    }
}
