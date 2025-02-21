package com.hfad.finalproject

import Artist
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class WorkDetailActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var workDetailFragment: WorkDetailFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_work_detail)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val intent = intent
        val position = intent.getIntExtra("ID", 0)

        workDetailFragment = WorkDetailFragment()
        val bundle = Bundle()
        bundle.putInt("POSITION", position)
        workDetailFragment.arguments = bundle

        toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        supportFragmentManager.beginTransaction().replace(R.id.workDetailFragment, workDetailFragment).commit()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_layout, menu)

        val addWorkMenuItem = menu?.findItem(R.id.addWork)
        val settingsMenuItem = menu?.findItem(R.id.settingsButton)

        if (addWorkMenuItem != null) {
            addWorkMenuItem.isVisible = false
        }
        if(settingsMenuItem != null){
            settingsMenuItem.isVisible = false;
        }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId){
            R.id.homeButton -> {
                val intent = Intent(applicationContext, ArtistViewActivity::class.java)
                startActivity(intent)

                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}