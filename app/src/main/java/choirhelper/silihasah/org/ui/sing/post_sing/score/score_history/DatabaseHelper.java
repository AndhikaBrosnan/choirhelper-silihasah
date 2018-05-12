package choirhelper.silihasah.org.ui.sing.post_sing.score.score_history;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import choirhelper.silihasah.org.data.Song;

import static android.provider.BaseColumns._ID;

public class DatabaseHelper extends SQLiteOpenHelper {

    //DATABASE CONTRACTS
    public static final String TABLE_NAME = "score_list";
    public static final String COLUMN_SONG = "score_song";
    public static final String COLUMN_VOICETYPE = "score_voicetype";
    public static final String COLUMN_SCORE = "score_value";

    // Database Version
    private static final int DATABASE_VERSION = 6;
    // Database Name
    private static final String DATABASE_NAME = "ScoreListsManager.db";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_SCORE_TABLE = "CREATE TABLE " + TABLE_NAME + " ("+
                _ID + " INTEGER NOT NULL, " +
                COLUMN_SONG + " TEXT NOT NULL, " +
                COLUMN_VOICETYPE + " TEXT NOT NULL, " +
                COLUMN_SCORE + " TEXT NOT NULL " +
                "); ";
        db.execSQL(SQL_SCORE_TABLE);

    }

    //drop Todo table
    private String DROP_SONGS_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_SONGS_TABLE);

        // Create tables again
        onCreate(db);
    }

    public void addTodo (Song song){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(_ID,song.getSongId());
        values.put(COLUMN_SONG,song.getmTitle());
        values.put(COLUMN_VOICETYPE,song.getVoiceType());
        values.put(COLUMN_SCORE,song.getScore());

        db.insert(TABLE_NAME,null,values);
        db.close();
    }

    public List<Song> getAllScore() {
        // array of columns to fetch
        String[] columns = {
                _ID ,
                COLUMN_SONG ,
                COLUMN_VOICETYPE ,
                COLUMN_SCORE
        };
        // sorting orders
        String sortOrder =
                COLUMN_SCORE + " ASC";
        List<Song> songs = new ArrayList<Song>();

        SQLiteDatabase db = this.getReadableDatabase();


        Cursor cursor = db.query(TABLE_NAME, //Table to query
                columns,    //columns to return
                null,        //columns for the WHERE clause
                null,        //The values for the WHERE clause
                null,       //group the rows
                null,       //filter by row groups
                sortOrder); //The sort order


        // Traversing through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Song song = new Song();
                song.setSongId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(_ID))));
                song.setmTitle(cursor.getString(cursor.getColumnIndex(COLUMN_SONG)));
                song.setVoiceType(cursor.getString(cursor.getColumnIndex(COLUMN_VOICETYPE)));
                song.setScore(cursor.getString(cursor.getColumnIndex(COLUMN_SCORE)));
                // Adding user record to list
                songs.add(song);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        // return user list
        return songs;
    }
}
