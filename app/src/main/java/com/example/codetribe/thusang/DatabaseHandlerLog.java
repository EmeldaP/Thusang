package com.example.codetribe.thusang;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.view.View;
import android.widget.Toast;

/**
 * Created by codetribe on 2017/09/28.
 */

public class DatabaseHandlerLog extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Community";
    public static final String TABLE_NAME = "crime";


    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_ADDRESS= "address";
    public static final String COLUMN_PASSWORD = "password";



    public DatabaseHandlerLog(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    //create column into table
    public void onCreate(SQLiteDatabase sl) {
        String CREATE_CODE_TABLE = "CREATE TABLE " +
                TABLE_NAME + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_NAME + " TEXT,"
                + COLUMN_ADDRESS + " TEXT,"
                + COLUMN_EMAIL + " TEXT,"
                + COLUMN_PASSWORD + " TEXT" + ")";
        //pass SQL () object to create table with with column
        sl.execSQL(CREATE_CODE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sl, int oldVersion, int newVersion) {
        sl.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sl);
    }

    public void addMember(LogIn logIn){
        ContentValues values = new ContentValues();
        values.put(COLUMN_ADDRESS,logIn.getAddress());
        values.put(COLUMN_NAME,logIn.getAddress());
        values.put(COLUMN_EMAIL,logIn.getEmail());
        values.put(COLUMN_PASSWORD,logIn.getNotes());

        SQLiteDatabase sl = this.getWritableDatabase();
        sl.insert(TABLE_NAME, null, values);
        sl.close();
    }

   public boolean delete(String email) {

       boolean result = false;
       String query = " Select * FROM " + TABLE_NAME + " WHERE " + COLUMN_EMAIL +
               " = \"" + email + "\"";

       SQLiteDatabase sl = this.getWritableDatabase();
       Cursor cursor = sl.rawQuery(query, null);
       LogIn logIn = new LogIn();

       if (cursor.moveToFirst()) {
           logIn.setEmail(cursor.getString(0));
           sl.delete(TABLE_NAME, COLUMN_EMAIL + " = ?",
                   new String[]
                           {String.valueOf(logIn.getEmail())});
           cursor.close();
           result = true;
       }
       sl.close();
       return result;
        }
    public void updateMember(LogIn logIn) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, logIn.getName());
        values.put(COLUMN_EMAIL, logIn.getEmail());
        //values.put(COLUMN_PASSWORD, logIn.getPassword());
        values.put(COLUMN_ADDRESS, logIn.getAddress());
        values.put(COLUMN_PASSWORD,logIn.password);

        //reference SQLiteDAtabase to writable db
        SQLiteDatabase sl = this.getWritableDatabase();
        sl.update(TABLE_NAME, values, COLUMN_EMAIL + "=?", new String[]{
                String.valueOf(logIn.getEmail())});
        sl.close();
        //Toast.makeText(this,"Update was sucessful",Toast.LENGTH_SHORT).show();
    }
}

