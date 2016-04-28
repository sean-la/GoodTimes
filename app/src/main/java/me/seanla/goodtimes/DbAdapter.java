package me.seanla.goodtimes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbAdapter {

    private DbHelper mDbHelper;
    private SQLiteDatabase mDb;

    private final Context mCtx;

    public static final String KEY_ROWID = "_id";
    public static final String KEY_DATE = "date";
    public static final String KEY_EVENT = "event";
    public static final String SQLITE_TABLE = "GoodTimes";

    private class DbHelper extends SQLiteOpenHelper {
        // If you change the database schema, you must increment the database version
        public static final int DATABASE_VERSION = 1;
        public static final String DATABASE_NAME = "GoodTimes.db";


        public DbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        public void onCreate(SQLiteDatabase db) {
            String SQL_CREATE_ENTRIES = "CREATE TABLE " + SQLITE_TABLE + "(" +
                    KEY_ROWID + " INTEGER PRIMARY KEY," +
                    KEY_DATE + " DATE NOT NULL," +
                    KEY_EVENT + " VARCHAR NOT NULL" +
                    ");";
            db.execSQL(SQL_CREATE_ENTRIES);
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            if (oldVersion != newVersion) {
                db.execSQL("DROP TABLE IF EXISTS " + SQLITE_TABLE);
                onCreate(db);
            }
        }

    }

    public DbAdapter(Context context) {
        this.mCtx = context;
    }

    public DbAdapter openForWriting() throws SQLException {
        mDbHelper = new DbHelper(mCtx);
        mDb = this.mDbHelper.getWritableDatabase();
        return this;
    }

    public DbAdapter openForReading() throws SQLException {
        mDbHelper = new DbHelper(mCtx);
        mDb = this.mDbHelper.getReadableDatabase();
        return this;
    }

    public void close() {
        if (mDbHelper != null) {
            mDbHelper.close();
        }
    }

    public long insertGoodTime(String date, String goodTime) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_DATE, date);
        initialValues.put(KEY_EVENT, goodTime);
        return mDb.insert(SQLITE_TABLE, null, initialValues);
    }

    public void deleteGoodTime(String id) {
        try {
            mDb.delete(SQLITE_TABLE, KEY_ROWID + "=" + id, null);
        } catch (Exception e) {
            // Put logging system?
        }
    }

    public String getRandomGoodTime() {
        String goodTime;

        Cursor numCursor = mDb.rawQuery("SELECT count(*) FROM GoodTimes", null);
        numCursor.moveToFirst();
        int numPosts = numCursor.getInt(0);

        if (numPosts > 0) {
            Cursor cursor = mDb.rawQuery("SELECT * FROM GoodTimes ORDER BY RANDOM() LIMIT 1", null);
            cursor.moveToFirst();

            String date = cursor.getString(cursor.getColumnIndexOrThrow(KEY_DATE));
            String description = cursor.getString(cursor.getColumnIndexOrThrow(KEY_EVENT));

            cursor.close();

            goodTime = "A good time from " + date + ":\n" + description;

        } else {
            goodTime = "There doesn't seem to be any good times... yet!\nAdd one by clicking the button below!";
        }

        numCursor.close();
        return goodTime;
    }

    public Cursor fetchAllGoodTimes() {
        Cursor mCursor = mDb.query(SQLITE_TABLE,new String[] {KEY_ROWID, KEY_DATE, KEY_EVENT},
                null, null, null, null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }

        return mCursor;
    }


}