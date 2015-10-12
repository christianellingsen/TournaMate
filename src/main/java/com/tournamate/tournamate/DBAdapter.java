// ------------------------------------ DBADapter.java ---------------------------------------------

// TODO: Change the package to match your project.
package com.tournamate.tournamate;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


// TO USE:
// Change the package (at top) to match your project.
// Search for "TODO", and make the appropriate changes.
public class DBAdapter {

    /////////////////////////////////////////////////////////////////////
    //	Constants & Data
    /////////////////////////////////////////////////////////////////////
    // For logging:
    private static final String TAG = "DBAdapter";

    // DB Fields
    public static final String KEY_ROWID = "_id";
    /*
     * CHANGE 1:
     */
    // Setup your fields here:
    // Tournaments fields
    public static final String KEY_TOURNAMENTS_NAME = "name";
    public static final String KEY_TOURNAMENTS_TYPE = "type";
    public static final String KEY_TOURNAMENTS_DONE = "isDone";
    public static final String KEY_TOURNAMENTS_ACTIVEMATCH = "activeMatch";
    public static final String KEY_TOURNAMENTS_MATCHESPLAYED = "matchesPlayed";
    public static final String KEY_TOURNAMENTS_ONLINE = "isOnline";
    public static final String KEY_TOURNAMENTS_PARSE= "parseID";
    public static final String KEY_TOURNAMENTS_DATE = "date";
    // Matches fields
    public static final String KEY_MATCHES_TOURNAMENTID = "tournamentID";
    public static final String KEY_MATCHES_TEAM1ID = "team1ID";
    public static final String KEY_MATCHES_TEAM2ID = "team2ID";
    public static final String KEY_MATCHES_TEAM1SCORE = "team1Score";
    public static final String KEY_MATCHES_TEAM2SCORE = "team2Score";
    public static final String KEY_MATCHES_NUMBER = "matchNumber";
    public static final String KEY_MATCHES_PLAYED = "isPlayed";
    // Teams fields
    public static final String KEY_TEAMS_NAME = "name";
    public static final String KEY_TEAMS_WON = "won";
    public static final String KEY_TEAMS_LOST = "lost";
    public static final String KEY_TEAMS_DRAW = "draw";
    public static final String KEY_TEAMS_SCORE = "score";
    public static final String KEY_TEAMS_PLAYED = "played";


    public static final String[] ALL_KEYS_TOURNAMENTS = new String[] {KEY_ROWID, KEY_TOURNAMENTS_NAME, KEY_TOURNAMENTS_TYPE, KEY_TOURNAMENTS_DONE, KEY_TOURNAMENTS_ACTIVEMATCH,
                                                                      KEY_TOURNAMENTS_MATCHESPLAYED, KEY_TOURNAMENTS_ONLINE,KEY_TOURNAMENTS_PARSE,KEY_TOURNAMENTS_DATE};
    public static final String[] ALL_KEYS_MATCHES = new String[] {KEY_ROWID, KEY_MATCHES_TOURNAMENTID, KEY_MATCHES_TEAM1ID, KEY_MATCHES_TEAM2ID,KEY_MATCHES_TEAM1SCORE,KEY_MATCHES_TEAM2SCORE,
                                                                  KEY_MATCHES_NUMBER, KEY_MATCHES_PLAYED};
    public static final String[] ALL_KEYS_TEAMS = new String[] {KEY_ROWID, KEY_TEAMS_NAME, KEY_TEAMS_WON, KEY_TEAMS_LOST, KEY_TEAMS_DRAW,KEY_TEAMS_SCORE, KEY_TEAMS_PLAYED};

    // DB info: it's name, and the table we are using (just one).
    public static final String DATABASE_NAME = "TournMateDB";
    public static final String DATABASE_TABLE_TOURNAMENTS = MyApplication.DATABASE_TABLE_TOURNAMENTS;
    public static final String DATABASE_TABLE_MATCHES = MyApplication.DATABASE_TABLE_MATCHES;
    public static final String DATABASE_TABLE_TEAMS = MyApplication.DATABASE_TABLE_TEAMS;
    // Track DB version if a new version of your app changes the format.
    public static final int DATABASE_VERSION = 11;

    private static final String DATABASE_TOURNAMENTS_CREATE_SQL =
            "create table " + DATABASE_TABLE_TOURNAMENTS
                    + " (" + KEY_ROWID + " integer primary key autoincrement, "

                    + KEY_TOURNAMENTS_NAME + " text not null, "
                    + KEY_TOURNAMENTS_TYPE + " text not null, "
                    + KEY_TOURNAMENTS_DONE + " text not null, "
                    + KEY_TOURNAMENTS_ACTIVEMATCH + " integer not null, "
                    + KEY_TOURNAMENTS_MATCHESPLAYED + " integer not null, "
                    + KEY_TOURNAMENTS_ONLINE + " text not null, "
                    + KEY_TOURNAMENTS_PARSE + " text not null, "
                    + KEY_TOURNAMENTS_DATE + " text not null"

                    + ");";

    private static final String DATABASE_TEAMS_CREATE_SQL =
            "create table " + DATABASE_TABLE_TEAMS
                    + " (" + KEY_ROWID + " integer primary key autoincrement, "

                    + KEY_TEAMS_NAME + " text not null, "
                    + KEY_TEAMS_WON + " integer not null, "
                    + KEY_TEAMS_LOST + " integer not null, "
                    + KEY_TEAMS_DRAW + " integer not null, "
                    + KEY_TEAMS_SCORE + " integer not null, "
                    + KEY_TEAMS_PLAYED + " integer not null"

                    + ");";

    private static final String DATABASE_MATCHES_CREATE_SQL =
            "create table " + DATABASE_TABLE_MATCHES
                    + " (" + KEY_ROWID + " integer primary key autoincrement, "

                    + KEY_MATCHES_TOURNAMENTID + " integer references "
                    + DATABASE_TABLE_TOURNAMENTS+"("+KEY_ROWID+"), "
                    + KEY_MATCHES_TEAM1ID + " integer references "
                    + DATABASE_TABLE_TEAMS+"("+KEY_ROWID+"), "
                    + KEY_MATCHES_TEAM2ID + " integer references "
                    + DATABASE_TABLE_TEAMS+"("+KEY_ROWID+"), "
                    + KEY_MATCHES_TEAM1SCORE + " integer not null, "
                    + KEY_MATCHES_TEAM2SCORE + " integer not null, "
                    + KEY_MATCHES_NUMBER + " integer not null, "
                    + KEY_MATCHES_PLAYED + " text not null"

                    + ");";



    // Context of application who uses us.
    private final Context context;

    private DatabaseHelper myDBHelper;
    private SQLiteDatabase db;

    /////////////////////////////////////////////////////////////////////
    //	Public methods:
    /////////////////////////////////////////////////////////////////////

    public DBAdapter(Context ctx) {
        this.context = ctx;
        myDBHelper = new DatabaseHelper(context);
    }

    // Open the database connection.
    public DBAdapter open() {
        db = myDBHelper.getWritableDatabase();
        return this;
    }

    // Close the database connection.
    public void close() {
        myDBHelper.close();
    }

    // Add a new set of values to the database.
    public long insertRowTournament(String name, String type, int activeMatch, int matchesPlayed, String isDone, String isOnline,String parseID, String date) {
		/*
		 * CHANGE 3:
		 */
        // TODO: Update data in the row with new fields.
        // TODO: Also change the function's arguments to be what you need!
        // Create row's data:
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_TOURNAMENTS_NAME, name);
        initialValues.put(KEY_TOURNAMENTS_TYPE, type);
        initialValues.put(KEY_TOURNAMENTS_ACTIVEMATCH, activeMatch);
        initialValues.put(KEY_TOURNAMENTS_MATCHESPLAYED, matchesPlayed);
        initialValues.put(KEY_TOURNAMENTS_DONE, isDone);
        initialValues.put(KEY_TOURNAMENTS_ONLINE, isOnline);
        initialValues.put(KEY_TOURNAMENTS_PARSE, parseID);
        initialValues.put(KEY_TOURNAMENTS_DATE, date);

        Log.d("DB","Inserted into tournament table");

        // Insert it into the database.
        return db.insert(DATABASE_TABLE_TOURNAMENTS, null, initialValues);
    }

    // Add a new set of values to the database.
    public long insertRowTeams(String name, int won, int lost, int draw, int score, int played) {
		/*
		 * CHANGE 3:
		 */
        // TODO: Update data in the row with new fields.
        // TODO: Also change the function's arguments to be what you need!
        // Create row's data:
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_TEAMS_NAME, name);
        initialValues.put(KEY_TEAMS_WON, won);
        initialValues.put(KEY_TEAMS_LOST, lost);
        initialValues.put(KEY_TEAMS_DRAW, draw);
        initialValues.put(KEY_TEAMS_SCORE, score);
        initialValues.put(KEY_TEAMS_PLAYED, played);

        // Insert it into the database.
        return db.insert(DATABASE_TABLE_TEAMS, null, initialValues);
    }

    // Add a new set of values to the database.
    public long insertRowMatches(String isPlayed, int tournament_id, int t1ID, int t2ID,int t1score, int t2score, int matchNumber) {
		/*
		 * CHANGE 3:
		 */
        // TODO: Update data in the row with new fields.
        // TODO: Also change the function's arguments to be what you need!
        // Create row's data:
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_MATCHES_PLAYED, isPlayed);
        initialValues.put(KEY_MATCHES_TOURNAMENTID, tournament_id);
        initialValues.put(KEY_MATCHES_TEAM1ID, t1ID);
        initialValues.put(KEY_MATCHES_TEAM2ID, t2ID);
        initialValues.put(KEY_MATCHES_TEAM1SCORE,t1score);
        initialValues.put(KEY_MATCHES_TEAM2SCORE,t2score);
        initialValues.put(KEY_MATCHES_NUMBER, matchNumber);

        // Insert it into the database.
        return db.insert(DATABASE_TABLE_MATCHES, null, initialValues);
    }

    // Delete a row from the database, by rowId (primary key)
    public boolean deleteRow(long rowId) {
        String where = KEY_ROWID + "=" + rowId;
        return db.delete(DATABASE_TABLE_TOURNAMENTS, where, null) != 0;
    }

    public void deleteAll(String table) {
        Cursor c = getAllRows(table);
        long rowId = c.getColumnIndexOrThrow(KEY_ROWID);
        if (c.moveToFirst()) {
            do {
                deleteRow(c.getLong((int) rowId));
            } while (c.moveToNext());
        }
        c.close();
    }

    // Return all data in the database.
    public Cursor getAllRows(String table) {
        String where = null;
        if(table==DATABASE_TABLE_TOURNAMENTS) {
            Cursor c = db.query(true, DATABASE_TABLE_TOURNAMENTS, ALL_KEYS_TOURNAMENTS,
                    where, null, null, null, null, null);
            if (c != null) {
                c.moveToFirst();
            }
            return c;
        }
        else if(table==DATABASE_TABLE_TEAMS){
            Cursor c = db.query(true, DATABASE_TABLE_TEAMS, ALL_KEYS_TEAMS,
                    where, null, null, null, null, null);
            if (c != null) {
                c.moveToFirst();
            }
            return c;
        }
        else if (table==DATABASE_TABLE_MATCHES){
            Cursor c = db.query(true, DATABASE_TABLE_MATCHES, ALL_KEYS_MATCHES,
                    where, null, null, null, null, null);
            if (c != null) {
                c.moveToFirst();
            }
            return c;
        }
        else{return null;}
            }

    // Get a specific row (by rowId)
    public Cursor getRow(String table, long rowId) {
        String where = KEY_ROWID + "=" + rowId;
        String[] keys = null;
        if (table==DATABASE_TABLE_TOURNAMENTS){keys=ALL_KEYS_TOURNAMENTS;}
        else if (table==DATABASE_TABLE_TEAMS){keys=ALL_KEYS_TEAMS;}
        else if (table==DATABASE_TABLE_MATCHES){keys=ALL_KEYS_MATCHES;}

        Cursor c = 	db.query(true, table, keys,
                where, null, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    // Get a specific row (by rowId)
    public Cursor getMatches(String table, long rowId) {
        String where = KEY_MATCHES_TOURNAMENTID + "=" + rowId;
        String[] keys = null;
        if (table==DATABASE_TABLE_TOURNAMENTS){keys=ALL_KEYS_TOURNAMENTS;}
        else if (table==DATABASE_TABLE_TEAMS){keys=ALL_KEYS_TEAMS;}
        else if (table==DATABASE_TABLE_MATCHES){keys=ALL_KEYS_MATCHES;}

        Cursor c = 	db.query(true, table, keys,
                where, null, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    public Cursor getTeam(String table, long rowId) {
        String where = KEY_ROWID + "=" + rowId;
        String[] keys = null;
        if (table==DATABASE_TABLE_TOURNAMENTS){keys=ALL_KEYS_TOURNAMENTS;}
        else if (table==DATABASE_TABLE_TEAMS){keys=ALL_KEYS_TEAMS;}
        else if (table==DATABASE_TABLE_MATCHES){keys=ALL_KEYS_MATCHES;}

        Cursor c = 	db.query(true, table, keys,
                where, null, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }


    // Change an existing row to be equal to new data.
    public boolean updateRowTournament(long rowId, String name, String type, int activeMatch, int matchesPlayed, String isDone, String isOnline, String parseID, String date) {
        String where = KEY_ROWID + "=" + rowId;

        // Create row's data:
        ContentValues newValues = new ContentValues();
        newValues.put(KEY_TOURNAMENTS_NAME, name);
        newValues.put(KEY_TOURNAMENTS_TYPE, type);
        newValues.put(KEY_TOURNAMENTS_ACTIVEMATCH, activeMatch);
        newValues.put(KEY_TOURNAMENTS_MATCHESPLAYED, matchesPlayed);
        newValues.put(KEY_TOURNAMENTS_DONE, isDone);
        newValues.put(KEY_TOURNAMENTS_ONLINE, isOnline);
        newValues.put(KEY_TOURNAMENTS_PARSE,parseID);
        newValues.put(KEY_TOURNAMENTS_DATE, date);

        // Insert it into the database.
        return db.update(DATABASE_TABLE_TOURNAMENTS, newValues, where, null) != 0;
    }

    // Change an existing row to be equal to new data.
    public boolean updateRowTeam(long rowId, String name, int won, int lost, int draw, int score, int played) {
        String where = KEY_ROWID + "=" + rowId;

        // Create row's data:
        ContentValues newValues = new ContentValues();
        newValues.put(KEY_TEAMS_NAME, name);
        newValues.put(KEY_TEAMS_WON, won);
        newValues.put(KEY_TEAMS_LOST, lost);
        newValues.put(KEY_TEAMS_DRAW, draw);
        newValues.put(KEY_TEAMS_SCORE, score);
        newValues.put(KEY_TEAMS_PLAYED, played);

        // Insert it into the database.
        return db.update(DATABASE_TABLE_TEAMS, newValues, where, null) != 0;
    }

    // Change an existing row to be equal to new data.
    public boolean updateRowMatch(long rowId, String isPlayed, int tournament_id, int t1ID, int t2ID,int t1score, int t2score, int matchNumber) {
        String where = KEY_ROWID + "=" + rowId;

        // Create row's data:
        ContentValues newValues = new ContentValues();
        newValues.put(KEY_MATCHES_PLAYED, isPlayed);
        newValues.put(KEY_MATCHES_TOURNAMENTID, tournament_id);
        newValues.put(KEY_MATCHES_TEAM1ID, t1ID);
        newValues.put(KEY_MATCHES_TEAM2ID, t2ID);
        newValues.put(KEY_MATCHES_TEAM1SCORE, t1score);
        newValues.put(KEY_MATCHES_TEAM2SCORE, t2score);
        newValues.put(KEY_MATCHES_NUMBER, matchNumber);

        // Insert it into the database.
        return db.update(DATABASE_TABLE_MATCHES, newValues, where, null) != 0;
    }


    /////////////////////////////////////////////////////////////////////
    //	Private Helper Classes:
    /////////////////////////////////////////////////////////////////////

    /**
     * Private class which handles database creation and upgrading.
     * Used to handle low-level database access.
     */
    private static class DatabaseHelper extends SQLiteOpenHelper
    {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase _db) {
            _db.execSQL(DATABASE_TOURNAMENTS_CREATE_SQL);
            _db.execSQL(DATABASE_TEAMS_CREATE_SQL);
            _db.execSQL(DATABASE_MATCHES_CREATE_SQL);
            Log.d("DB","Databases created");
        }

        @Override
        public void onUpgrade(SQLiteDatabase _db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading application's database from version " + oldVersion
                    + " to " + newVersion + ", which will destroy all old data!");

            // Destroy old database:
            _db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_TOURNAMENTS);
            _db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_TEAMS);
            _db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_MATCHES);

            // Recreate new database:
            onCreate(_db);
        }
    }
}
