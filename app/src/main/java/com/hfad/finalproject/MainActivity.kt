package com.hfad.finalproject

import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.widget.FrameLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.FragmentTransaction

class MainActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        var mainFragment = supportFragmentManager.findFragmentById(R.id.mainFragment) as? MainFragment

        if (mainFragment != null) {
            val frame: FrameLayout? = findViewById(R.id.frameLayoutTablet)
            if(frame != null) {
                mainFragment = MainFragment()

                val ft = supportFragmentManager.beginTransaction()
                ft.addToBackStack(null)
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                ft.commit()
            }
        }
    }

    /*
   I wanted to make it so that you can't press back to get back into the app after logging out.

   I googled "is there a way to disable the back button on the android app on a certain activity ?"

   and found this link..
   https://www.youtube.com/watch?v=o_A-cmmZRMc

   Below I disable the back button from this activity to disallow going back to login without logging out
    */
    override fun onBackPressed() {
        //do nothing
    }
}