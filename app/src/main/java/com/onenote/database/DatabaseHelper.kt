package com.onenote.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.onenote.model.Track
import com.onenote.model.Trackpoint


class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null,DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "gpstracker"
        private const val DATABASE_TABLE_NAME = "tracks"
        private const val DATABASE_VERSION = 1

        //Database table column names
        private const val KEY_ID = "id"
        private const val KEY_TITLE = "title"
        private const val KEY_STARTTIMESTAMP = "starttimestamp"
        private const val KEY_STOPTIMESTAMP = "stoptimestamp"
        private const val KEY_GPSPOINTS = "gpspoints"

        //Database cursor array
        private val CURSOR_ARRAY = arrayOf(
            KEY_ID, KEY_TITLE, KEY_STARTTIMESTAMP, KEY_STOPTIMESTAMP, KEY_GPSPOINTS
        )

        // DataBase create table statement
        private const val CREATE_TABLE = ("""CREATE TABLE $DATABASE_TABLE_NAME(
                $KEY_ID INTEGER PRIMARY KEY, 
                $KEY_TITLE TEXT, 
                $KEY_STARTTIMESTAMP INT,
                $KEY_STOPTIMESTAMP INT,
                $KEY_GPSPOINTS TEXT
                )""")

        // Select all Statement
        private const val SELECT_ALL = "SELECT * FROM $DATABASE_TABLE_NAME"

        // DataBase create table statement
        private const val DROP_TABLE = "DROP TABLE IF EXISTS $DATABASE_TABLE_NAME"
    }

    //INsertmethode
    fun insertNote(track: Track) : Long {
        return writableDatabase.insert(DATABASE_TABLE_NAME, null, trackToContentValues(track))
    }

    private fun trackToContentValues(track: Track) : ContentValues {
        val values = ContentValues()


        values.put(KEY_TITLE, track.title)
        values.put(KEY_STARTTIMESTAMP, track.starttimestamp)
        values.put(KEY_STOPTIMESTAMP, track.stoptimestamp)
        values.put(KEY_GPSPOINTS, serializeMe(track.GPSPoints))


        return values
    }

    private fun serializeMe(trackpoints : List<Trackpoint>): String {
        //TODO: Methode zum serialisieren ausprogrammieren
        return "abc"
    }

    private fun deserializeMe(serialstring : String): List<Trackpoint> {
        //TODO: Methode zum deserialiseren ausprogrammieren
        val point = Trackpoint(234242342,30.32,23.2)
        val points = ArrayList<Trackpoint>()
        points.add(point)
        return points
    }

    fun getAllTracks(): List<Track> {
        val tracks = ArrayList<Track>()
        val cursor = readableDatabase.rawQuery(SELECT_ALL, null)
        cursor.moveToFirst().run {
            do {
                cursorToTrack(cursor)?.let { tracks.add(it) }
            } while (cursor.moveToNext())
        }
        readableDatabase.close()

        return tracks
    }

    //einen einzelnen Track aus der DB holen
    fun getTrack(id: Long): Track? {
        val track: Track?
        val cursor = readableDatabase.query(
            DATABASE_TABLE_NAME, CURSOR_ARRAY, "$KEY_ID=?",
            arrayOf(id.toString()), null,null,null,null
            )
        cursor.moveToFirst()
        track = cursorToTrack(cursor)
        cursor.close()

        return track

    }

    //einzelne note akutalisieren
    fun updateTrack(track: Track): Int {
        val db = writableDatabase

        return db.update(
            DATABASE_TABLE_NAME, trackToContentValues(track), "$KEY_ID=?",
            arrayOf(track.id.toString())
        )
    }

    //einzelnen Track löschen
    fun deleteTrack(track: Track): Int {
        val db = writableDatabase

        return db.delete(DATABASE_TABLE_NAME, "$KEY_ID=?",
                arrayOf(track.id.toString())
        )
    }

    //neues track objekt vom cursor erstellen
    private fun cursorToTrack(cursor: Cursor?): Track? {
        if(cursor?.count == 0) return null

        var track: Track? = null
        cursor?.run {
            track = Track(
                getLong(getColumnIndex(KEY_ID)),
                getString(getColumnIndex(KEY_TITLE)),
                getLong(getColumnIndex(KEY_STARTTIMESTAMP)),
                getLong(getColumnIndex(KEY_STOPTIMESTAMP)),
                deserializeMe(getString(getColumnIndex(KEY_GPSPOINTS)))
            )
        }
    return track
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL(DROP_TABLE)
        db?.execSQL(CREATE_TABLE)

    }

}