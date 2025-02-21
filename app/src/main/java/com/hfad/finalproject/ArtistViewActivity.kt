package com.hfad.finalproject

import Artist
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.FragmentTransaction
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ArtistViewActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var sqLiteDatabase: SQLiteDatabase
    private lateinit var artist_cursor: Cursor
    private lateinit var user_cursor: Cursor
    private var artistViewFragment: ArtistViewFragment? = null

    private var artist_or_not = false;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_artist_view)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        databaseHelper = DatabaseHelper(this)
        sqLiteDatabase = databaseHelper.readableDatabase
        artist_cursor = databaseHelper.getAllArtists()

        toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        artistViewFragment = supportFragmentManager.findFragmentById(R.id.artistViewFragment) as? ArtistViewFragment

        artistViewFragment?.setOnItemClickListener(AdapterView.OnItemClickListener { adapterView, view, position, l ->

            if (artist_cursor.moveToPosition(position)) {
                val artist_col = artist_cursor.getColumnIndexOrThrow("artist_name")
                val artist = artist_cursor.getString(artist_col)

                Artist.currentArtist = artist

                val intent = Intent(applicationContext, ArtistGalleryActivity::class.java)
                startActivity(intent)
            } else {
                Log.e("ArtistViewActivity", "Cursor is empty or invalid position")
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_layout, menu)

        if(databaseHelper.artist_or_viewer(User.currentUser)) {
            artist_or_not = true
        }

        val addWorkMenuItem = menu?.findItem(R.id.addWork)
        val homeMenuItem = menu?.findItem(R.id.homeButton)

        if (addWorkMenuItem != null) {
            addWorkMenuItem.isVisible = artist_or_not
        }
        if(homeMenuItem != null){
            homeMenuItem.isVisible = false;
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId){
            R.id.addWork -> {
                val intent = Intent(applicationContext, CreateWorkActivity::class.java)
                startActivity(intent)

                true
            }
            R.id.settingsButton -> {
                settingsPopup()

                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    /*
    I wanted to make it so that when you logged in, you can't press back to get to the login page.

    I googled "is there a way to disable the back button on the android app on a certain activity ?"

    and found this link..
    https://www.youtube.com/watch?v=o_A-cmmZRMc

    Below I disable the back button from this activity to disallow going back to login without logging out
     */

    override fun onBackPressed() {
        //do nothing
    }

    private fun settingsPopup() {

        /*
        after attempting to add two edit texts to the dialog based on the way i added one in the
        color datebase assignment, it didn't work and i looked up how to use a custom layout for
        an alert dialog. I've done similar things in my current java POS system project so i assumed
        I had that option in kotlin.

        https://www.geeksforgeeks.org/how-to-create-a-custom-alertdialog-in-android/
         */

        val dialogLayout = layoutInflater.inflate(R.layout.settings_popup, null)

        val builder = AlertDialog.Builder(this)

        val usernameField = dialogLayout.findViewById<EditText>(R.id.usernameField)
        val passwordField = dialogLayout.findViewById<EditText>(R.id.passwordField)
        val errorText = dialogLayout.findViewById<TextView>(R.id.errorText)

        if(User.currentUser.isEmpty()){
            usernameField.setText("Username")
            passwordField.hint = "password"
        }
        else {
            user_cursor = databaseHelper.getUser(User.currentUser)

            val username_col = user_cursor.getColumnIndexOrThrow("username")
            val password_col = user_cursor.getColumnIndexOrThrow("password")
            val id_col = user_cursor.getColumnIndexOrThrow("_id")

            val username = user_cursor.getString(username_col)
            val password = user_cursor.getString(password_col)
            val id = user_cursor.getInt(id_col)

            usernameField.setText(username)
            passwordField.setText(password)

            builder.setPositiveButton("Update") { dialog, which ->

                val updatedUsername = usernameField.text.toString()
                val updatedPassword = passwordField.text.toString()

                if(databaseHelper.usernameExists(updatedUsername) && updatedUsername != username){
                    val error = "Username Exists. Please Try Again"
                    errorText.text = error
                }
                else{
                    databaseHelper.updateUser(id, updatedUsername, updatedPassword)
                    User.currentUser = updatedUsername
                    artistViewFragment?.updatePageTitle(updatedUsername)
                }
            }
        }

        builder.setView(dialogLayout)
        builder.setTitle("Update Profile")
        builder.setNegativeButton("Cancel") { dialog, which -> }

        builder.show()

        user_cursor.close()

    }

    override fun onDestroy() {
        super.onDestroy()
        artist_cursor.close()
    }
}