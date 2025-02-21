package com.hfad.finalproject

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context): SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
    companion object {
        private const val DB_NAME = "animas_gallery.sqlite"
        private const val DB_VERSION = 1
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createUsersTableQuery = """
            CREATE TABLE users (
                _id INTEGER PRIMARY KEY AUTOINCREMENT,
                username TEXT UNIQUE,
                password TEXT, 
                artist BOOLEAN,
                artist_name TEXT DEFAULT ""
            );
            """.trimIndent()

        db?.execSQL(createUsersTableQuery)

        val createWorksTableQuery = """
            CREATE TABLE works (
                _id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT,
                description TEXT,
                date TEXT,
                picture TEXT,
                artist TEXT
            );
            """.trimIndent()

        db?.execSQL(createWorksTableQuery)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {

    }

    fun insertUser(username: String, password: String, artist: Boolean, artistName: String) {
        val db = this.writableDatabase
        val newUser = ContentValues().apply {
            put("username", username)
            put("password", password)
            put("artist", artist)
            put("artist_name", artistName)
        }
        db?.insert("users", null, newUser)
    }

    fun insertWorks(name: String, desc: String, date: String, imagePath: String, artist: String) {
        val db = this.writableDatabase
        val newWorks = ContentValues().apply {
            put("name", name)
            put("description", desc)
            put("date", date)
            put("picture", imagePath)
            put("artist", artist )
        }
        db?.insert("works", null, newWorks)
    }

    fun getAllWorks(artistName: String): Cursor {
        val db = this.readableDatabase
        val cursor = db.query(
            "works",
            arrayOf("_id", "name", "description", "date", "picture", "artist" ),
            "artist = ?",
            arrayOf(artistName),
            null,
            null,
            null
        )
        return cursor
    }

    fun getAllArtists(): Cursor {
        val db = this.readableDatabase
        val cursor = db.query(
            "users",
            arrayOf("_id", "username", "artist", "artist_name"),
            "artist = 1",
            null,
            null,
            null,
            null
        )
        return cursor
    }

    fun artist_or_viewer(username: String): Boolean {
        val db = this.readableDatabase
        val cursor = db.query(
            "users",
            arrayOf("artist"),
            "username = ?",
            arrayOf(username),
            null,
            null,
            null
        )

        if (cursor.moveToFirst()) {
            val artist_col = cursor.getColumnIndexOrThrow("artist")
            val artist_or_not = cursor.getInt(artist_col)
            if(artist_or_not == 1){
                cursor.close()
                return true
            }
            else {
                cursor.close()
                return false
            }
        }
        cursor.close()
        return false
    }

    fun getArtistName(username: String): String {
        val db = this.readableDatabase
        val cursor = db.query(
            "users",
            arrayOf("artist_name"),
            "username = ?",
            arrayOf(username),
            null,
            null,
            null
        )

        if(cursor.moveToFirst()){
            val artist_name_col = cursor.getColumnIndexOrThrow("artist_name")
            val artist_name = cursor.getString(artist_name_col)

            cursor.close()
            return artist_name
        }
        else{
            cursor.close()
            return ""
        }
    }

    fun userAuthentication(username: String, password: String): Boolean {
        val db = this.readableDatabase
            val cursor = db.query(
                "users",
                arrayOf("_id", "username", "password", "artist_name"),
                "username = ? AND password = ?",
                arrayOf(username, password),
                null,
                null,
                null
            )
            val isAuthenticated = cursor.moveToFirst()
            cursor.close()

            return isAuthenticated
    }

    fun usernameExists(username: String): Boolean {
        val db = this.readableDatabase
        val cursor = db.query(
            "users",
            arrayOf("_id"),
            "username = ?",
            arrayOf(username),
            null,
            null,
            null
        )
        val exists = cursor.moveToFirst()
        cursor.close()

        return exists
    }

    fun artistExists(artist: String): Boolean {
        val db = this.readableDatabase
        val cursor = db.query(
            "users",
            arrayOf("_id"),
            "artist_name = ?",
            arrayOf(artist),
            null,
            null,
            null
        )
        val exists = cursor.moveToFirst()
        cursor.close()

        return exists
    }

    fun getUser(username: String): Cursor {
        val db = this.readableDatabase
        val cursor = db.query(
            "users",
            arrayOf("_id", "username", "password", "artist_name"),
            "username = ?",
            arrayOf(username),
            null,
            null,
            null,
        )

        cursor.moveToFirst()

        return cursor
    }

    fun updateUser(id: Int, username: String, password: String) {
        val db = this.writableDatabase

        val sql = "UPDATE users SET username = ?, password = ? WHERE _id = ?"

        try {
            db.execSQL(sql, arrayOf(username, password, id))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}